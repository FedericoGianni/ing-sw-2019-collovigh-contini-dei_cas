package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.model.Color;

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

    /**
     * need to be implemented
     */
    public void use() {

    }

}