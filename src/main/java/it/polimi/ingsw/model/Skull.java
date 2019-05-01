package it.polimi.ingsw.model;

public class Skull {

    private final int playerId;
    private final int overkill;

    public Skull(int playerId, int overkill) {
        this.playerId = playerId;
        this.overkill = overkill;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getOverkill() {
        return overkill;
    }

    public Skull(Skull copy){

        this.overkill = copy.overkill;
        this.playerId = copy.playerId;
    }
}
