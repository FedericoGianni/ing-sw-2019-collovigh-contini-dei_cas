package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.actions.usepowerup.ScopeAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.awt.*;
import java.util.List;

public class ShootAction extends JsonAction{


    private final Directions move;

    private final ScopeAction targetingScope;

    private final List<CachedPowerUp> powerUpList;

    private final List<List<Integer>> targetIds;
    private final List<Integer> effects;
    private final List<Point> cells;

    private final String weaponName;

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

    public String getWeaponName() {
        return weaponName;
    }

    public ScopeAction getTargetingScope() {
        return targetingScope;
    }

    public List<CachedPowerUp> getPowerUpList() {
        return powerUpList;
    }
}
