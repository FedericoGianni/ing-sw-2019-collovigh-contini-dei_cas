package it.polimi.ingsw.view.actions.usepowerup;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.view.actions.ActionTypes;
import it.polimi.ingsw.view.actions.JsonAction;

public class PowerUpAction extends JsonAction {

    private final Color color;
    private final PowerUpType powerUpType;

    public PowerUpAction(Color color, PowerUpType type) {
        super(ActionTypes.POWER_UP);
        this.color = color;
        this.powerUpType = type;
    }

    public Color getColor() {
        return color;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}
