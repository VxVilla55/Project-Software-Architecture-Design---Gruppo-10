package com.group10.service.filter;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author group10
 * Test delle ConcreteStrategy usate dalle playlist automatiche (pattern Strategy).
 * Sono classi senza stato interno, quindi si possono testare direttamente senza
 * toccare il catalogo o il player.
 */
class TrackFilterStrategyTest {

    private TrackComponent makeTrack(String genre, int year, TrackComponent.Tag... tags) {
        TrackBuilder builder = new TrackBuilder()
                .setTitle("Title")
                .setAuthor("Author")
                .setDuration(200)
                .setGenre(genre)
                .setYear(year);
        for (TrackComponent.Tag tag : tags) {
            builder.addTag(tag);
        }
        return builder.build();
    }

    // ---------- GenreFilterStrategy ----------

    @Test
    void genreFilter_sameGenre_matches() {
        GenreFilterStrategy filter = new GenreFilterStrategy("Rock");
        assertTrue(filter.matches(makeTrack("Rock", 2020)));
    }

    @Test
    void genreFilter_differentGenre_doesNotMatch() {
        GenreFilterStrategy filter = new GenreFilterStrategy("Rock");
        assertFalse(filter.matches(makeTrack("Pop", 2020)));
    }

    @Test
    void genreFilter_genreWithDifferentCase_matches() {
        //il confronto e' fatto con equalsIgnoreCase, "rock" e "ROCK" valgono uguale
        GenreFilterStrategy filter = new GenreFilterStrategy("rock");
        assertTrue(filter.matches(makeTrack("ROCK", 2020)));
    }

    // ---------- YearFilterStrategy ----------

    @Test
    void yearFilter_yearInsideRange_matches() {
        YearFilterStrategy filter = new YearFilterStrategy(2000, 2020);
        assertTrue(filter.matches(makeTrack("Pop", 2010)));
    }

    @Test
    void yearFilter_yearEqualToLowerBound_matches() {
        //caso limite: l'anno di inizio va incluso
        YearFilterStrategy filter = new YearFilterStrategy(2000, 2020);
        assertTrue(filter.matches(makeTrack("Pop", 2000)));
    }

    @Test
    void yearFilter_yearEqualToUpperBound_matches() {
        //caso limite: anche l'anno di fine va incluso
        YearFilterStrategy filter = new YearFilterStrategy(2000, 2020);
        assertTrue(filter.matches(makeTrack("Pop", 2020)));
    }

    @Test
    void yearFilter_yearBeforeRange_doesNotMatch() {
        YearFilterStrategy filter = new YearFilterStrategy(2000, 2020);
        assertFalse(filter.matches(makeTrack("Pop", 1999)));
    }

    @Test
    void yearFilter_yearAfterRange_doesNotMatch() {
        YearFilterStrategy filter = new YearFilterStrategy(2000, 2020);
        assertFalse(filter.matches(makeTrack("Pop", 2021)));
    }

    @Test
    void yearFilter_openRange_matchesAnyYear() {
        //e' come lo usa il controller quando l'utente lascia un campo vuoto
        YearFilterStrategy filter = new YearFilterStrategy(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertTrue(filter.matches(makeTrack("Pop", 1965)));
        assertTrue(filter.matches(makeTrack("Pop", 2024)));
    }

    // ---------- TagFilterStrategy ----------

    @Test
    void tagFilter_noTagRequested_matchesEveryTrack() {
        //nessun tag richiesto: il filtro non esclude niente
        TagFilterStrategy filter = new TagFilterStrategy(false, false, false);
        assertTrue(filter.matches(makeTrack("Pop", 2020)));
    }

    @Test
    void tagFilter_favouriteRequested_trackWithThatTag_matches() {
        TagFilterStrategy filter = new TagFilterStrategy(true, false, false);
        assertTrue(filter.matches(makeTrack("Pop", 2020, TrackComponent.Tag.FAVORITE)));
    }

    @Test
    void tagFilter_favouriteRequested_trackWithoutThatTag_doesNotMatch() {
        TagFilterStrategy filter = new TagFilterStrategy(true, false, false);
        assertFalse(filter.matches(makeTrack("Pop", 2020)));
    }

    @Test
    void tagFilter_twoTagsRequested_trackWithBoth_matches() {
        TagFilterStrategy filter = new TagFilterStrategy(true, true, false);
        assertTrue(filter.matches(makeTrack("Pop", 2020,
                TrackComponent.Tag.FAVORITE, TrackComponent.Tag.NEW_RELEASE)));
    }

    @Test
    void tagFilter_twoTagsRequested_trackWithOnlyOne_doesNotMatch() {
        //i tag richiesti valgono in AND: se ne manca uno la traccia non passa
        TagFilterStrategy filter = new TagFilterStrategy(true, true, false);
        assertFalse(filter.matches(makeTrack("Pop", 2020, TrackComponent.Tag.FAVORITE)));
    }

    @Test
    void tagFilter_allThreeTagsRequested_trackWithAllOfThem_matches() {
        TagFilterStrategy filter = new TagFilterStrategy(true, true, true);
        assertTrue(filter.matches(makeTrack("Pop", 2020,
                TrackComponent.Tag.FAVORITE,
                TrackComponent.Tag.NEW_RELEASE,
                TrackComponent.Tag.EXPLICIT)));
    }
}
