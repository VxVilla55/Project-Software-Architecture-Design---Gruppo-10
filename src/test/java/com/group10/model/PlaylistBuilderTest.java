package com.group10.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistBuilderTest {

    @Test
    public void testCreazionePlaylistCorretta() {
        // Usiamo withName() come definito nel tuo Builder
        PlaylistComponent playlist = new PlaylistBuilder()
                .setName("Rock Classico")
                .build();

        assertNotNull(playlist);
        assertEquals("Rock Classico", playlist.getName()); 
    }

    @Test
    public void testFallimentoNomeVuoto() {
        // Ora ci aspettiamo IllegalArgumentException, che è quella che lancia il tuo withName()
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                    .setName("") //questo serve a far scattare l'eccezione immediatamente
                    .build();
        });

        //controlliamo che l'errore sia corretto
        assertTrue(exception.getMessage().toLowerCase().contains("vuoto"));
    }

    @Test
    public void testFallimentoNomeDuplicato() {
        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        String nomePlaylist = "Allenamento";
        
        catalogue.addPlaylist(new PlaylistBuilder()
                .setName(nomePlaylist)
                .build());
        
        //tentativo di duplicato
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                .setName(nomePlaylist)
                .build();
        });

        assertTrue(exception.getMessage().toLowerCase().contains("duplicato"));
    }
}