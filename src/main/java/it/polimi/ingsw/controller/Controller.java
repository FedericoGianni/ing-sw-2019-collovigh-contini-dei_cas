package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.customsexceptions.PlayerNonExistentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.model.powerup.Teleporter;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.cachemodel.updates.InitialUpdate;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.TurnPhase.SPAWN;
import static it.polimi.ingsw.model.powerup.PowerUpType.NEWTON;
import static it.polimi.ingsw.model.powerup.PowerUpType.TELEPORTER;

/**
 *
 * This class represent the Controller and invokes actions which modify the Model directly.
 * The Controller initialize the model inside its constructor, and then handles every turn
 * with its main method handleTurnPhase(). this method changes basing on the TurnPhase.
 *
 */
public class Controller {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private int gameId;

    private int roundNumber;

    private Boolean frenzy = false;

    private Boolean hasSomeoneDied = false;

    //used for TagBackGrenade (check if it is necessary)

    private List<Integer> shotPlayerThisTurn;

    private TurnPhase turnPhase = SPAWN;

    private int frenzyStarter;

    private final Model model;

    private List<VirtualView> players;

    private final Observers observers;

    // Methods Classes

    private SpawnPhase spawnPhase = new SpawnPhase(this);

    private PowerUpPhase powerUpPhase = new PowerUpPhase(this);




    /**
     * Constructor
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors, int gameId) {

        this.observers = new Observers(this, nameList.size()); // needs to stay first

        int mapType= this.chooseMap(nameList.size());
        this.roundNumber = 0;
        this.gameId = gameId;
        this.players = new ArrayList<>();


        for (int i = 0; i < nameList.size(); i++) {
            players.add(new VirtualView(i, this, Server.getClient(i)));
        }

        sendInitialUpdate( nameList, playerColors, gameId, mapType);

        this.model = new Model(nameList,playerColors,mapType);

        LOGGER.log(level,"[CONTROLLER] Initialized Model with random MapType");
    }

    /**
     * OverLoaded Constructor that takes choosen mapType
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     * @param mapType is the mapType
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors,int gameId, int mapType) {


        this.observers = new Observers(this, nameList.size()); // needs to stay first

        this.model = new Model(nameList,playerColors,mapType);

        this.roundNumber = 0;
        this.gameId = gameId;
        this.players = new ArrayList<>();



        for (int i = 0; i < nameList.size(); i++) {
            players.add(new VirtualView(i, this, Server.getClient(i)));
        }

        sendInitialUpdate( nameList, playerColors, gameId, mapType);

        LOGGER.log(level,"[CONTROLLER] Initialized Model with chosen MapType");
    }


    // Getters

    public Model getModel() {
        return model;
    }

    public VirtualView getVirtualView(int id) {
        return players.get(id);
    }

    public List<VirtualView> getVirtualViews(){
        return new ArrayList<>(players);
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public int getCurrentPlayer(){

        if(roundNumber == 0){
            return 0;
        }

        return roundNumber % players.size();
    }


    // management Methods

    /**
     *
     * @param playerNumber is the number of player
     * @return a random int representing a random MapType
     */
    private int chooseMap(int playerNumber){

        Random rand = new Random();

        switch (playerNumber) {

            case 3:
                return 1 + rand.nextInt(2);

            case 4:
                return 1 + rand.nextInt(3);

            case 5:
                return 2 + rand.nextInt(2);

            default:
                return 2;
        }
    }


    /**
     *
     * @param name
     * @return an integer representing the position of the player in the array of Players, -1 if not present
     */
    public int findPlayerByName(String name) {

        List<String>nameList = Model
                .getGame()
                .getPlayers()
                .stream()
                .map(Player::getPlayerName)
                .collect(Collectors.toList());

        try {

            return nameList
                    .indexOf(nameList
                            .stream()
                            .filter(n -> n.equals(name))
                            .collect(Collectors.toList())
                            .get(0));

        }catch (NullPointerException e){

            return -1;
        }
    }

    /**
     *
     * @param playerId is the id of the given player
     * @return the name of the given player
     */
    public String getPlayerName(int playerId){

        try {

            return Model.getPlayer(playerId).getPlayerName();

        }catch (NullPointerException e){

            return null;
        }
    }


    public void setPlayerOnline(int playerId){ Model.getPlayer(playerId).getStats().setOnline(true); }

    public void setPlayerOffline(int playerId){ Model.getPlayer(playerId).getStats().setOnline(false); }



    // Turn Management

    private void sendInitialUpdate(List<String> nameList, List<PlayerColor>playerColors, int gameId, int mapType ){

        InitialUpdate update = new InitialUpdate(nameList,playerColors, gameId, mapType);
        UpdateClass updateClass = new UpdateClass(UpdateType.INITIAL,update,-1);

        for (VirtualView v: players){

            v.sendUpdates(updateClass);

            //inform the virtual views that the game has started
            v.startGame();
        }
    }

    public void handleTurnPhase(){
        switch(turnPhase){
            //once the virtual wiev has done the action the controller sets the next turnPhase to the next turnphase
            //so that the next invocation will handle next phase -> this is done by calling incrementPhase()
            //the last phase (ACTION) will increment roundNumber
            case SPAWN:
                spawnPhase.handleSpawn();
                //increment phase should be done by the virtual view, who calls the function spawn
                break;

            case POWERUP1:
                powerUpPhase.handlePowerUp();
                break;

            case ACTION1:
                handleAction();
                break;

            case POWERUP2:
                powerUpPhase.handlePowerUp();
                break;

            case ACTION2:
                handleAction();
                break;

            case POWERUP3:
                powerUpPhase.handlePowerUp();
                break;

            case RELOAD:
                //TODO handle last turn phase. reload and increment round number
                handleReload();
                //check if it's ok to increment roundNumber here

                //this 2 commands should be called by reload method, not here
                roundNumber++;
                incrementPhase();
                break;
        }
    }


    public void incrementPhase(){
        turnPhase = TurnPhase.values()[turnPhase.ordinal() + 1];
        handleTurnPhase();
    }

    // Spawn Phase

    public void spawn(PowerUpType type, Color color){ spawnPhase.spawn(type,color); }


    // Power Up

    public void drawPowerUp(){
        Model.getPlayer(getCurrentPlayer()).drawPowerUp();
    }

    public void discardPowerUp(PowerUpType type, Color color){ powerUpPhase.discardPowerUp(type,color);}

    public void doAction(JsonAction jsonAction) {

        switch (jsonAction.getType()){

            case POWER_UP:

                powerUpPhase.usePowerUp((PowerUpAction) jsonAction);

                break;

            case MOVE:

                //TODO move

                break;

            default:

                break;
        }
    }



    public void handleAction(){
        players.get(getCurrentPlayer()).startAction();
    }

    public void handleReload(){
        players.get(getCurrentPlayer()).startReload();
    }


    public void useTeleport(Color color, int r, int c){
        //TODO check if exception logic is viable
        LOGGER.info("[CONTROLLER] player id " + getCurrentPlayer() + "calling useTeleport");
        Cell cell = Model.getMap().getCell(r,c);
        Teleporter t = (Teleporter) Model.getPlayer(getCurrentPlayer()).getPowerUpBag().findItem(TELEPORTER, color);
        try {
            t.use(cell);
        } catch (CardNotPossessedException e){
            //this shouldn't happen since the client can only send PowerUps that has in hand
        } catch (CellNonExistentException e){
            //this shouldn't happen too since the client can only send Cell which exists?
            //TODO check for SameCellException? -> or it is a valid action?
        }
     }

    public void useGranade(){
        LOGGER.info("[CONTROLLER] player id " + getCurrentPlayer() + "calling useGranade");
        //TODO
    }

    public void useTargetingScope(){
        //TODO
        LOGGER.info("[CONTROLLER] player id " + getCurrentPlayer() + "calling useTargetingScope");
    }

    //ACTIONS

    public void move(int r, int c){
        LOGGER.info("[CONTROLLER] player id " + getCurrentPlayer() + "calling move");
        Cell cell = Model.getMap().getCell(r, c);
        Model.getPlayer(getCurrentPlayer()).setPlayerPos(cell);
        incrementPhase();
    }

    public void moveGrab(int r, int c){
        LOGGER.info("[CONTROLLER] player id " + getCurrentPlayer() + "calling moveGrab");
        Cell cell = Model.getMap().getCell(r, c);
        LOGGER.info("[CONTROLLER] accessing Model to set new position");
        Model.getPlayer(getCurrentPlayer()).setPlayerPos(cell);
        grab();
        incrementPhase();
    }

    public void grab(){
        LOGGER.info("[CONTROLLER] accessing Model to grab Ammo inside cell: " + Model.getPlayer(getCurrentPlayer()).getStats().getCurrentPosition());
        Model.getPlayer(getCurrentPlayer()).pickAmmoHere();
        incrementPhase();
    }

    //TODO need to know Cached_Weapon logic and MacroEffect logic
    public void shoot(int weapon, int target){
        incrementPhase();
    }


}