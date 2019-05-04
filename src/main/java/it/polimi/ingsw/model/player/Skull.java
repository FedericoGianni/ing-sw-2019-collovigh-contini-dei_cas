package it.polimi.ingsw.model.player;

public class Skull {

    private final int playerId;
    private final Boolean overkill;

    public Skull(int killerId, Boolean overkill) {
        this.playerId = killerId;
        this.overkill = overkill;
    }

    public int getKillerId() {
        return playerId;
    }

    public int getAmount() {
        return overkill ? 2 : 1;
    }
}
