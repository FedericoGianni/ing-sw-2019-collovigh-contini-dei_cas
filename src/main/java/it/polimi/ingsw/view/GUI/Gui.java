package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.PopupWindow;

import javax.management.Notification;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.FutureTask;

public class Gui extends Application implements UserInterface {

    public static final float DEFAULT_MIN_WIDTH = 920;
    public static final float DEFAULT_MIN_HEIGHT = 540;
    public static final float DEFAULT_MAX_WIDTH = 900;
    public static final float DEFAULT_MAX_HEIGHT = 506;

    Parent root;
    Scene scene;
    //Stage stage;

    //reference to this thread to open alert messages
    Thread t;


    public Gui(){
        super();
    }

    private static View view;

    public void setView(View v) {
        view = v;
        System.out.println("setView in GuiController called");
    }

    public View getView() {
        return view;
    }

    @FXML
    Button login;

    @Override
    public void start(Stage stage) throws Exception {

                root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
                root.setId("pane");

                GuiController.setGui(this);
                scene = new Scene(root, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
                scene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());
                Image img = new Image("/images/background_image.png");
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                stage.setMinWidth(DEFAULT_MIN_WIDTH);
                stage.setMinHeight(DEFAULT_MIN_HEIGHT);
                stage.setMaxWidth(gd.getDisplayMode().getWidth());
                stage.setMaxHeight(gd.getDisplayMode().getHeight());
                stage.setTitle("Adrenalina");
                stage.setScene(scene);
                stage.show();
    }

    @Override
    public void startUI() {
        launch();
    }

    @Override
    public void show(String s) {
        Platform.runLater( () -> {
            System.out.println("DEBUG show chimato con stringa " + s);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
            alert.show();
            if(alert.getResult() == ButtonType.OK){
                alert.close();
            }
        });

    }

    @Override
    public void gameSelection() {
    }

    @Override
    public void login() {
    }

    @Override
    public void retryLogin(String error) {
        Platform.runLater( () -> {
            System.out.println("[DEBUG] Chiamato retrylogin con errore: " + error);
            show(error);
        });

    }

    @Override
    public void retryLogin(Exception e) {

    }

    @Override
    public void startSpawn() {

    }

    @Override
    public void startPowerUp() {

    }

    @Override
    public void startAction() {

    }

    @Override
    public void startReload() {

    }
}
