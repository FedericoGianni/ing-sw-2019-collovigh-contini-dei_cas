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

    private static Gui gui;
    public static void setGui(Gui g) {
        gui = g;
    }

    private Stage myStage;

    private Scene loginScene;
    private Scene mainScene;

    @FXML
    private ListView lobbyPlayersListView;

    private static ObservableList<String> lobbyPlayers = FXCollections.observableArrayList();
    final AtomicReference<ListChangeListener.Change<? extends String>> change = new AtomicReference<>(null);

    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
        lobbyPlayersListView.setItems(lobbyPlayers);

        lobbyPlayers.addListener((ListChangeListener<String>) c -> {lobbyPlayersListView.refresh();});

        /*
        lobbyPlayers.addListener((ListChangeListener.Change<? extends Player> change) -> {
            while(change.next()) {
                if (change.wasUpdated()) {
                    lobbyPlayersListView.refresh();
                } else if (change.wasPermutated()) {
                    lobbyPlayersListView.refresh();
                } else {
                    for (Player remitem : change.getRemoved()) {
                        lobbyPlayersListView.refresh();
                    }
                    for (Player additem : change.getAddedSubList()) {
                        lobbyPlayersListView.refresh();
                    }
                }
            }
        });
        */
    }

    public void setFirstScene(Scene scene) {
        loginScene = scene;
    }

    public void openFirstScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(loginScene);
    }

    public void setThirdScene(Scene scene){
        mainScene = scene;
    }

    public void openThirdScene(ActionEvent actionEvent) {
        myStage.setScene(mainScene);
    }

    public void clearLobbyPlayers(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyPlayers.clear();
            }
        });
    }

    public void addLobbyPlayers(String playerName){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyPlayers.add(playerName);
            }
        });

    }



}
