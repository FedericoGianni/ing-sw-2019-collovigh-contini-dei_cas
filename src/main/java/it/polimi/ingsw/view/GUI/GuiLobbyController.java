package it.polimi.ingsw.view.GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class GuiLobbyController {

    /**
     * Reference to the gui linked to this controller
     */
    private static Gui gui;

    /**
     * set the gui parameter
     * @param g gui to set as reference
     */
    public static void setGui(Gui g) {
        gui = g;
    }

    /**
     * Refeerence to the stage
     */
    private Stage myStage;

    /**
     * Reference to the login window
     */
    private Scene loginScene;

    /**
     * Reference to the main game scene
     */
    private Scene mainScene;

    /**
     * List view of the current connected players in the lobby
     */
    @FXML
    private ListView lobbyPlayersListView;

    /**
     * Observable List which contains the currently connected players waiting to join a new game
     */
    private static ObservableList<String> lobbyPlayers = FXCollections.observableArrayList();

    /**
     * Useful to update list when it is changed
     */
    final AtomicReference<ListChangeListener.Change<? extends String>> change = new AtomicReference<>(null);

    /**
     * GuiLobbyController initializer
     * @param stage reference to the stage
     */
    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
        lobbyPlayersListView.setItems(lobbyPlayers);

        lobbyPlayers.addListener((ListChangeListener<String>) c -> {lobbyPlayersListView.refresh();});
    }

    /**
     * set the Login scene (in case of invalid login user will go back to the login scene)
     * @param scene login scene to be set
     */
    public void setFirstScene(Scene scene) {
        loginScene = scene;
    }

    /**
     * Open login scene in case of invalid login
     * @param actionEvent
     */
    public void openFirstScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(loginScene);
    }

    /**
     * Set the third scene
     * @param scene
     */
    public void setThirdScene(Scene scene){ mainScene = scene;
    }

    /**
     * Opens the main game scene after the game has started
     * @param actionEvent
     */
    public void openThirdScene(ActionEvent actionEvent) {
        myStage.setScene(mainScene);
        //myStage.show();---?
    }

    /**
     * Removes all player from the observable array list
     */
    public void clearLobbyPlayers(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyPlayers.clear();
            }
        });
    }

    /**
     * add a new Player to the observable array list
     * @param playerName name of the player in the lobby waiting to join game
     */
    public void addLobbyPlayers(String playerName){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyPlayers.add(playerName);
            }
        });

    }



}
