package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.actions.GrabAction;
import it.polimi.ingsw.view.actions.SkipAction;
import it.polimi.ingsw.view.cachemodel.Player;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_TIMER_EXPIRED;
import static it.polimi.ingsw.utils.Protocol.*;
import static java.lang.Thread.sleep;


public class Gui extends Application implements UserInterface {

    public static final float DEFAULT_MIN_WIDTH = 920;
    public static final float DEFAULT_MIN_HEIGHT = 540;

    public static final float DEFAULT_MIN_WIDTH_MAP = 1000;
    public static final float DEFAULT_MIN_HEIGHT_MAP = 700;

    private static GuiController guiController;
    private static GuiLobbyController guiLobbyController;
    private static GuiMapController guiMapController;
    private int validMove=-1;
    private List<Boolean> wasOnline = new ArrayList<>(Collections.nCopies(5, true));
    public void setGuiLobbyController(GuiLobbyController guic) {
        this.guiLobbyController = guic;
    }

    public void setGuiMapController(GuiMapController guic) {
        this.guiMapController = guic;
    }

    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    public Gui(){
    super();
    }

    private static View view;

    public void setView(View v) {
        view = v;
        //System.out.println("setView in GuiController called");
    }

    public View getView() {
        return view;
    }


    @Override
    public void start(Stage stage) throws Exception {

        GuiController.setGui(this);
        GuiLobbyController.setGui(this);
        GuiMapController.setGui(this);

        Image img = new Image("/images/background_image.png");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        stage.setMinWidth(DEFAULT_MIN_WIDTH);
        stage.setMinHeight(DEFAULT_MIN_HEIGHT);
        stage.setMaxWidth(gd.getDisplayMode().getWidth());
        stage.setMaxHeight(gd.getDisplayMode().getHeight());
        stage.setTitle("Adrenalina");

        // login window
        // loader will then give a possibility to get related controller
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));  // TEST
        Parent firstPane = firstPaneLoader.load();
        Scene firstScene = new Scene(firstPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        //firstScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());  // non serve : basta specificare nell FXML ( by d)

        // game lobby window
        FXMLLoader secondPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("sampleLobby.fxml"));
        Parent secondPane = secondPageLoader.load();
        Scene secondScene = new Scene(secondPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        //secondScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleLobby.css").toExternalForm());  // non serve : basta specificare nell FXML ( by d)

        // main game window
        FXMLLoader thirdPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("sampleMap.fxml"));
        Parent thirdPane = thirdPageLoader.load();
        Scene thirdScene = new Scene(thirdPane, DEFAULT_MIN_WIDTH_MAP, DEFAULT_MIN_HEIGHT_MAP);
        thirdScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleMap.css").toExternalForm());

        // injecting second scene into the controller of the first scene
        guiController = (GuiController) firstPaneLoader.getController();
        guiController.setSecondScene(secondScene);
        setGuiController(guiController);
        guiController.setStageAndSetupListeners(stage);

        // injecting first scene into the controller of the second scene
        guiLobbyController = (GuiLobbyController) secondPageLoader.getController();
        setGuiLobbyController(guiLobbyController);
        guiLobbyController.setFirstScene(firstScene);
        guiLobbyController.setThirdScene(thirdScene);
        guiLobbyController.setStageAndSetupListeners(stage);

        guiMapController = (GuiMapController) thirdPageLoader.getController();
        setGuiMapController(guiMapController);
        //TODO guiMapController.setStageAndSetupListeners(stage);

        stage.setScene(firstScene);
        stage.show();
    }

    @Override
    public void startUI() {
        launch();
    }

    @Override
    public void startGame() {
        Platform.runLater( () -> {
            System.out.println("guiLobbyController: " + guiLobbyController);
            guiMapController.mapCreator();
            guiLobbyController.openThirdScene(new ActionEvent());
        });
    }

    @Override
    public void show(String s) {

        Platform.runLater( () -> {
            //System.out.println("DEBUG show chimato con stringa " + s);
            String header;
            String msg;
            Boolean retryLogin = false;

            switch (s){
                case DEFAULT_LOGIN_OK_REPLY:
                    header = "login";
                    msg = "Benvenuto dal server!";
                    retryLogin = true;
                    break;

                case DEFAULT_COLOR_ALREADY_TAKEN_REPLY:
                    header = "login";
                    msg = "Colore già preso! Riprova";
                    retryLogin = false;
                    break;

                case DEFAULT_MAX_PLAYER_REACHED:
                    header = "login";
                    msg = "Massimo numero di giocatori raggiunto!";
                    retryLogin = false;
                    break;

                case DEFAULT_GAME_ALREADY_STARTED_REPLY:
                    header = "login";
                    msg = "Partita già in corso!";
                    retryLogin = false;
                    break;


                default:
                    header = s.substring(0, 10);
                    msg = s;
                    guiMapController.show(msg);
            }

            if(retryLogin) {
                System.out.println("guiController: " + guiController);
                guiController.openSecondScene(new ActionEvent());
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.setHeaderText(header);
            alert.show();

            if(alert.getResult() == ButtonType.OK){
                alert.close();
            }

        });
    }

    public void grab(GrabAction grabAction){
        view.doAction(grabAction);
    }

    @Override
    public void notifyUpdate(UpdateType updateType, int playerId, TurnUpdate turnUpdate) {

        switch (updateType){

            case LOBBY:

                guiLobbyController.clearLobbyPlayers();

                List<String> names = view.getCacheModel()
                        .getCachedPlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());

                for (String name: names){

                    guiLobbyController.addLobbyPlayers(name);
                }

                break;

            case STATS: //possibilità: cambio pos,danni subiti, spostmanto e marchi, disconnessioni
                if (!view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline()) {
                    wasOnline.set(playerId, false);
                    Alert a=new Alert(Alert.AlertType.INFORMATION,"Il giocatore "+playerId+" si è disconnesso");
                    a.show();
                } else if(view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline() &&
                        !wasOnline.get(playerId)){
                    //player reconnected
                    Alert a=new Alert(Alert.AlertType.INFORMATION,"Il giocatore "+playerId+" si è ricollegato");
                    a.show();
                    wasOnline.set(playerId, true);
                }
                guiMapController.statsUpdater(playerId);
                break;

            case INITIAL:
                for(Player item:view.getCacheModel().getCachedPlayers())
                    guiMapController.loginUpdater(item.getName(),item.getPlayerId(),item.getPlayerColor());
                break;

            case POWERUP_BAG:
                    guiMapController.powerUpDisplayer();
                break;
            case AMMO_CELL://remember--- at end turn i need to replace everyCell, even if it has something inside!
                    guiMapController.ammoPlacer();
                break;
            case SPAWN_CELL://weapons changed don't need nothing
                guiMapController.spawnCellWeaponsUpdate();
                break;
            case WEAPON_BAG:
                guiMapController.changedWeapons();//display my new weapons
                break;
            case AMMO_BAG:
                guiMapController.changedAmmos();
                break;

            case TURN:
                guiMapController.notifyTurnUpdate(turnUpdate);
                break;

            default:
                break;
        }
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
    public void startSpawn() {//pox: 1) rosso e giallo non fanno bene lo spawn 2) blu non fa bene lo spawn

        while (view.getCacheModel().getCachedPlayers().size() <= 0 || view.getPlayerId() == -1) {
            System.out.println("Attendi ricezione dell'update iniziale...");

            try {
                sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //view.spawn(powerUps.get(read));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                guiMapController.startSpawn();
            }
        });
    }


    @Override
    public void startPowerUp() {
        //here i need to let them use power ups, devo eliminare tutti gli effetti all'inziio di start action
        while(view.getPlayerId() == -1) {
            try{
                sleep(200);
            } catch (InterruptedException e){

            }
        }
        guiMapController.actionButtonDisable();
        guiMapController.powerUpEnable();
    }

    @Override
    public void askGrenade() {

    }

    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {
        //here i need to validate the buttons
        //this method enables the action buttons to do something
        while(view.getPlayerId() == -1) {
            try{
                sleep(200);
            } catch (InterruptedException e){

            }
        }
        guiMapController.actionButtonsEnabler();

        //view.doAction(new Move(view.doAction(new Move(directionsList, finalPos));));----->
        //view.doAction(new GrabAction(directionsList, weapons.get(0), weapons.get(1)));
        //view.doAction(new ShootAction("LOCK RIFLE", targetList, effects, cells));
    }

    @Override
    public void reDoFrenzyAtomicShoot() {
        //TODO
    }

    @Override
    public void startReload() {
        //view.doAction(new SkipAction());
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi ricaricare qualche arma?", ButtonType.YES, ButtonType.NO);
        a.showAndWait();
        if (a.getResult().equals(ButtonType.NO)) {
            view.doAction(new SkipAction());
        } else {
            List <String> weapons=new ArrayList<>();
            guiMapController.reloadWeaponChooser(weapons);

        }
    }

    @Override
    public void endGame() {

    }

    @Override
    public void setValidMove(boolean b) {
        if(b){
            guiMapController.setValidMove(1);
        } else {
            guiMapController.setValidMove(0);
        }

    }

    @Override
    public void close() {
        guiMapController.log.appendText(DEFAULT_TIMER_EXPIRED);
        System.exit(0);
    }

    @Override
    public List<Integer> askMapAndSkulls() {
        return null;
    }
}
