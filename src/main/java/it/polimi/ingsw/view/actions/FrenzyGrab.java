package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;

import java.util.List;

public class FrenzyGrab extends JsonAction {

    private final List<Directions> directions;

    private final String newWeaponName;

    private final String discardedWeapon;

    /**
     * Constructor for grabbing weapons
     * @param directions is a list of movement
     * @param newWeaponName is the name of the weapon to buy
     * @param discardedWeapon is the name of the weapon to discard
     */
    public FrenzyGrab( List<Directions> directions, String newWeaponName, String discardedWeapon) {
        super(ActionTypes.FRENZY_GRAB);
        this.directions = directions;
        this.newWeaponName = newWeaponName;
        this.discardedWeapon = discardedWeapon;
    }

    /**
     *
     * @param directions
     */
    public FrenzyGrab( List<Directions> directions) {
        super(ActionTypes.FRENZY_GRAB);
        this.directions = directions;

        this.newWeaponName = null;
        this.discardedWeapon = null;
    }
}
