package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.UiHelpers;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.ScopeAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.EffectType;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.Directions.*;
import static java.lang.Thread.sleep;

/**
 * contains the controller of the gui, alla the methods that works on the gui are here
 */
public class GuiMapController {

    /**
     * if an effect need a cell is true
     */
    private static boolean needCell;
    /**
     * Reference to the gui linked to this controller
     */
    private static Gui gui;
    /**
     * define the rows and columns of the map
     */
    private final int rows = 3, col = 4;
    private VBox map[][];

    /**
     * set the gui parameter
     * @param g gui to set as reference
     */
    public static void setGui(Gui g) {
        gui = g;
    }

    /**
     * reference to the end game scene
     */
    private static Scene endScene;

    /**
     * reference to the Stage, needed to switch scenes
     */
    private static Stage myStage;
    /**
     * cosntains the directions
     */
    private ArrayList<Directions> movementDirections;

    /**
     * used to ask the server if the specified direction is valid (= no walls), since the cacheModel version
     * doesn't store the cell adjacences we need to ask the server if a direction is valid or not
     */
    private int validMove = -1;

    /**
     * true if, in frenzy phase, the current player is before the first player, false otherwise
     */
    private boolean isBeforeFrenzyStarter;

    /**
     * current actionType useful for distinguish trough shoot,frenzy shoot , move and grab ecc
     */
    private List<String> actionTypes;

    /**
     * true if the action called on the UI by the controller is on frenzy phase
     */
    private boolean isFrenzy = false;

    /**
     * set isFrenzy value to true
     */
    public void setFrenzy(){
        isFrenzy=true;
    }

    /**
     * set the end scene reference
     * @param scene reference to the end game scene
     */
    public void setEndScene(Scene scene){
        endScene = scene;
    }

    /**
     * open the end game scene after the game has ended
     * @param actionEvent
     */
    public void openEndScene(ActionEvent actionEvent) {
        myStage.setScene(endScene);
        myStage.show();
    }

    /**
     * Gui Map controller initializer to set the reference to the stage, used to switch scenes
     * @param stage
     */
    public static void setStageAndSetupListeners(Stage stage){
        myStage = stage;
    }

    /**
     * Set the parameter isBeforeFrenzyStarter to true
     */
    public void setBeforeFrenzyStarter()
    {
        isBeforeFrenzyStarter=true;
    }

    /**
     * background pane of the gui
     */
    @FXML
    BorderPane pane;
    /**
     * log of the match
     */
    @FXML
    TextArea log;
    /**
     * map displayed
     */
    @FXML
    GridPane innerMap;
    /**
     * cells, all VBoxes
     */
    @FXML
    VBox b00, b01, b02, b03, b10, b11, b12, b13, b20, b21, b22, b23;
    /**
     * the control panel
     */
    @FXML
    HBox plance;
    /**
     * powerUps ,weapons and weapons ofthe current player
     */
    @FXML
    ImageView powerUp1, powerUp2, powerUp3, weapon1, weapon2, weapon3, myWeapon1, myWeapon2, myWeapon3;
    /**
     * action buttons on the bottom
     */
    @FXML
    Button stopMov, moveButton, grabButton, moveGrabButton, shootButton;



    //-------------------------------------------------------MAP CREATION and gestion methods
    @FXML
    public void initialize() {

    }



    /**
     * Notify update regarding other players actions
     *
     * @param turnUpdate
     */
    protected void notifyTurnUpdate(TurnUpdate turnUpdate) {

        PowerUpTurnUpdate powerUpTurnUpdate;
        ShootTurnUpdate shootTurnUpdate;
        GrabTurnUpdate grabTurnUpdate;
        MoveTurnUpdate moveTurnUpdate;

        switch (turnUpdate.getActionType()) {

            case POWERUP:

                powerUpTurnUpdate = (PowerUpTurnUpdate) turnUpdate;
                printLog("[!] Il giocatore " + turnUpdate.getPlayerId() +
                        " ha usato il powerUp " + powerUpTurnUpdate.getPowerUp());
                break;

            case SHOOT:

                shootTurnUpdate = (ShootTurnUpdate) turnUpdate;
                printLog("[!] Il giocatore " + turnUpdate.getPlayerId() +
                        " ha sparato con l'arma " + UiHelpers.weaponTranslator(shootTurnUpdate.getWeapon()) + " al player con id: " +
                        shootTurnUpdate.getTargetId());
                break;

            case GRAB:

                grabTurnUpdate = (GrabTurnUpdate) turnUpdate;

                if (grabTurnUpdate.getWeapon() != null) {
                    printLog("[!] Il giocatore " + turnUpdate.getPlayerId() +
                            " ha raccolto " + UiHelpers.weaponTranslator(grabTurnUpdate.getWeapon()));
                } else {
                    printLog("[!] Il giocatore " + turnUpdate.getPlayerId() +
                            " ha raccolto ");
                }

                break;

            case MOVE:

                moveTurnUpdate = (MoveTurnUpdate) turnUpdate;
                printLog("[!] Il giocatore " + turnUpdate.getPlayerId() +
                        " si è mosso");

                break;

            default:

                break;
        }
    }

    /**
     * crreate the map and the backgrounds
     */
    public void mapCreator() {
        System.out.println();
        switch (gui.getView().getCacheModel().getMapType()) {
            case 1: {
                innerMap.setStyle("-fx-background-image: url('/images/Map1in.png')");//use this for sample fot he
                buttonCreator();
                System.out.println("Crea butun, "+map[1][1]);
                b03.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });
                b20.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });

                break;
            }
            case 2: {
                buttonCreator();
                System.out.println("Crea butun, "+map[1][1]);
                innerMap.setStyle("-fx-background-image: url('/images/Map2in.png')");//use this for sample fot he maps everytime
                b20.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });
                break;
            }
            case 3: {
                buttonCreator();
                System.out.println("Crea butun, "+map[1][1]);
                innerMap.setStyle("-fx-background-image: url('/images/Map3in.png')");//use this for sample fot he
                break;
            }

        }
        actionTypes = new ArrayList<>();
        actionTypes.add("MOVE");
        actionTypes.add("MOVE&GRAB");
        actionTypes.add("SHOOT");

        //-------plance creator
        for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().size(); i++) {
            VBox vb = new VBox();

            Label lbl = new Label("Giocatore: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getName());
            vb.getChildren().add(lbl);

            Label lbl2 = new Label("Danni subiti: 0");
            vb.getChildren().add(lbl2);

            Label lbl3 = new Label("Marchi ricevuti: 0");
            vb.getChildren().add(lbl3);

            Label lbl4 = new Label("Punti : 0");
            vb.getChildren().add(lbl4);

            Label lbl5 = new Label("Morti subite: 0");
            vb.getChildren().add(lbl5);

            Label lbl6 = new Label("Armi: Nessuna.");
            vb.getChildren().add(lbl6);
            Label lbl7;
            if (gui.getView().getCacheModel().getCachedPlayers().get(i).getAmmoBag() == null) {
                lbl7 = new Label("Munizioni: Nessuna.");
                vb.getChildren().add(lbl7);
            } else {
                lbl7 = new Label("Munizioni: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getAmmoBag().getAmmoList());
                vb.getChildren().add(lbl7);
            }


            plance.getChildren().add(vb);
        }


        weaponSeeEventEnabler();
    }

    /**
     * enable and always keep enabled the event of see weapons in spawn cell
     */
    private void weaponSeeEventEnabler() {
        b02.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                spawnCellWeaponShow(0, 2);
            }
        });

        b10.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                spawnCellWeaponShow(1, 0);
            }
        });
        b23.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                spawnCellWeaponShow(2, 3);
            }
        });

    }


    /**
     * Initialize the gui and get updates if reconnecting
     *
     */
    public void initial()
    {

        Platform.runLater( () -> {
            this.mapCreator();

            for(int i=0;i<gui.getView().getCacheModel().getCachedPlayers().size();i++) {
                if (gui.getView().getCacheModel().getCachedPlayers().get(i).getStats() != null)
                    gui.notifyUpdate(UpdateType.STATS, i, null);

                if (gui.getView().getCacheModel().getCachedPlayers().get(i).getWeaponbag() != null)
                    gui.notifyUpdate(UpdateType.WEAPON_BAG, i, null);

                if (gui.getView().getCacheModel().getCachedPlayers().get(i).getPowerUpBag() != null)
                    gui.notifyUpdate(UpdateType.POWERUP_BAG, i, null);

            }
            //gui.getGuiLobbyController().openThirdScene(new ActionEvent());

        });

    }

    /**
     * create the buttons for the gui
     *
     */
    private void buttonCreator() {
        map= new VBox[rows][col];
        for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS); // allow row to grow
            rc.setFillHeight(true); // ask nodes to fill height for row

            innerMap.getRowConstraints().add(rc);
        }
        for (int colIndex = 0; colIndex < 4; colIndex++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS); // allow column to grow
            cc.setFillWidth(true); // ask nodes to fill space for column

            innerMap.getColumnConstraints().add(cc);
        }

        b00.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b01.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b02.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b03.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        b10.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b11.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b12.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b13.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        b20.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b21.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b22.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b23.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        map[0][0] = b00;
        map[0][1] = b01;
        map[0][2] = b02;
        map[0][3] = b03;

        map[1][0] = b10;
        map[1][1] = b11;
        map[1][2] = b12;
        map[1][3] = b13;

        map[2][0] = b20;
        map[2][1] = b21;
        map[2][2] = b22;
        map[2][3] = b23;
    }


    /**
     * enables the action buttons for the gui, like muovi spara ecc
     *
     */
    @FXML
    public void actionButtonsEnabler() {

        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert a=new Alert(Alert.AlertType.CONFIRMATION,"Puoi fare un' azione");
                a.show();
                actionButtonDisable();//i need to disable everything else
                if(isFrenzy && !isBeforeFrenzyStarter)
                {
                     a=new Alert(Alert.AlertType.INFORMATION,"Non è disponibile questa azione");
                    a.show();
                }
                else {
                    move("MOVE");
                }
            }
        });
        shootButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag() != null) {
                    actionButtonDisable();//i need to disable everything else
                    move("SHOOT");
                } else {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "No puoi sparare senza armi");
                    a.show();
                }

            }
        });

        grabButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionButtonDisable();//i need to disable everything else
                List<Directions> dir = new ArrayList<>();
                grabHere(-1, -1, dir);//means grab here

            }
        });

        moveGrabButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionButtonDisable();//i need to disable everything else
                move("MOVE&GRAB");

            }
        });
    }

    /**
     * disables the gui buttons events
     *
     *
     */
    @FXML
    public void actionButtonDisable()//disable action buttons
    {
        moveButton.setOnAction(null);
        stopMov.setOnAction(null);
        grabButton.setOnAction(null);
        moveGrabButton.setOnAction(null);
        shootButton.setOnAction(null);
        mapEventDeleter();
    }

    /**
     * disables the gui buttons and events, clean everything in the gui
     */
    private void mapEventDeleter()//disable map events except for spawn see, it renables it
    {
        for (int i = 0; i < rows; i++)//reset buttons on the map to do nothing
        {
            for (int j = 0; j < col; j++) {
                map[i][j].setOnMouseClicked(null);
                map[i][j].setOnMousePressed(null);
            }
        }

        weapon1.setOnMouseClicked(null);
        weapon2.setOnMouseClicked(null);
        weapon3.setOnMouseClicked(null);
        weapon1.setOnMousePressed(null);
        weapon2.setOnMousePressed(null);
        weapon3.setOnMousePressed(null);
        myWeapon1.setOnMouseClicked(null);
        myWeapon2.setOnMouseClicked(null);
        myWeapon3.setOnMouseClicked(null);
        myWeapon1.setOnMousePressed(null);
        myWeapon2.setOnMousePressed(null);
        myWeapon3.setOnMousePressed(null);

        powerUp1.setOnMouseClicked(null);
        powerUp2.setOnMouseClicked(null);
        powerUp3.setOnMouseClicked(null);
        powerUp1.setOnMousePressed(null);
        powerUp2.setOnMousePressed(null);
        powerUp3.setOnMousePressed(null);
        weaponSeeEventEnabler();
    }

    /**
     * print s on the player log
     * @param s
     */
    public void printLog(String s) {

        Platform.runLater(() -> { log.appendText("\n" + s);
        });
    }

    /**
     * remove clickable effects form the player---used from mapEventDeleter
     */
    private void playersEffectRemover() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < col; y++) {
                for (int id = 0; id < gui.getView().getCacheModel().getCachedPlayers().size(); id++) {
                    if (map[x][y].getChildren().size() == 1)//primo HBOX
                    {
                        int j = 0;
                        boolean found = false;
                        while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
                        {
                            //System.out.println("Confronto: "+((HBox)map[x][y].getChildren().get(0)).getChildren().get(j).getId()+" - "+id);

                            if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                found = true;
                                break;
                            }
                            j++;
                        }
                        if (found) {

                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                }
                            });
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                }
                            });
                        }
                    } else if (map[x][y].getChildren().size() == 2) {//primo e secondo HBOX
                        int j = 0;
                        boolean found = false;

                        while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size()) {
                            //System.out.println("Confronto: "+((HBox)map[x][y].getChildren().get(0)).getChildren().get(j).getId()+" - "+id);

                            if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                found = true;
                                break;
                            }
                            j++;
                        }
                        if (found) {

                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                }
                            });
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                }
                            });
                            continue;
                        }
                        j = 0;
                        while (((HBox) map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id)) != 0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
                        {
                            j++;
                        }
                        {

                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                }
                            });
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /**
     * updates the spwan cell weapons
     * outdated
     */
    public void spawnCellWeaponsUpdate() {
    }

    /**
     * Show when someone login/out
     * @param msg
     */
    public void onlineStateSignal(String msg)
    {
        System.out.println(msg);

        printLog(msg);
    }

    //------------------------------------------------------------Weapons show methods

    /**
     * enable the show of the weapons
     * @param r
     * @param c
     */
    private void spawnCellWeaponShow(int r, int c) {
        //prima di tutto quali sono le immagini
        weapon1.setFitHeight(156);
        weapon1.setFitWidth(100);
        weapon2.setFitHeight(156);
        weapon2.setFitWidth(100);
        weapon3.setFitHeight(156);
        weapon3.setFitWidth(100);
        for (int i = 0; i < ((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(r, c)).getWeaponNames().size(); i++) {
            String url = fromWNameToUrl(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(r, c)).getWeaponNames().get(i));
            weaponDisplayer(url, i);
        }
    }

    /**
     * routine method that thakes the weapon name and turns it into his image
     * @param name
     * @return
     */
    private String fromWNameToUrl(String name) {

        switch (name) {
            case "LOCK RIFLE":
                return "/images/weapons/distruttore.png";
            case "MACHINE GUN":
                return "/images/weapons/mitragliatrice.png";
            case "ELECTROSCYTHE":
                return "/images/weapons/falceProtonica.png";
            case "TRACTOR BEAM":
                return "/images/weapons/raggioTraente.png";
            case "T.H.O.R.":
                return "/images/weapons/torpedine.png";
            case "PLASMA GUN":
                return "/images/weapons/fucilePlasma.png";
            case "WHISPER":
                return "/images/weapons/fucilePrecisione.png";
            case "VORTEX CANNON":
                return "/images/weapons/cannoneVortex.png";
            case "FURNACE":
                return "/images/weapons/vulcanizzatore.png";
            case "HEATSEEKER":
                return "/images/weapons/razzoTermico.png";
            case "HELLION":
                return "/images/weapons/raggioSolare.png";
            case "FLAMETHROWER":
                return "/images/weapons/lanciaFiamme.png";
            case "GRENADE LAUNCHER":
                return "/images/weapons/lanciaGranate.png";
            case "ROCKET LAUNCHER":
                return "/images/weapons/lanciaRazzi.png";
            case "RAILGUN":
                return "/images/weapons/fucileLaser.png";
            case "CYBERBLADE":
                return "/images/weapons/spadaFotonica.png";
            case "ZX-2":
                return "/images/weapons/zx2.png";
            case "SHOTGUN":
                return "/images/weapons/fucilePompa.png";
            case "POWER GLOVE":
                return "/images/weapons/cyberGuanto.png";
            case "SHOCKWAVE":
                return "/images/weapons/ondaUrto.png";
            case "SLEDGEHAMMER":
                return "/images/weapons/martelloIonico.png";

        }
        return null;
    }

    /**
     * set the image for real
     * @param url
     * @param weapon
     */
    private void weaponDisplayer(String url, int weapon) {
        Image img = new Image(url);

        switch (weapon) {
            case 0:
                weapon1.setImage(img);
                break;
            case 1:
                weapon2.setImage(img);
                break;
            case 2:
                weapon3.setImage(img);
                break;
        }

    }

    /**
     * show the cost when mouse over
     * @param x
     * @param y
     */
    private void costDisplay(int x, int y)//display in a tootltip the cost of the pointed weapon
    {
        String cost1 = "";
        String cost2 = "";
        String cost3 = "";

        try {
            for (int i = 0; i < gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0)).getBuyEffect().size(); i++) {
                if (gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0)).getBuyEffect().size() != 0) {
                    if (gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0)).getBuyEffect().get(i) != null)
                        cost1 = cost1 + fromACtoString(gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0)).getBuyEffect().get(i));
                }
            }
            for (int i = 0; i < gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1)).getBuyEffect().size(); i++) {
                if (gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1)).getBuyEffect().size() != 0) {
                    if (gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1)).getBuyEffect().get(i) != null)
                        cost2 = cost2 + fromACtoString(gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1)).getBuyEffect().get(i));
                }
            }
            for (int i = 0; i < gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2)).getBuyEffect().size(); i++) {
                if (gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2)).getBuyEffect().size() != 0) {
                    if (gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2)).getBuyEffect().get(i) != null)
                        cost3 = cost3 + fromACtoString(gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2)).getBuyEffect().get(i));
                }
            }
        } catch (WeaponNotFoundException e)//can't happend in this special case
        {
            System.out.println("-----------weapon not found in weaponTooltip---------");
        }
        Tooltip t1 = new Tooltip("Costo " + cost1);
        Tooltip t2 = new Tooltip("Costo " + cost2);
        Tooltip t3 = new Tooltip("Costo " + cost3);
        Tooltip.install(weapon1, t1);
        Tooltip.install(weapon2, t2);
        Tooltip.install(weapon3, t3);

    }

    /**
     * translator of the ammo to String
     * @param c
     * @return
     */
    private String fromACtoString(Color c) {
        if (c == Color.RED) {
            return " 1 Ammo rossa";
        }
        if (c == Color.BLUE) {
            return " 1 Ammo blu";
        }
        if (c == Color.YELLOW) {
            return " 1 Ammo gialla";
        }
        return " non pervenuto";
    }

    //-------------------------------------------------------------movements things

    /**
     *
     * VERY IMPORTANT - Every action with movements passes from this method,
     * differenciates the movement actions like shoot, move and grab ecc..
     *
     * @param actionType
     */
    @FXML
    private void move(String actionType) {
        movementDirections = new ArrayList<>();
        int x = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosX();
        int y = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosY();
        switch (actionType) {
            case "MOVE":
                System.out.println("MOVE in move();");
                //if non frenzy
                Platform.runLater(() -> {
                    if(isFrenzy)
                    {
                        if(isBeforeFrenzyStarter) {
                            handleMovement(x, y, UiHelpers.DEFAULT_MAX_FRENZY_MOVES, movementDirections, actionType);
                        }
                        //else can't occour
                    }else {
                        handleMovement(x, y, UiHelpers.DEFAULT_MAX_NORMAL_MOVES, movementDirections, actionType);
                    }
                    });
                //if frenzy cases here--->
                //Platform.runLater(() ->  {handleMovement(x,y,FRENZY_MOV,movementDirections);});
                break;
            case "MOVE&GRAB":
                if (isFrenzy && isBeforeFrenzyStarter)
                {
                    Platform.runLater(() -> {
                        handleMovement(x, y, UiHelpers.DEFAULT_MOVES_WITH_FRENZY, movementDirections, actionType);
                    });
                }
                else if (isFrenzy && !isBeforeFrenzyStarter) {
                    Platform.runLater(() -> {
                        handleMovement(x, y, UiHelpers.DEFAULT_MOVES_WITH_ENHANCED_FRENZY, movementDirections, actionType);
                    });
                }
                else if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getDmgTaken().size() < UiHelpers.DEFAULT_DMG_TO_UNLOCK_ENHANCED_GRAB ) {
                    Platform.runLater(() -> {
                        handleMovement(x, y, UiHelpers.DEFAULT_MOVES_WITH_GRAB, movementDirections, actionType);
                    });
                } else  {//one move more here
                    Platform.runLater(() -> {
                        handleMovement(x, y, UiHelpers.DEFAULT_ENHANCED_MOVES_WITH_GRAB, movementDirections, actionType);
                    });
                }
                break;
            case "SHOOT":
                if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getDmgTaken().size() < 6 && !isFrenzy) {
                    Platform.runLater(() -> {
                        shootWeaponChooser( movementDirections);
                    });
                } else if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getDmgTaken().size() >= 6 || isFrenzy) {//one move more here before shoot
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi fare un movimento prima di sparare?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.NO) {
                        Platform.runLater(() -> {
                            if (isFrenzy)//send empty mov
                            {
                                System.out.println("Non vuoi fare mov  prima di sparare");
                                List<Directions> dir = new ArrayList<>();
                                gui.getView().doAction(new FrenzyShoot(new Move(dir, gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosition())));
                            }else{//standard shoot without moves
                                shootWeaponChooser(movementDirections);
                            }
                        });
                    } else {
                        Platform.runLater(() -> {
                            if(isFrenzy && isBeforeFrenzyStarter)
                            {
                                handleMovement(x, y, UiHelpers.DEFAULT_MOVES_WITH_FRENZY_SHOOT, movementDirections, actionType);
                            }
                            else if(isFrenzy && !isBeforeFrenzyStarter)
                            {
                                handleMovement(x, y, UiHelpers.DEFAULT_MOVES_WITH_ENHANCED_FRENZY_SHOOT, movementDirections, actionType);
                            }
                            else{//more tha 6 damages
                                handleMovement(x, y, UiHelpers.DEFAULT_ENHANCED_MOVES_WITH_SHOOT, movementDirections, actionType);
                            }

                        });
                    }

                }//else frenzy shit
                break;

        }

    }

    /**
     * gathers the position where i need to place the player in the gui
     * @param r is the row of the player
     * @param c is the column of the player
     * @param id the id of the player
     */
    private void mapPos(int r, int c, int id) {

        boolean found = false;
        if (map[r][c].getChildren().size() != 0 && ((HBox) map[r][c].getChildren().get(0)).getChildren() != null) {
            for (int j = 0; j < ((HBox) map[r][c].getChildren().get(0)).getChildren().size(); j++)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
            {
                if (((HBox) map[r][c].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                    found = true;
                }

            }
            if (found) return;
        }//if the player is already here don't re-put it
        fromIDtoIMG(id, map[r][c]);

        log.appendText("\n Placed player " + id + " in cell " + r + c);


        //eliminating the powerups effects after the beginning
        powerUp1.setOnMouseClicked((e) -> {

        });
        powerUp2.setOnMouseClicked((e) -> {

        });
        powerUp3.setOnMouseClicked((e) -> {

        });

        //afetr move i delete moving things
        mapEventDeleter();

    }

    /**
     * movement handler,get every direction and ask the server if valid
     * if is valid do the move then call event mover
     * @param x;
     * @param y;
     * @param m;
     * @param movementDirections;
     * @param actionType;
     */
    private void handleMovement(int x, int y, int m, ArrayList<Directions> movementDirections, String actionType)//called from move,do stuff for real
    {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Muovi la pedina in una cella adicente alla corrente \n hai ancora" + m + " mosse");
        a.showAndWait();
        a.setContentText(" Hai " + m + " altri movimenti");
        a.show();
        int M = m - 1;
        //enable button events

        stopMov.setOnAction(new EventHandler<ActionEvent>() {//stop button
            @Override
            public void handle(ActionEvent e) {
                switch (actionType) {
                    case "MOVE":
                        System.out.println("MOVE in hanldemovement e stoppo il moviment, mi muovo così : " + movementDirections);
                        gui.getView().doAction(new Move(movementDirections, new Point(x, y)));
                        actionButtonDisable();
                        break;
                    case "MOVE&GRAB":
                        grabHere(x, y, movementDirections);
                        break;
                    case "SHOOT":
                        System.out.println("SHOOT in hanldemovement e stoppo il moviment, mi muovo così : " + movementDirections);
                        if(!isFrenzy)
                        {shootWeaponChooser( movementDirections);}
                        else {
                            gui.getView().doAction(new FrenzyShoot(new Move(movementDirections, new Point(x,y))));
                        }
                        break;

                }

            }
        });

        //buttons here enable the movements in adjacent cells
        if (y < 3) {
            map[x][y + 1].setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("EAST", x, y)) {
                        movementDirections.add(Directions.EAST);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x][y + 1]);
                        eventMover(x, y + 1, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
        if (x < 2) {
            map[x + 1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("SOUTH", x, y)) {
                        movementDirections.add(Directions.SOUTH);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x + 1][y]);
                        eventMover(x + 1, y, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
        if (y - 1 >= 0) {
            map[x][y - 1].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("WEST", x, y)) {
                        movementDirections.add(Directions.WEST);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x][y - 1]);
                        eventMover(x, y - 1, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
        if (x - 1 >= 0) {
            map[x - 1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("NORTH", x, y)) {
                        movementDirections.add(NORTH);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x - 1][y]);
                        eventMover(x - 1, y, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
    }

    /**
     * Moves the movements listeners following the moving player
     *x and y are the acutal position (x=row y=column)
     * m is the remaining Moves and actionType is the action type
     * @param x
     * @param y
     * @param m
     * @param actionType
     */
    private void eventMover(int x, int y, int m, String actionType) {
        stopMov.setOnAction(new EventHandler<ActionEvent>() {//stop button
            @Override
            public void handle(ActionEvent e) {
                switch (actionType) {
                    case "MOVE":
                        System.out.println("MOVE in hanldemovement e stoppo il moviment, mi muovo così : " + movementDirections);

                        gui.getView().doAction(new Move(movementDirections, new Point(x, y)));
                        actionButtonDisable();
                        break;
                    case "MOVE&GRAB":
                        grabHere(x, y, movementDirections);
                        break;
                    case "SHOOT"://-------------------------if you have more than some damages like 6
                        System.out.println("SHOOT in hanldemovement e stoppo il moviment, mi muovo così : " + movementDirections);
                        if(!isFrenzy) {
                            shootWeaponChooser(movementDirections);
                        }
                        else{
                            gui.getView().doAction(new FrenzyShoot(new Move(movementDirections, new Point(x,y))));
                        }
                        break;

                }

            }
        });
        if (m == 0 && actionType.compareTo("MOVE") == 0)//movement
        {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Movimenti esauriti");
            a.show();
            gui.getView().doAction(new Move(movementDirections, new Point(x, y)));//updates the model for real
            actionButtonDisable();
            return;
        }
        if (m == 0 && actionType.compareTo("MOVE&GRAB") == 0)//move & grab actions
        {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Movimenti esauriti, raccolgo qui..");
            a.showAndWait();
            grabHere(x, y, movementDirections);//x and y are my position in the gui, not already in the model
            return;
        }
        if (m == 0 && actionType.compareTo("SHOOT") == 0) {
            if(!isFrenzy) {
                shootWeaponChooser(movementDirections);
            }
            else{
                gui.getView().doAction(new FrenzyShoot(new Move(movementDirections, new Point(x,y))));
            }
            return;
        }
        for (int i = 0; i < rows; i++)//reset buttons on the map to do nothing
        {
            for (int j = 0; j < col; j++) {
                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

                    }

                });
            }
        }
        int M = m - 1;
        //buttons here enable the movements in adjacent cells
        if (y < 3) {
            map[x][y + 1].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("EAST", x, y)) {
                        movementDirections.add(Directions.EAST);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x][y + 1]);

                        eventMover(x, y + 1, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
        if (x < 2) {
            map[x + 1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("SOUTH", x, y)) {
                        movementDirections.add(Directions.SOUTH);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x + 1][y]);
                        eventMover(x + 1, y, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
        if (y - 1 >= 0) {
            map[x][y - 1].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("WEST", x, y)) {
                        movementDirections.add(Directions.WEST);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x][y - 1]);
                        eventMover(x, y - 1, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }
        if (x - 1 >= 0) {
            map[x - 1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {
                    if (moveValidator("NORTH", x, y)) {
                        movementDirections.add(NORTH);
                        playerRemover(gui.getView().getPlayerId(), x, y);
                        fromIDtoIMG(gui.getView().getPlayerId(), map[x - 1][y]);
                        eventMover(x - 1, y, M, actionType);
                    } else {
                        eventMover(x, y, m, actionType);
                    }
                }
            });
        }

    }

    /**
     * takes the id returns the string of the image
     *
     * @param id
     *
     */
   private void fromIDtoIMG(int id, VBox b) {
        if (b.getChildren().size() == 0) {

            b.getChildren().add(new HBox());

            inserter(id, (HBox) b.getChildren().get(0));


            return;
        }
        if (((HBox) b.getChildren().get(0)).getChildren().size() == 3) {

            b.getChildren().add(new HBox());
            inserter(id, (HBox) b.getChildren().get(1));

            return;
        }

        if (((HBox) b.getChildren().get(0)).getChildren().size() <= 3) {
            inserter(id, (HBox) b.getChildren().get(0));
            return;
        }
        inserter(id, (HBox) b.getChildren().get(1));

    }

    /**
     *  search for the icon of the player with that id and deletes it
     * x and y are the acutal position (x=row y=column)
     *   id is
     * @param id
     * @param x
     * @param y
     */
    public void playerRemover(int id, int x, int y) {
        // System.out.println("Sto guardando cela :"+x+" "+y);
        if (map[x][y].getChildren().size() == 1)//primo HBOX
        {
            int j = 0;
            boolean found = false;
            while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
            {
                //System.out.println("Confronto: "+((HBox)map[x][y].getChildren().get(0)).getChildren().get(j).getId()+" - "+id);

                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                    found = true;
                    break;
                }
                j++;
            }
            if (found)
                ((HBox) map[x][y].getChildren().get(0)).getChildren().remove(j);
        } else if (map[x][y].getChildren().size() == 2) {//primo e secondo HBOX
            int j = 0;
            boolean found = false;

            while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
            {
                //System.out.println("Confronto: "+((HBox)map[x][y].getChildren().get(0)).getChildren().get(j).getId()+" - "+id);

                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                    found = true;
                    break;
                }
                j++;
            }
            if (found) {
                ((HBox) map[x][y].getChildren().get(0)).getChildren().remove(j);
                return;
            }
            j = 0;
            while (((HBox) map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id)) != 0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
            {
                j++;
            }
            ((HBox) map[x][y].getChildren().get(1)).getChildren().remove(j);
        }

    }

    /**
     * same as formIDToImg
     * @param id;
     * @param r;
     * @param c;
     */
    public void playerDisplay(int id,int r,int c)
    {
        fromIDtoIMG(id,map[r][c]);
    }

    /**
     * add the ImageView in the selected HBox
     * @param id;
     * @param h;
     */
    private void inserter(int id, HBox h) {
        ImageView img = new ImageView();
        Image image;
        switch (id) {
            case 0:
                image = new Image("/images/player0.png");
                img.setImage(image);
                img.setId("0");
                h.getChildren().add(img);
                break;
            case 1:
                image = new Image("/images/player1.png");
                img.setImage(image);
                img.setId("1");
                h.getChildren().add(img);
                break;
            case 2:
                image = new Image("/images/player2.png");
                img.setImage(image);
                img.setId("2");
                h.getChildren().add(img);
                break;
            case 3:
                image = new Image("/images/player3.png");
                img.setImage(image);
                img.setId("3");
                h.getChildren().add(img);
                break;
            case 4:
                image = new Image("/images/player4.png");
                img.setImage(image);
                img.setId("4");
                h.getChildren().add(img);
                break;
        }
    }

    /**
     * Method useful for movements. First the client calls askMoveValid, with a single direction, the the
     * server reply back to the client throught this setter, and it sets validMove to true if the direction is valid
     * (no walls, nor out of map), otherwise false
     * @param validMove boolean, true if direction is valid, false otherwise
     */
    public void setValidMove(int validMove) {
        this.validMove = validMove;
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * directly ask the server if a move is valid
     * @param dir;
     * @param x;
     * @param y;
     * @return;
     */
    private boolean moveValidator(String dir, int x, int y)//x and y are the arrive postions of the move dir is the direction
    {
        validMove = -1;
        gui.getView().askMoveValid(x, y, Directions.valueOf(dir));
        while (validMove == -1) {
            try {
                synchronized (this) {

                    this.wait();
                }

            } catch (InterruptedException e) {

            }

        }

        if (validMove == 1) {

            printLog("direzione valida");
            return true;

        } else {//validmove=0

           printLog("Direzione non valida");
            return false;
        }

    }


    //-------------------------------------------------------------loign methods and match beginning

    /**
     * Update the log about connections
     * @param name;
     * @param id;
     * @param color;
     */
    @FXML
    public void loginUpdater(String name, int id, PlayerColor color) {
        printLog("Si è collegato: " + name + " con l'id: " + id + " ed il colore: " + color);
    }

    /**
     * updates the control board of the players
     */
    public void planciaUpdater() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {//-----update plance
                for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().size(); i++) {
                    plance.getChildren().removeAll(plance.getChildren());
                }
                for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().size(); i++) {
                    VBox vb = new VBox();

                    Label lbl = new Label("Giocatore: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getName());
                    vb.getChildren().add(lbl);

                    if (gui.getView().getCacheModel().getCachedPlayers().get(i).getStats() == null) {
                        Label lbl2 = new Label("Danni subiti: 0");
                        vb.getChildren().add(lbl2);

                        Label lbl3 = new Label("Marchi ricevuti: 0");
                        vb.getChildren().add(lbl3);

                        Label lbl4 = new Label("Punti : 0");
                        vb.getChildren().add(lbl4);

                        Label lbl5 = new Label("Morti subite: 0");
                        vb.getChildren().add(lbl5);
                    } else {
                        Label lbl2;
                        if (gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getDmgTaken().isEmpty()) {
                            lbl2 = new Label("Danni subiti: 0");
                        } else {
                            lbl2 = new Label("Danni subiti: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getDmgTaken());
                        }
                        vb.getChildren().add(lbl2);


                        Label lbl3;
                        if (gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getMarks().isEmpty()) {
                            lbl3 = new Label("Marchi ricevuti: 0");
                        } else {
                            lbl3 = new Label("Marchi ricevuti: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getMarks());
                        }
                        vb.getChildren().add(lbl3);
                        Label lbl4 = new Label("Punti : " + gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getScore());
                        vb.getChildren().add(lbl4);

                        Label lbl5;
                        if (gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getDeaths() == 0) {
                            lbl5 = new Label("Numero di morti : 0");
                        } else {
                            lbl5 = new Label("Numero di morti : " + gui.getView().getCacheModel().getCachedPlayers().get(i).getStats().getDeaths());
                        }
                        vb.getChildren().add(lbl5);
                    }
                    //-------weapons display
                    Label lbl6;
                    if (gui.getView().getCacheModel().getCachedPlayers().get(i).getWeaponbag() != null) {
                        lbl6 = new Label("Armi: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getWeaponbag().getWeapons());
                    } else {
                        lbl6 = new Label("Armi: Nessuna.");
                    }
                    vb.getChildren().add(lbl6);

                    //------ammunitions display
                    Label lbl7;
                    if (gui.getView().getCacheModel().getCachedPlayers().get(i).getAmmoBag() == null) {
                        lbl7 = new Label("Munizioni: Nessuna.");
                    } else {
                        lbl7 = new Label("Munizioni: " + gui.getView().getCacheModel().getCachedPlayers().get(i).getAmmoBag().getAmmoList());
                    }
                    vb.getChildren().add(lbl7);
                    plance.getChildren().add(vb);
                }
            }
        });
    }

    /**
     * update the stats of the player id
     * @param id
     */
    public void statsUpdater(int id) {//the player is removed from its postion before the update

        /*if (!gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getOnline()) {
            log.appendText("\nIl giocatore " + id + " si è scollegato.");
            return;
        }*/
        printLog("Updated stats del player " + id);
        if (gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosition() != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int r = gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosX();
                    int c = gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosY();

                    for (int i = 0; i < rows; i++)//remove player icons from everywhere
                    {
                        for (int j = 0; j < col; j++) {
                            playerRemover(id, i, j);
                        }
                    }


                    mapPos(r, c, id);//positions the player
                    stopMov.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                        }
                    });


                }
            });
            planciaUpdater();
            mapEventDeleter();
        }
    }

    /**
     * Handles the user spawn, asking for a powerUp to discard and forwarding it to the view linked to this UI
     */
    public void startSpawn() {
        //diplaye the powerUps in the player's screen
        powerUpDisplayer();

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Choose a powerUp (on the right) to discard for the swpawn location");
        a.show();
        powerUp1.setOnMouseClicked((e) -> {//eliminate the effect
            System.out.println("cliccato pup 1");
            powerUp1.setImage(null);
            Color c = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0));
            mapPos(colorToCord(c).x, colorToCord(c).y, gui.getView().getPlayerId());
        });
        powerUp2.setOnMouseClicked((e) -> {
            System.out.println("cliccato pup 2");
            powerUp2.setImage(null);
            Color c = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1));
            mapPos(colorToCord(c).x, colorToCord(c).y, gui.getView().getPlayerId());
        });
        powerUp3.setOnMouseClicked((e) -> {
            System.out.println("cliccato pup 3");
            powerUp3.setImage(null);
            Color c = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2));
            mapPos(colorToCord(c).x, colorToCord(c).y, gui.getView().getPlayerId());
        });
    }


    //------------------------------------------------------------powerUp  gestion

    /**
     * return the location where a color of powerUp let you spawn
     * @param c
     * @return
     */
    private Point colorToCord(Color c) {
        Point p = new Point();
        if (c == Color.BLUE) {
            p.x = 0;
            p.y = 2;
            return p;
        }
        if (c == Color.RED) {
            p.x = 1;
            p.y = 0;
            return p;
        }
        if (c == Color.YELLOW) {
            p.x = 2;
            p.y = 3;
            return p;
        }
        return p;
    }

    /**
     * display the player's power ups
     */
    public void powerUpDisplayer() {

        while(gui.getView().getPlayerId() == -1){
            try{
                sleep(200);
            } catch (InterruptedException e){

            }
        }

        powerUp1.setImage(null);
        powerUp2.setImage(null);
        powerUp3.setImage(null);
        PowerUpType pt;
        ArrayList<ImageView> powerUps;
        powerUps = new ArrayList<>();

        powerUps.add(powerUp1);
        powerUps.add(powerUp2);
        powerUps.add(powerUp3);
        powerUp1.setFitHeight(156);
        powerUp1.setFitWidth(100);
        powerUp2.setFitHeight(156);
        powerUp2.setFitWidth(100);
        powerUp3.setFitHeight(156);
        powerUp3.setFitWidth(100);
        Image image;

        Color c;
        int i = 0;
        //iterates the poweUps of the player for letting themload on the guiMap
        while (i < gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size()) {
            pt = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getType();
            c = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getColor();
            if (pt == PowerUpType.TELEPORTER) {
                if (c == Color.BLUE) {
                    image = new Image("/images/powerUp/teleportBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c == Color.RED) {
                    image = new Image("/images/powerUp/teleportRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                    image = new Image("/images/powerUp/teleportYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else if (pt == PowerUpType.NEWTON) {
                if (c == Color.BLUE) {
                    image = new Image("/images/powerUp/kineticBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c == Color.RED) {
                    image = new Image("/images/powerUp/kineticRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                    image = new Image("/images/powerUp/kineticYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else if (pt == PowerUpType.TAG_BACK_GRENADE) {
                if (c == Color.BLUE) {
                    image = new Image("/images/powerUp/venomBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c == Color.RED) {
                    image = new Image("/images/powerUp/venomRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                    image = new Image("/images/powerUp/venomYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else//TARGETING_SCOPE
            {
                if (c == Color.BLUE) {
                    image = new Image("/images/powerUp/aimBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c == Color.RED) {
                    image = new Image("/images/powerUp/aimRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                    image = new Image("/images/powerUp/aimYellow.png");
                    powerUps.get(i).setImage(image);
                }
            }
            i++;
        }
    }

    /**
     * tag back grenade effect activator
     */
    public void granade()
    {
        Platform.runLater( () ->
        {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi usare una granata?", ButtonType.YES, ButtonType.NO);
            a.showAndWait();
            if (a.getResult() == ButtonType.NO) {
                mapEventDeleter();
                gui.getView().doAction(new GrenadeAction(null, gui.getView().getPlayerId()));

            } else {
                 a=new Alert(Alert.AlertType.CONFIRMATION,"Scegli granata da usare sulla destra");
                a.show();
                for(int i=0;i<gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size();i++)
                {
                    if(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getType().equals(PowerUpType.TAG_BACK_GRENADE))
                    {
                        switch (i) {
                            case 0:
                                powerUp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        mapEventDeleter();
                                        gui.getView().doAction(new GrenadeAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0).getColor(), gui.getView().getPlayerId()));
                                    }
                                });
                                break;
                            case 1:
                                powerUp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        mapEventDeleter();
                                        gui.getView().doAction(new GrenadeAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1).getColor(), gui.getView().getPlayerId()));
                                    }
                                });
                                break;
                            case 2:
                                powerUp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        mapEventDeleter();
                                        gui.getView().doAction(new GrenadeAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2).getColor(), gui.getView().getPlayerId()));
                                    }
                                });
                                break;
                        }
                    }
                }

            }
        });
    }

    /**
     * enable click actions of the power ups
     */
    public void powerUpEnable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {


                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Puoi usare un potenziamento di tipo Teletrasporto o Newton, vuoi?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    gui.getView().doAction(new SkipAction());
                    mapEventDeleter();
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Clicca sulla destra sul potenziamento da usare");
                    alert.show();
                }
                int i = 0;
                for (CachedPowerUp item : gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList()) {
                    if (item.getType() == PowerUpType.TELEPORTER) {

                        switch (i) {
                            case 0:
                                powerUp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        teleporterAction(0);
                                    }
                                });
                                break;
                            case 1:
                                powerUp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        teleporterAction(1);
                                    }
                                });
                                break;
                            case 2:
                                powerUp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        teleporterAction(2);
                                    }
                                });
                                break;
                        }
                    } else if (item.getType() == PowerUpType.NEWTON) {
                        switch (i) {
                            case 0:
                                powerUp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        newtonAction(0);
                                    }
                                });
                                break;
                            case 1:
                                powerUp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        newtonAction(1);
                                    }
                                });
                                break;
                            case 2:
                                powerUp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        newtonAction(2);
                                    }
                                });
                                break;
                        }
                    }
                    i++;
                }
            }
        });
    }

    /**
     * teleports the player n
     * @param n
     */
    private void teleporterAction(int n) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Teletrasporto! Clicca la cella dove vuoi muoverti");
        a.show();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                int ii = i;
                int jj = j;
                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        int x = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosX();
                        int y = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosY();
                        playerRemover(gui.getView().getPlayerId(), x, y);

                        gui.getView().doAction(new TeleporterAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(n), new Point(ii, jj)));
                        powerUp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                            }
                        });
                        powerUp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                            }
                        });
                        powerUp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                            }
                        });
                        mapEventDeleter();
                    }
                });
            }
        }
    }

    /**
     * newton effect, moves the player n
     * @param n
     */
    private void newtonAction(int n) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Raggio traente! Seleziona il giocatore che vuoi spostare.");
        a.show();

        for (int x = 0; x < rows; x++)//find the plyer IW and set the action listener on him
        {
            for (int y = 0; y < col; y++) {
                for (int id = 0; id < gui.getView().getCacheModel().getCachedPlayers().size(); id++)//search for every player in every cell
                {
                    if (map[x][y].getChildren().size() == 1)//primo HBOX
                    {
                        int j = 0;
                        boolean found = false;
                        while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
                        {


                            if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                found = true;
                                break;
                            }
                            j++;
                        }
                        if (found)//set the event listener that turn on the moverAction
                        {

                            int iid = id, xx = x, yy = y;
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    playersEffectRemover();
                                    newtonMover(iid, xx, yy, n);
                                }
                            });
                        }
                    } else if (map[x][y].getChildren().size() == 2) {//primo e secondo HBOX
                        int j = 0;
                        boolean found = false;

                        while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size()) {


                            if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                found = true;
                                break;
                            }
                            j++;
                        }
                        if (found) {
                            int iid = id, xx = x, yy = y;
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    playersEffectRemover();
                                    newtonMover(iid, xx, yy, n);
                                }
                            });
                            continue;
                        }
                        j = 0;
                        while (((HBox) map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id)) != 0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
                        {
                            j++;
                        }
                        int iid = id, xx = x, yy = y;
                        ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                playersEffectRemover();
                                newtonMover(iid, xx, yy, n);
                            }
                        });
                    }

                }
            }
        }
    }

    /**
     * scope action, all these parameters beacuse do the shootAction, check noScope(..) for other cases
     * @param w;
     * @param targetLists;
     * @param effects;
     * @param cells;
     * @param pUp;
     * @param dir;
     * @param c;
     */
    private void scopeAction(String w, List<List<Integer>> targetLists, List<Integer> effects, List<Point> cells, List<CachedPowerUp> pUp, List<Directions> dir, Color c) {
        mapEventDeleter();
        System.out.println("Stiamo per sparare davvero, bisogna solo selezioanre bersaglio del mirino");
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Seleziona il bersaglio del mirino");
        a.showAndWait();


        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < col; y++) {
                for (int id = 0; id < gui.getView().getCacheModel().getCachedPlayers().size(); id++)//search for every player in every cell
                {
                    if (map[x][y].getChildren().size() == 1)//primo HBOX
                    {
                        int j = 0;
                        boolean found = false;
                        while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
                        {


                            if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                found = true;
                                break;
                            }
                            j++;
                        }
                        if (found)//set the event listener that turn on the moverAction
                        {

                            int iid = id;
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    playersEffectRemover();
                                    mapEventDeleter();

                                    if (isFrenzy) {

                                        gui.getView().doAction(new FrenzyShoot(new ShootAction(w, targetLists, effects, cells, pUp, new ScopeAction(c, iid))));
                                        mapEventDeleter();
                                    } else {
                                        if(!dir.isEmpty())
                                        {
                                            gui.getView().doAction(new ShootAction(w,dir.get(0), targetLists, effects, cells, pUp, new ScopeAction(c, iid)));
                                            mapEventDeleter();
                                        }
                                        else {
                                            gui.getView().doAction(new ShootAction(w, targetLists, effects, cells, pUp, new ScopeAction(c, iid)));
                                            mapEventDeleter();
                                        }
                                    }



                                }
                            });
                        }
                    } else if (map[x][y].getChildren().size() == 2) {//primo e secondo HBOX
                        int j = 0;
                        boolean found = false;

                        while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size()) {


                            if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                found = true;
                                break;
                            }
                            j++;
                        }
                        if (found) {

                            int iid = id;
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    playersEffectRemover();
                                    mapEventDeleter();
                                    if (isFrenzy) {

                                        gui.getView().doAction(new FrenzyShoot(new ShootAction(w, targetLists, effects, cells, pUp, new ScopeAction(c, iid))));
                                        mapEventDeleter();
                                    } else {
                                        if(!dir.isEmpty())
                                        {
                                            gui.getView().doAction(new ShootAction(w,dir.get(0), targetLists, effects, cells, pUp, new ScopeAction(c, iid)));
                                            mapEventDeleter();
                                        }
                                        else {
                                            gui.getView().doAction(new ShootAction(w, targetLists, effects, cells, pUp, new ScopeAction(c, iid)));
                                            mapEventDeleter();
                                        }
                                    }


                                }
                            });
                            continue;
                        }
                        j = 0;
                        while (((HBox) map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id)) != 0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
                        {
                            j++;
                        }

                        int iid = id;
                        ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                playersEffectRemover();
                                mapEventDeleter();


                                if (isFrenzy) {
                                    mapEventDeleter();
                                    gui.getView().doAction(new FrenzyShoot(new ShootAction(w, targetLists, effects, cells, pUp, new ScopeAction(c, iid))));
                                } else {
                                    if(!dir.isEmpty())
                                    {
                                        gui.getView().doAction(new ShootAction(w,dir.get(0), targetLists, effects, cells, pUp, new ScopeAction(c, iid)));
                                        mapEventDeleter();
                                    }
                                    else {
                                        gui.getView().doAction(new ShootAction(w, targetLists, effects, cells, pUp, new ScopeAction(c, iid)));
                                        mapEventDeleter();
                                    }
                                }


                            }
                        });
                    }

                }
            }
        }
    }

    /**
     * Mover part of newton's effect
     * @param id
     * @param x
     * @param y
     * @param listNum
     */
    private void newtonMover(int id, int x, int y, int listNum)//position of this player
    {
        //set mover Actions here
        //mapEvent deleter alla fine me recumandi
        mapEventDeleter();
        System.out.println("Hai selezionato di newtonare il player: " + id);
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Ora seleziona la cella dove vuoi muoverlo");
        a.show();
        //----------ask NORTH
        if (moveValidator("NORTH", x, y)) {//check id i can go north
            map[x - 1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 1, NORTH));
                    mapEventDeleter();
                }
            });
            if (moveValidator("NORTH", x - 1, y))//check north 2 times
            {
                map[x - 2][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 2, NORTH));
                        mapEventDeleter();
                    }
                });
            }
        }
        //-------------ask East
        if (moveValidator("EAST", x, y)) {//check id i can go north
            map[x][y + 1].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 1, EAST));
                    mapEventDeleter();
                }
            });
            if (moveValidator("EAST", x, y + 1))//check north 2 times
            {
                map[x][y + 2].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 2, EAST));
                        mapEventDeleter();
                    }
                });
            }
        }
        //----------------------------ask west
        if (moveValidator("WEST", x, y)) {//check id i can go north
            map[x][y - 1].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 1, WEST));
                    mapEventDeleter();
                }
            });
            if (moveValidator("WEST", x, y - 1))//check north 2 times
            {
                map[x][y - 2].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 2, WEST));
                        mapEventDeleter();
                    }
                });
            }
        }
        //-----------ask south
        if (moveValidator("SOUTH", x, y)) {//check id i can go north
            map[x + 1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 1, SOUTH));
                    mapEventDeleter();
                }
            });
            if (moveValidator("SOUTH", x + 1, y))//check north 2 times
            {
                map[x + 2][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        gui.getView().doAction(new NewtonAction(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpColorList().get(listNum), id, 2, SOUTH));
                        mapEventDeleter();
                    }
                });
            }
        }


    }

    //--------------------------------------------------------------ammo gestion

    /**
     * navigates the map and call placer if is an ammo Cell
     */
    public void ammoPlacer() {
        //remove every ammo from the table
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < col; c++) {
                        if (gui.getView().getCacheModel().getCachedMap().getCachedCell(r, c) != null) {
                            if (gui.getView().getCacheModel().getCachedMap().getCachedCell(r, c).getCellType().equals(CellType.AMMO)) {
                                VBox b = map[r][c];
                                for (int i = 0; i < b.getChildren().size(); i++) {
                                    for (int j = 0; j < ((HBox) b.getChildren().get(i)).getChildren().size(); j++) {

                                        if (((HBox) b.getChildren().get(i)).getChildren().get(j).getId().compareTo("ammo") == 0) {
                                            ((HBox) b.getChildren().get(i)).getChildren().remove((((HBox) b.getChildren().get(i)).getChildren().get(j)));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < col; c++) {
                        if (gui.getView().getCacheModel().getCachedMap().getCachedCell(r, c) != null) {
                            if (gui.getView().getCacheModel().getCachedMap().getCachedCell(r, c).getCellType().equals(CellType.AMMO)) {
                                final int rr = r, cc = c;


                                if (!containsAmmo(map[rr][cc])) {
                                    placer(((CachedAmmoCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(rr, cc)).getAmmoList(), map[rr][cc]);
                                }


                            }

                        }
                    }
                }
            }
        });
    }

    /**
     * creates the imageView
     * @param imgUrl
     * @param h
     */
    private void imageCreator(String imgUrl, HBox h)//ammo Id="ammo"
    {

        if (imgUrl != null) {
            ImageView img = new ImageView();
            Image image = new Image(imgUrl);
            img.setImage(image);
            img.setId("ammo");
            h.getChildren().add(img);
        }


    }

    /**
     * check if b have an ammo yet
     * @param b
     * @return
     */
    private boolean containsAmmo(VBox b) {
        for (int i = 0; i < b.getChildren().size(); i++) {
            for (int j = 0; j < ((HBox) b.getChildren().get(i)).getChildren().size(); j++) {

                if (((HBox) b.getChildren().get(i)).getChildren().get(j).getId().compareTo("ammo") == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * navigates the Vbox and HBox of every cell and choose where to place the ammo
     * @param a
     * @param b
     */
    private void placer(List<Color> a, VBox b) {
        String url;
        url = fromAmmoCubetoIMG(a);


        if (b.getChildren().size() == 0)//if i don't have the hbox
        {

            b.getChildren().add(new HBox());

            imageCreator(url, (HBox) b.getChildren().get(0));


            return;
        }

        if (((HBox) b.getChildren().get(0)).getChildren().size() == 3) { //if the first Hbox is full

            b.getChildren().add(new HBox());

            imageCreator(url, (HBox) b.getChildren().get(1));

            return;
        }
        if (((HBox) b.getChildren().get(0)).getChildren().size() <= 3) //use the second HBox
        {
            imageCreator(url, (HBox) b.getChildren().get(0));
            return;
        }
        imageCreator(url, (HBox) b.getChildren().get(1));
    }

    /**
     * return a string that contains the url of the image of that type of ammo
     * @param a
     * @return
     */
    private String fromAmmoCubetoIMG(List<Color> a)
    {
        ArrayList<Color> card = new ArrayList<>();
        card.add(Color.BLUE);
        card.add(Color.RED);
        card.add(Color.RED);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/brr.png";
        }
        card.removeAll(card);

        card.add(Color.BLUE);
        card.add(Color.YELLOW);
        card.add(Color.YELLOW);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/byy.png";
        }
        card.removeAll(card);


        card.add(Color.BLUE);
        card.add(Color.BLUE);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/cbb.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.BLUE);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/crb.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.RED);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/crr.png";
        }
        card.removeAll(card);


        card.add(Color.YELLOW);
        card.add(Color.BLUE);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/cyb.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.RED);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/cyr.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.YELLOW);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/cyy.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.BLUE);
        card.add(Color.BLUE);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/rbb.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.YELLOW);
        card.add(Color.YELLOW);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/ryy.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.BLUE);
        card.add(Color.BLUE);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/ybb.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.RED);
        card.add(Color.RED);
        if (a.equals(card))//brr type
        {
            return "/images/ammo/yrr.png";
        }
        card.removeAll(card);

        System.out.println("Returnato NULL!");
        return null;
    }

    /**
     * update the player board after ammo updates
     */
    public void changedAmmos() {
        planciaUpdater();
    }

    //------------------------------------------------------------ grab

    /**
     * grab something in the cell (x,y)
     * dir because if you do a move before you have to rememeber it
     * @param x
     * @param y
     * @param dir
     */
    private void grabHere(int x, int y, List<Directions> dir)//----called from the button grab
    {
        //se questa cella è spawn o ammo
        if (x == -1 && y == -1)//means actual position
        {
            x = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosX();
            y = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosY();
        }
        int xx = x, yy = y;
        if (gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y).getCellType() == CellType.AMMO) {
            //se ammo aggiungi quelle munizie alle nostre/ powerUp
            Platform.runLater(() -> {
                grabAmmoCard(xx, yy, dir);
            });
        } else {//spawn cell
            //se arma : abilita il click su un arma, se puoi pagare bella
            Platform.runLater(() -> {
                grabWeapon(xx, yy, dir);
            });
        }


    }

    /**
     * if ammo celle grab ammo in the cell (x,y)
     * @param x
     * @param y
     * @param dir
     */
    private void grabAmmoCard(int x, int y, List<Directions> dir) {
        if (!containsAmmo(map[x][y])) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Non ci sono munizioni in questa cella, scegli un'altra azione");//maybe need to be changed??
            alert.show();
            return;
        }
        VBox b = map[x][y];
        for (int i = 0; i < b.getChildren().size(); i++) {
            for (int j = 0; j < ((HBox) b.getChildren().get(i)).getChildren().size(); j++) {
                if (((HBox) b.getChildren().get(i)).getChildren().get(j).getId().compareTo("ammo") == 0) {
                    ((ImageView) ((HBox) b.getChildren().get(i)).getChildren().get(j)).setImage(null);//remove the ammoImage
                    //((HBox)b.getChildren().get(i)).getChildren().remove((((HBox)b.getChildren().get(i)).getChildren().get(j)));
                    System.out.println("Sto raccogliendo una muniozione e ho fatto questi spotamenti: " + dir);
                    gui.getView().doAction(new GrabAction(dir));
                    mapEventDeleter();
                }
            }
        }

    }

    /**
     * if spawnCell you can buy a weapon
     * @param x
     * @param y
     * @param dir
     */
    private void grabWeapon(int x, int y, List<Directions> dir) {
        if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getAmmoBag() != null)
            System.out.println("Tue munizioni: " + gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getAmmoBag().getAmmoList());
        List<Integer> effects = new ArrayList<>();
        List<String> weapons = new ArrayList<>();
        if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag() != null && gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons() != null) {
            if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().size() == 3) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Hai gia 3 armi , se vuoi comprare devi scartarne una, vuoi scartare?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Clicca a sinistra quale arma scartare dopodichè procedi all'acquisto");
                    alert.show();
                    //discard weapon clicked and disbale discarder

                    weapon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            weapons.add(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0));

                            myWeapon1.setImage(null);
                            weapon1.setOnMouseClicked(null);//remove discard effects
                            weapon2.setOnMouseClicked(null);
                            weapon3.setOnMouseClicked(null);
                        }
                    });
                    weapon2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            weapons.add(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1));
                            myWeapon2.setImage(null);

                            weapon1.setOnMouseClicked(null);//remove discard effects
                            weapon2.setOnMouseClicked(null);
                            weapon3.setOnMouseClicked(null);
                        }
                    });
                    weapon3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            weapons.add(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2));
                            myWeapon3.setImage(null);
                            weapon1.setOnMouseClicked(null);//remove discard effects
                            weapon2.setOnMouseClicked(null);
                            weapon3.setOnMouseClicked(null);
                        }
                    });


                }
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }

            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Scegli un' arma da acquistare nella schermata sinistra");
        alert.show();
        //show the current spawn cell weapons
        spawnCellWeaponShow(x, y);
        //show the cost in toolTip
        costDisplay(x, y);

        weapon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    mapEventDeleter();
                    if (weapons.size() == 0)//no discard
                    {
                        weapons.add(null);
                    }
                    weapons.add(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0));

                    //System.out.println("Stai cercndo di acquistare :" + ((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0));

                    checkPayWithPowerUp(gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0)).getBuyEffect(), weapons, dir, "BUY", effects);
                } catch (WeaponNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        weapon2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    mapEventDeleter();
                    //System.out.println("Stai cercndo di acquistare :" + ((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0));
                    if (weapons.size() == 0)//no discard
                    {
                        weapons.add(null);
                    }
                    weapons.add(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1));
                    checkPayWithPowerUp(gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(1)).getBuyEffect(), weapons, dir, "BUY", effects);
                } catch (WeaponNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        weapon3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    mapEventDeleter();
                    //System.out.println("Stai cercndo di acquistare :" + ((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(0));
                    if (weapons.size() == 0)//no discard
                    {
                        weapons.add(null);
                    }
                    weapons.add(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2));
                    checkPayWithPowerUp(gui.getView().getCacheModel().getWeaponInfo(((CachedSpawnCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(x, y)).getWeaponNames().get(2)).getBuyEffect(), weapons, dir, "BUY", effects);
                } catch (WeaponNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Same as checkPayWithPowerUp but simpler version, which in case you don't need to specify local ammo and powerups
     * will just read them from cacheModel and then call the main checkPayWithPowerUps method with them as parameters
     *
     * @param cost        to be checked
     * @param weaponNames in position 0 weapon i want to discard, in position 1 weapon iw ant to buy
     * @param actionType  contins the type of cation: BUY or SHOOT or RELOAD
     */
    private void checkPayWithPowerUp(List<Color> cost, List<String> weaponNames, List<Directions> dir, String actionType, List<Integer> effects) {
        View view = gui.getView();
        List<CachedPowerUp> powerUps = new ArrayList<>();
        CopyOnWriteArrayList<Color> ammo = new CopyOnWriteArrayList<>();
        List<CachedPowerUp> powerUpsToDiscard = new ArrayList<>();

        if (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag() != null)
            ammo.addAll(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag().getAmmoList());

        if (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag() != null) {
            powerUps = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpList();
            //powerUpsColor = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpColorList();
        }

        checkPayWithPowerUp(cost, powerUps, ammo, 0, weaponNames, powerUpsToDiscard, dir, actionType, effects);//start from zero go to infinite and beyond


    }

    /**
     * @param cost        cost to be checked if payable with powerups
     * @param powerUps    take powerups as parameter because you can remove some of them for partial cost checks
     * @param ammo        take ammo as parameter because you can remove some of them for partial checks
     * @param costCount   contains the index of which ammo i'm checking
     * @param weaponNames contains the weapon i want to discrad(0) e the weapon i want to buy (1)
     * @param actionType  contains the type of cation: BUY or SHOOT or RELOAD
     */
    private void checkPayWithPowerUp(List<Color> cost, List<CachedPowerUp> powerUps, List<Color> ammo, int costCount, List<String> weaponNames, List<CachedPowerUp> powerUpsToDiscard, List<Directions> dir, String actionType, List<Integer> effects) {

        System.out.println("Seconda fase del pagamento, vero pagamento");
        if(actionType.equals("RELOAD"))
            System.out.println("Devo pagare di ricarica:"+cost);
        System.out.println("Contronto: " + costCount + " --!-- " + cost.size());
        if (costCount == cost.size() && actionType.equals("BUY"))// i need to buy at this point!
        {//if it's a grab here dir is empty
            System.out.println("Provo ad acquistare una arma con queste robe: " + dir + " Acquisto: " + weaponNames.get(1) + " uso questi pup: " + powerUpsToDiscard + " scarto: " + weaponNames.get(0));
            gui.getView().doAction(new GrabAction(dir, weaponNames.get(0), weaponNames.get(1), powerUpsToDiscard));
            mapEventDeleter();
            return;
        } else if (costCount == cost.size() && actionType.equals("SHOOT")) {
            System.out.println("Provo a sparare con " + weaponNames.get(0) + " effetti: " + effects + "E sccarto " + powerUpsToDiscard);
            shootTargetChooser(weaponNames.get(0), effects, powerUpsToDiscard, dir);
            return;
        }else if(costCount == cost.size() && actionType.equals("RELOAD")){
            System.out.println("Provo a ricaricare " + weaponNames + "   E scarto " + powerUpsToDiscard);
            if(!isFrenzy)
            {gui.getView().doAction(new ReloadAction(weaponNames,powerUpsToDiscard));mapEventDeleter();}
            else
            {//reload then ask shoot
                System.out.println("reload frenetico finale");
                mapEventDeleter();
                gui.getView().doAction(new FrenzyShoot(new ReloadAction(weaponNames, powerUpsToDiscard)));
            }
            return;
        }

        Color c = cost.get(costCount);
        if (ammo.contains(c) && hasPowerUpOfColor(powerUps, c)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setContentText("Puoi pagare " + c.toString() + " usando un PowerUp o con una munizione.");
            alert.showAndWait();
            alert = new Alert(Alert.AlertType.CONFIRMATION, "\"Vuoi usare un PowerUp per pagare al posto delle munizioni?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Clicca sul powerUp da scartare a destra: ");
                alert.show();
                System.out.println("Puoi usare sia pUp che munizie");
                //here get powerup to discard porcodyo
                for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size(); i++) {
                    if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getColor().equals(c)) {
                        switch (i) {
                            case 0:

                                powerUp1.setOnMousePressed(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        System.out.println("-----------------------------------------------------------");
                                        mapEventDeleter();
                                        int cc = costCount;
                                        cc++;
                                        CachedPowerUp powerUpToDiscard = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0);
                                        powerUps.remove(powerUpToDiscard);
                                        powerUpsToDiscard.add(powerUpToDiscard);
                                        checkPayWithPowerUp(cost, powerUps, ammo, cc, weaponNames, powerUpsToDiscard, dir, actionType, effects);
                                        return;
                                    }
                                });
                                break;
                            case 1:

                                powerUp2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        System.out.println("-----------------------------------------------------------");
                                        mapEventDeleter();
                                        CachedPowerUp powerUpToDiscard = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1);
                                        powerUps.remove(powerUpToDiscard);
                                        powerUpsToDiscard.add(powerUpToDiscard);
                                        int cc = costCount;
                                        cc++;
                                        checkPayWithPowerUp(cost, powerUps, ammo, cc, weaponNames, powerUpsToDiscard, dir, actionType, effects);
                                        return;
                                    }
                                });
                                break;
                            case 2:

                                powerUp3.setOnMousePressed(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        System.out.println("-----------------------------------------------------------");
                                        mapEventDeleter();
                                        CachedPowerUp powerUpToDiscard = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2);
                                        powerUps.remove(powerUpToDiscard);
                                        powerUpsToDiscard.add(powerUpToDiscard);
                                        int cc = costCount;
                                        cc++;
                                        checkPayWithPowerUp(cost, powerUps, ammo, cc, weaponNames, powerUpsToDiscard, dir, actionType, effects);
                                        return;
                                    }
                                });
                                break;

                        }
                    }
                }

            } else {
                ammo.remove(c);
                checkPayWithPowerUp(cost, powerUps, ammo, costCount + 1, weaponNames, powerUpsToDiscard, dir, actionType, effects);
            }
        }
        else if (hasPowerUpOfColor(powerUps, c) && !ammo.contains(c)) {//answer is no
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Puoi pagare " + c.toString() + " solamente con un PowerUp: ");
            alert.showAndWait();


            alert.setContentText("Scegli powerUp da scartare: ");//need to wait before iterating----
            alert.showAndWait();

            for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size(); i++) {
                if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getColor().equals(c)) {
                    {
                        switch (i) {
                            case 0:
                                powerUp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        System.out.println("-----------------------------------------------------------");
                                        mapEventDeleter();
                                        CachedPowerUp powerUpToDiscard = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0);
                                        powerUps.remove(powerUpToDiscard);
                                        powerUpsToDiscard.add(powerUpToDiscard);
                                        int cc = costCount;
                                        cc++;
                                        checkPayWithPowerUp(cost, powerUps, ammo, cc, weaponNames, powerUpsToDiscard, dir, actionType, effects);

                                    }
                                });
                                break;
                            case 1:

                                powerUp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        System.out.println("-----------------------------------------------------------");
                                        mapEventDeleter();
                                        CachedPowerUp powerUpToDiscard = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1);
                                        powerUps.remove(powerUpToDiscard);
                                        powerUpsToDiscard.add(powerUpToDiscard);
                                        int cc = costCount;
                                        cc++;
                                        checkPayWithPowerUp(cost, powerUps, ammo, cc, weaponNames, powerUpsToDiscard, dir, actionType, effects);

                                    }
                                });
                                break;
                            case 2:

                                powerUp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        System.out.println("-----------------------------------------------------------");
                                        mapEventDeleter();
                                        CachedPowerUp powerUpToDiscard = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2);
                                        powerUps.remove(powerUpToDiscard);
                                        powerUpsToDiscard.add(powerUpToDiscard);
                                        int cc = costCount;
                                        cc++;
                                        checkPayWithPowerUp(cost, powerUps, ammo, cc, weaponNames, powerUpsToDiscard, dir, actionType, effects);

                                    }
                                });
                                break;
                        }
                    }
                }


            }
        } else if (ammo.contains(c)) {
            System.out.println("Paga tutto in ammo");
            ammo.remove(c);
            checkPayWithPowerUp(cost, powerUps, ammo, costCount + 1, weaponNames, powerUpsToDiscard, dir, actionType, effects);
        } else if(actionType.equals("RELOAD")){//this shouldn't do anythign , just forward the choice and then controller will
            //reply back that player hasn't got enough ammo
            System.out.println("Provo a ricaricare " + weaponNames + "E scarto " + powerUpsToDiscard);
            System.out.println("NON HO ABBA MUNIZIONI!");
            Alert a=new Alert(Alert.AlertType.INFORMATION,"Non hai abbastanza munizioni !");
            a.show();
            if(!isFrenzy)
            {gui.getView().doAction(new ReloadAction(weaponNames,powerUpsToDiscard));mapEventDeleter();}
            else
            {//reload frenzy
                System.out.println("reload frenetico finale");
                gui.getView().doAction(new FrenzyShoot(new ReloadAction(weaponNames, powerUpsToDiscard)));
                mapEventDeleter();
            }
            return;
        }else if(actionType.equals("BUY")){
                //this shouldn't do anythign , just forward the choice and then controller will
            //reply back that player hasn't got enough ammo
            Alert a=new Alert(Alert.AlertType.INFORMATION,"Non hai abbastanza munizioni ");
            a.show();
            System.out.println("Non abba munizie ecc");
            System.out.println("Direzioni: "+dir);
            gui.getView().doAction(new GrabAction(dir,weaponNames.get(0),weaponNames.get(1),powerUpsToDiscard));
            mapEventDeleter();
            return;
        }else if(actionType.equals("SHOOT"))
        {
            Alert a=new Alert(Alert.AlertType.INFORMATION,"Non hai abbastanza munizioni ");
            a.show();
            List<List<Integer>> targetLists=new ArrayList<>();
            List<Point> cells=new ArrayList<>();
            gui.getView().doAction(new ShootAction(weaponNames.get(0),targetLists,effects,cells,powerUpsToDiscard,null));
            mapEventDeleter();
            return;
            //new ShootAction(w, targetLists, effects, cells, pUp, null)
        }


        System.out.println("[DEBUG] PowerUp da scartare scelti: -------NON SI DOVREBBE VEDERE MAI_______ " + powerUpsToDiscard);

    }

    /**
     *
     * @param powerUps list of CachedPowerUps to be checked
     * @param c color to find
     * @return true if the powerups list contains a powerup of the specified color, false otherwise
     */
    private boolean hasPowerUpOfColor(List<CachedPowerUp> powerUps, Color c) {
        List<CachedPowerUp> result = powerUps
                .stream()
                .filter(x -> x.getColor().equals(c))
                .collect(Collectors.toList());

        return !result.isEmpty();

    }

    /**
     * @param weapon    name of the weapon to be checked
     * @param ammoCubes list of ammocubes copied from cachemodel (can be modified by methods)
     * @param powerUps  list of powerups copied from cachemodel (can be modified by methods, to track local changes)
     * @return true if the weapon can be reloaded with current powerups and ammo, false otherwise
     */
    //------------------------------------------------------reload
    private boolean canReload(String weapon, List<Color> ammoCubes, List<CachedPowerUp> powerUps) {
        View view = gui.getView();
        CachedFullWeapon w = null;
        UiHelpers uih = new UiHelpers();
        try {
            w = view.getCacheModel().getWeaponInfo(weapon);
        } catch (WeaponNotFoundException e) {

        }

        w.getFirstEffectCost();

        if (uih.canPay(w.getFirstEffectCost(), ammoCubes, UiHelpers.genColorListFromPowerUps(powerUps))) {
            return true;
        } else {
            return false;
        }
    }

    //------------------------------------------------------------ move and grab --> check move
    //-----------------------------------------------------------plancia

    /**
     * Show my weapons. called from updates in gui
     */
    public void changedWeapons() {

        //--------display my weapons
        myWeapon1.setImage(null);
        myWeapon2.setImage(null);
        myWeapon3.setImage(null);
        myWeapon1.setFitHeight(156);
        myWeapon1.setFitWidth(100);
        myWeapon2.setFitHeight(156);
        myWeapon2.setFitWidth(100);
        myWeapon3.setFitHeight(156);
        myWeapon3.setFitWidth(100);

        //disable alla effects
        weapon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            }
        });
        weapon2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            }
        });
        weapon3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            }
        });

        while(gui.getView().getPlayerId() == -1){
            try{
                sleep(200);
            } catch (InterruptedException e){

            }
        }

        if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag() != null) {
            for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().size(); i++) {
                String url = fromWNameToUrl(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(i));
                Image img = new Image(url);

                switch (i) {
                    case 0:
                        myWeapon1.setImage(img);
                        break;
                    case 1:
                        myWeapon2.setImage(img);
                        break;
                    case 2:
                        myWeapon3.setImage(img);
                        break;
                }

            }
        }
        planciaUpdater();
    }


    //--------------------------------------------------------------------------shoot things

    /**
     * choose the weapon you want to shoot with
     * dir because you may move before shoot
     * @param dir
     */
    public void shootWeaponChooser( List<Directions> dir) {
        System.out.println("Now you can choose the weapon you want to use");
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Scegli con quale arma sparare");
        a.showAndWait();
        a = new Alert(Alert.AlertType.CONFIRMATION, "STOP annulla lo sparo,solo pre-selezione arma ");
        a.show();
        if(isFrenzy)
        {
            stopMov.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert a=new Alert(Alert.AlertType.INFORMATION,"Sparo annullato");
                    a.show();
                    mapEventDeleter();
                    gui.getView().doAction(FrenzyShoot.genFrenzyShootActionSkipShoot());
                }
            });
            //setUp StopMov for interrupting everything-- need new button??
        }

        myWeapon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String name = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(0);
                try {
                    mapEventDeleter();
                    CachedFullWeapon w = gui.getView().getCacheModel().getWeaponInfo(name);
                    shootEffectsChooser(w, dir);

                } catch (WeaponNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        myWeapon2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String name = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(1);
                try {
                    mapEventDeleter();
                    CachedFullWeapon w = gui.getView().getCacheModel().getWeaponInfo(name);
                    shootEffectsChooser(w, dir);

                } catch (WeaponNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        myWeapon3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String name = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(2);
                try {
                    mapEventDeleter();
                    CachedFullWeapon w = gui.getView().getCacheModel().getWeaponInfo(name);
                    shootEffectsChooser(w, dir);

                } catch (WeaponNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * choose the effects
     * dir because you may move before shoot
     * @param w weapon you want to use
     * @param dir
     */
    private void shootEffectsChooser(CachedFullWeapon w, List<Directions> dir) {
        mapEventDeleter();
        System.out.println("------------------SELEZIOANTORE DI EFFETTI---------");
        List<Integer> effects = new ArrayList<>();
        if (w.getEffectTypes().get(0).equals(EffectType.ESCLUSIVE))//choose one of the exclusive effects
        {
            System.out.println("------------------EFFFETTI ESCLUSIVI---------");
            ButtonType first = new ButtonType("Primo Effetto");
            ButtonType second = new ButtonType("Secondo Effetto");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Choose one of the effects of " + w.getName(), first, second);
            alert.showAndWait();

            if (alert.getResult() == first) {
                effects.add(0);
                shootEffectPay(w, effects, dir);
            } else {
                effects.add(1);
                shootEffectPay(w, effects, dir);
            }

        } else if (w.getSecondEffectCost() == null)//use the only effect
        {
            System.out.println("------------------EFFFETTO SINGOLO---------");
            effects.add(0);
            shootEffectPay(w, effects, dir);
        } else if (w.getThirdEffectCost() == null && w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE_NON_ORD))//2 effects---- options: 1, 2, 1-2,2-1
        {
            System.out.println("------------------2 EFFETTI PER QUEST'ARMA NON IN ORDN---------");
            //ask if first and second or only second
            //scegli effetti da usare in ordine di utilizzo
            ButtonType first = new ButtonType("Primo Effetto");
            //ButtonType second = new ButtonType("Secondo Effetto");
            ButtonType firstAndSec = new ButtonType("Primo Effetto seguito dal secondo");
            ButtonType secAndFirst = new ButtonType("Secondo Effetto seguito dal primo");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli gli effetti di " + w.getName(), first, firstAndSec, secAndFirst);
            alert.showAndWait();

            if (alert.getResult() == first) {//use first effect
                effects.add(0);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == firstAndSec) {//use first and second effect
                effects.add(0);
                effects.add(1);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == secAndFirst)//use second then first effect
            {
                effects.add(1);
                effects.add(0);
                shootEffectPay(w, effects, dir);
            }
        } else if (w.getThirdEffectCost() == null && w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE)) {//2 effects
            //ask if first and second or only second
            //scegli effetti da usare in ordine di utilizzo
            System.out.println("------------------2 EFFETTI PER CODESTA ---------");
            ButtonType first = new ButtonType("Primo Effetto");
            ButtonType firstAndSec = new ButtonType("Primo Effetto seguito dal secondo");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli gli effetti di " + w.getName(), first, firstAndSec);
            alert.showAndWait();

            if (alert.getResult() == first) {//use first effect
                effects.add(0);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == firstAndSec) {//use first and second effect
                effects.add(0);
                effects.add(1);
                shootEffectPay(w, effects, dir);
            }
        } else if (w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE)) {
            System.out.println("------------------3 EFFETTI ORDINATI---------");
            //3 effects, in order
            ButtonType first = new ButtonType("Primo Effetto");
            ButtonType firstAndSec = new ButtonType("Primo Effetto seguito dal secondo");
            ButtonType firstAndSecAndThird = new ButtonType("Primo Effetto seguito dal secondo seguito dal terzo");

            Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli gli effetti di " + w.getName(), first, firstAndSec, firstAndSecAndThird);
            alert.showAndWait();

            if (alert.getResult() == first) {//use first effect
                effects.add(0);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == firstAndSec) {//use first and second effect
                effects.add(0);
                effects.add(1);
                shootEffectPay(w, effects, dir);
            } else {//first second and third
                effects.add(0);
                effects.add(1);
                effects.add(2);
                shootEffectPay(w, effects, dir);
            }
        } else if (w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE_NON_ORD)) {
            System.out.println("------------------3 EFFETTI NON ORDINATI---------");
            //3 effects, maybe non in order
            ButtonType first = new ButtonType("Primo Effetto");
            ButtonType firstAndSec = new ButtonType("Primo Effetto seguito dal secondo");
            ButtonType firstAndSecAndThird = new ButtonType("Primo Effetto seguito dal secondo seguito dal terzo");
            ButtonType firstThird = new ButtonType("Primo Effetto seguito dal terzo");
            ButtonType secFirst = new ButtonType("Secondo effetto seguito dal primo");
            ButtonType secFirstThird = new ButtonType("Secondo effetto seguito dal primo seguito dal terzo");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Scegli gli effetti di " + w.getName(), first, firstAndSec, firstAndSecAndThird, firstThird, secFirst, secFirstThird);
            alert.showAndWait();

            if (alert.getResult() == first) {//use first effect
                effects.add(0);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == firstAndSec) {//use first and second effect
                effects.add(0);
                effects.add(1);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == firstAndSecAndThird) {//first second and third
                effects.add(0);
                effects.add(1);
                effects.add(2);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == firstThird) {
                effects.add(0);
                effects.add(2);
                shootEffectPay(w, effects, dir);
            } else if (alert.getResult() == secFirst) {
                effects.add(1);
                effects.add(0);
                shootEffectPay(w, effects, dir);
            } else {
                effects.add(1);
                effects.add(0);
                effects.add(2);
                shootEffectPay(w, effects, dir);
            }
        }

    }

    /**
     * check if you can pay
     * uses checkPayWithPowerUP
     * @param w
     * @param effects
     * @param dir
     */
    private void shootEffectPay(CachedFullWeapon w, List<Integer> effects, List<Directions> dir)//goes to checkpaywithpowerUp
    {
        System.out.println("Ho scelto gli effetti: " + effects);
        List<Color> cost = new ArrayList<>();
        for (Integer item : effects) {
            switch (item)//check null here!!!
            {
                case 0:
                    break;
                case 1:
                    cost.addAll(w.getSecondEffectCost());
                    break;
                case 2:
                    cost.addAll(w.getThirdEffectCost());
                    break;
            }
        }
        List<String> weaponName = new ArrayList<>();
        weaponName.add(w.getName());
        System.out.println("Andiamo al pagameto");
        checkPayWithPowerUp(cost, weaponName, dir, "SHOOT", effects);

    }

    /**
     * choose the targets then call cell if required the go to the shoot target iterator
     * @param w
     * @param effects
     * @param pUp
     * @param dir
     */
    private void shootTargetChooser(String w, List<Integer> effects, List<CachedPowerUp> pUp, List<Directions> dir) {
       needCell=false;
        for(int e=0;e<effects.size();e++) {
            try {
                if (gui.getView().getCacheModel().getWeaponInfo(w).getEffectRequirements().get(effects.get(e)).getCellRequired()) {
                    needCell = true;
                }
            } catch (WeaponNotFoundException ee) {
                ee.printStackTrace();
            }
        }
        try {
            CachedFullWeapon weapon = gui.getView().getCacheModel().getWeaponInfo(w);//-------------------weapon name

            if (effects.get(0) == effects.size() && weapon.getEffectRequirements().get(effects.get(0)).getNumberOfTargets().size() == 0)//movement
            {
                //no target needed so it's a movement
                //movement of the shooter effect exactly
                System.out.println("begin with 0 targets and a movement effect");
                List<Integer> targets = new ArrayList<>();
                List<List<Integer>> targetsLists = new ArrayList<>();
                List<Point> p = new ArrayList<>();
                shootCell(w, effects, pUp, dir, targetsLists, 0, 0, p);
                return;
            }


            int effectNum = 0;//take the effect num for the ist and get its targets
            int targetNum = 0;
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Seleziona i bersagli per il primo effetto che vuoi utilizzare ");
            a.showAndWait();
            a = new Alert(Alert.AlertType.CONFIRMATION, "Clicca STOP a sinistra per finire questo effetto");
            a.show();
            mapEventDeleter();
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < col; y++) {
                    for (int id = 0; id < gui.getView().getCacheModel().getCachedPlayers().size(); id++)//search for every player in every cell
                    {
                        if (map[x][y].getChildren().size() == 1)//primo HBOX
                        {
                            System.out.println("Cerco bersagi in cella "+x+" "+y);
                            int j = 0;
                            while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
                            {


                                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                    int iid = id;
                                    System.out.println("Beccato player con id "+iid );
                                    ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent mouseEvent) {
                                            playersEffectRemover();
                                            mapEventDeleter();
                                            List<Integer> targets = new ArrayList<>();
                                            List<List<Integer>> targetsLists = new ArrayList<>();
                                            targets.add(iid);
                                            List<Point> p = new ArrayList<>();

                                            targetsLists.add(targets);

                                            shootTargetIterator(w, effects, pUp, dir, targetsLists, effectNum, targetNum + 1, p);

                                        }
                                    });
                                }
                                j++;
                            }


                        } else if (map[x][y].getChildren().size() == 2) {
                            System.out.println("Vado a cercare anche nella seconda riga!");
                            int j = 0;


                            while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size()) {

                                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                    System.out.println("Trovato nella prima riga player con questo id: " + id);
                                    int iid = id;
                                    ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent mouseEvent) {
                                            System.out.println("Parte l'effetto del player " + iid);
                                            playersEffectRemover();
                                            mapEventDeleter();
                                            List<Integer> targets = new ArrayList<>();
                                            List<List<Integer>> targetsLists = new ArrayList<>();
                                            targets.add(iid);
                                            targetsLists.add(targets);
                                            List<Point> p = new ArrayList<>();

                                            shootTargetIterator(w, effects, pUp, dir, targetsLists, effectNum, targetNum + 1, p);
                                        }
                                    });

                                }
                                j++;
                            }


                            System.out.println("Ora cerco nella seocnda riga i regaz");
                            while (j < ((HBox) map[x][y].getChildren().get(1)).getChildren().size()) {

                                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                    System.out.println("Trovato nella seconda riga player con questo id: " + id);
                                    int iid = id;
                                    ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent mouseEvent) {
                                            System.out.println("Parte l'effetto del player " + iid);
                                            playersEffectRemover();
                                            mapEventDeleter();
                                            List<Integer> targets = new ArrayList<>();
                                            List<List<Integer>> targetsLists = new ArrayList<>();
                                            targets.add(iid);
                                            targetsLists.add(targets);
                                            List<Point> p = new ArrayList<>();

                                            shootTargetIterator(w, effects, pUp, dir, targetsLists, effectNum, targetNum + 1, p);
                                        }
                                    });

                                }
                                j++;
                            }

                        }
                    }
                }
            }

        } catch (WeaponNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ietartes trough targets and cells
     * @param w
     * @param effects
     * @param pUp
     * @param dir
     * @param targetLists
     * @param effectNum
     * @param targetNum
     * @param cells
     */

    private void shootTargetIterator(String w, List<Integer> effects, List<CachedPowerUp> pUp, List<Directions> dir, List<List<Integer>> targetLists, int effectNum, int targetNum, List<Point> cells) {

        try {
            //here add stop button
            CachedFullWeapon weapon = gui.getView().getCacheModel().getWeaponInfo(w);
            //---need to be done every time for adj things
            int eeeffectNum = effectNum, tttarget = targetNum;
            stopMov.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mapEventDeleter();
                    System.out.println("Pigiato stopMov in questa situa: effetto: " + eeeffectNum + " bersaglio numero : " + tttarget + " effetti: " + effects.size());
                    if (!weapon.getEffectRequirements().get(effects.get(eeeffectNum)).getCellRequired() && (effects.size() - 1) == eeeffectNum) {
                        //ouch all ince
                        shootTargetIterator(w, effects, pUp, dir, targetLists, eeeffectNum + 1, 0, cells);
                    } else if (!weapon.getEffectRequirements().get(effects.get(eeeffectNum)).getCellRequired() && effects.size() > eeeffectNum) {
                        shootTargetIterator(w, effects, pUp, dir, targetLists, eeeffectNum, tttarget, cells);
                    } else {//mov effect
                        shootCell(w, effects, pUp, dir, targetLists, eeeffectNum, 0, cells);
                    }

                }

            });

            /*System.out.println("Effetto dell'arma numero : "+effects.get(effectNum));
            System.out.println("Richiedimento "+weapon.getEffectRequirements().get(effects.get(effectNum)));
            System.out.println("Disperatamente "+weapon.getEffectRequirements().get(effects.get(effectNum)).getNumberOfTargets());
                */
            if (effectNum >= effects.size())//shouldn't have cell required
            {
                stopMov.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    }
                });

                checkScope(w, effects, pUp, dir, targetLists, cells);
                return;
            }
            if (targetNum == weapon.getEffectRequirements().get(effects.get(effectNum)).getNumberOfTargets().size())//--------------------finsihed to take this effect's targets, then control things
            {
                System.out.println("Entrato nei controlli dello shoot");
                stopMov.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    }
                });

                //1)-----this effect requires also a cell. So at the end of this effect it can take it
                if (weapon.getEffectRequirements().get(effects.get(effectNum)).getCellRequired()) {
                    System.out.println("Vai a effetto cella ");
                    shootCell(w, effects, pUp, dir, targetLists, effectNum, 0, cells);
                    return;
                }
                else //2)----otherwise
                {
                    if(needCell)// if he requires a cell but not here
                    {
                       cells.add(null);
                    }
                    effectNum++;
                    targetNum = 0;

                    //--------2.1) next effect need only cell
                    if (effectNum == effects.size()) {//effects finished
                        System.out.println("Fine della magagna degli effetti");
                        //do the shoot for real , but before check scope
                        //-------!!!!!!------check mirino
                        checkScope(w, effects, pUp, dir, targetLists, cells);
                        return;
                    }//--------------2.2) the next one is a move only effect--otherwise goes on
                    else if (weapon.getEffectRequirements().get(effects.get(effectNum)).getNumberOfTargets().isEmpty() && weapon.getEffectRequirements().get(effects.get(effectNum)).getCellRequired()) {
                        System.out.println("............................................................");
                        shootCell(w, effects, pUp, dir, targetLists, effectNum, 0, cells);//not invented yet lol
                        return;
                    }

                }
                System.out.println("Contorolli indenni, ho tutto regular");
            }
            // --------------------------------------go on to taking the targets
            if (targetNum == 0) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Seleziona i bersagli per questo effetto");
                a.showAndWait();
                a = new Alert(Alert.AlertType.CONFIRMATION, "Clicca STOP a sinistra per finire questo effetto");
                a.show();
            } else {
                int aa = weapon.getEffectRequirements().get(effects.get(effectNum)).getNumberOfTargets().size() - targetNum;

                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Seleziona un altro bersaglio te ne restano: " + aa);
                a.show();
            }
            mapEventDeleter();
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < col; y++) {
                    for (int id = 0; id < gui.getView().getCacheModel().getCachedPlayers().size(); id++)//search for every player in every cell
                    {
                        if (map[x][y].getChildren().size() == 1)//primo HBOX
                        {
                            int j = 0;
                            boolean found = false;
                            while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size())//devo rimuovere il giocatore che ha quell'id e allora lo cerco, la sua img ha id=playerId
                            {


                                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                    found = true;
                                    break;
                                }
                                j++;
                            }
                            if (found)//set the event listener that turn on the moverAction
                            {

                                int iid = id, eeffectNum = effectNum, ttargetNum = targetNum;
                                ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        playersEffectRemover();
                                        mapEventDeleter();

                                        if (ttargetNum == 0)//beginning of a new list of targets
                                        {
                                            List<Integer> targets = new ArrayList<>();
                                            targets.add(iid);
                                            targetLists.add(targets);
                                        } else {
                                            targetLists.get(eeffectNum).add(iid);
                                        }


                                        shootTargetIterator(w, effects, pUp, dir, targetLists, eeffectNum, ttargetNum + 1, cells);

                                    }
                                });
                            }
                        } else if (map[x][y].getChildren().size() == 2)//second HBox and first
                        {//primo e secondo HBOX
                            int j = 0;
                            boolean found = false;

                            while (j < ((HBox) map[x][y].getChildren().get(0)).getChildren().size()) {


                                if (((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id)) == 0) {
                                    found = true;
                                    break;
                                }
                                j++;
                            }
                            if (found) {

                                int iid = id, eeffectNum = effectNum, ttargetNum = targetNum;
                                ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        playersEffectRemover();
                                        mapEventDeleter();
                                        List<Integer> targets = new ArrayList<>();
                                        List<List<Integer>> targetsLists = new ArrayList<>();
                                        targets.add(iid);
                                        targetsLists.add(targets);

                                        shootTargetIterator(w, effects, pUp, dir, targetsLists, eeffectNum, ttargetNum + 1, cells);

                                    }
                                });
                                continue;
                            }
                            j = 0;
                            while (((HBox) map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id)) != 0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
                            {
                                j++;
                            }

                            int iid = id, eeffectNum = effectNum, ttargetNum = targetNum;
                            ((HBox) map[x][y].getChildren().get(0)).getChildren().get(j).setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    playersEffectRemover();
                                    mapEventDeleter();
                                    List<Integer> targets = new ArrayList<>();
                                    List<List<Integer>> targetsLists = new ArrayList<>();
                                    targets.add(iid);
                                    targetsLists.add(targets);

                                    shootTargetIterator(w, effects, pUp, dir, targetsLists, eeffectNum, ttargetNum + 1, cells);

                                }
                            });
                        }

                    }
                }
            }


        } catch (WeaponNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * ask for the cells if neeeded
     * @param w
     * @param effects
     * @param pUp
     * @param dir
     * @param targetLists
     * @param effectNum
     * @param targetNum
     * @param cells
     */
    private void shootCell(String w, List<Integer> effects, List<CachedPowerUp> pUp, List<Directions> dir, List<List<Integer>> targetLists, int effectNum, int targetNum, List<Point> cells)// only one per effect jump always to next effect at the end so increment effectNUM
    {
        //various options : 1) do and next 2) do and do action
        //-------do cell things
        mapEventDeleter();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Ti serve selezionare una cella,cliccala ");
        a.showAndWait();

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < col; y++) {
                int xx = x, yy = y;


                map[x][y].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        int eN = effectNum;
                        eN++;
                        mapEventDeleter();
                        cells.add(new Point(xx, yy));
                        System.out.println("Sposto bers in cella "+xx+" "+yy);
                        if (eN == effects.size()) {
                            System.out.println("Abbiamo finito i bersagli");
                            checkScope(w, effects, pUp, dir, targetLists, cells);

                        } else {
                            System.out.println("Andiamo con altri bersagli");
                            //-------target nume should be 0?????
                            shootTargetIterator(w, effects, pUp, dir, targetLists, eN, targetNum, cells);
                        }

                    }
                });
            }
        }

    }

    /**
     * check if you want to use the socpe then calls scopeAction
     * @param w
     * @param effects
     * @param pUp
     * @param dir
     * @param targetLists
     * @param cells
     */
    private void checkScope(String w, List<Integer> effects, List<CachedPowerUp> pUp, List<Directions> dir, List<List<Integer>> targetLists, List<Point> cells)
    {
        System.out.println("Sparo con arma " + w + " a questi bersagli:" + targetLists + " con questi effetti " + effects + " In queste celle " + cells);
        //if player has a scope powerUP he can use it, otherwise shoot
        //controls over the scope target??
        int found = 0;
        for (int i = 0; i < gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size(); i++)
        {
            if (gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getType().equals(PowerUpType.TARGETING_SCOPE))
            {
                if (found == 0)
                {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi usare Il miriro?", ButtonType.YES, ButtonType.NO);
                    a.showAndWait();

                    if (a.getResult().equals(ButtonType.NO)) {
                        System.out.println("Mirino no");
                        noScope(w,effects,pUp,dir,targetLists,cells);
                        return;
                    }
                    else{
                        a = new Alert(Alert.AlertType.CONFIRMATION, "Seleziona il mirino da usare");
                        a.show();
                    }

                }
                found++;
                switch (i) {
                    case 0:
                        powerUp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                scopeAction(w, targetLists, effects, cells, pUp, dir, gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0).getColor());
                                return;
                            }
                        });
                        break;
                    case 1:
                        powerUp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                scopeAction(w, targetLists, effects, cells, pUp, dir, gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1).getColor());
                                return;
                            }
                        });
                        break;
                    case 2:
                        powerUp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                scopeAction(w, targetLists, effects, cells, pUp, dir, gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2).getColor());
                                return;
                            }
                        });
                        break;

                }
            }
        }
        if(found==0)
        {
            noScope(w,effects,pUp,dir,targetLists,cells);
        }


    }

    /**
     * no scope, do the shoot action
     * @param w
     * @param effects
     * @param pUp
     * @param dir
     * @param targetLists
     * @param cells
     */
    public void noScope(String w, List<Integer> effects, List<CachedPowerUp> pUp, List<Directions> dir, List<List<Integer>> targetLists, List<Point> cells)
    {
        System.out.println("No scope");
        mapEventDeleter();
        if (isFrenzy) {

            gui.getView().doAction(new FrenzyShoot(new ShootAction(w, targetLists, effects, cells, pUp, null)));
            mapEventDeleter();
        } else {
            if(!dir.isEmpty())
            {
                gui.getView().doAction(new ShootAction(w, dir.get(0),targetLists, effects, cells, pUp, null));
                mapEventDeleter();
            }
            else {
                gui.getView().doAction(new ShootAction(w, targetLists, effects, cells, pUp, null));
                mapEventDeleter();
            }
        }
    }

    /**
     * Show a message on the user interface
     * @param error string to show
     */
    public void show(String error)
    {

        System.out.println(error);
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,error);
    }

    //---------------------------------------------------------------------RELOAD

    /**
     * ask to the player if he wants to reload
     */
    public void checkReload()
    {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi ricaricare qualche arma?", ButtonType.YES, ButtonType.NO);//non credo vada
        a.showAndWait();
        if (a.getResult().equals(ButtonType.NO)) {
            gui.getView().doAction(new SkipAction());
            mapEventDeleter();
            return;
        } else{
                List<String> weapons = new ArrayList<>();
                reloadWeaponChooser(weapons);
            }

        });

    }

    /**
     * choose weapons to reload
     * @param weapons
     */
    public void reloadWeaponChooser(List <String> weapons)
    {
        Alert a=new Alert(Alert.AlertType.INFORMATION,"Seleziona un'arma da ricaricare");
        a.show();
        System.out.println("Sleeziona arma da ricaricare");
        //if arma  non in armi
        myWeapon1.setOnMouseClicked(null);
        myWeapon2.setOnMouseClicked(null);
        myWeapon3.setOnMouseClicked(null);
        if(!weapons.contains(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(0))) {
            myWeapon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseevent) {

                    weapons.add(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(0));
                    Alert a=new Alert(Alert.AlertType.CONFIRMATION,"Vuoi Ricaricare un'altra arma?",ButtonType.YES,ButtonType.NO);
                    a.showAndWait();
                    if(a.getResult().equals(ButtonType.YES))
                    {
                        reloadWeaponChooser(weapons);
                    }else{
                        reloadCostCalc(weapons);
                    }
                    myWeapon1.setOnMouseClicked(null);
                    myWeapon2.setOnMouseClicked(null);
                    myWeapon3.setOnMouseClicked(null);

                }
            });
        }

        if(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().size()==2) {
            if (!weapons.contains(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(1))) {
                myWeapon2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

                        weapons.add(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(1));
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi Ricaricare un'altra arma?", ButtonType.YES, ButtonType.NO);
                        a.showAndWait();
                        if (a.getResult().equals(ButtonType.YES)) {
                            reloadWeaponChooser(weapons);
                        } else {
                            reloadCostCalc(weapons);
                        }
                        myWeapon1.setOnMouseClicked(null);
                        myWeapon2.setOnMouseClicked(null);
                        myWeapon3.setOnMouseClicked(null);
                    }

                });
            }
        }
        if(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().size()==3) {
            if (!weapons.contains(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(2))) {
                myWeapon3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

                        weapons.add(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getWeaponbag().getWeapons().get(2));
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi Ricaricare un'altra arma?", ButtonType.YES, ButtonType.NO);
                        a.show();
                        if (a.getResult().equals(ButtonType.YES)) {
                            reloadWeaponChooser(weapons);
                        } else {
                            reloadCostCalc(weapons);
                        }
                        myWeapon1.setOnMouseClicked(null);
                        myWeapon2.setOnMouseClicked(null);
                        myWeapon3.setOnMouseClicked(null);
                    }

                });
            }
        }
    }

    /**
     * calculate the cost needed to reload then call cehckPAyWithPower
     * @param weapons
     */
    private void reloadCostCalc(List <String> weapons)
    {
        List <Color> cost=new ArrayList<>();
        for(String w:weapons)
        {
            try {
                cost.addAll(gui.getView().getCacheModel().getWeaponInfo(w).getFirstEffectCost());//this because of a sgamo--- firstEffectCost contains reload cost
            } catch (WeaponNotFoundException e) {
                e.printStackTrace();
            }
        }

        checkPayWithPowerUp(cost,weapons,null,"RELOAD",null);//weapNames, dir,actiontype,

    }

    //----------------------------------------------------------------------FRENZY

    /**
     * frenzy reload action
     */
    public void askFrenzyReload()
    {//platform.runlater necessario
        Platform.runLater( () -> {
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,"Vuoi ricaricare ?",ButtonType.YES,ButtonType.NO);
        a.showAndWait();
        if(a.getResult().equals(ButtonType.NO))
        {
            //send empty action
            List <CachedPowerUp> pUp=new ArrayList<>();
            List <String> weapons=new ArrayList<>();
            gui.getView().doAction(new FrenzyShoot(new ReloadAction(weapons, pUp)));
            mapEventDeleter();
        }
        else{
            List<String> weapons = new ArrayList<>();
            reloadWeaponChooser(weapons);
        }
        });

    }

}
