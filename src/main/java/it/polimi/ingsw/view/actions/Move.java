package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;

import java.awt.*;
import java.util.List;

/**
 * this class let the plyer move
 */
public class Move extends JsonAction  {

    /**
     * this is a list of char that with the following symbols 'n' = 1 move up, 's' = one move down, w , e
     */
    private final List<Directions> moves;
    /**
     * final position ofthe player, after the movements
     */
    private final Point finalPos;

    /**
     * method that do the move
     * @param moves movements in terms of directions
     * @param finalPos where the player will be
     */
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
