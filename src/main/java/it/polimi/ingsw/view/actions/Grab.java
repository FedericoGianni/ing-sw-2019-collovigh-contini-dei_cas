package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.model.map.Directions;

import java.util.List;

public class Grab extends JsonAction {


    // max movement are 2 ( 2 if player has more than 2 damage )

    private final List<Directions> directions;

    public Grab(List<Directions> directions) {

        super(ActionTypes.GRAB);

        this.directions = directions;

    }

    public Grab() {

        super(ActionTypes.GRAB);

        this.directions = null;
    }

    public List<Directions> getDirection() { return directions; }
}
