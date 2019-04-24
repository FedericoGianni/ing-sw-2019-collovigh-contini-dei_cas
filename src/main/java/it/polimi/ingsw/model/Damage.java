package it.polimi.ingsw.model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;

public class Damage extends MicroEffect {

    private int damage;//how much damage you can do
    private int playerNum;//some effects can deal damage to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players

    public int getDamage() {
        return damage;
    }

    public boolean isMelee() {
        return melee;
    }

    public void setMelee(boolean melee) {
        this.melee = melee;
    }

    public boolean isDifferentPlayer() {
        return differentPlayer;
    }

    public void setDifferentPlayer(boolean differentPlayer) {
        this.differentPlayer = differentPlayer;
    }

    public boolean isAlreadyTargetd() {
        return alreadyTargetd;
    }

    public void setAlreadyTargetd(boolean alreadyTargetd) {
        this.alreadyTargetd = alreadyTargetd;
    }

    public int getDistMin() {
        return distMin;
    }

    public void setDistMin(int distMin) {
        this.distMin = distMin;
    }

    private boolean melee;//some weapons can deal damage to players only in your current cell
    private boolean differentPlayer;//in secondary effects sometimes you need to target different players from the first
    private boolean alreadyTargetd;//sometimes in secondary effects you have to choose between already targeted players of the first effect
    private int distMin;//some effects require a minimum distance, calculated by moves
    //particuarities: if both differentPlayer and alreadyTargeted are true you can choose to apply one or both effects(check machineGun III)
    // the player number is 100 you need to trget every player in the traget's square(check electroshyte-melee-  and grande launcher)
    //player num is 10 you must target the number/10 every one from different cell
    //if the minimum distance is over 10 it means maximum distance, like 20 is at maximun distnce of 2 (20/10-> 2)(check tractor beam)
    //if the distance is 1000 is like 1 , no more no less
    //if the minimum distance is 100 is an unseeblePlayer by default, you have to target an unseeable player(check heatseeker)
    //only the damage tag have the distance inside
    private static ArrayList <Damage> damages=new ArrayList<>();

    public static void insertDamage(Damage dm) {
        damages.add(dm);
    }

    public static ArrayList <Damage> getDamagesList()
    {
        return damages;
    }

    public Damage(int a, int b, boolean c, boolean d, boolean diff,int dm,boolean at) {
        this.damage=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.melee=d;
        this.differentPlayer=diff;
        this.distMin=dm;
        this.alreadyTargetd=at;
    }



    public int getDamages()
    {
        return this.damage;
    }
    public void setDamage(int damages)
    {
        this.damage=damages;
    }
    public int getPlayerNum() {
        return playerNum;
    }


    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public boolean isSeeAbleTargetNeeded() {
        return seeAbleTargetNeeded;
    }

    public void setSeeAbleTargetNeeded(boolean seeAbleTargetNeeded) {
        this.seeAbleTargetNeeded = seeAbleTargetNeeded;
    }

    public boolean isMeelee() {
        return melee;
    }

    public void setMeelee(boolean meelee) {
        this.melee = meelee;
    }


    /**
     * this method uses the semplified JSON to populate the microeffets-Damage Class that are in a known number
     */
    public static void populator()//static beacuse no damages types may exixst before the first call of this method
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\bl4ck\\IdeaProjects\\ing-sw-2019-collovigh-contini-dei_cas\\src\\main\\java\\it\\polimi\\ingsw\\model\\damageTypes"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray damageTypes = (JSONArray) obj;

                for (int i = 0; i < damageTypes.size(); i++) {
                     parseDamageObject((JSONObject)damageTypes.get(i));
                }
            //for each Json input object

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static void parseDamageObject(JSONObject damages)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) damages.get("Damage");//Choose the class

        //get the damage amount
        String t = (String) employeeObject.get("damage");
        //System.out.println(t);

        //Get playerNum
        String d = (String) employeeObject.get("playerNum");
        //System.out.println(d);

        //Get seeAble
        String stn = (String) employeeObject.get("seeAbleTargetNeeded");
        //System.out.println(stn);

        //Get melee
        String melee = (String) employeeObject.get("melee");
        //System.out.println(melee);

        //Get melee
        String diffP = (String) employeeObject.get("differentPlayer");
        //System.out.println(melee);

        //Get melee
        String dist = (String) employeeObject.get("distMin");
        //System.out.println(melee);

        //Get melee
        String alTarg= (String) employeeObject.get("alreadyTargeted");
        //System.out.println(melee);
        Damage dd=new Damage(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee),Boolean.parseBoolean(diffP),Integer.parseInt(dist),Boolean.parseBoolean(alTarg));
        Damage.insertDamage(dd);

    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }

}
