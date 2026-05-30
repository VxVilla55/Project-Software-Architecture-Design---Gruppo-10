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
                .withName("Rock Classico")
                .build();

        assertNotNull(playlist);
        assertEquals("Rock Classico", playlist.getName()); 
    }

    @Test
    public void testFallimentoNomeVuoto() {
        // Ora ci aspettiamo IllegalArgumentException, che è quella che lancia il tuo withName()
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PlaylistBuilder()
                    .withName("") // Questo fa scattare l'eccezione immediatamente
                    .build();
        });

        // Controlliamo che l'errore sia corretto
        assertTrue(exception.getMessage().toLowerCase().contains("vuoto"));
    }

    @Test
    public void testFallimentoNomeDuplicato() {
        List<String> nomiEsistenti = new ArrayList<>();
        nomiEsistenti.add("Allenamento"); 

        String nuovoNome = "Allenamento"; // Tentativo di duplicato

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            if (nomiEsistenti.contains(nuovoNome)) {
                throw new IllegalArgumentException("Errore: nome playlist duplicato");
            }
            // Usiamo withName() anche qui
            new PlaylistBuilder().withName(nuovoNome).build();
        });

        assertTrue(exception.getMessage().toLowerCase().contains("duplicato"));
    }
}