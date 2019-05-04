package it.polimi.ingsw.customsexceptions;

public class FrenzyActivatedException extends Exception implements DeathExceptions{

    private final int playerId;

    public FrenzyActivatedException(int playerId) {

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
