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
        testRiproduzioneSimulata(); // <-- AGGIUNGI QUESTA RIGA QUI
        
        scene = new Scene(new MainViewController().getRoot());
        stage.setScene(scene);
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
// Metodo per testare la simulazione (Task T8.4)
   // Metodo per testare la simulazione (Task T8.4)
    public static void testRiproduzioneSimulata() {
        System.out.println("--- TEST T8.4: RIPRODUZIONE SIMULATA ---");
        
        com.group10.model.state.PlaybackEngine player = com.group10.model.state.PlaybackEngine.getInstance();
        
        // 1. Creiamo due tracce brevi usando il percorso corretto per il Builder (model.builder.TrackBuilder)
        com.group10.model.TrackComponent traccia1 = new com.group10.model.builder.TrackBuilder()
                .setTitle("Brano 1").setAuthor("Autore A").setDuration(5).build();
                
        com.group10.model.TrackComponent traccia2 = new com.group10.model.builder.TrackBuilder()
                .setTitle("Brano 2").setAuthor("Autore B").setDuration(6).build();
                
        // 2. Le aggiungiamo alla coda
        player.addTrackToQueue(traccia1);
        player.addTrackToQueue(traccia2);
        
        // 3. Eseguiamo il test in un Thread separato per non far bloccare la schermata JavaFX
        new Thread(() -> {
            player.play(); 
            
            try {
                // Lasciamo suonare per circa 3 secondi
                Thread.sleep(3500); 
                
                System.out.println("\n*** TEST: Premo PAUSA! ***");
                player.pause(); 
                
                // Lasciamo il player in pausa per 2 secondi
                Thread.sleep(2000); 
                
                System.out.println("\n*** TEST: Premo di nuovo PLAY! ***");
                player.play(); // Deve riprendere dal punto di prima!
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}