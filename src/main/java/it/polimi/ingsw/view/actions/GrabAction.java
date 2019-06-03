package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.model.map.Directions;

import java.util.ArrayList;
import java.util.List;

public class GrabAction extends JsonAction {


    // max movement are 2 ( 2 if player has more than 2 damage )

    private final List<Directions> directions;

    private final String weaponName;

    public GrabAction(List<Directions> directions) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.weaponName=null;

    }

    public GrabAction() {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.weaponName = null;
    }

    public GrabAction( List<Directions> directions, String weaponName) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.weaponName = weaponName;

    }

    public GrabAction(String weaponName) {

        super(ActionTypes.GRAB);

        this.weaponName = weaponName;

        this.directions = new ArrayList<>();

    }

    public List<Directions> getDirection() { return directions; }

    public String getWeaponName() {
        return weaponName;
    }
}
