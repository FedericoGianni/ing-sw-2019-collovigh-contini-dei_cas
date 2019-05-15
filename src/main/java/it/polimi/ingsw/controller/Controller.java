package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.view.virtualView.Observer;
import it.polimi.ingsw.view.virtualView.Observers;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.TurnPhase.*;

/**
 * This class represent the Controller and invokes actions which modify the Model directly
 *
 */
public class Controller {


    private int gameId;

    private int roundNumber = 0;

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


        int mapType= this.chooseMap(nameList.size());
        this.model = new Model(nameList,playerColors,mapType);
        this.roundNumber = 0;
        this.gameId = gameId;
        this.players = new ArrayList<>();
        this.observers = new Observers(nameList.size());


    }

    /**
     * OverLoaded Constructor that takes choosen mapType
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     * @param mapType is the mapType
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors,int gameId, int mapType) {


        this.model = new Model(nameList,playerColors,mapType);

        this.roundNumber = 0;
        this.gameId = gameId;
        this.players = new ArrayList<>();
        this.observers = new Observers(nameList.size());

    }


    public Model getModel() {
        return model;
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
        return roundNumber % players.size();
    }

    public void handleTurnPhase(){
        switch(turnPhase){
            //TODO implement a switch case to handle a single turn phase, it is called repeteadly
            //once the vwiev has done the action the controller sets the next turnPhase to the next turnphase
            //so that the next invocation will handle next phase
            //the last phase will increment roundNumber
            case SPAWN:

                if (Model.getPlayer(getCurrentPlayer()).getPowerUpBag().getList().isEmpty()){

                    //if currentPlayer already has 0 powerups in hand -> draw 2

                    drawPowerUp();
                    drawPowerUp();
                }else {

                    //if currentPlayer already has more thean 0 -> draw only 1

                    drawPowerUp();
                }

                //players.get(getCurrentPlayer()).startPhase0();

                break;

            case POWERUP1:
                //TODO
                if(!(Model.getPlayer(getCurrentPlayer()).getPowerUpBag().getList().size() > 0)){
                    incrementPhase();
                }else{
                    //TODO handle phase 1 (need virtualview)
                    //VirtualView.startPhase1();
                }
                break;

            case ACTION1:
                //TODO
                break;

            case POWERUP2:
                //TODO
                break;

            case ACTION2:
                //TODO
                break;

            case POWERUP3:
                //TODO
                break;

            case RELOAD:
                //TODO
                break;
        }
    }

    private void drawPowerUp(){
        Model.getPlayer(getCurrentPlayer()).drawPowerUp();
    }

    private void discardPowerUp(PowerUpType type, Color color){
        Model.getPlayer(getCurrentPlayer()).getPowerUpBag()
                .sellItem(Model.getPlayer(getCurrentPlayer()).getPowerUpBag().findItem(type, color));

    }

    public void spawn(PowerUpType type, Color color){
        discardPowerUp(type, color);
        Model.getPlayer(getCurrentPlayer()).setPlayerPos(Model.getMap().getSpawnCell(color));
        incrementPhase();
    }

    public void useNewton(){
        //TODO
    }

    public void useTeleport(){
        //TODO
    }

    public void useGranade(){
        //TODO
    }

    public void useTargetingScope(){
        //TODO
    }

    public void endPowerUpPhase(){
        //check if it's correct -> should increment the current enum phase to the next one
        incrementPhase();
    }

    public void incrementPhase(){
        turnPhase = TurnPhase.values()[turnPhase.ordinal() + 1];
    }

    //TODO check if it's possible to handle actions in only one class or split in single actions methods
    public void doAction(){
        incrementPhase();
    }





}