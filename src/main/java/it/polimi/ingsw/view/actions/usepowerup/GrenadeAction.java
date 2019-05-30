package it.polimi.ingsw.view.actions.usepowerup;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.powerup.PowerUpType;

public class GrenadeAction extends PowerUpAction {

    private final int possessorId;

    public GrenadeAction(Color color, int possessorId) {

        super(color, PowerUpType.TAG_BACK_GRENADE);

        this.possessorId = possessorId;  // identify the player who launched it

    }

    public int getPossessorId() {
        return possessorId;
    }
}
