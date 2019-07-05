package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.ShootAction;

/**
 * This class represent the TargetingScope powerUp
 *
 * this card will add one damage to a target in a shoot action
 *
 * @see it.polimi.ingsw.controller.ActionPhase
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