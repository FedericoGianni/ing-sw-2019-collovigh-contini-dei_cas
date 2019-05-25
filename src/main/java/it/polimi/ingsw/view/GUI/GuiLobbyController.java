package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.cachemodel.Player;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiLobbyController implements Initializable {

    private static Gui gui;
    public static void setGui(Gui g) {
        gui = g;
    }

    private Stage myStage;

    private Scene loginScene;
    private Scene mainScene;


    @FXML
    private ListView lobbyPlayersListView;

    //private List<String> list = new ArrayList<>();
    protected ListProperty<String> listProperty = new SimpleListProperty<>();
    private ObservableList<Player> lista = FXCollections.observableArrayList(gui.getView().getCacheModel().getCachedPlayers());


    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
        //list.addAll(gui.getView().getCacheModel().getCachedPlayers());
        lista.addListener((ListChangeListener.Change<? extends Player> change) -> {
            while(change.next()) {

                if (change.wasUpdated()) {
                    System.out.println("Update detected");
                } else if (change.wasPermutated()) {

                } else {
                    for (Player remitem : change.getRemoved()) {
                        gui.show("haiusdhsakdjsal un player si è disconnesso");
                    }
                    for (Player additem : change.getAddedSubList()) {
                        gui.show("USAHDKUASJDLKASHDKJA un player si è connesso!");
                        lobbyPlayersListView.edit(1);
                    }
                }
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Player> names = FXCollections.observableArrayList(gui.getView().getCacheModel().getCachedPlayers());
        lobbyPlayersListView.itemsProperty().bind(listProperty);

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
