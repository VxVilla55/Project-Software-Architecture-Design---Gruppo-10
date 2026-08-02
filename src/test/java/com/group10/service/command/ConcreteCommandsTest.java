package com.group10.service.command;

import com.group10.TestSupport;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author group10
 * Test dei ConcreteCommand (pattern Command). Per ogni comando si verifica sia l'effetto
 * di execute() sia che undo() riporti davvero il sistema allo stato di partenza: era la
 * parte scoperta, perche' CommandManagerTest usa uno stub e controlla solo lo stack.
 */
class ConcreteCommandsTest {

    private MusicCatalogue catalogue;

    @BeforeEach
    void setUp() {
        TestSupport.resetSingletons();
        catalogue = MusicCatalogue.getInstance();
    }

    @AfterEach
    void tearDown() {
        TestSupport.stopPlaybackTimer();
    }

    private TrackComponent makeTrack(String title, String author) {
        return new TrackBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setDuration(180)
                .setGenre("Pop")
                .setYear(2020)
                .build();
    }

    // ---------- AddTrackCommand ----------

    @Test
    void addTrackCommand_execute_addsTrackToCatalogue() {
        TrackComponent track = makeTrack("Song", "Artist");

        new AddTrackCommand(track).execute();

        assertTrue(catalogue.getTracks().contains(track));
    }

    @Test
    void addTrackCommand_undo_removesTrackAgain() {
        TrackComponent track = makeTrack("Song", "Artist");
        AddTrackCommand command = new AddTrackCommand(track);

        command.execute();
        command.undo();

        assertFalse(catalogue.getTracks().contains(track));
    }

    // ---------- AddPlaylistCommand ----------

    @Test
    void addPlaylistCommand_execute_addsPlaylistToCatalogue() {
        PlaylistComponent playlist = new PlaylistComponent("Rock");

        new AddPlaylistCommand(playlist).execute();

        assertSame(playlist, catalogue.getPlaylist("Rock"));
    }

    @Test
    void addPlaylistCommand_undo_removesPlaylistAgain() {
        PlaylistComponent playlist = new PlaylistComponent("Rock");
        AddPlaylistCommand command = new AddPlaylistCommand(playlist);

        command.execute();
        command.undo();

        assertNull(catalogue.getPlaylist("Rock"));
    }

    // ---------- DeletePlaylistCommand ----------

    @Test
    void deletePlaylistCommand_execute_removesPlaylist() {
        PlaylistComponent playlist = new PlaylistComponent("Jazz");
        catalogue.addPlaylist(playlist);

        new DeletePlaylistCommand(playlist).execute();

        assertNull(catalogue.getPlaylist("Jazz"));
    }

    @Test
    void deletePlaylistCommand_undo_restoresPlaylist() {
        PlaylistComponent playlist = new PlaylistComponent("Jazz");
        catalogue.addPlaylist(playlist);
        DeletePlaylistCommand command = new DeletePlaylistCommand(playlist);

        command.execute();
        command.undo();

        assertSame(playlist, catalogue.getPlaylist("Jazz"));
    }

    // ---------- AddTrackToPlaylistCommand ----------

    @Test
    void addTrackToPlaylistCommand_execute_addsTrackToThatPlaylist() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent track = makeTrack("Song", "Artist");
        catalogue.addPlaylist(playlist);
        catalogue.addTrack(track);

        new AddTrackToPlaylistCommand(track, "PL").execute();

        assertTrue(playlist.contains(track));
    }

    @Test
    void addTrackToPlaylistCommand_undo_removesTrackFromThatPlaylist() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent track = makeTrack("Song", "Artist");
        catalogue.addPlaylist(playlist);
        catalogue.addTrack(track);
        AddTrackToPlaylistCommand command = new AddTrackToPlaylistCommand(track, "PL");

        command.execute();
        command.undo();

        assertFalse(playlist.contains(track));
    }

    // ---------- RemoveTrackFromPlaylistCommand ----------

    @Test
    void removeTrackFromPlaylistCommand_execute_removesTrack() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent track = makeTrack("Song", "Artist");
        catalogue.addPlaylist(playlist);
        playlist.add(track);

        new RemoveTrackFromPlaylistCommand(track, "PL").execute();

        assertFalse(playlist.contains(track));
    }

    @Test
    void removeTrackFromPlaylistCommand_undo_putsTrackBack() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent track = makeTrack("Song", "Artist");
        catalogue.addPlaylist(playlist);
        playlist.add(track);
        RemoveTrackFromPlaylistCommand command = new RemoveTrackFromPlaylistCommand(track, "PL");

        command.execute();
        command.undo();

        assertTrue(playlist.contains(track));
    }

    // ---------- AddTagCommand / RemoveTagCommand ----------

    @Test
    void addTagCommand_execute_addsTagToTrack() {
        TrackComponent track = makeTrack("Song", "Artist");

        new AddTagCommand(track, TrackComponent.Tag.FAVORITE).execute();

        assertTrue(track.hasTag(TrackComponent.Tag.FAVORITE));
    }

    @Test
    void addTagCommand_undo_removesTagAgain() {
        TrackComponent track = makeTrack("Song", "Artist");
        AddTagCommand command = new AddTagCommand(track, TrackComponent.Tag.FAVORITE);

        command.execute();
        command.undo();

        assertFalse(track.hasTag(TrackComponent.Tag.FAVORITE));
    }

    @Test
    void addTagCommand_withTagList_addsAllOfThem() {
        TrackComponent track = makeTrack("Song", "Artist");
        ArrayList<TrackComponent.Tag> tags = new ArrayList<>();
        tags.add(TrackComponent.Tag.FAVORITE);
        tags.add(TrackComponent.Tag.EXPLICIT);

        new AddTagCommand(track, tags).execute();

        assertTrue(track.hasTag(TrackComponent.Tag.FAVORITE));
        assertTrue(track.hasTag(TrackComponent.Tag.EXPLICIT));
    }

    @Test
    void removeTagCommand_execute_removesTagFromTrack() {
        TrackComponent track = new TrackBuilder()
                .setTitle("Song").setAuthor("Artist").setDuration(180)
                .addTag(TrackComponent.Tag.FAVORITE)
                .build();

        new RemoveTagCommand(track, TrackComponent.Tag.FAVORITE).execute();

        assertFalse(track.hasTag(TrackComponent.Tag.FAVORITE));
    }

    @Test
    void removeTagCommand_undo_putsTagBack() {
        TrackComponent track = new TrackBuilder()
                .setTitle("Song").setAuthor("Artist").setDuration(180)
                .addTag(TrackComponent.Tag.FAVORITE)
                .build();
        RemoveTagCommand command = new RemoveTagCommand(track, TrackComponent.Tag.FAVORITE);

        command.execute();
        command.undo();

        assertTrue(track.hasTag(TrackComponent.Tag.FAVORITE));
    }

    // ---------- ReorderTrackCommand ----------

    @Test
    void reorderTrackCommand_execute_movesTrackToNewPosition() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent first = makeTrack("First", "A");
        TrackComponent second = makeTrack("Second", "B");
        TrackComponent third = makeTrack("Third", "C");
        playlist.add(first);
        playlist.add(second);
        playlist.add(third);

        //sposto la prima traccia in ultima posizione
        new ReorderTrackCommand(playlist, 0, 2).execute();

        assertEquals(first, playlist.getTracks().get(2));
    }

    @Test
    void reorderTrackCommand_undo_restoresOriginalOrder() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent first = makeTrack("First", "A");
        TrackComponent second = makeTrack("Second", "B");
        TrackComponent third = makeTrack("Third", "C");
        playlist.add(first);
        playlist.add(second);
        playlist.add(third);
        ReorderTrackCommand command = new ReorderTrackCommand(playlist, 0, 2);

        command.execute();
        command.undo();

        assertEquals(first, playlist.getTracks().get(0));
        assertEquals(second, playlist.getTracks().get(1));
        assertEquals(third, playlist.getTracks().get(2));
    }

    // ---------- DeleteTrackCommand ----------

    @Test
    void deleteTrackCommand_execute_removesTrackFromCatalogueAndPlaylists() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent track = makeTrack("Song", "Artist");
        catalogue.addTrack(track);
        catalogue.addPlaylist(playlist);
        playlist.add(track);

        new DeleteTrackCommand(track).execute();

        assertFalse(catalogue.getTracks().contains(track));
        assertFalse(playlist.contains(track));
    }

    @Test
    void deleteTrackCommand_undo_restoresTrackInCatalogueAndPlaylists() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent track = makeTrack("Song", "Artist");
        catalogue.addTrack(track);
        catalogue.addPlaylist(playlist);
        playlist.add(track);
        DeleteTrackCommand command = new DeleteTrackCommand(track);

        command.execute();
        command.undo();

        assertTrue(catalogue.getTracks().contains(track), "la traccia deve tornare nel catalogo");
        assertTrue(playlist.contains(track), "la traccia deve tornare nelle playlist che la contenevano");
    }

    @Test
    void deleteTrackCommand_undo_restoresTrackInQueueAtSamePosition() {
        //verifica il fix sulla coda: la traccia va rimessa dov'era
        PlaybackEngine engine = PlaybackEngine.getInstance();
        TrackComponent first = makeTrack("First", "A");
        TrackComponent second = makeTrack("Second", "B");
        catalogue.addTrack(first);
        catalogue.addTrack(second);
        engine.addTrackToQueue(first);
        engine.addTrackToQueue(second);
        DeleteTrackCommand command = new DeleteTrackCommand(second);

        command.execute();
        assertFalse(engine.getQueue().contains(second), "dopo execute la traccia non e' piu' in coda");

        command.undo();
        assertTrue(engine.getQueue().contains(second), "dopo undo la traccia torna in coda");
        assertEquals(1, engine.getQueue().indexOf(second), "torna nella posizione che aveva");
    }

    // ---------- UpdateTrackCommand ----------

    @Test
    void updateTrackCommand_execute_replacesTrackEverywhere() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent oldTrack = makeTrack("Old", "A");
        TrackComponent newTrack = makeTrack("New", "A");
        catalogue.addTrack(oldTrack);
        catalogue.addPlaylist(playlist);
        playlist.add(oldTrack);

        new UpdateTrackCommand(oldTrack, newTrack).execute();

        assertTrue(catalogue.getTracks().contains(newTrack));
        assertFalse(catalogue.getTracks().contains(oldTrack));
        assertTrue(playlist.contains(newTrack));
    }

    @Test
    void updateTrackCommand_undo_bringsBackOldTrack() {
        PlaylistComponent playlist = new PlaylistComponent("PL");
        TrackComponent oldTrack = makeTrack("Old", "A");
        TrackComponent newTrack = makeTrack("New", "A");
        catalogue.addTrack(oldTrack);
        catalogue.addPlaylist(playlist);
        playlist.add(oldTrack);
        UpdateTrackCommand command = new UpdateTrackCommand(oldTrack, newTrack);

        command.execute();
        command.undo();

        assertTrue(catalogue.getTracks().contains(oldTrack));
        assertFalse(catalogue.getTracks().contains(newTrack));
        assertTrue(playlist.contains(oldTrack));
    }

    // ---------- RenamePlaylistCommand ----------

    @Test
    void renamePlaylistCommand_execute_playlistIsRetrievableWithNewName() {
        PlaylistComponent playlist = new PlaylistComponent("Old Name");
        catalogue.addPlaylist(playlist);

        new RenamePlaylistCommand(playlist, "New Name").execute();

        assertNotNull(catalogue.getPlaylist("New Name"));
        assertNull(catalogue.getPlaylist("Old Name"));
    }

    @Test
    void renamePlaylistCommand_execute_keepsTracksAndPlayCount() {
        //rinominare non deve far perdere le tracce ne' azzerare gli ascolti
        PlaylistComponent playlist = new PlaylistComponent("Old Name");
        TrackComponent track = makeTrack("Song", "Artist");
        playlist.add(track);
        playlist.incrementPlayCount();
        playlist.incrementPlayCount();
        catalogue.addPlaylist(playlist);

        new RenamePlaylistCommand(playlist, "New Name").execute();

        PlaylistComponent renamed = catalogue.getPlaylist("New Name");
        assertTrue(renamed.contains(track), "le tracce devono restare");
        assertEquals(2, renamed.getPlayCount(), "il conteggio degli ascolti deve restare");
    }

    @Test
    void renamePlaylistCommand_undo_restoresOldName() {
        PlaylistComponent playlist = new PlaylistComponent("Old Name");
        catalogue.addPlaylist(playlist);
        RenamePlaylistCommand command = new RenamePlaylistCommand(playlist, "New Name");

        command.execute();
        command.undo();

        assertNotNull(catalogue.getPlaylist("Old Name"));
        assertNull(catalogue.getPlaylist("New Name"));
    }
}
