package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.customsexceptions.PlayerNonExistentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.model.powerup.Teleporter;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.virtualView.observers.Observers;
import it.polimi.ingsw.view.virtualView.VirtualView;

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




    /**
     * Constructor
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors, int gameId) {

        this.observers = new Observers(this, nameList.size()); // needs to stay first

        int mapType= this.chooseMap(nameList.size());
        this.model = new Model(nameList,playerColors,mapType);
        this.roundNumber = 0;
        this.gameId = gameId;
        this.players = new ArrayList<>();


        for (int i = 0; i < nameList.size(); i++) {
            players.add(new VirtualView(i, this, Server.getClient(i)));
        }
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

    }


    public Model getModel() {
        return model;
    }

    public VirtualView getVirtualView(int id) {
        return players.get(id);
    }

    public List<VirtualView> getVirtualViews(){
        return new ArrayList<>(players);
    }



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
                return -1;
        }
    }


    // management Methods

    /**
     *
     * @param name
     * @return an integer representing the position of the player in the array of Players, -1 if not present
     */
    public int findPlayerByName(String name) {

        List<String>nameList = model.getGame()
                .getPlayers()
                .stream()
                .map(player -> player.getPlayerName())
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

    public String getPlayerName(int playerId){

        try {

            return Model.getPlayer(playerId).getPlayerName();

        }catch (NullPointerException e){

            return null;
        }
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     *
     * @return and integer representing the id index of the virtualView representing the current playing player
     */
    private int getCurrentPlayer(){

        if(roundNumber == 0){
            return 0;
        }

        return roundNumber % players.size();
    }


    // Turn Management

    public void handleTurnPhase(){
        switch(turnPhase){
            //once the virtual wiev has done the action the controller sets the next turnPhase to the next turnphase
            //so that the next invocation will handle next phase -> this is done by calling incrementPhase()
            //the last phase (ACTION) will increment roundNumber
            case SPAWN:
                handleSpawn();
                //increment phase should be done by the virtual view, who calls the function spawn
                break;

            case POWERUP1:
                handlePowerUp();
                break;

            case ACTION1:
                handleAction();
                break;

            case POWERUP2:
                handlePowerUp();
                break;

            case ACTION2:
                handleAction();
                break;

            case POWERUP3:
                handlePowerUp();
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

    public void handleSpawn(){

        if(Model.getPlayer(getCurrentPlayer()).getStats().getCurrentPosition() != null) {
            //if player has already spawned it skips this phase
            LOGGER.info("[CONTROLLER] skipping SPAWN phase for player: " +getCurrentPlayer());
            incrementPhase();

        } else if (Model.getPlayer(getCurrentPlayer()).getPowerUpBag().getList().isEmpty()) {

            //if currentPlayer already has 0 powerups in hand -> draw 2
            LOGGER.info("[CONTROLLER] accessing Model to drawPowerUp for player: " + getCurrentPlayer());
            drawPowerUp();
            LOGGER.info("[CONTROLLER] accessing Model to drawPowerUp for player: " + getCurrentPlayer());
            drawPowerUp();
            LOGGER.info("[CONTROLLER] calling startPhase0 for virtual view id: " + getCurrentPlayer());
            players.get(getCurrentPlayer()).startSpawn();

        } else {

            //if currentPlayer already has more thean 0 -> draw only 1
            LOGGER.info("[CONTROLLER] accessing Model to drawPowerUp for player: " + getCurrentPlayer());
            drawPowerUp();
            LOGGER.info("[CONTROLLER] calling startPhase0 for virtual view id: " + getCurrentPlayer());
            players.get(getCurrentPlayer()).startSpawn();
        }
    }

    public void handlePowerUp(){
        //if current player hasn't got any PowerUp in hand -> skip this phase
        if(Model.getPlayer(getCurrentPlayer()).getPowerUpBag().getList().isEmpty()){
            incrementPhase();
        }else{
            players.get(getCurrentPlayer()).startPowerUp();
        }
    }

    public void handleAction(){
        players.get(getCurrentPlayer()).startAction();
    }

    public void handleReload(){
        players.get(getCurrentPlayer()).startReload();
    }

    /*
     *Here there should be atomic functions which will be used by the main handlePhase() method
     */

    private void drawPowerUp(){
        Model.getPlayer(getCurrentPlayer()).drawPowerUp();
    }

    private void discardPowerUp(PowerUpType type, Color color){
        Model.getPlayer(getCurrentPlayer()).getPowerUpBag()
                .sellItem(Model.getPlayer(getCurrentPlayer()).getPowerUpBag().findItem(type, color));

    }

    public void spawn(PowerUpType type, Color color){
        LOGGER.info("[CONTROLLER] accessing Model to discard PowerUp for player: " + getCurrentPlayer());
        discardPowerUp(type, color);
        Model.getPlayer(getCurrentPlayer()).setPlayerPos(Model.getMap().getSpawnCell(color));
        LOGGER.info("[CONTROLLER] incrementingGamePhase: " + getTurnPhase() );
        incrementPhase();
        LOGGER.info("[CONTROLLER] new Phase: " + getTurnPhase());
    }

    public void useNewton(Color color, int playerID, Directions d, int amount){
        //TODO check if exception logic is viable
        LOGGER.info("[CONTROLLER] player id: " + getCurrentPlayer() + "calling useNewton");

        try {
            Newton n = (Newton) Model.getPlayer(getCurrentPlayer()).getPowerUpBag().findItem(NEWTON, color);
            n.use(Model.getPlayer(playerID), d, amount);
        } catch(PlayerNonExistentException e){
            //can be validated inside client so that he can send only existent players
        } catch(CellNonExistentException e){
            //same as before
        }
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

    //TODO need to know CachedWeapon logic and MacroEffect logic
    public void shoot(int weapon, int target){
        incrementPhase();
    }

    public void incrementPhase(){
        turnPhase = TurnPhase.values()[turnPhase.ordinal() + 1];
        handleTurnPhase();
    }
}