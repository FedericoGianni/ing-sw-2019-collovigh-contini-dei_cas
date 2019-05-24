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

import java.awt.*;

public class Gui extends Application implements UserInterface {

    public static final float DEFAULT_MIN_WIDTH = 920;
    public static final float DEFAULT_MIN_HEIGHT = 540;
    public static final float DEFAULT_MAX_WIDTH = 900;
    public static final float DEFAULT_MAX_HEIGHT = 506;

    public static final String DEFAULT_LOGIN_OK_REPLY = "OK";
    public static final String DEFAULT_NAME_ALREADY_TAKEN_REPLY = "NAME_ALREADY_TAKEN";
    public static final String DEFAULT_COLOR_ALREADY_TAKEN_REPLY = "COLOR_ALREADY_TAKEN";
    public static final String DEFAULT_GAME_ALREADY_STARTED_REPLY = "GAME_ALREADY_STARTED";
    public static final String DEFAULT_MAX_PLAYER_READCHED = "MAX_PLAYER_REACHED";


    Parent root;
    Scene firstScene, secondScene;
    private Stage stage;

    GuiController firstPaneController;
    GuiLobbyController secondPaneController;
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
                this.stage = stage;

                root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
                root.setId("pane");

                GuiController.setGui(this);
                //firstScene = new Scene(root, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);

                // getting loader and a pane for the first scene.
                // loader will then give a possibility to get related controller
                FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
                Parent firstPane = firstPaneLoader.load();
                Scene firstScene = new Scene(firstPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
                firstScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());

                // getting loader and a pane for the second scene
                FXMLLoader secondPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("sampleLobby.fxml"));
                Parent secondPane = secondPageLoader.load();
                Scene secondScene = new Scene(secondPane, 300, 275);
                secondScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleLobby.css").toExternalForm());

                // injecting second scene into the controller of the first scene
                firstPaneController = (GuiController) firstPaneLoader.getController();
                firstPaneController.setSecondScene(secondScene);

                // injecting first scene into the controller of the second scene
                secondPaneController = (GuiLobbyController) secondPageLoader.getController();
                secondPaneController.setFirstScene(firstScene);

                stage.setTitle("Switching scenes");
                stage.setScene(firstScene);

                Image img = new Image("/images/background_image.png");
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                stage.setMinWidth(DEFAULT_MIN_WIDTH);
                stage.setMinHeight(DEFAULT_MIN_HEIGHT);
                stage.setMaxWidth(gd.getDisplayMode().getWidth());
                stage.setMaxHeight(gd.getDisplayMode().getHeight());
                stage.setTitle("Adrenalina");

                stage.show();
                //stage.setScene(firstScene);
                //stage.show();

    }

    @Override
    public void startUI() {
        launch();
    }

    @Override
    public void show(String s) {

        Platform.runLater( () -> {
            //System.out.println("DEBUG show chimato con stringa " + s);
            String header;
            String msg;

            switch (s){
                case DEFAULT_LOGIN_OK_REPLY:
                    header = "login";
                    msg = "Benvenuto dal server!";
                    break;

                case DEFAULT_COLOR_ALREADY_TAKEN_REPLY:
                    header = "login";
                    msg = "Colore già preso! Riprova";
                    break;

                case DEFAULT_MAX_PLAYER_READCHED:
                    header = "login";
                    msg = "Massimo numero di giocatori raggiunto!";
                    break;

                case DEFAULT_GAME_ALREADY_STARTED_REPLY:
                    header = "login";
                    msg = "Partita già in corso!";
                    break;

                default:
                    header = s.substring(0, 10);
                    msg = s;

            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.setHeaderText(header);
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
