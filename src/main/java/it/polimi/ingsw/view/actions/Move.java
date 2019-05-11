package it.polimi.ingsw.view.actions;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Move implements Serializable {


    private final ActionTypes type = ActionTypes.MOVE;
    /**
     * this is a list of char that with the following symbols 'n' = 1 move up, 's' = one move down, w , e
     */
    private final List<String> moves;

    private final Point finalPos;

    public Move(Point finalPos, List<String> moves) {
        this.finalPos = finalPos;
        this.moves = moves;
    }
}
