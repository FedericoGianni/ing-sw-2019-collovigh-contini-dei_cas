package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.powerup.*;
import it.polimi.ingsw.view.updates.Update;

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
