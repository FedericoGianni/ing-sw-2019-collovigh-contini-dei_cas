package it.polimi.ingsw.view.cacheModel;

import java.io.Serializable;

public class PlayerAttributes implements Serializable {

    private final int playerId;
    private final CachedStats stats;
    private final CachedPowerUpBag powerUpBag;
    private final CachedAmmoBag ammoBag;

    public PlayerAttributes(int playerId, CachedStats stats, CachedPowerUpBag powerUpBag, CachedAmmoBag ammoBag) {
        this.playerId = playerId;
        this.stats = stats;
        this.powerUpBag = powerUpBag;
        this.ammoBag = ammoBag;
    }

    public int getPlayerId() {
        return playerId;
    }

    public CachedStats getStats() {
        return stats;
    }

    public CachedPowerUpBag getPowerUpBag() {
        return powerUpBag;
    }

    public CachedAmmoBag getAmmoBag() {
        return ammoBag;
    }
}
