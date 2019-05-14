package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.TurnPhase.SPAWN;

/**
 * This class represent the Controller and invokes actions which modify the Model directly
 *
 */
public class Controller {


    private int gameId;
    private int roundNumber;
    private Boolean frenzy = false;
    private TurnPhase turnPhase;
    private int frenzyStarter;
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
        this.roundNumber = 0;
        this.turnPhase = SPAWN;
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

        this.roundNumber = 0;
        this.turnPhase = SPAWN;
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

        try {

            return nameList
                    .indexOf(nameList
                            .stream()
                            .filter(n -> n == name)
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

    public void turn(){

    }


}