package it.polimi.ingsw.view.actions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrenzyShoot extends JsonAction {

    private final Move moveAction;
    private final ReloadAction reloadAction;
    private final JsonAction shootAction;


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

    /**
     * Constructor to be used in Controller
     * @param moveAction is the move component of the action
     * @param reloadAction is the reload component of the action
     * @param shootAction is the shoot component of the action
     */
    private FrenzyShoot( Move moveAction, ReloadAction reloadAction, JsonAction shootAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.moveAction = moveAction;
        this.reloadAction = reloadAction;
        this.shootAction = shootAction;
    }

    public static FrenzyShoot genFrenzyShootActionSkipShoot(){

        return new FrenzyShoot(null,null, new SkipAction());
    }

    public static FrenzyShoot genFrenzyShootActionSkipMove(){

        return new FrenzyShoot(new Move(new ArrayList<>(), null),null, null);
    }

    public static FrenzyShoot genFrenzyShootActionSkipReload(){

        return new FrenzyShoot(null,new ReloadAction(new ArrayList<>(),new ArrayList<>()), null);
    }

    public Move getMoveAction() {
        return moveAction;
    }

    public ReloadAction getReloadAction() {
        return reloadAction;
    }

    public JsonAction getShootAction() {
        return shootAction;
    }

    public List<Integer> getFieldsNonNull(){

        List<Integer> fieldList = new ArrayList<>();

        if (shootAction != null) fieldList.add(3);

        if (reloadAction != null) fieldList.add(2);

        if (moveAction != null) fieldList.add(1);

        return fieldList;
    }

    public Boolean isFirstPartFull(){

        return ( (moveAction != null) && (reloadAction != null) );
    }

    /**
     * This method merge two frenzyShoot actions into one with more parameters, allowing the possibility of a modular action
     * @param frenzyShoot is the source frenzy shoot action
     * @return a new frenzy action
     */
    public FrenzyShoot addPart(FrenzyShoot frenzyShoot){

        Move move = null;
        ReloadAction reload = null;
        JsonAction shoot = null;

        if (frenzyShoot.getFieldsNonNull().contains(1)){

            if (this.getMoveAction() != null ) throw new IllegalArgumentException();

            move = frenzyShoot.getMoveAction();
        }

        if (frenzyShoot.getFieldsNonNull().contains(2)){

            if (this.getReloadAction() != null ) throw new IllegalArgumentException();

            reload = frenzyShoot.getReloadAction();

        }

        if (frenzyShoot.getFieldsNonNull().contains(3)){

            if (this.getShootAction() != null ) throw new IllegalArgumentException();

            shoot = frenzyShoot.getShootAction();
        }

        return new FrenzyShoot(move,reload,shoot);
    }
}
