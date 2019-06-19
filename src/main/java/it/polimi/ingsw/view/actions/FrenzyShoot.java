package it.polimi.ingsw.view.actions;

import java.util.ArrayList;
import java.util.List;

public class FrenzyShoot extends JsonAction {

    private final Move moveAction;
    private final ReloadAction reloadAction;
    private final ShootAction shootAction;


    public FrenzyShoot(Move moveAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.moveAction = moveAction;

        this.reloadAction = null;
        this.shootAction = null;
    }

    public FrenzyShoot(ReloadAction reloadAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.reloadAction = reloadAction;

        this.moveAction = null;
        this.shootAction = null;
    }

    public FrenzyShoot(ShootAction shootAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.shootAction = shootAction;

        this.reloadAction = null;
        this.moveAction = null;
    }

    /**
     * Constructor to be used in Controller
     * @param moveAction is the move component of the action
     * @param reloadAction is the reload component of the action
     * @param shootAction is the shoot component of the action
     */
    public FrenzyShoot( Move moveAction, ReloadAction reloadAction, ShootAction shootAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.moveAction = moveAction;
        this.reloadAction = reloadAction;
        this.shootAction = shootAction;
    }

    public Move getMoveAction() {
        return moveAction;
    }

    public ReloadAction getReloadAction() {
        return reloadAction;
    }

    public ShootAction getShootAction() {
        return shootAction;
    }

    public List<Integer> getFieldsNonNull(){

        List<Integer> fieldList = new ArrayList<>();

        if (shootAction != null) fieldList.add(3);

        if (reloadAction != null) fieldList.add(2);

        if (moveAction != null) fieldList.add(1);

        return fieldList;
    }
}
