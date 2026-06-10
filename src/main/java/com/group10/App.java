package com.group10;

import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.MainViewController;
import com.group10.controller.playlist.PlaylistUIDetailsController;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.PlaylistComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.TrackComponent;
import com.group10.model.persistence.JsonPersistenceManager;
import com.group10.model.persistence.PersistenceManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        JsonPersistenceManager persistence = new JsonPersistenceManager();
        persistence.load();
        MusicCatalogue.getInstance().addSubscriber(persistence);
        
        stage.setOnCloseRequest(event -> {
        com.group10.model.state.PlaybackEngine.getInstance().stopSimulation();
        javafx.application.Platform.exit();
        System.exit(0);
    });

    // -- ESEGUITE UNA SOLA VOLTA per riempire il programma inzialmente
    // -- Altrimenti dovete cancellare il catalogue.json nella cartella "data"
    // fillInstance();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/MainView.fxml"));
    MainViewController controller = MainViewController.getInstance();
    loader.setController(controller);

    Scene scena = new Scene(loader.load());
    scena.getStylesheets().add(getClass().getResource("/com/group10/view/styles.css").toExternalForm());
    stage.setScene(scena);
    
    stage.setMinWidth(1300); 
    stage.setMinHeight(800);
    stage.setTitle("MyMusicPlayer");
    stage.show();        
}

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    //metodo per testare
    public static void openAddTrackForm() throws IOException {
        /*FXMLLoader loader = new FXMLLoader(App.class.getResource("AddTrackView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Traccia");
        stage.setScene(new Scene(root));
        stage.show();*/
        TrackUIAdderController p = new TrackUIAdderController(); //magari includiamo PlaylistUIController nel pattern factory

        Parent root = p.getRoot();

        Stage stage = new Stage();
        stage.setTitle("Aggiungi traccia");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    public static void openPlaylistUIComponent() throws IOException {
        PlaylistComponent playlist = new PlaylistBuilder().setName("PlaylistCreata").build();
        MusicCatalogue.getInstance().addPlaylist(playlist);


        for(int i = 0; i<5; i++) {
            TrackComponent t = new TrackBuilder().setTitle("Titolo"+i).setAuthor("Autore"+i).setDuration(20).build();
            MusicCatalogue.getInstance().addTrack(t);
            playlist.add(t);
        }

        //simuliamo la selezione di una playlist
        PlaylistUIDetailsController p = (PlaylistUIDetailsController) new TrackUIComponentFactory().createUIComponentDetails(playlist);

        Parent root = p.getRoot();

        Stage stage = new Stage();
        stage.setTitle("Visualizza playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }

    
    public static void openCreazionePlaylist() throws IOException {
        Parent root = loadFXML("view/CreazionePlaylist");
        Stage stage = new Stage();
        stage.setTitle("Crea Nuova Playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void fillInstance() {
        PlaylistComponent playlist = new PlaylistBuilder().setName("Classic Rock Anthems").build();
        MusicCatalogue.getInstance().addPlaylist(playlist);


        // 🎸 Bon Jovi - Livin' on a Prayer (1986) -> 4:09 (249 secondi)
        TrackComponent t1 = new TrackBuilder()
            .setTitle("Livin' on a Prayer")
            .setAuthor("Bon Jovi")
            .setDuration(249)
            .setGenre("Hard Rock")
            .setYear(1986)
            .build();
        MusicCatalogue.getInstance().addTrack(t1);
        MusicCatalogue.getInstance().addTrackToPlaylist("Classic Rock Anthems", t1);

        // 🎸 Guns N' Roses - Sweet Child O' Mine (1987) -> 5:55 (355 secondi)
        TrackComponent t2 = new TrackBuilder()
            .setTitle("Sweet Child O' Mine")
            .setAuthor("Guns N' Roses")
            .setDuration(355)
            .setGenre("Hard Rock")
            .setYear(1987)
            .build();
        MusicCatalogue.getInstance().addTrack(t2);
        MusicCatalogue.getInstance().addTrackToPlaylist("Classic Rock Anthems", t2);

        // 🎸 Aerosmith - Dream On (1973) -> 4:28 (268 secondi)
        TrackComponent t3 = new TrackBuilder()
            .setTitle("Dream On")
            .setAuthor("Aerosmith")
            .setDuration(268)
            .setGenre("Classic Rock")
            .setYear(1973)
            .build();
        MusicCatalogue.getInstance().addTrack(t3);
        MusicCatalogue.getInstance().addTrackToPlaylist("Classic Rock Anthems", t3);

        // 🎸 AC/DC - Back In Black (1980) -> 4:15 (255 secondi)
        TrackComponent t4 = new TrackBuilder()
            .setTitle("Back In Black")
            .setAuthor("AC/DC")
            .setDuration(255)
            .setGenre("Hard Rock")
            .setYear(1980)
            .build();
        MusicCatalogue.getInstance().addTrack(t4);
        MusicCatalogue.getInstance().addTrackToPlaylist("Classic Rock Anthems", t4);

        // 🎸 Europe - The Final Countdown (1986) -> 5:09 (309 secondi)
        TrackComponent t5 = new TrackBuilder()
            .setTitle("The Final Countdown")
            .setAuthor("Europe")
            .setDuration(309)
            .setGenre("Glam Metal")
            .setYear(1986)
            .build();
        MusicCatalogue.getInstance().addTrack(t5);
        MusicCatalogue.getInstance().addTrackToPlaylist("Classic Rock Anthems", t5);
    }
}
