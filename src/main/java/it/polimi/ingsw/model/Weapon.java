package it.polimi.ingsw.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 
 */
public class Weapon {

    private String name;
    private boolean isLoaded;
    private List<AmmoCube> cost;
    private List<MacroEffect> effects;
    private static ArrayList<Weapon> weapons=new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public void setCost(List<AmmoCube> cost) {
        this.cost = cost;
    }


    /**
     *
     * @return
     */
    public ArrayList<AmmoCube> getbuyCost()//tell me how much it costs to buy that weapon(reloadCost-first ammo cube)
    {
        ArrayList <AmmoCube> bC=new ArrayList();
        for(int i=1;i<this.cost.size();i++)
        {
            bC.add(this.cost.get(i));
        }
        return bC;
    }

    public static ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public static void insertWeapon(Weapon w) {
        Weapon.weapons.add(w);
    }


    /**
     * Constructor,
     *
     * isLoaded is set on true because Weapons are loaded when bought
     * effects are not filled in the creator
     */

    public Weapon(String name, List<AmmoCube> cost,List<MacroEffect>l) {

        this.name = name;
        this.isLoaded = true;
        this.cost = cost;//rememeber that the firts is already payed
        effects=new ArrayList<>();
        effects=l;
    }

    public Weapon(Weapon clone){
        this.name = clone.name;
        this.isLoaded = clone.isLoaded;
        this.cost = clone.cost;
        this.effects = new ArrayList<>();

        for(MacroEffect e : clone.effects){
            this.effects.add(e);
        }
    }


    /**
     * @return Boolean for if the weapon is loaded
     */
    public Boolean isLoaded() {

        return this.isLoaded;
    }

    /**
     * @return true only if the player has enough ammo for reloading the Weapon
     */
    public Boolean canBeReloaded() {

        //TODO
        return false;
    }

    /**
     *  reload the weapon
     */
    public void reload() {
        // TODO implement here
    }

    /**
     * @return the cost of buying this Weapon so the cost of recharge without the first cube
     */
    public List<AmmoCube> getCost() {

        return cost.subList(1,cost.size());
    }

    /**
     *
     * @return the name of the Weapon
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the list of macro-effects
     */
    public List<MacroEffect> getEffects() {
        return effects;
    }

    /**
     *
     * @param macroEffect is the effect that will be added to the Weapon
     */
    public void addMacroEffect(MacroEffect macroEffect){

        this.effects.add(macroEffect);
    }

    public Weapon getWeapon()//get a random weapon from the weapon List
    {
        Random rand=new Random();
        int casuale=rand.nextInt(Weapon.getWeapons().size());//the random number can't exceed the List bounds
        Weapon rnd=Weapon.getWeapons().get(casuale);//returns a random weapon from the list
        Weapon.getWeapons().remove(rnd);//you can't retake a weapon
        return rnd;
    }

    public static void weaponsCreator()
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\bl4ck\\IdeaProjects\\ing-sw-2019-collovigh-contini-dei_cas\\src\\main\\java\\it\\polimi\\ingsw\\model\\Weaponary"))
        {//change to relative files paths
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray wps = (JSONArray) obj;

            for (int i = 0; i < wps.size(); i++) {
                parseWeaponObject((JSONObject)wps.get(i));
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

    private static void parseWeaponObject(JSONObject micros)
    {
        //Get  object within list
        JSONObject employeeObject = (JSONObject) micros.get("Weapon");//Choose the class

        //get the damage amount
        String n = (String) employeeObject.get("name");
        //System.out.println(n);

        JSONArray types = (JSONArray) employeeObject.get("cost");//iterate the ammocubes cost codification
        ArrayList <AmmoCube> wpCost=new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {//read ammoCube type and differenciate it
            JSONObject type=(JSONObject)types.get(i);
            String typeEncoded= (String)type.get("ammoC");
            ammoAnalizer(wpCost,typeEncoded);//method that can decodify the ammos code---see documentatio
        }
        types = (JSONArray) employeeObject.get("macroEffects");//iterate the ammocubes cost codification
        ArrayList <MacroEffect> mf=new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {//read Every Effect type and differenciate it
            JSONObject type=(JSONObject)types.get(i);
            int typeEncoded=Integer.parseInt((String)type.get("num"));
            effectsAnalizer(mf,typeEncoded);//method that can decodify the microevfect code---see documentation
            //here changes microF
        }


        Weapon w=new Weapon(n,wpCost,mf);
        weapons.add(w);
    }

    public static ArrayList<AmmoCube> ammoAnalizer(ArrayList<AmmoCube> wpCost,String type)
    {
            if(type=="BLUE")
            {
                wpCost.add(new AmmoCube(Color.BLUE));
            }else if(type=="RED") {
                wpCost.add(new AmmoCube(Color.RED));
            }else{
                wpCost.add(new AmmoCube(Color.YELLOW));
            }
            return wpCost;
    }

    public static ArrayList<MacroEffect> effectsAnalizer (ArrayList<MacroEffect> mf,int typeEncoded)
    {
        mf.add(MacroEffect.getMacroEffects().get(typeEncoded));
        return mf;
    }
}