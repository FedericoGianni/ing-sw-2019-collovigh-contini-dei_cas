package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.model.map.Directions;

import java.util.ArrayList;
import java.util.List;

public class GrabAction extends JsonAction {


    // max movement are 2 ( 2 if player has more than 2 damage )

    private final List<Directions> directions;

    private final String newWeaponName;

    private final String discardedWeapon;


    public GrabAction(List<Directions> directions) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.newWeaponName = null;

        this.discardedWeapon = null;

    }

    public GrabAction() {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.newWeaponName = null;

        this.discardedWeapon = null;
    }

    public GrabAction( List<Directions> directions, String newWeaponName, String discardedWeapon) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.newWeaponName = newWeaponName;

        this.discardedWeapon = discardedWeapon;

    }

    public GrabAction(  String newWeaponName, String discardedWeapon) {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.newWeaponName = newWeaponName;

        this.discardedWeapon = discardedWeapon;

    }

    public List<Directions> getDirection() { return directions; }

    public String getNewWeaponName() { return newWeaponName; }

    public String getDiscardedWeapon() { return discardedWeapon; }
}
