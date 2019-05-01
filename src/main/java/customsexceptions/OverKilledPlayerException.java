package customsexceptions;

public class OverKilledPlayerException extends Exception implements DeathExceptions{

    private final int playerId;

    public OverKilledPlayerException(int playerId) {
        super();
        this.playerId=playerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerId() {
        return this.playerId;
    }
}
