package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.actions.usepowerup.ScopeAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.awt.*;
import java.util.List;
/**
 * do the normal shoot and move+shootactions
  */

public class ShootAction extends JsonAction{

    /**
     * if you move you have the directions method
     */
    private final Directions move;
    /**
     * if you use the scope
     */
    private final ScopeAction targetingScope;
    /**
     * powerUps you can discard for pay the effects
     */
    private final List<CachedPowerUp> powerUpList;
    /**
     *List of list of the targets
     */
    private final List<List<Integer>> targetIds;
    /**
     * List of the ffects the player have decided to use
     * integer because effect.get(0) is the number of effect of the weapon like 0 is the first effect ecc...
     */
    private final List<Integer> effects;
    /**
     * list of eventually cells if the target or the player have movement effects
     */
    private final List<Point> cells;
    /**
     * name of the shooting weapon
     */
    private final String weaponName;

    /**
     * shoot without a movement before
     * @param weaponName weapon you use
     * @param targetIds id of targets
     * @param effects effects choosen
     * @param cells for movmeents if required
     * @param powerUpList powerUp you want to discard for paying
     * @param scopeAction eventual scope action
     */
    public ShootAction(String weaponName,List<List<Integer>> targetIds, List<Integer> effects, List<Point> cells, List<CachedPowerUp> powerUpList, ScopeAction scopeAction) {

        super(ActionTypes.SHOOT);

        this.weaponName = weaponName;

        this.move = null;

        this.targetIds = targetIds;

        this.effects = effects;

        this.cells = cells;

        this.targetingScope = scopeAction;

        this.powerUpList = powerUpList;

    }

    /**
     shoot without a movement before
     * @param weaponName weapon you use
     * @param move movements done before the actual shoot
     * @param targetIds id of targets
     * @param effects effects choosen
     * @param cells for movmeents if required
     * @param powerUpList powerUp you want to discard for paying
     * @param scopeAction eventual scope action
     */
    public ShootAction( String weaponName, Directions move, List<List<Integer>> targetIds, List<Integer> effects, List<Point> cells, List<CachedPowerUp> powerUpList, ScopeAction scopeAction ) {

        super(ActionTypes.SHOOT);

        this.weaponName = weaponName;

        this.targetIds = targetIds;

        this.move = move;

        this.effects = effects;

        this.cells = cells;

        this.targetingScope = scopeAction;

        this.powerUpList = powerUpList;
    }

    /**
     *
     * @return the directions
     */
    public Directions getMove() {
        return move;
    }

    /**
     *
     * @return the ids of targets
     */
    public List<List<Integer>> getTargetIds() {
        return targetIds;
    }

    /**
     *
     * @return the list of effects
     */
    public List<Integer> getEffects() {
        return effects;
    }

    /**
     *
     * @return the cells
     */
    public List<Point> getCells() {
        return cells;
    }

    /**
     * @return the weapon name
     */
    public String getWeaponName() {
        return weaponName;
    }

    /**
     * @return the scope action
     */
    public ScopeAction getTargetingScope() {
        return targetingScope;
    }

    /**
     * @return the list of powerUps discarded
     */
    public List<CachedPowerUp> getPowerUpList() {
        return powerUpList;
    }
}
