package it.polimi.ingsw.view.updates.otherplayerturn;

import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

public class PowerUpTurnUpdate extends TurnUpdate{

    private final CachedPowerUp powerUp;

    public PowerUpTurnUpdate( int playerId, CachedPowerUp powerUp) {
        super(TurnUpdateType.POWERUP, playerId);
        this.powerUp = powerUp;
    }

    public CachedPowerUp getPowerUp() {
        return powerUp;
    }
}
