package it.polimi.ingsw.model.player;

/**
 * This class represents the skull on the map
 */
public class Skull {

    /**
     * is the id of the player who has killed
     */
    private final int playerId;
    /**
     *is true if the kill was an overkill
     */
    private final Boolean overkill;

    /**
     *
     * @param killerId is the id of the killer
     * @param overkill is true if the kill was overkill
     */
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
