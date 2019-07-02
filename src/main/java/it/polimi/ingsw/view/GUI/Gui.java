package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.actions.GrabAction;
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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_TIMER_EXPIRED;
import static it.polimi.ingsw.utils.Protocol.*;
import static java.lang.Thread.sleep;


public class Gui extends Application implements UserInterface {

    public static final float DEFAULT_MIN_WIDTH = 920;
    public static final float DEFAULT_MIN_HEIGHT = 540;

    public static final float DEFAULT_MIN_WIDTH_MAP = 1000;
    public static final float DEFAULT_MIN_HEIGHT_MAP = 700;

    private static GuiStartController guiStartController;
    private static GuiReconnectionController guiReconnectionController;
    private static GuiController guiController;
    private static GuiLobbyController guiLobbyController;
    private static GuiMapController guiMapController;
    private static GuiEndController guiEndController;
    private int validMove=-1;
    private boolean isReconnection=true;
    private boolean receivedReconnectOk = false;
    private List<Boolean> wasOnline = new ArrayList<>(Collections.nCopies(5, true));
    private  boolean bb=false;

    public void setGuiLobbyController(GuiLobbyController guic) {
        this.guiLobbyController = guic;
    }
    public GuiLobbyController getGuiLobbyController(){return guiLobbyController;
    }

    public void setGuiMapController(GuiMapController guic) {
        this.guiMapController = guic;
    }

    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    public void setGuiStartController(GuiStartController g) {
        this.guiStartController = g;
    }

    public void setGuiReconnectionController(GuiReconnectionController g) {
        this.guiReconnectionController = g;
    }

    public void setGuiEndController(GuiEndController g) {
        this.guiEndController = g;
    }

    public void setIsReconnection(Boolean b)
    {
        this.isReconnection=b;
    }
    public boolean getIsReconnection()
    {
        return this.isReconnection;
    }

    public Gui(){
    super();
    }

    private static View view;

    public void setView(View v) {
        view = v;
    }

    public View getView() {
        return view;
    }


    @Override
    public void start(Stage stage) throws Exception {

        GuiStartController.setGui(this);
        GuiReconnectionController.setGui(this);
        GuiController.setGui(this);
        GuiLobbyController.setGui(this);
        GuiMapController.setGui(this);
        GuiEndController.setGui(this);

        Image img = new Image("/images/background_image.png");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        stage.setMinWidth(DEFAULT_MIN_WIDTH);
        stage.setMinHeight(DEFAULT_MIN_HEIGHT);
        stage.setMaxWidth(gd.getDisplayMode().getWidth());
        stage.setMaxHeight(gd.getDisplayMode().getHeight());
        stage.setTitle("Adrenalina");

        //start window
        FXMLLoader startPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("start.fxml"));
        Parent startPane = startPaneLoader.load();
        Scene startScene = new Scene(startPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);

        //reconnect window
        FXMLLoader reconnectPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("reconnect.fxml"));
        Parent reconnectPane = reconnectPaneLoader.load();
        Scene reconnectScene = new Scene(reconnectPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);

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

        FXMLLoader endPageLoader = new FXMLLoader((getClass().getClassLoader().getResource("end.fxml")));
        Parent fourhtPane = endPageLoader.load();
        Scene endScene = new Scene(fourhtPane, DEFAULT_MIN_WIDTH_MAP, DEFAULT_MIN_HEIGHT_MAP);

        //injecting reconnect scene into the controller of the start scene
        //injecting login scene into the controller of the start scene
        guiStartController = (GuiStartController) startPaneLoader.getController();
        setGuiStartController(guiStartController);
        guiStartController.setLoginScene(firstScene);
        guiStartController.setReconnectScene(reconnectScene);
        guiStartController.setStageAndSetupListeners(stage);

        guiReconnectionController = (GuiReconnectionController) reconnectPaneLoader.getController();
        setGuiReconnectionController(guiReconnectionController);
        guiReconnectionController.setThirdScene(thirdScene);
        guiReconnectionController.setStageAndSetupListeners(stage);


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
        guiMapController.setEndScene(endScene);
        guiMapController.setStageAndSetupListeners(stage);

        guiEndController = (GuiEndController) endPageLoader.getController();
        setGuiEndController(guiEndController);
        guiEndController.setUp();

        //stage.setScene(firstScene);
        stage.setScene(startScene);
        stage.show();
    }

    @Override
    public void startUI() {
        launch();
    }

    @Override
    public void startGame() {
        isReconnection = false;
        Platform.runLater( () -> {
            System.out.println("guiLobbyController: " + guiLobbyController);
            //guiMapController.mapCreator();
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

            if(s.equals("reconnectok")){
                receivedReconnectOk = true;
                for (int i = 0; i < view.getCacheModel().getCachedPlayers().size(); i++) {
                    guiMapController.statsUpdater(i);
                }
                isReconnection = false;
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
                if(isReconnection){
                    System.out.println("STATS RICEVUTO MA SKIPPATO oerchè sono in riconnessione");
                } else {
                    if (view.getCacheModel().getCachedPlayers().get(playerId).getStats() != null) {
                        if (!view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline()) {
                            wasOnline.set(playerId, false);
                            String msg = "Il giocatore " + playerId + " si è disconnesso";
                            guiMapController.onlineStateSignal(msg);
                        } else if (view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline() &&
                                !wasOnline.get(playerId)) {
                            //player reconnected
                            String msg = "Il giocatore " + playerId + " si è riconnesso";
                            wasOnline.set(playerId, true);
                            guiMapController.onlineStateSignal(msg);
                        }
                    }

                    guiMapController.statsUpdater(playerId);
                }

                break;

            case INITIAL:
                if(view.getPlayerId()!=-1)
                System.out.println("Giocatore con id= "+view.getPlayerId()+" nome: "+view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getName());
                guiMapController.initial();

                if(isReconnection) {
                    //TODO Devo risettarmi il mio vecchio id nella view
                    for (int i = 0; i < view.getCacheModel().getCachedPlayers().size(); i++) {
                        if(view.getCacheModel().getCachedPlayers().get(i).getName().equals(guiReconnectionController.getPlayerName())){
                            getView().setPlayerId(i);
                        }
                    }

                }

                for(Player item:view.getCacheModel().getCachedPlayers()) {
                    guiMapController.loginUpdater(item.getName(), item.getPlayerId(), item.getPlayerColor());

                }
                break;

            case POWERUP_BAG:
                //if(isReconnection)
                //    break;
                guiMapController.powerUpDisplayer();
                break;

            case AMMO_CELL://remember--- at end turn i need to replace everyCell, even if it has something inside!
                guiMapController.ammoPlacer();
                break;

            case SPAWN_CELL://weapons changed don't need nothing
                guiMapController.spawnCellWeaponsUpdate();
                break;

            case WEAPON_BAG:
                //if(isReconnection)
                //    break;
                guiMapController.changedWeapons();//display my new weapons
                break;
            case AMMO_BAG:
                //if(isReconnection)
                //    break;
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
        //TODO questo metodo è chiamato all'avvio della view -> va messo qui il codice per cambiare alla schermata
        //TODO della mappa?  però viene chiamato anche quando non è ancora iniziato gioco e nullpointera
      /*  Platform.runLater( () -> {
            System.out.println("guiLobbyController: " + guiLobbyController);
            guiMapController.mapCreator();
            guiReconnectionController.openThirdScene(new ActionEvent());
        });*/

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
        if(isFrenzy)
        {
            guiMapController.printLog("ATTENZIONE !!! Attivzione della fase di FRENESIA FINALE");
            guiMapController.setFrenzy();
            if(isBeforeFrenzyStarter)
                guiMapController.setBeforeFrenzyStarter();
        }
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
    public void doFrenzyAtomicShoot() {
        //TODO
    }

    @Override
    public void doFrenzyReload() {
        //TODO
    }

    @Override
    public void startReload() {
        //view.doAction(new SkipAction());
        guiMapController.checkReload();

    }

    @Override
    public void endGame() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                guiMapController.openEndScene(new ActionEvent());
                guiEndController.showPoints(new ActionEvent());
            }
        });

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
        List<Integer> values = new ArrayList<>();


        Platform.runLater( () -> {
            System.out.println("Nel runLater");

            Alert a = new Alert(Alert.AlertType.INFORMATION, "Seleziona quale  mappa usare");
            a.showAndWait();
            //platfrm.runlater qui
            if (view.getCacheModel().getCachedPlayers().size() == 3)//tipo 1 o 2
            {
                ButtonType first = new ButtonType("Per 3/4 giocatori");// map 1
                ButtonType second = new ButtonType("Qualsiasi numero");//map 2
                Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli una mappa ", first, second);
                alert.showAndWait();
                if (alert.getResult() == first) {
                    values.add(1);
                } else {
                    values.add(2);
                }
            } else if (view.getCacheModel().getCachedPlayers().size() == 5) //tipo 2 o 3
            {
                ButtonType first = new ButtonType("Per 4/5 giocatri");// map 1
                ButtonType second = new ButtonType("Qualsiasi numero");//map 2
                Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli una mappa ", first, second);
                alert.showAndWait();
                if (alert.getResult() == first) {
                    values.add(3);
                } else {
                    values.add(2);
                }
            } else {//tipo 1 o 2 o 3
                ButtonType first = new ButtonType("Per 3/4 giocatori");// map 1
                ButtonType third = new ButtonType("Per 4/5 giocatri");// map 1
                ButtonType second = new ButtonType("Qualsiasi numero");//map 2
                Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli una mappa ", first, second, third);
                alert.showAndWait();
                if (alert.getResult() == first) {
                    values.add(1);
                } else if (alert.getResult() == second) {
                    values.add(2);
                } else {
                    values.add(3);
                }
            }
            //-----------------------------------ask for skulls
            values.add(skullSelector());
            bb=true;
        });
        while(!bb)
        {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bb=false;
        System.out.println(values);
        return values;
    }

    private int skullSelector()
    {
        List<String> choices = new ArrayList<>();
        choices.add("1");
        choices.add("2");
        choices.add("3");
        choices.add("4");
        choices.add("5");
        choices.add("6");
        choices.add("7");
        choices.add("8");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("8", choices);
        dialog.setTitle("Teschi");
        dialog.setContentText("Seleziona il numero di teschi per questa partita:");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            return Integer.parseInt(result.get());
        }
        else{
            return skullSelector();
        }

    }
}
