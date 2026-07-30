package com.group10.model.state;

import java.util.*;
import java.util.function.Consumer;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Publisher;
import com.group10.model.common.Subscriber;

import com.group10.model.playback.Sequential;
import com.group10.model.playback.PlaybackMode;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

/**
 *
 * @author group10
 * PATTERN: Singleton; e' anche il Context del pattern State (delega play/pause/stop allo
 * stato corrente) e il Context del pattern Strategy (usa playbackMode per decidere cosa
 * fare a fine traccia). E' anche Publisher del pattern Observer, per notificare le viste
 * quando cambia la coda/traccia corrente.
 * Modella lo stato del player: coda di riproduzione, traccia corrente e simulazione del
 * tempo che passa.
 */
public class PlaybackEngine implements Publisher{

    // secondi di ascolto dopo i quali una traccia lunga viene conteggiata come ascoltata
    private static final double PLAYCOUNT_THRESHOLD = 30.0;

    private static PlaybackEngine instance;
    private PlayerState currentState;
    
    private List<TrackComponent> queue;
    private int currentIndex;
    private TrackComponent currentTrack;
    private double currentTime;
    private Timeline timer;
    private long lastTickNanos = -1;

    // per accodamento playlist
    private PlaylistComponent currentPlaylist;
    private final List<PlaylistComponent> pendingPlaylists = new ArrayList<>();

    // pattern strategy
    private PlaybackMode playbackMode = new Sequential();
    private boolean shuffled = false;
    private final List<TrackComponent> originalOrder = new ArrayList<>();
    
    private Consumer<TrackComponent> onTrackChanged;
    private Consumer<Double> onTick;
    private boolean playCounted = false; 
    private Consumer<Boolean> onPlayStateChanged; 

    private List<Subscriber> subscribers;
    
    private PlaybackEngine() {
        changeState(new StoppedState()); // stato iniziale
        this.queue = new ArrayList<>();
        this.currentIndex = -1;
        this.currentTime = 0;
        this.subscribers = new ArrayList<>();
    }

    public static PlaybackEngine getInstance() {
        if (instance == null) {
            instance = new PlaybackEngine();
        }
        return instance;
    }

    public void changeState(PlayerState state) {
        this.currentState = state;
        this.currentState.setContext(this);
    }

    public PlayerState getState() {
        return currentState;
    }

    public boolean isPlaying() {
        return currentState instanceof PlayingState;
    }

    public double getCurrentTime() {
        return currentTime;
    }
    
    public void setCurrentTrack(TrackComponent track) {
        if (track == null) return;
        if (!queue.contains(track)) {
            queue.add(track);
        }
        this.currentIndex = queue.indexOf(track);
        switchTrack(track);
        play();
    }

    public void setCurrentPlaylist(PlaylistComponent playlist) {
        this.currentPlaylist = playlist;
    }

    public PlaylistComponent getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void addPendingPlaylist(PlaylistComponent playlist) {
        pendingPlaylists.add(playlist);
    }

    // prende e rimuove la prima playlist in attesa, null se non ce ne sono
    public PlaylistComponent getPendingPlaylist() {
        if (pendingPlaylists.isEmpty()) {
            return null;
        }
        return pendingPlaylists.remove(0);
    }

    // riparte da capo con una playlist: la mette come corrente, carica le tracce e fa play
    public void startPlaylist(PlaylistComponent playlist) {
        setCurrentPlaylist(playlist);
        addListToQueue(new ArrayList<>(playlist.getTracks()));
        play();
    }

    public List<TrackComponent> getQueue() {
        return new ArrayList<>(queue); // la view non deve poter modificare la coda interna
    }

    public void clearQueue() {
        // ferma la riproduzione e poi azzera la coda
        stop();
        this.queue.clear();
        this.currentIndex = -1;
        this.currentTrack = null;
        this.currentTime = 0;

        // notifica alla view che non c'è più un brano corrente
        if (onTrackChanged != null) {
            onTrackChanged.accept(null);
        }
        if (onTick != null) {
            onTick.accept(0.0);
        }
    }
    
    public void addTrackToQueue(TrackComponent track) {
        queue.add(track);
        if (currentTrack == null) {
            currentIndex = 0;
            currentTrack = queue.get(currentIndex);
        }
        notifySubscribers();
    }
    
    public void addTrackAsNext(TrackComponent track) {
        addTrackToQueueAtIndex(track, currentIndex+1);
        notifySubscribers();
    }
    
    public TrackComponent getCurrentTrack() {
        return this.currentTrack;
    }

    // serve per riprodurrre le playlist
    public void addListToQueue(List<TrackComponent> tracks) {
        clearQueue();
        queue.addAll(tracks);
        if (queue.isEmpty()) return;

        // se lo shuffle era gia' attivo quando premo play, mischio subito la coda
        if (shuffled) {
            originalOrder.clear();
            originalOrder.addAll(queue);
            Collections.shuffle(queue);
        }
        currentIndex = 0;
        switchTrack(queue.get(0));
        notifySubscribers();
    }


    public void next() {
        if (queue.isEmpty() || currentTrack == null) return;

        if (currentIndex < queue.size() - 1) {
            // sequenziale: va alla prossima traccia in coda
            currentIndex++;
            switchTrack(queue.get(currentIndex));
        } else if ((currentIndex == queue.size() -1) && playbackMode.loopsQueue()) {
            // loop playlist, riparte dall'inizio alla fine della coda
            currentIndex = 0;
            switchTrack(queue.get(currentIndex));
        } else {
            // sequenziale: si ferma alla fine della coda
            stop();
        }
    }

    public void previous() {
        if (queue.isEmpty() || currentTrack == null) return;

        if (currentIndex > 0) {
            currentIndex--;
            switchTrack(queue.get(currentIndex));
        } else if (currentIndex == 0 && playbackMode.loopsQueue()) {
            // loop playlist, dalla prima traccia si torna all'ultima
            currentIndex = queue.size() - 1;
            switchTrack(queue.get(currentIndex));
        } else {
            // già sulla prima traccia: la si fa ripartire dall'inizio
            switchTrack(queue.get(0));
        }
    }

    // cambia la traccia corrente e azzera il tempo. Se si stava gia' riproducendo,
    // il vecchio Timer (che simulava la traccia precedente) va fermato e se ne
    // riparte uno nuovo per la traccia nuova, altrimenti continuerebbe a girare
    // col tempo/durata sbagliati
    private void switchTrack(TrackComponent newTrack) {
        this.currentTrack = newTrack;
        resetTime();

        if (onTrackChanged != null) {
            onTrackChanged.accept(newTrack);
        }

        if (onTick != null) {
            onTick.accept(0.0);
        }

        if (isPlaying()) {
            stopSimulation();
            startSimulation();
        }
    }

    // questi 3 setter sono gli "aggganci" (callback) che il controller usa per farsi
    // avvisare quando succede qualcosa nel player, senza che il model conosca la UI.
    // vengono chiamati dal Timer di startSimulation(), quindi da un thread diverso da
    // quello di JavaFX: chi li usa deve usare Platform.runLater
    public void setOnTick(Consumer<Double> onTick) {
        this.onTick = onTick;
    }

    public void setOnTrackChanged(Consumer<TrackComponent> listener) {
        this.onTrackChanged = listener;
    }

    public void setOnPlayStateChanged(Consumer<Boolean> listener) {
        this.onPlayStateChanged = listener;
    }

    public void startSimulation() {
        if (currentTrack == null) {
            changeState(new StoppedState());
            return;
        }

        if (onPlayStateChanged != null) {
            onPlayStateChanged.accept(true);
        }
        
        final double INTERVAL = 0.1;

        timer = new Timeline(new KeyFrame(Duration.seconds(INTERVAL), e -> {
            currentTime += INTERVAL;

            double playCountThreshold = Math.min(PLAYCOUNT_THRESHOLD, currentTrack.getDurationInSeconds());
            if (currentTime >= playCountThreshold && !playCounted) {
                currentTrack.incrementPlayCount();
                playCounted = true;
            }

            if (onTick != null) {
                onTick.accept(currentTime);
            }

            if (currentTime >= currentTrack.getDurationInSeconds()) {
                timer.stop();
                playbackMode.onTrackEnd(PlaybackEngine.this);
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    // sposta manualmente il tempo corrente (usato quando l'utente trascina lo slider)
    public void seek(double seconds) {
        this.currentTime = seconds;
    }

    // ferma e distrugge la Timeline: va richiamato ogni volta che si mette in pausa,
    // si passa traccia o si ferma, altrimenti il vecchio Timer continuerebbe a girare
    // in background insieme al nuovo
    public void stopSimulation() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        if (onPlayStateChanged != null) {
            onPlayStateChanged.accept(false);
        }
    }

    public void resetTime() {
        this.currentTime = 0;
        this.playCounted = false; // l'ascolto può essere ricontato
    }


public Integer removeTrackFromQueue(TrackComponent track) {
        //se la coda è vuota
        if (queue.isEmpty())
            return null;

        //controllo se è presente la traccia nella coda
        if (queue.contains(track)) {
            int removedIndex = queue.indexOf(track);
            queue.remove(track);
            
            //se è quella in riproduzione attualmente
            if ( track.equals(PlaybackEngine.getInstance().getCurrentTrack()) ) {

                if (queue.isEmpty()) {
                    //rimossa l'ultima traccia: non c'è più nulla da riprodurre
                    currentTrack = null;
                    currentIndex = -1;
                    switchTrack(null);
                    stopSimulation();
                    changeState(new StoppedState());
                } else {
                    //se la traccia rimossa era l'ultima della coda, arretra l'indice
                    if(queue.size()-1 < currentIndex) {
                        currentIndex = queue.size()-1;
                    }
                    currentTrack = queue.get(currentIndex);
                    switchTrack(currentTrack);
                    //ferma la riproduzione (l'utente dovrà premere play manualmente)
                    stopSimulation();
                    changeState(new StoppedState());
                }
            }
            return removedIndex;
        } else {
            return null;
        }
    }

    public void addTrackToQueueAtIndex(TrackComponent track, int index) {
        if (index >= 0 && index <= queue.size()) {
            queue.add(index, track);
        }
    }

    public void play() {
        currentState.play();
    }

    public void pause() {
        currentState.pause();
    }

    public void stop() {
        currentState.stop();
    }

    public PlaybackMode getPlaybackMode() {
        return playbackMode;
    }

    // setter del pattern Strategy
    // il client (controller) sostituisce la strategia a runtime
    public void setPlaybackMode(PlaybackMode mode) {
        this.playbackMode = mode;
    }

    // shuffle e' un semplice toggle, non una Strategy: quando si attiva, si salva l'ordine
    // vero in originalOrder e si mischiano solo le tracce dopo quella corrente (quelle gia'
    // ascoltate restano dove sono). queue.subList(...) ritorna una "vista" della stessa
    // lista, quindi Collections.shuffle su upcoming rimescola davvero anche queue.
    // Quando si disattiva si butta via l'ordine mischiato e si ripristina originalOrder
    public void toggleShuffle() {
        shuffled = !shuffled;
        if (shuffled) {
            originalOrder.clear();
            originalOrder.addAll(queue);
            if (currentIndex < 0 || currentIndex >= queue.size() - 1) {
                return;
            }
            List<TrackComponent> upcoming = queue.subList(currentIndex + 1, queue.size());
            Collections.shuffle(upcoming);
        } else {
            TrackComponent current = currentTrack;
            queue.clear();
            queue.addAll(originalOrder);
            currentIndex = queue.indexOf(current);
        }
        notifySubscribers();
    }

    public boolean isShuffled() {
        return shuffled;
    }

    public boolean hasNext() {
        return currentIndex < queue.size() - 1;
    }

    public void playFromStart() {
        currentIndex = 0;
        switchTrack(queue.get(0));
        play();
    }

    public void replayCurrent() {
        resetTime();
        stopSimulation();
        startSimulation();
    }

    public void replaceInQueue(TrackComponent oldTrack, TrackComponent newTrack) {
        int i = queue.indexOf(oldTrack);
        if (i >= 0) {
            queue.set(i, newTrack);
        }
        if (currentTrack != null && currentTrack.equals(oldTrack)) {
            currentTrack = newTrack;
        }
        notifySubscribers();
    }

    @Override
    public void notifySubscribers() {
        for (Subscriber s: subscribers) {
            s.update();
        }
    }
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber s) {
        subscribers.remove(s);
    }

}