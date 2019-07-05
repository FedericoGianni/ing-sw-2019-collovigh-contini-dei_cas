package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

/**
 *  Abstract Class for PowerUps
 *
 * @see Newton
 * @see TargetingScope
 * @see TagbackGrenade
 * @see Teleporter
 */
public abstract class PowerUp {

    /**
     * color of the powerUp
     */
    private final Color color;
    /**
     * type of the powerUp
     */
    private PowerUpType type;

    /**
     *
     * @param color of the card and of the cube that will be obtained if sold
     */
    public PowerUp(Color color) {

        this.color = color;
    }


    /**
     * @return an AmmoCube of the color of the card
     */
    public AmmoCube sell() {

        return new AmmoCube(this.color);
    }

    public Color getColor() {
        return color;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

}