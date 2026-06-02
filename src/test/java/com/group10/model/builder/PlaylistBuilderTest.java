package com.group10.model.builder;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID; // Importante per generare nomi univoci!

public class PlaylistBuilderTest {

    @Test
    public void testCreazionePlaylistCorretta() {
        // Usiamo setName() come definito nel tuo Builder
        PlaylistComponent playlist = new PlaylistBuilder()
                .setName("Rock Classico")
                .build();

        assertNotNull(playlist);
        assertEquals("Rock Classico", playlist.getName()); 
    }

    @Test
    public void testFallimentoNomeVuoto() {
        // Ci aspettiamo IllegalArgumentException quando si chiama build() con nome vuoto
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                    .setName("   ") // Spazi vuoti (il tuo trim() li eliminerà)
                    .build();
        });

        assertTrue(exception.getMessage().toLowerCase().contains("vuoto"));
    }

    @Test
    public void testFallimentoNomeDuplicato() {
        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        
        // Generiamo un nome UNIVOCO per questo test (es: "TestPlaylist_a1b2c3d4...")
        // Così siamo certi al 100% che non esiste già nel catalogo da test precedenti
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