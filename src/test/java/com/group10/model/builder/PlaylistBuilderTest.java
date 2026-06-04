package com.group10.model.builder;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID; 

public class PlaylistBuilderTest {

    @Test
    public void testCreazionePlaylistCorretta() {
        
        PlaylistComponent playlist = new PlaylistBuilder()
                .setName("Rock Classico")
                .build();

        assertNotNull(playlist);
        assertEquals("Rock Classico", playlist.getName()); 
    }

    @Test
    public void testFallimentoNomeVuoto() {
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                    .setName("   ") 
                    .build();
        });

        assertTrue(exception.getMessage().toLowerCase().contains("vuoto"));
    }

    @Test
    public void testFallimentoNomeDuplicato() {
        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        String nomePlaylist = "TestPlaylist_" + UUID.randomUUID().toString();
        
        // 1. Creiamo e aggiungiamo la PRIMA playlist (questo NON deve fallire)
        PlaylistComponent playlist1 = new PlaylistBuilder()
                .setName(nomePlaylist)
                .build();
        catalogue.addPlaylist(playlist1);
        
        // 2. Tentativo di creare la SECONDA playlist con lo STESSO NOME (questo DEVE fallire)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                .setName(nomePlaylist)
                .build();
        });

        // 3. Verifichiamo che l'errore sia effettivamente quello del duplicato
        assertTrue(exception.getMessage().toLowerCase().contains("già") || 
                   exception.getMessage().toLowerCase().contains("nome"));
    }
}