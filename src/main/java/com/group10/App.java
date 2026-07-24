package com.group10;

import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.MainViewController;
import com.group10.model.MusicCatalogue;
import com.group10.model.persistence.JsonPersistenceManager;
import com.group10.model.state.PlaybackEngine;
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
            persistence.save();
            PlaybackEngine.getInstance().stopSimulation();
            javafx.application.Platform.exit();
            System.exit(0);
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/MainView.fxml"));
        MainViewController controller = MainViewController.getInstance();
        loader.setController(controller);
        MusicCatalogue.getInstance().addSubscriber(controller);


        Scene scena = new Scene(loader.load());
        scena.getStylesheets().add(getClass().getResource("/com/group10/view/styles.css").toExternalForm());
        stage.setScene(scena);

        stage.setMinWidth(1300); 
        stage.setMinHeight(800);
        stage.setTitle("MusicPlaylistManager");
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
}
