package it.polimi.ingsw.model.weapons;

import com.google.gson.Gson;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
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


    public void microEffectApplicator(ArrayList<Player> playerList, Weapon w, Cell c) throws OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException {

        if(facoltative==true)
        {
            if(playerList==null) {return; }//you can do nothing
        }
        if(target==true)//you move the target
        {
            if(myCell==true)//tractor beam second
            {      //here the geometrical distance is correct
                if(playerList.size()>1 && Map.getDist(w.isPossessedBy().getCurrentPosition(),c)<cellNumber)//tractor beam peculiarities--max 2 targets, in this case is another
                {
                    playerList.get(1).setPlayerPos(w.isPossessedBy().getCurrentPosition());
                }else if(Map.getDist(w.isPossessedBy().getCurrentPosition(),c)<cellNumber){//case same target
                    playerList.get(0).setPlayerPos(w.isPossessedBy().getCurrentPosition());
                }else{
                    throw new UncorrectDistanceException();
                }
            }
            else if(toCell==true)//move to cell, then check if the distance is correft if the distane is neededand if you can move there for real
            {

                playerList.get(0).setPlayerPos(c);
            }
            else{ if(c==null)c=playerList.get(0).getCurrentPosition();//may not be enough
                if(toCell)//move to cell, then check if the distance is correct if the distance is needed and if you can move there for real
                {
                    playerList.get(0).setPlayerPos(c);
                }else {
                    if(cellNumber>10)//exactly that distance
                    {
                        if(Map.getDist(c, playerList.get(0).getCurrentPosition())==cellNumber)//check if the distace is correct
                        {
                            playerList.get(0).setPlayerPos(w.isPossessedBy().getCurrentPosition());
                        }
                        else{
                            throw new UncorrectDistanceException();
                        }
                    }
                    else{//cell number is max
                        if(Map.getDist(c, playerList.get(0).getCurrentPosition())<=cellNumber)//check if the distace is correct
                        {
                            playerList.get(0).setPlayerPos(w.isPossessedBy().getCurrentPosition());
                        }
                        else{
                            throw new UncorrectDistanceException();
                        }
                    }

                }
            }
        }
        else//the shooter is moved
            if(toCell==true)//move to cell, then check if the distance is correft if the distane is neededand if you can move there for real
            {
                w.isPossessedBy().setPlayerPos(c);
            }else {
                if(cellNumber>10)//exactly that distance
                {
                    if(Map.getDist(c, w.isPossessedBy().getCurrentPosition())==cellNumber)//check if the distace is correct
                    {
                        w.isPossessedBy().setPlayerPos(c);
                    }
                    else{
                        throw new UncorrectDistanceException();
                    }
                }
                else{//cell number is max
                    if(Map.getDist(c, w.isPossessedBy().getCurrentPosition())<=cellNumber)//check if the distace is correct
                    {
                        w.isPossessedBy().setPlayerPos(c);
                    }
                    else{
                        throw new UncorrectDistanceException();
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
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = new File("resources/json/mover").getAbsolutePath();
        try (FileReader reader = new FileReader(path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray damageTypes = (JSONArray) obj;

            for (int i = 0; i < damageTypes.size(); i++) {parseMoverObject((JSONObject)damageTypes.get(i)); }
            //for each Json input object

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



    /**
     * creates a Mover and adds it t the list
     * @param damages
     */
    private static void parseMoverObject(JSONObject damages)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) damages.get("Mover");//Choose the class

        //get the damage amount
        String t = (String) employeeObject.get("cellNumber");
        //System.out.println(t);

        //Get playerNum
        String d = (String) employeeObject.get("beforeShooting");
        //System.out.println(d);

        //Get seeAble
        String stn = (String) employeeObject.get("afterShooting");
        //System.out.println(stn);

        //Get melee
        String melee = (String) employeeObject.get("facoltative");
        //System.out.println(melee);

        //Get melee
        String diffP = (String) employeeObject.get("toCell");
        //System.out.println(melee);

        //Get melee
        String dist = (String) employeeObject.get("target");
        //System.out.println(melee);

        //Get melee
        String my = (String) employeeObject.get("mycell");
        //System.out.println(melee);


        Mover dd=new Mover(Integer.parseInt(t),Boolean.parseBoolean(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee),Boolean.parseBoolean(diffP),Boolean.parseBoolean(dist),Boolean.parseBoolean(my));
        Mover.insertWeaponMov(dd);

    }

}
