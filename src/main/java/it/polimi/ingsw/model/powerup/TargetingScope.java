package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

/**
 * 
 */
public class TargetingScope extends PowerUp {

    /**
     * Default constructor
     */
    public TargetingScope(Color color) {

        super(color);
        this.setType(PowerUpType.TARGETING_SCOPE);
    }

}