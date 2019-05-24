package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GuiMap extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static final float DEFAULT_MIN_WIDTH = 920;
    public static final float DEFAULT_MIN_HEIGHT = 540;

    public GuiMap(){
        super();
    }


    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sampleMap.fxml"));
            root.setId("pane");

            Scene scene = new Scene(root, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
            scene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleMap.css").toExternalForm());
            Image img = new Image("/images/background_image.png");
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

            stage.setMinWidth(DEFAULT_MIN_WIDTH);
            stage.setMinHeight(DEFAULT_MIN_HEIGHT);
            stage.setMaxWidth(gd.getDisplayMode().getWidth());
            stage.setMaxHeight(gd.getDisplayMode().getHeight());
            stage.setTitle("Adrenalina");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void startUI(){
        launch();
    }
}
