package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.controller.TurnPhase;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used to serialize the attributes needed from controller in a file to be read, allowing game persistence
 */
public class SavedController  implements Serializable {

    /**
     * id of the game needed to save and load different savegames
     */
    private final int gameId;

    /**
     * number of the current turn of the game
     */
    private final int roundNumber;

    /**
     * true if in frenzy mode, false otherwise
     */
    private final Boolean frenzy;

    /**
     * true if someone died during this turn
     */
    private final Boolean hasSomeoneDied;

    /**
     * list of player IDs representing which player has been shot this tunrn
     */
    private final List<Integer> shotPlayerThisTurn;

    /**
     * current turn phase
     */
    private final TurnPhase turnPhase;

    /**
     * id of the player who started frenzy phase
     */
    private final int frenzyStarter;

    /**
     * size of the player numbers at the time of save
     */
    private final int playerSize;

    /**
     * true if game has ended, false otherwise
     */
    private final boolean gameEnded ;

    /**
     * useful boolean to check if the current game is a game read from file
     */
    private final boolean reloadGame = true;

    /**
     * Contructor wich takes paramter of the controller to be saved and places them in the serializable SavedController class
     * @param gameId
     * @param roundNumber
     * @param frenzy
     * @param hasSomeoneDied
     * @param shotPlayerThisTurn
     * @param turnPhase
     * @param frenzyStarter
     * @param playerSize
     * @param gameEnded
     */
    public SavedController(int gameId, int roundNumber, Boolean frenzy, Boolean hasSomeoneDied, List<Integer> shotPlayerThisTurn, TurnPhase turnPhase, int frenzyStarter, int playerSize, boolean gameEnded) {
        this.gameId = gameId;
        this.roundNumber = roundNumber;
        this.frenzy = frenzy;
        this.hasSomeoneDied = hasSomeoneDied;
        this.shotPlayerThisTurn = shotPlayerThisTurn;
        this.turnPhase = turnPhase;
        this.frenzyStarter = frenzyStarter;
        this.playerSize = playerSize;
        this.gameEnded = gameEnded;
    }

    public boolean isReloadGame() {
        return reloadGame;
    }

    public int getGameId() {
        return gameId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Boolean getFrenzy() {
        return frenzy;
    }

    public Boolean getHasSomeoneDied() {
        return hasSomeoneDied;
    }

    public List<Integer> getShotPlayerThisTurn() {
        return shotPlayerThisTurn;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public int getFrenzyStarter() {
        return frenzyStarter;
    }

    public int getPlayerSize() {
        return playerSize;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }
}
