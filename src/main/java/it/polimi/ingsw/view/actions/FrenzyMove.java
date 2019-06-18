package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;

import java.awt.*;
import java.util.List;

public class FrenzyMove extends JsonAction {

    private final List<Directions> moves;
    private final Point finalPos;

    public FrenzyMove(ActionTypes actionType, List<Directions> moves, Point finalPos) {
        super(actionType);
        this.moves = moves;
        this.finalPos = finalPos;
    }

    public List<Directions> getMoves() {
        return moves;
    }

    public Point getFinalPos() {
        return finalPos;
    }
}
