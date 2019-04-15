package it.polimi.ingsw;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Damage extends MicroEffect {

    private int damage;//how much damage you can do
    private int playerNum;//some effects can deal damage to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players
    private boolean melee;//some weapons can deal damage to players only in your current cell
    private static ArrayList <Damage> damages;

    public static void setDamagesList(ArrayList<Damage> damage) {
        damages = damage;
    }

    public static ArrayList <Damage> getDamagesList()
    {
        return damages;
    }

    public Damage(int a, int b, boolean c, boolean d) {
        this.damage=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.melee=d;
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
        ArrayList<Damage> dmgTypes=new ArrayList<>();
        try (FileReader reader = new FileReader("C:\\Users\\bl4ck\\IdeaProjects\\ing-sw-2019-collovigh-contini-dei_cas\\src\\main\\java\\it\\polimi\\ingsw\\damageTypes"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray damageTypes = (JSONArray) obj;

                for (int i = 0; i < damageTypes.size(); i++) {
                    dmgTypes = parseDamageObject((JSONObject)damageTypes.get(i), dmgTypes);
                }
            //for each Json input object

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        damages=dmgTypes;
    }

    private static ArrayList<Damage> parseDamageObject(JSONObject damages,ArrayList <Damage> dmgTypes)
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
        Damage dd=new Damage(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee));
        dmgTypes.add(dd);
        return dmgTypes;

    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }

}
