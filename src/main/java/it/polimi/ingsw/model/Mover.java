package it.polimi.ingsw.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Mover extends MicroEffect {

    private int cellNumber;//number of cells up to cellNumber
    private boolean beforeShootig;//if true you need to move before shooting
    private boolean afterShooting;//if true you can use after shooting the effect
    private boolean facoltative;//sometimes you don't have to move, you can choose
    private boolean toCell;//you have to move to a specified cell if this flag is true
    private boolean target;//set true if you need to move the target not the shooter
    private static ArrayList <Mover> weaponMov=new ArrayList<>();

    //if something moves before shooting pay attention, you need to calculate if you can shoot him after the move


    public Mover(int n,boolean a,boolean b,boolean c,boolean d,boolean e){
        this.cellNumber=n;
        this.beforeShootig=a;
        this.afterShooting=b;
        this.facoltative=c;
        this.toCell=d;
        this.target=e;
        this.isMover();
    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
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

    public boolean isBeforeShootig() {
        return beforeShootig;
    }

    public void setBeforeShootig(boolean beforeShootig) {
        this.beforeShootig = beforeShootig;
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

    public static ArrayList<Mover> getMoversArray() {
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
        String path = new File("src/main/java/it/polimi/ingsw/model/mover").getAbsolutePath();
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


        Mover dd=new Mover(Integer.parseInt(t),Boolean.parseBoolean(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee),Boolean.parseBoolean(diffP),Boolean.parseBoolean(dist));
        Mover.insertWeaponMov(dd);

    }

}
