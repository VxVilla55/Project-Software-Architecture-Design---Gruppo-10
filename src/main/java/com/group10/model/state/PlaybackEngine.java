package com.group10.model.state;

import java.util.*;
import java.util.function.Consumer;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Publisher;
import com.group10.model.common.Subscriber;

import com.group10.model.playback.Sequential;
import com.group10.model.playback.PlaybackMode;
import com.group10.model.playback.RepeatPlaylist;
import com.group10.model.playback.RepeatTrack;

/**
 * Singleton: classe che modella lo stato del player
 */
public class PlaybackEngine implements Publisher{
    
    private static PlaybackEngine instance;
    private PlayerState currentState;
    
    private List<TrackComponent> queue;
    private int currentIndex;
    private TrackComponent currentTrack;
    private double currentTime;
    private Timer timer;
    private PlaylistComponent currentPlaylist;

    // Pattern STRATEGY
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

    public List<TrackComponent> getQueue() {
        return new ArrayList<>(queue); // la view non deve poter modificare la coda interna
    }

public void clearQueue() {
        // svuota tutto
        // prima ferma la riproduzione (stop), poi azzera la coda
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

        // controllo se alla pressione di Play sulla playlist/home sia attiva la modalità shuffle
        // in tal caso, mischia la coda
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
        } else if ((currentIndex == queue.size() -1) && playbackMode instanceof RepeatPlaylist) {
            // loop playlist: riparte dall'inizio alla fine della coda
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
        } else if (currentIndex == 0 && (playbackMode instanceof RepeatPlaylist)) {
            // loop playlist: dalla prima traccia si torna all'ultima
            currentIndex = queue.size() - 1;
            switchTrack(queue.get(currentIndex));
        } else {
            // già sulla prima traccia: la si fa ripartire dall'inizio
            switchTrack(queue.get(0));
        }
    }

private void switchTrack(TrackComponent newTrack) {
        this.currentTrack = newTrack;
        this.currentTime = 0;
        this.playCounted = false; // ogni cambio traccia riapre il conteggio dell'ascolto

        if (onTrackChanged != null) {
            onTrackChanged.accept(newTrack);
        }

        if (onTick != null) {
            onTick.accept(0.0);
        }

        if (currentState instanceof PlayingState) {
            stopSimulation();
            startSimulation();
        }
    }
    
    
    
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
            // senza traccia corrente non c'è nulla da simulare
            changeState(new StoppedState());
            return;
        }

        if (onPlayStateChanged != null) {
            onPlayStateChanged.accept(true);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime += 0.1;

                // l'ascolto viene conteggiato una sola volta, dopo i primi 30 secondi
                if (currentTime >= 30.0 && !playCounted) {
                    currentTrack.incrementPlayCount();
                    playCounted = true;
                }

                if (onTick != null) {
                    onTick.accept(currentTime);
                }
                
                if (currentTrack != null) {
                    if (currentTime >= currentTrack.getDurationInSeconds()) {
                        playbackMode.onTrackEnd(PlaybackEngine.this);
                    }
                }
            }
        }, 100, 100); 
    }

    public void seek(double seconds) {
        this.currentTime = seconds;
    }
    
    public void stopSimulation() {
        if (timer != null) {
            timer.cancel(); 
            timer = null;
        }
        
        
        if (onPlayStateChanged != null) {
            onPlayStateChanged.accept(false);
        }
    }

    public void resetTime() {
        this.currentTime = 0; 
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
        if (queue.size()-1>index) {
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

    public void cycleRepeatMode() {
        if (playbackMode instanceof Sequential) {
            // se sequenziale, imposta loop su playlist
            playbackMode = new RepeatPlaylist();
        } else if (playbackMode instanceof RepeatPlaylist) {
            // se loop su playlist, imposta loop su singola traccia
            playbackMode = new RepeatTrack();
        } else {
            // ritorna a sequenziale
            playbackMode = new Sequential();
        }
    }

    public PlaybackMode getPlaybackMode() {
        return playbackMode;
    }

    // tratta lo shuffle come toggle e usa due queue, quella mischiata e quella originale
    public void toggleShuffle() {
        shuffled = !shuffled;
        if (shuffled) {
            // salva l'ordine originale
            originalOrder.clear();
            originalOrder.addAll(queue);
            if (currentIndex < 0 || currentIndex >= queue.size() - 1) {
                return;
            }
            // fa shuffle solo sulle canzoni successive
            List<TrackComponent> upcoming = queue.subList(currentIndex + 1, queue.size());
            Collections.shuffle(upcoming);
        } else {
            // resetta l'ordine originale
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
        play();
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