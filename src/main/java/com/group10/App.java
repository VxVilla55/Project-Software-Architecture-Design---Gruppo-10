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
        
        //testStatePattern(); // <-- AGGIUNGI QUESTA RIGA QUI
        //caricamento per test di una playlist e aggiunta di alcuni brani ad essa
        PlaylistComponent playlist = new PlaylistBuilder().setName("PlaylistCreata").build();
        MusicCatalogue.getInstance().addPlaylist(playlist);
        for(int i = 0; i<5; i++) {
            TrackComponent t = new TrackBuilder().setTitle("Titolo"+i).setAuthor("Autore"+i).setDuration(20).build();
            MusicCatalogue.getInstance().addTrack(t);
            playlist.add(t);
        }
        
        //istanzio la MainView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/MainView.fxml"));
        MainViewController controller = MainViewController.getInstance();
        loader.setController(controller);
        //caricamento della MainView
        stage.setScene(new Scene(loader.load()));
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

    // Metodo per testare la UI di creazione playlist
    public static void openCreazionePlaylist() throws IOException {
        // Usiamo il metodo loadFXML già presente nel tuo file App.java!
        // Se il file è insieme a primary.fxml, basta scriverne il nome così:
        Parent root = loadFXML("view/CreazionePlaylist");
        
        // (NOTA: Se invece lo avessi messo dentro la cartella view, 
        // ti basterà cambiare la riga sopra in:

        Stage stage = new Stage();
        stage.setTitle("Crea Nuova Playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
