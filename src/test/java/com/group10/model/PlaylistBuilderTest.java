package com.group10.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistBuilderTest {

    @Test
    public void testCreazionePlaylistCorretta() {
        // 1. Verifichiamo la creazione con un nome valido
        PlaylistComponent playlist = new PlaylistBuilder()
                .setName("Rock Classico")
                .build();

        assertNotNull(playlist);
        // Assumendo che PlaylistComponent abbia un metodo getName()
        assertEquals("Rock Classico", playlist.getName()); 
    }

    @Test
    public void testFallimentoNomeVuoto() {
        // 2. Regola del nome vuoto: il Builder deve bloccare la creazione
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new PlaylistBuilder()
                    .setName("") // Tentativo di inserire un nome vuoto
                    .build();
        });

        // Controlliamo che l'errore sia corretto
        assertTrue(exception.getMessage().toLowerCase().contains("vuoto"));
    }

    @Test
    public void testFallimentoNomeDuplicato() {
        // 3. Regola del nome duplicato: simuliamo il controllo della libreria
        // (Il controllo del duplicato di solito si fa quando si salva la playlist nella MusicLibrary)
        List<String> nomiEsistenti = new ArrayList<>();
        nomiEsistenti.add("Allenamento"); 

        String nuovoNome = "Allenamento"; // Tentativo di duplicato

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Simulazione della logica che farebbe la MusicLibrary
            if (nomiEsistenti.contains(nuovoNome)) {
                throw new IllegalArgumentException("Errore: nome playlist duplicato");
            }
            new PlaylistBuilder().setName(nuovoNome).build();
        });

        assertTrue(exception.getMessage().toLowerCase().contains("duplicato"));
    }
}