package it.polimi.ingsw.view.actions.usepowerup;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

/**
 * action of the scope
 */
public class ScopeAction extends PowerUpAction {
    /**
     * id of the target of the scope
     */
    private final Integer targetId;

    public ScopeAction(Color color, int targetId) {

        super(color, PowerUpType.TARGETING_SCOPE);

        this.targetId = targetId;

    }

    public Integer getTargetId() {
        return targetId;
    }
}
