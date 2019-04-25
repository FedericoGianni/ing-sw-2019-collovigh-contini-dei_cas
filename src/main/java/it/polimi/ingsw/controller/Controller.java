package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.PlayerColor;

import java.util.*;

/**
 * This class represent the Controller and invokes actions which modify the Model directly
 *
 */
public class Controller {

    /**
     * Default constructor
     */
    public Controller() {
        players = new ArrayList<>();
        playercolors = new ArrayList<>();
    }


    private int gameId;
    private List<String> players;
    private List<PlayerColor> playercolors;
    private int actionCounter;
    private int roundNumber;
    private Boolean frenzy;
    private int RoundPhase;
    private int frenzyStarter;

    /**
     * @param plist list of player's names
     * @param clist list of player's colors
     */
    public void initGame(List<String> plist, List<PlayerColor> clist) {
        // TODO implement here
    }

    /**
     *
     * @param name
     * @return an integer representing the position of the player in the array of Players, -1 if not present
     */
    public int findPlayerByName(String name) {
        for(int i=0; i < players.size(); i++) {
            if(name.equals(players.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param name chosen by the user at login
     * @return true if the name hasn't already been taken, false otherwise
     */
    public boolean isNameUnused(String name){
        int res = findPlayerByName(name);
        if(res == -1){
            return true;
        }

        return false;
    }

    /**
     *
     * @param playerColor chosen by the user at login
     * @return true if the PlayerColor hasn't already been taken, false otherwise
     */
    public boolean isColorUnused(String playerColor){

        for(PlayerColor p : playercolors){
            if (PlayerColor.valueOf(playerColor.toUpperCase()).equals(p)){
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param name
     * @param playerColor
     */
    public void addPlayerToWaitingRoom(String name, PlayerColor playerColor){
        players.add(name);
        playercolors.add(playerColor);
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