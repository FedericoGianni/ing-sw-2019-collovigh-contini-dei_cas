package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.util.ArrayList;
import java.util.List;

public class GrabAction extends JsonAction {


    // max movement are 2 ( 2 if player has more than 2 damage )

    private final List<Directions> directions;

    private final List<CachedPowerUp> powerUpsForPay;

    private final String newWeaponName;

    private final String discardedWeapon;

    //AMMO GRAB with moves
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

    public List<Directions> getDirections() { return directions; }

    public String getNewWeaponName() { return newWeaponName; }

    public String getDiscardedWeapon() { return discardedWeapon; }

    public List<CachedPowerUp> getPowerUpsForPay() { return powerUpsForPay; }
}
