package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.model.map.Directions;

public class ShootAction extends JsonAction{

    private final int targetId;
    private final Directions move;

    public ShootAction(int targetId) {

        super(ActionTypes.SHOOT);

        this.targetId = targetId;

        this.move = null;
    }

    public ShootAction( int targetId, Directions move) {

        super(ActionTypes.SHOOT);

        this.targetId = targetId;

        this.move = move;
    }

    public int getTargetId() { return targetId; }

    public Directions getMove() { return move; }
}
