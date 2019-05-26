package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.cachemodel.Player;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    private ObservableList<Player> lobbyPlayers = gui.getView().getCacheModel().getCachedPlayers();

    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
        lobbyPlayersListView.setItems(lobbyPlayers);

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


}
