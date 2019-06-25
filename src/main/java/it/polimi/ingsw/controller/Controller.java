package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.serveronly.Server;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;
import it.polimi.ingsw.view.virtualView.VirtualView;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.TurnPhase.END;
import static it.polimi.ingsw.controller.TurnPhase.SPAWN;
import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_RECEIVED_FRENZY_BUT_EXPECTED_NORMAL;
import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_RECEIVED_NORMAL_BUT_EXPECTED_FRENZY;


/**
 *
 * This class represent the Controller and invokes actions which modify the Model directly.
 * The Controller initialize the model inside its constructor, and then handles every turn
 * with its main method handleTurnPhase(). this method changes basing on the TurnPhase.
 *
 */
public class Controller {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private int gameId;

    private int roundNumber;

    private Boolean frenzy = false;

    private Boolean hasSomeoneDied = false;

    private Boolean expectingAnswer = false;

    //used for TagBackGrenade (check if it is necessary)

    private List<Integer> shotPlayerThisTurn = new ArrayList<>(); // TODO add to shoot

    private TurnPhase turnPhase = SPAWN;

    private int frenzyStarter;

    private final Model model;

    private List<VirtualView> players;

    private final Observers observers;

    // Methods Classes

    private UtilityMethods utilityMethods = new UtilityMethods(this);

    private SpawnPhase spawnPhase = new SpawnPhase(this);

    private PowerUpPhase powerUpPhase = new PowerUpPhase(this);

    private ActionPhase actionPhase = new ActionPhase(this);

    private PointCounter pointCounter = new PointCounter(this);

    private ReloadPhase reloadPhase = new ReloadPhase(this);

    private Timer timer = new Timer(this);




    /**
     * Constructor
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors, int gameId, int skulls) {

        this.observers = new Observers(this, nameList.size()); // needs to stay first

        int mapType= this.chooseMap(nameList.size());
        this.roundNumber = 0;
        this.gameId = gameId;
        this.players = new ArrayList<>();


        for (int i = 0; i < nameList.size(); i++) {
            players.add(new VirtualView(i, this, Server.getClient(i)));
        }

        sendInitialUpdate( nameList, playerColors, gameId, mapType);

        this.model = new Model(nameList,playerColors,mapType,skulls);

        LOGGER.log(level,"[CONTROLLER] Initialized Model with random MapType");
    }

    /**
     * OverLoaded Constructor that takes choosen mapType
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     * @param mapType is the mapType
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors,int gameId, int mapType, int skulls) {


        this.observers = new Observers(this, nameList.size()); // needs to stay first

        this.model = new Model(nameList,playerColors,mapType,skulls);

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

    public List<Integer> getShotPlayerThisTurn() { return shotPlayerThisTurn; }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public int getGameId() { return gameId; }

    public int getCurrentPlayer(){

        if(roundNumber == 0){
            return 0;
        }

        return roundNumber % players.size();
    }

    public List<Integer> getPlayerOnline(){

        return   Model
                .getGame()
                .getPlayers()
                .stream()
                .filter( x -> x.getStats().getOnline())
                .map(Player::getPlayerId)
                .collect(Collectors.toList());


    }

    public Boolean getFrenzy() {
        return frenzy;
    }

    public Boolean getHasSomeoneDied() {
        return hasSomeoneDied;
    }

    public int getFrenzyStarter() {
        return frenzyStarter;
    }

    public void setFrenzy(Boolean frenzy) {
        this.frenzy = frenzy;
    }

    public void setHasSomeoneDied(Boolean hasSomeoneDied) {
        this.hasSomeoneDied = hasSomeoneDied;
    }

    public void setFrenzyStarter(int frenzyStarter) {
        this.frenzyStarter = frenzyStarter;
    }

    public UtilityMethods getUtilityMethods() {
        return utilityMethods;
    }

    public ReloadPhase getReloadPhase() { return reloadPhase; }

    public void setExpectingAnswer(Boolean expectingAnswer) { this.expectingAnswer = expectingAnswer; }

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

        }catch ( NullPointerException | IndexOutOfBoundsException e){

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

    public void setPlayerOffline(int playerId){

        Model.getPlayer(playerId).getStats().setOnline(false);

        if ((playerId == getCurrentPlayer()) && (expectingAnswer)){

            timer.stopTimer();

            defaultAnswer();
        }
    }

    public Boolean isPlayerOnline(int playerId){ return Model.getPlayer(playerId).getStats().getOnline(); }

    public Timer getTimer() { return timer; }

    // Turn Management

    private void sendInitialUpdate(List<String> nameList, List<PlayerColor>playerColors, int gameId, int mapType ){

        UpdateClass updateClass = new InitialUpdate(nameList,playerColors, gameId, mapType);

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

                // increment phase if virtual view method is called will be done in the answer method

                break;

            case POWERUP1:

                powerUpPhase.handlePowerUp();

                break;

            case ACTION1:

                actionPhase.handleAction();

                break;

            case POWERUP2:

                powerUpPhase.handlePowerUp();

                break;

            case ACTION2:

                actionPhase.handleAction();

                break;

            case POWERUP3:

                powerUpPhase.handlePowerUp();

                break;

            case RELOAD:

                // calls the method on the virtual view

                reloadPhase.handleReload();

                break;

            case END:

                // replaces the empty ammoCard

                Model.getMap().replaceAmmoCard();

                if(hasSomeoneDied){

                    pointCounter.calculateTurnPoints();

                }

                // if frenzy is activated check if game has ended

                if (frenzy && frenzyStarter == getCurrentPlayer()) endGame();

                incrementPhase();

        }
    }


    public void incrementPhase(){
        if(turnPhase == END){
            turnPhase = SPAWN;
            roundNumber++;
            LOGGER.log(level, "[CONTROLLER] switch to phase: {0}", turnPhase);
            handleTurnPhase();
        } else {
            turnPhase = TurnPhase.values()[turnPhase.ordinal() + 1];

            LOGGER.log(level, "[CONTROLLER] switch to phase: {0}", turnPhase);

            handleTurnPhase();
        }

    }

    // Spawn Phase

    public void spawn(PowerUpType type, Color color){

        // stops the timer

        timer.stopTimer();

        spawnPhase.spawn(type,color);
    }


    // Power Up

    public void drawPowerUp(){
        Model.getPlayer(getCurrentPlayer()).drawPowerUp();
    }

    public void discardPowerUp(PowerUpType type, Color color){ powerUpPhase.discardPowerUp(type,color);}

    public void doAction(JsonAction jsonAction) {

        // stops the timer

        timer.stopTimer();

        expectingAnswer = false;

        LOGGER.log(level,"[Controller] Received do action of type: {0}", jsonAction.getType());

        switch (jsonAction.getType()){

            case POWER_UP:

                powerUpPhase.usePowerUp((PowerUpAction) jsonAction);

                break;

            case MOVE:

                if (frenzy) actionPhase.frenzyMoveAction((Move)jsonAction);

                else actionPhase.moveAction((Move)jsonAction);

                break;

            case GRAB:

                if (frenzy) actionPhase.frenzyGrabAction((GrabAction) jsonAction);

                else actionPhase.grabAction((GrabAction) jsonAction);

                break;

            case SHOOT:

                checkActionIsNotFrenzy();

                actionPhase.shootAction((ShootAction) jsonAction);

                break;

            case FRENZY_SHOOT:

                checkActionIsFrenzy();

                actionPhase.frenzyShootAction((FrenzyShoot) jsonAction);

                break;

            case RELOAD:

                reloadPhase.reload((ReloadAction) jsonAction);

                break;

            case SKIP:

                incrementPhase();

                break;

            default:

                break;
        }
    }

    private void checkActionIsFrenzy(){

        if (!frenzy){

            getVirtualView(getCurrentPlayer()).show(DEFAULT_RECEIVED_NORMAL_BUT_EXPECTED_FRENZY);

            actionPhase.handleAction();
        }
    }

    private void checkActionIsNotFrenzy(){

        if (frenzy){

            getVirtualView(getCurrentPlayer()).show(DEFAULT_RECEIVED_FRENZY_BUT_EXPECTED_NORMAL);

            actionPhase.handleAction();
        }
    }

    public boolean askMoveValid(int row, int column, Directions direction){
        return utilityMethods.askMoveValid(row,column,direction);
    }



    //ACTIONS


    /**
     *
     * @param turnUpdate is the update to send to the Inactive player
     */
    public void updateInactivePlayers(TurnUpdate turnUpdate){

        for (VirtualView player : players){

            if (isPlayerOnline(player.getPlayerId()) && (player.getPlayerId() != getCurrentPlayer())){

                player.sendUpdates(turnUpdate);
            }
        }
    }

    /**
     * This method will send default answer if the timer for the action ended
     */
    public void defaultAnswer(){

        if (turnPhase.equals(SPAWN)){

            // if the phase is spawn perform a spawn action

            this.spawnPhase.defaultSpawn();

        }else {

            // else perform a skip action

            this.doAction(new SkipAction());
        }
    }

    /**
     * This method will kill the player
     * @param playerId is the id of the dead player
     */
    public void killPlayer(int playerId){

        hasSomeoneDied = true;

        LOGGER.log(level, "[Controller] Player w/ id {0} has been killed ", playerId);

        Model.getPlayer(playerId).setPlayerPos(null);

        Model.getGame().addkills(getCurrentPlayer(),false);


    }

    /**
     * This method will over kill the player
     * @param playerId is the id of the dead player
     */
    public void overKillPlayer(int playerId){

        hasSomeoneDied = true;

        LOGGER.log(level, "[Controller] Player w/ id {0} has been overKilled ", playerId);

        Model.getPlayer(playerId).setPlayerPos(null);

        // add skull to killShotTrack

        Model.getGame().addkills(getCurrentPlayer(),true);

        // set revenge Mark

        Model.getPlayer(getCurrentPlayer()).getStats().addMarks(playerId);

    }

    /**
     * This method activate Frenzy Mode
     */
    public void activateFrenzy(){

        // get the current game

        CurrentGame game = Model.getGame();

        // set the frenzy starter

        observers.getController().setFrenzyStarter(game.getKillShotTrack().get(game.getKillShotTrack().size() -1).getKillerId());

        // set the frenzy check to true

        observers.getController().setFrenzy(true);

        // set the damage boards to frenzy if empty

        for (Player player : game.getPlayers()){

            if (player.getStats().getDmgTaken().isEmpty()) player.getStats().setFrenzyBoard(true);
        }
    }

    /**
     * This method ends the game
     */
    public void endGame(){

        pointCounter.calcGamePoints();

        for (int i = 0; i < players.size(); i++) {

            if (isPlayerOnline(i)){

                getVirtualView(i).endGame();
            }
        }
    }



}
