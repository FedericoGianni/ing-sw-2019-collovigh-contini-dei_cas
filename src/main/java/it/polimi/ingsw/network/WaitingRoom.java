package it.polimi.ingsw.network;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoom {

    public static final int TIMER = 30;
    private List<String> players;
    private List<PlayerColor> colors;
    private Boolean active;
    private int activeGame;
    private Games games;


    /**
     * Constructor: creates a new waitingRoom for the player to join both if they want to join a new game or load an old game
     * @param gameId is -1 if the game will be a new one, or the game id if the game is saved
     */
    public WaitingRoom(int gameId) throws GameNonExistentException {

        // things to do for both loaded games and new ones
        this.active = true;
        this.games = new Games();

        // -1 = new game
        if (gameId == -1) {
            this.colors = new ArrayList<>();
            this.players = new ArrayList<>();
        }else{
            if (!games.contains(gameId)) throw new GameNonExistentException();
            // need to catch all saved games and start the correspondent one
        }
    }

    public Boolean isActive(){

        return this.active;
    }

    public synchronized void initGame(){

        this.games.addGame();
        this.active = false;
    }

    /**
     *
     * @param name is the name of the new player
     * @param playerColor is the color of the new player
     * @return id of the player if name and color or throws exceptions if those are already taken
     * @throws NameAlreadyTakenException if the name is already taken
     * @throws ColorAlreadyTakenException of the color is already taken
     */
    public synchronized int addPlayer(String name, PlayerColor playerColor) throws NameAlreadyTakenException,ColorAlreadyTakenException{

        if (isNameAlreadyTaken(name)) {

            throw new NameAlreadyTakenException(name);
        }

        if (isColorAlreadyTaken(playerColor)){

            throw new ColorAlreadyTakenException(playerColor);
        }


        players.add(name);

        colors.add(playerColor);

        return players.indexOf(name);

    }

    /**
     *
     * @param name is the name to check
     * @return true if the name is free
     */
    public Boolean isNameAlreadyTaken(String name){

        return (players.contains(name));
    }

    /**
     *
     * @param color is the color to check
     * @return true if the color is free
     */
    public Boolean isColorAlreadyTaken(PlayerColor color){

        return (colors.contains(color));
    }

    /**
     *
     * @param id is the id of the player to remove
     */
    public synchronized void removePlayer(int id){

        players.remove(id);
        colors.remove(id);
    }

    public synchronized void reconnect(int playerId){

        //TODO
    }



}
