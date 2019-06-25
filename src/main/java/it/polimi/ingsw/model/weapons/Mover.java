package it.polimi.ingsw.model.weapons;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.actions.Move;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Mover extends MicroEffect {

    private int cellNumber;//number of cells up to cellNumber
    private boolean beforeShooting;//if true you need to move before shooting--- done at WeaponLevel
    private boolean afterShooting;//if true you can use after shooting the effect---unuseful i think
    private boolean facoltative;//sometimes you don't have to move, you can choose
    private boolean toCell;//you have to move to a specified cell if this flag is true--must change to a Cell type or to a point type
    private boolean target;//set true if you need to move the target not the shooter
    private boolean myCell;//set true if i need to move other players to my cell
    private static List <Mover> weaponMov=new ArrayList<>();

    //if something moves before shooting pay attention, you need to calculate if you can shoot him after the move
    //movere delays a lot of things to the other classes so check them, for example in tractor beam 1 the damage checks that i can see the trget after the movement

    public Mover(int n,boolean a,boolean b,boolean c,boolean d,boolean e,boolean f){
        this.cellNumber=n;
        this.beforeShooting=a;
        this.afterShooting=b;
        this.facoltative=c;
        this.toCell=d;
        this.target=e;
        this.myCell=f;
    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public Mover copy() {
        return this;
    }

    @Override
    public void print() {
       System.out.println("cellNumber: "+cellNumber);
        System.out.println("beforeShooting"+beforeShooting);

        System.out.println("afterShooting"+afterShooting);
        System.out.println("facoltative: "+facoltative);
        System.out.println("target: "+target);
        System.out.println("myCell: "+myCell);


    }

    @Override
    public void microEffectApplicator(List<Player> playerList, Weapon w, Cell c,int n) throws   PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException {
        print();

        if (facoltative == true) {
            if (playerList == null) {
                return;
            }//you can do nothing
        }
        if (target == true)//you move the target
        {
            if (myCell == true)//tractor beam second
            {      //here the geometrical distance is correct
                c=w.isPossessedBy().getCurrentPosition();
                if (playerList.size() > 1 && Map.getDist(w.isPossessedBy().getCurrentPosition(), c) < cellNumber)//tractor beam peculiarities--max 2 targets, in this case is another
                {
                    playerList.get(1).setPlayerPos(w.isPossessedBy().getCurrentPosition());
                } else if (Map.getDist(w.isPossessedBy().getCurrentPosition(), c) < cellNumber) {//case same target

                    playerList.get(0).setPlayerPos(w.isPossessedBy().getCurrentPosition());
                } else {
                    throw new UncorrectDistanceException();
                }
            } else if (toCell == true)//move to cell, then check if the distance is correft if the distane is neededand if you can move there for real
            {
                playerList.get(0).setPlayerPos(c);
            } else {
                if (c == null)
                    c = playerList.get(0).getCurrentPosition();//may not be enough

                if (cellNumber > 10)//exactly that distance
                {
                    if (Map.getDist(c, playerList.get(0).getCurrentPosition()) == cellNumber)//check if the distace is correct
                    {
                        playerList.get(0).setPlayerPos(c);
                    } else {
                        throw new UncorrectDistanceException();
                    }
                } else {//cell number is max
                    if (Map.getDist(c, playerList.get(0).getCurrentPosition()) <= cellNumber)//check if the distace is correct
                    {
                        System.out.println("celle7");
                        if(c== Model.getMap().getCell(1,3))
                            System.out.println("ssssssssssssssssssssss");
                        playerList.get(0).setPlayerPos(c);
                    } else {
                        throw new UncorrectDistanceException();
                    }
                }

            }
        } else {
            //the shooter is moved
            if (toCell == true)//move to cell, then check if the distance is correft if the distane is neededand if you can move there for real
            {
                w.isPossessedBy().setPlayerPos(c);
            } else {
                if (c == null)
                    c = playerList.get(0).getCurrentPosition();//may not be enough
                if (cellNumber > 10)//exactly that distance
                {
                    if (Map.getDist(c, w.isPossessedBy().getCurrentPosition()) == cellNumber)//check if the distace is correct
                    {
                        w.isPossessedBy().setPlayerPos(c);
                    } else {
                        throw new UncorrectDistanceException();
                    }
                } else {//cell number is max
                    if (Map.getDist(c, w.isPossessedBy().getCurrentPosition()) <= cellNumber)//check if the distace is correct
                    {
                        w.isPossessedBy().setPlayerPos(c);
                    } else {
                        throw new UncorrectDistanceException();
                    }
                }

            }
        }
    }


    @Override
    public boolean moveBefore() {
        return beforeShooting;
    }


    public int getMoves()
    {
        return this.cellNumber;
    }
    public void setMoves(int n)
    {
        this.cellNumber=n;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public boolean isBeforeShooting() {
        return beforeShooting;
    }

    public void setBeforeShooting(boolean beforeShootig) {
        this.beforeShooting = beforeShootig;
    }

    public boolean isAfterShooting() {
        return afterShooting;
    }

    public void setAfterShooting(boolean afterShooting) {
        this.afterShooting = afterShooting;
    }

    public boolean isFacoltative() {
        return facoltative;
    }

    public void setFacoltative(boolean facoltative) {
        this.facoltative = facoltative;
    }

    public boolean isToCell() {
        return toCell;
    }

    public void setToCell(boolean toCell) {
        this.toCell = toCell;
    }

    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public static List<Mover> getMoversArray() {
        return weaponMov;
    }

    public static void insertWeaponMov(Mover wM) {
        weaponMov.add(wM);
    }

    /**
     * this method uses the semplified JSON to populate the microeffets-Damage Class that are in a known number
     */
    public static void populator()//static beacuse no damages types may exixst before the first call of this method
    {
        List<Mover> moverList = Parser.moverReader();

        for (Mover mover: moverList){

            Mover.insertWeaponMov(mover);
        }

    }

}
