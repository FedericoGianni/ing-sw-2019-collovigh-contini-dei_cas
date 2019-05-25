package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
    Scene firstScene, secondScene, thirdScene;
    private static Stage stages;

    private ScreenController screenController;

    public String screenToActivate = "login";

    private static GuiController guiController;
    private static GuiLobbyController guiLobbyController;
    private static GuiMapController guiMapController;

    FXMLLoader firstPaneLoader;

    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

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

        //root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        //root.setId("pane");

        GuiController.setGui(this);


        Image img = new Image("/images/background_image.png");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        stage.setMinWidth(DEFAULT_MIN_WIDTH);
        stage.setMinHeight(DEFAULT_MIN_HEIGHT);
        stage.setMaxWidth(gd.getDisplayMode().getWidth());
        stage.setMaxHeight(gd.getDisplayMode().getHeight());
        stage.setTitle("Adrenalina");

        // login window
        // loader will then give a possibility to get related controller
        this.firstPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent firstPane = firstPaneLoader.load();
        Scene firstScene = new Scene(firstPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        firstScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());

        // game lobby window
        FXMLLoader secondPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("sampleLobby.fxml"));
        Parent secondPane = secondPageLoader.load();
        Scene secondScene = new Scene(secondPane, DEFAULT_MAX_WIDTH, DEFAULT_MIN_HEIGHT);
        secondScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleLobby.css").toExternalForm());

        // main game window
        FXMLLoader thirdPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("sampleMap.fxml"));
        Parent thirdPane = thirdPageLoader.load();
        thirdScene = new Scene(thirdPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        thirdScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleMap.css").toExternalForm());

        // injecting second scene into the controller of the first scene
        guiController = (GuiController) firstPaneLoader.getController();
        guiController.setSecondScene(secondScene);
        setGuiController(guiController);
        guiController.setStageAndSetupListeners(stage);

        // injecting first scene into the controller of the second scene
        guiLobbyController = (GuiLobbyController) secondPageLoader.getController();
        guiLobbyController.setFirstScene(firstScene);
        guiLobbyController.setThirdScene(thirdScene);

        stage.setScene(firstScene);


                /*
                screenController = new ScreenController(firstScene);
                screenController.addScreen("login", FXMLLoader.load(getClass().getClassLoader().getResource( "sample.fxml" )));
                screenController.addScreen("map", FXMLLoader.load(getClass().getClassLoader().getResource( "sampleMap.fxml" )));
                screenController.activate("login");
                */
        stage.show();
    }

    @Override
    public void startUI() {
        launch();
    }

    @Override
    public void show(String s) {

        //guiController = firstPaneLoader.getController();

        Platform.runLater( () -> {
            System.out.println("guiController: " +guiController);
            guiController.openSecondScene(new ActionEvent());

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
