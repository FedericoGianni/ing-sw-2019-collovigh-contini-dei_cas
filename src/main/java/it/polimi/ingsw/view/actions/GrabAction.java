package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.util.ArrayList;
import java.util.List;

/**
 * this class contains the grab action methods
 */
public class GrabAction extends JsonAction {


    // max movement are 2 ( 2 if player has more than 2 damage )
    /**
     * directions where you want to move
     */
    private final List<Directions> directions;
    /**
     * power ups you would eventually use to pay
     */
    private final List<CachedPowerUp> powerUpsForPay;
    /**
     * contains the name of the weapon you pickup
     */
    private final String newWeaponName;
    /**
     * contains the name of the weapon you want to discard
     */
    private final String discardedWeapon;

    //AMMO GRAB with moves

    /**
     * method che do the move and grab action, requires directions
     * @param directions;
     */
    public GrabAction(List<Directions> directions) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.newWeaponName = null;

        this.discardedWeapon = null;

        this.powerUpsForPay = null;

    }

    //AMMO GRAB without moves
    public GrabAction() {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.newWeaponName = null;

        this.discardedWeapon = null;

        this.powerUpsForPay = null;
    }

    //SPAWN GRAB with moves (discardedWeapon only if he has already 3 weapons, otherwise null)

    /**
     * do the grab and move of a wepons
     * @param directions if you move
     * @param discardedWeapon if you have 3 weapons
     * @param newWeaponName;
     * @param powerUpsForPay;
     */
    public GrabAction( List<Directions> directions, String discardedWeapon, String newWeaponName, List<CachedPowerUp> powerUpsForPay) {

        super(ActionTypes.GRAB);

        this.directions = directions;

        this.newWeaponName = newWeaponName;

        this.discardedWeapon = discardedWeapon;

        this.powerUpsForPay = powerUpsForPay;

    }

    //SPAWN GRAB without moves (discardedWeapon only if he has already 3 weapons, otherwise null)
    public GrabAction( String discardedWeapon, String newWeaponName,List<CachedPowerUp> powerUpsForPay) {

        super(ActionTypes.GRAB);

        this.directions = new ArrayList<>();

        this.newWeaponName = newWeaponName;

        this.discardedWeapon = discardedWeapon;

        this.powerUpsForPay = powerUpsForPay;

    }

    /**
     *
     * @return a list of directions
     */
    public List<Directions> getDirections() { return directions; }

    /**
     *
     * @return the weapons name
     */
    public String getNewWeaponName() { return newWeaponName; }

    /**
     * @return discarded weapon
     */
    public String getDiscardedWeapon() { return discardedWeapon; }

    /**
     *
     * @return a list of powerups
     */
    public List<CachedPowerUp> getPowerUpsForPay() { return powerUpsForPay; }
}
