package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Gui extends Application implements UserInterface {

    public Gui(){
        super();
    }

    private View view;

    public void setView(View view) {
        this.view = view;
    }

    @FXML
    Button login;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        Scene scene = new Scene(root, 800, 600);

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

    }

    @Override
    public void gameSelection() {

    }

    @Override
    public void login() {

    }

    @Override
    public void retryLogin(String error) {

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
