package it.polimi.ingsw.view.actions;

import java.awt.*;
import java.util.List;

public class FrenzyMove extends JsonAction {

    private final List<String> moves;
    private final Point finalPos;

    public FrenzyMove(ActionTypes actionType, List<String> moves, Point finalPos) {
        super(actionType);
        this.moves = moves;
        this.finalPos = finalPos;
    }

    public List<String> getMoves() {
        return moves;
    }

    public Point getFinalPos() {
        return finalPos;
    }
}
