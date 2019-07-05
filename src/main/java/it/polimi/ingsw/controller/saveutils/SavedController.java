package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.controller.TurnPhase;

import java.io.Serializable;
import java.util.List;

public class SavedController  implements Serializable {

    private final int gameId;

    private final int roundNumber;

    private final Boolean frenzy;

    private final Boolean hasSomeoneDied;

    private final List<Integer> shotPlayerThisTurn;

    private final TurnPhase turnPhase;

    private final int frenzyStarter;

    private final int playerSize;

    private final boolean gameEnded ;

    private final boolean reloadGame = true;


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
