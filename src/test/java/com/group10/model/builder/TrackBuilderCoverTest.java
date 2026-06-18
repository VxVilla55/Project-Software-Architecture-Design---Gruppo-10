package com.group10.model.builder;

import com.group10.model.TrackComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test unitari per il campo coverImagePath nel TrackBuilder
class TrackBuilderCoverTest {

    // Helper: costruisce una traccia con o senza copertina
    private TrackComponent buildTrack(String coverPath) {
        TrackBuilder builder = new TrackBuilder()
                .setTitle("Test Track")
                .setAuthor("Test Author")
                .setDuration(180);
        if (coverPath != null) builder.setCoverImagePath(coverPath);
        return builder.build();
    }

    @Test
    void coverPath_savedCorrectly() {
        // Il path inserito deve essere recuperabile dalla traccia costruita
        TrackComponent track = buildTrack("data/covers/mycover.png");
        assertEquals("data/covers/mycover.png", track.getCoverImagePath());
    }

    @Test
    void coverPath_nullWhenNotSet() {
        // Se non si chiama setCoverImagePath, il campo deve restare null
        TrackComponent track = buildTrack(null);
        assertNull(track.getCoverImagePath());
    }

    @Test
    void coverPath_emptyStringPreserved() {
        // Una stringa vuota è un valore valido, non deve essere convertita
        TrackComponent track = buildTrack("");
        assertEquals("", track.getCoverImagePath());
    }

    @Test
    void coverPath_doesNotAffectEquals() {
        // equals si basa solo su titolo + autore, la copertina non influisce
        TrackComponent t1 = buildTrack("cover_a.png");
        TrackComponent t2 = buildTrack("cover_b.png");
        assertEquals(t1, t2);
    }

    @Test
    void coverPath_doesNotAffectHashCode() {
        // hashCode segue le stesse regole di equals
        TrackComponent t1 = buildTrack("cover_a.png");
        TrackComponent t2 = buildTrack("cover_b.png");
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void coverPath_propagatedToBuiltObject() {
        // Il path assoluto deve arrivare intatto all'oggetto finale
        String path = "/some/absolute/path/to/image.jpg";
        TrackComponent track = new TrackBuilder()
                .setTitle("Song")
                .setAuthor("Artist")
                .setDuration(200)
                .setCoverImagePath(path)
                .build();
        assertEquals(path, track.getCoverImagePath());
    }
}
