package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;

import java.util.ArrayList;
import java.util.List;

public class GrabAction extends JsonAction {


    // max movement are 2 ( 2 if player has more than 2 damage )

    private final List<Directions> directions;

    private final String newWeaponName;

    private final String discardedWeapon;

    //AMMO GRAB with moves
    public GrabAction(List<Directions> directions) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.newWeaponName = null;

        this.discardedWeapon = null;

    }

    //AMMO GRAB without moves
    public GrabAction() {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.newWeaponName = null;

        this.discardedWeapon = null;
    }

    //SPAWN GRAB with moves (discardedWeapon only if he has already 3 weapons, otherwise null)
    public GrabAction( List<Directions> directions, String discardedWeapon, String newWeaponName) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.newWeaponName = newWeaponName;

        this.discardedWeapon = discardedWeapon;

    }

    //SPAWN GRAB without moves (discardedWeapon only if he has already 3 weapons, otherwise null)
    public GrabAction( String discardedWeapon, String newWeaponName) {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.newWeaponName = newWeaponName;

        this.discardedWeapon = discardedWeapon;

    }

    public List<Directions> getDirections() { return directions; }

    public String getNewWeaponName() { return newWeaponName; }

    public String getDiscardedWeapon() { return discardedWeapon; }
}
