package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.model.map.Directions;

import java.awt.*;
import java.util.List;

public class Move extends JsonAction  {


    /**
     * this is a list of char that with the following symbols 'n' = 1 move up, 's' = one move down, w , e
     */
    private final List<Directions> moves;
    private final Point finalPos;

    public Move( List<Directions> moves, Point finalPos) {
        super(ActionTypes.MOVE);

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
