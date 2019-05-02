package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.PlayerColor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represent the Controller and invokes actions which modify the Model directly
 *
 */
public class Controller {


    private int gameId;
    private int roundNumber;
    private Boolean frenzy = false;
    private int roundPhase;
    private int frenzyStarter;
    private final List<ClientController> players;
    private final Model game;


    /**
     *  Constructor
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors,int gameId) {


        int mapType= this.chooseMap(nameList.size());
        this.game = new Model(nameList,playerColors,mapType);

        players = new ArrayList<>();

        for (int i = 0; i < nameList.size(); i++) {

            players.add(new ClientController(i,this));
            
        }

        this.roundNumber = 0;
        this.roundPhase = 0;
        this.gameId = gameId;

    }

    /**
     *  OverLoaded Constructor that takes choosen mapType
     * @param nameList is a list of player's names
     * @param playerColors is a list of player's Color
     * @param gameId is the id of the game
     * @param mapType is the mapType
     */
    public Controller(List<String> nameList, List<PlayerColor>playerColors,int gameId, int mapType) {


        this.game = new Model(nameList,playerColors,mapType);

        players = new ArrayList<>();

        for (int i = 0; i < nameList.size(); i++) {

            players.add(new ClientController(i,this));

        }

        this.roundNumber = 0;
        this.roundPhase = 0;
        this.gameId = gameId;

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

        List<String>nameList = game.getGame()
                .getPlayers()
                .stream()
                .map(player -> player.getPlayerName())
                .collect(Collectors.toList());

        return  nameList
                .indexOf(nameList
                .stream()
                .filter(n -> n == name)
                .collect(Collectors.toList())
                .get(0));
    }



    /**
     * @return ID of the current playing Player
     */
    public int getCurrentPlayer() {
        // TODO need to be implemented
        return 0;
    }

    /**
     * @param Action
     *
    public void action(Action action) {
        // TODO implement here
    }*/

    /**
     *
     */
    public void endOfTurn() {
        // TODO need to be implemented
    }

    /**
     * @return
     */
    public Boolean isKillShotTrackEmpty() {
        // TODO need to be implemented
        return null;
    }

    /**
     *
     */
    public void turnPointsCalculator() {
        // TODO need to be implemented
    }

    /**
     * @param playerId
     */
    public void startNewRound(int playerId) {
        // TODO need to be implemented
    }

}