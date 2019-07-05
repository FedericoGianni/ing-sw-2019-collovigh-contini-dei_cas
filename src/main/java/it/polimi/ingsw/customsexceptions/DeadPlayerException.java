package it.polimi.ingsw.customsexceptions;

/**
 * Throws this exception if the player dies by taking more than 10 dmg
 */
public class DeadPlayerException extends Exception implements DeathExceptions{

    private final int  playerId;

    public DeadPlayerException(int playerId) {
        super();
        this.playerId = playerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerId() {
        return this.playerId;
    }
}
