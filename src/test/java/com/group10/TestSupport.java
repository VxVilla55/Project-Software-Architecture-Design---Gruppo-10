package com.group10;

import java.lang.reflect.Field;

import com.group10.model.MusicCatalogue;
import com.group10.model.state.PlaybackEngine;
import com.group10.service.command.CommandManager;

/**
 *
 * @author group10
 * Classe di appoggio usata solo dai test.
 * I Singleton restano vivi per tutta la durata della JVM e tutti i test girano nella
 * stessa JVM: senza pulizia lo stato lasciato da un test resterebbe visibile a quelli
 * successivi, e il risultato dipenderebbe dall'ordine di esecuzione.
 * Qui svuotiamo tutto una volta sola, cosi' ogni test parte da zero.
 */
public final class TestSupport {

    private TestSupport() {
        //classe di sola utilita', non va istanziata
    }

    // riporta i tre singleton allo stato iniziale, da chiamare in un @BeforeEach
    public static void resetSingletons() {
        stopPlaybackTimer();

        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.clearQueue();
        engine.setCurrentPlaylist(null);
        if (engine.isShuffled()) {
            engine.toggleShuffle();
        }

        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        catalogue.getTracks().clear();
        catalogue.getPlaylists().clear();

        clearUndoHistory();
    }

    // spegne il Timer della riproduzione: gira su un thread separato, quindi se un test
    // lo lascia acceso continua ad andare anche durante i test successivi
    public static void stopPlaybackTimer() {
        PlaybackEngine.getInstance().stopSimulation();
    }

    // svuota lo storico dell'undo. E' l'unico punto in cui serve la reflection: lo stack
    // di CommandManager e' privato e non esiste un metodo per svuotarlo (in produzione non
    // serve), ma nei test va azzerato o un undo potrebbe annullare il comando di un altro test
    private static void clearUndoHistory() {
        try {
            Field undoStack = CommandManager.class.getDeclaredField("undoStack");
            undoStack.setAccessible(true);
            ((java.util.Stack<?>) undoStack.get(CommandManager.getInstance())).clear();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Impossibile svuotare lo storico dei comandi", e);
        }
    }
}
