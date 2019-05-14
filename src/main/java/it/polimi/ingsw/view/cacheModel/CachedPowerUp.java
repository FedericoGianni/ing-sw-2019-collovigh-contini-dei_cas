package it.polimi.ingsw.view.cacheModel;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.powerup.*;

import java.io.Serializable;

public class CachedPowerUp implements Serializable {

    private final Color color;
    private final PowerUpType type;

    public CachedPowerUp(PowerUp powerUp) {
        this.color = powerUp.getColor();
        this.type = powerUp.getType();
    }

    public Color getColor() {
        return color;
    }

    public PowerUpType getType() {
        return type;
    }


    @Override
    public String toString() {
        return "PowerUp{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }
}
