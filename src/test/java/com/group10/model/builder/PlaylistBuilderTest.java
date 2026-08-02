package com.group10.model.builder;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID; 

public class PlaylistBuilderTest {

    @Test
    public void build_withNameAndTracks_createsPlaylist() {
        
        PlaylistComponent playlist = new PlaylistBuilder()
                .setName("Rock Classico")
                .build();

        assertNotNull(playlist);
        assertEquals("Rock Classico", playlist.getName()); 
    }

    @Test
    public void build_blankName_throwsException() {
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                    .setName("   ") 
                    .build();
        });

        assertTrue(exception.getMessage().toLowerCase().contains("vuoto"));
    }

@Test
    public void build_duplicateName_isAllowedByBuilder() {
        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        String playlistName = "TestPlaylist_" + UUID.randomUUID().toString();
        
        // 1. Creiamo e aggiungiamo la PRIMA playlist
        PlaylistComponent playlist1 = new PlaylistBuilder()
                .setName(playlistName)
                .build();
        catalogue.addPlaylist(playlist1); // Qui va a buon fine
        
        // 2. Creiamo la SECONDA playlist in memoria con lo STESSO NOME (il Builder non dà errore)
        PlaylistComponent playlist2 = new PlaylistBuilder()
                .setName(playlistName)
                .build();

        // 3. Tentativo di AGGIUNGERE la seconda playlist al catalogo (questo DEVE fallire!)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogue.addPlaylist(playlist2); // <-- È il catalogo che lancia l'eccezione!
        });

        // 4. Verifichiamo che l'errore sia effettivamente quello del duplicato
        assertTrue(exception.getMessage().toLowerCase().contains("già") || 
                   exception.getMessage().toLowerCase().contains("nome"));
    }
}