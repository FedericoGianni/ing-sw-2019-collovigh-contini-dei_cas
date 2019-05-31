package it.polimi.ingsw.view.updates.otherplayerturn;



import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

public abstract class TurnUpdate extends UpdateClass {

    private final TurnUpdateType actionType;

    public TurnUpdate(TurnUpdateType actionType, int playerId) {

        super(UpdateType.TURN,playerId);

        this.actionType = actionType;

    }

    public TurnUpdateType getActionType() {
        return actionType;
    }
}
