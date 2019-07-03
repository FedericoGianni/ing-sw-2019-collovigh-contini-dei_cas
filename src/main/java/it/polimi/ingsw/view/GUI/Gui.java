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

    /**
     * default min widht of the start, reconnect, lobby and end scenes
     */
    public static final float DEFAULT_MIN_WIDTH = 920;

    /**
     * default min height of the start, reconnect, lobby and end scenes
     */
    public static final float DEFAULT_MIN_HEIGHT = 540;

    /**
     * default min widht of the main game scene
     */
    public static final float DEFAULT_MIN_WIDTH_MAP = 1000;

    /**
     * default min height of the main game scene
     */
    public static final float DEFAULT_MIN_HEIGHT_MAP = 700;

    /**
     * Reference to the controller of the first scene
     */
    private static GuiStartController guiStartController;

    /**
     * Reference to the controller of the reconnection scene
     */
    private static GuiReconnectionController guiReconnectionController;

    /**
     * Reference to the controller of the login scene
     */
    private static GuiController guiController;

    /**
     * Reference to the controller of the lobby scene
     */
    private static GuiLobbyController guiLobbyController;

    /**
     * Reference to the controller of the main game scene
     */
    private static GuiMapController guiMapController;

    /**
     * Reference to the controller of the game end scene
     */
    private static GuiEndController guiEndController;

    /**
     * used to ask the server if the specified direction is valid (= no walls), since the cacheModel version
     * doesn't store the cell adjacences we need to ask the server if a direction is valid or not
     */
    private int validMove=-1;

    /**
     * True if it is a reconnection, false if it is a normal game login from the beginning
     * It will be set to false when gameStart is called, which is being called only at the beginning of the match so
     * reconnection will have this boolean to true, to manage some specific GUI case in which you need to
     * ignore first updates after reconnection
     */
    private boolean isReconnection=true;

    /**
     * After the GUI receives reconnectok, it means that it can start to handle again stats updates instead of skipping them,
     * need this only after reconnections
     */
    private boolean receivedReconnectOk = false;

    /**
     * used to store a previous version of online status, to check if user is reconnecting or the stats updates are the
     * same as before
     */
    private List<Boolean> wasOnline = new ArrayList<>(Collections.nCopies(5, true));

    private  boolean bb=false;

    /**
     *
     * @param guic reference to the gui lobby controller to be set
     */
    public void setGuiLobbyController(GuiLobbyController guic) {
        this.guiLobbyController = guic;
    }

    /**
     *
     * @return the actual reference to the lobby controller
     */
    public GuiLobbyController getGuiLobbyController(){return guiLobbyController;
    }

    /**
     * set the reference to the gui map controller
     * @param guic reference to the gui map controller to set
     */
    public void setGuiMapController(GuiMapController guic) {
        this.guiMapController = guic;
    }

    /**
     * set the reference to the gui controller
     * @param guiController reference to the gui controller to set
     */
    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    /**
     * set the reference to the gui start controller
     * @param g reference to the gui start controller to set
     */
    public void setGuiStartController(GuiStartController g) {
        this.guiStartController = g;
    }

    /**
     * set the reference to the gui reconnection controller
     * @param g reference to the gui reconnection controller to set
     */
    public void setGuiReconnectionController(GuiReconnectionController g) {
        this.guiReconnectionController = g;
    }

    /**
     * set the reference to the gui end controller
     * @param g reference to the gui end controller to set
     */
    public void setGuiEndController(GuiEndController g) {
        this.guiEndController = g;
    }

    /**
     * set the boolean isReconnection to the value passed as parameter
     * @param b boolean
     */
    public void setIsReconnection(Boolean b) {
        this.isReconnection=b;
    }

    /**
     * @return isReconnection boolean current value
     */
    public boolean getIsReconnection() {
        return this.isReconnection;
    }

    /**
     * default contructor
     */
    public Gui(){
    super();
    }

    /**
     * reference to the view linked to this UserInterface
     */
    private static View view;

    /**
     * set the view with the one passed as paramter
     * @param v view to be set as view
     */
    public void setView(View v) {
        view = v;
    }

    /**
     * @return the actual reference to the view
     */
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
        FXMLLoader startPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/start.fxml"));
        Parent startPane = startPaneLoader.load();
        Scene startScene = new Scene(startPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);

        //reconnect window
        FXMLLoader reconnectPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/reconnect.fxml"));
        Parent reconnectPane = reconnectPaneLoader.load();
        Scene reconnectScene = new Scene(reconnectPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);

        // login window
        // loader will then give a possibility to get related controller
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/Login.fxml"));  // TEST
        Parent firstPane = firstPaneLoader.load();
        Scene firstScene = new Scene(firstPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        //firstScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());  // non serve : basta specificare nell FXML ( by d)

        // game lobby window
        FXMLLoader secondPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/sampleLobby.fxml"));
        Parent secondPane = secondPageLoader.load();
        Scene secondScene = new Scene(secondPane, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        //secondScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("styleLobby.css").toExternalForm());  // non serve : basta specificare nell FXML ( by d)

        // main game window
        FXMLLoader thirdPageLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/sampleMap.fxml"));
        Parent thirdPane = thirdPageLoader.load();
        Scene thirdScene = new Scene(thirdPane, DEFAULT_MIN_WIDTH_MAP, DEFAULT_MIN_HEIGHT_MAP);
        thirdScene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("gui/styleMap.css").toExternalForm());

        FXMLLoader endPageLoader = new FXMLLoader((getClass().getClassLoader().getResource("gui/end.fxml")));
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void startUI() {
        launch();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        isReconnection = false;
        Platform.runLater( () -> {
            System.out.println("guiLobbyController: " + guiLobbyController);
            //guiMapController.mapCreator();
            guiLobbyController.openThirdScene(new ActionEvent());
        });
    }


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void login() {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void retryLogin(String error) {
        Platform.runLater( () -> {
            System.out.println("[DEBUG] Chiamato retrylogin con errore: " + error);
            show(error);
        });

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void retryLogin(Exception e) {

    }


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {
        guiMapController.granade();

    }


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyAtomicShoot() {
        Platform.runLater( () -> {
            guiMapController.shootWeaponChooser(null);
        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyReload() {
        guiMapController.askFrenzyReload();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {
        //view.doAction(new SkipAction());
        guiMapController.checkReload();

    }


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidMove(boolean b) {
        if(b){
            guiMapController.setValidMove(1);
        } else {
            guiMapController.setValidMove(0);
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        guiMapController.log.appendText(DEFAULT_TIMER_EXPIRED);
        System.exit(0);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> askMapAndSkulls() {
        System.out.println("ask skulls");
        /*List<Integer> values = new ArrayList<>();


        Platform.runLater( () -> {
            System.out.println("Nel runLater");

            Alert a = new Alert(Alert.AlertType.INFORMATION, "Seleziona quale  mappa usare");
            a.showAndWait();

                ButtonType first = new ButtonType("Tipo 1\nOttima Per 3/4 giocatori");// map 1
                ButtonType third = new ButtonType("Tipo 3\nOttima Per 4/5 giocatri");// map 1
                ButtonType second = new ButtonType("tipo 2 \nOttima Qualsiasi numero");//map 2
                Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli una mappa ", first, second, third);
                alert.showAndWait();
                if (alert.getResult() == first) {
                    values.add(1);
                } else if (alert.getResult() == second) {
                    values.add(2);
                } else {
                    values.add(3);
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
        return values;*/
        List<Integer> values = new ArrayList<>();
        values.add(2);
        values.add(1);
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
