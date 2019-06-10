package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;

import java.awt.*;
import java.util.List;

public class ShootAction extends JsonAction{


    private final Directions move;

    private final List<List<Integer>> targetIds;
    private final List<Integer> effects;
    private final List<Point> cells;

    public ShootAction(List<List<Integer>> targetIds, List<Integer> effects, List<Point> cells) {

        super(ActionTypes.SHOOT);

        this.move = null;

        this.targetIds = targetIds;

        this.effects = effects;

        this.cells = cells;

    }

    public ShootAction( Directions move, List<List<Integer>> targetIds, List<Integer> effects, List<Point> cells ) {

        super(ActionTypes.SHOOT);

        this.targetIds = targetIds;

        this.move = move;

        this.effects = effects;

        this.cells = cells;
    }

    public Directions getMove() {
        return move;
    }

    public List<List<Integer>> getTargetIds() {
        return targetIds;
    }

    public List<Integer> getEffects() {
        return effects;
    }

    public List<Point> getCells() {
        return cells;
    }
}
