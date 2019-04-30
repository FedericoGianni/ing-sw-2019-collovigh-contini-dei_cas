package it.polimi.ingsw.model;

import customsexceptions.CardNotPossessedException;
import customsexceptions.NotEnoughAmmoException;
import customsexceptions.WeaponNotLoadedException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class NormalWeapon extends Weapon{

    private String name;
    private boolean isLoaded;
    private ArrayList<AmmoCube> cost;
    private ArrayList<MacroEffect> effects;
    private static ArrayList<NormalWeapon> normalWeapons =new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(ArrayList<AmmoCube> cost) {
        this.cost = cost;
    }
    /**
     *
     * @return
     */
    public ArrayList<AmmoCube> getBuyCost()//tell me how much it costs to buy that weapon(reloadCost-first ammo cube)
    {
        ArrayList <AmmoCube> bC=new ArrayList();
        for(int i=1;i<this.cost.size();i++)
        {
            bC.add(this.cost.get(i));
        }
        return bC;
    }

    public static ArrayList<NormalWeapon> getNormalWeapons() {
        return normalWeapons;
    }

    public static void insertWeapon(NormalWeapon w) {
        NormalWeapon.normalWeapons.add(w);
    }


    /**
     * Constructor,
     *
     * isLoaded is set on true because Weapons are loaded when bought
     * effects are not filled in the creator
     */

    public NormalWeapon(String name, ArrayList<AmmoCube> cost, ArrayList<MacroEffect>l) {

        this.name = name;
        this.isLoaded = true;
        this.cost = cost;//rememeber that the firts is already payed
        effects=new ArrayList<>();
        effects=l;
    }

    public NormalWeapon(NormalWeapon clone){
        this.name = clone.name;
        this.isLoaded = clone.isLoaded;
        this.cost = clone.cost;
        this.effects = new ArrayList<>();

        for(MacroEffect e : clone.effects){
            this.effects.add(e);
        }
    }

    public boolean isSpecial()
    {
        return false;
    }

    /**
     * @return true only if the player has enough ammo for reloading the NormalWeapon and if it's not reloaded
     */
    public boolean canBeReloaded() {
        if(this.isLoaded==false && canPay(this.getCost(),isPossessedBy().getAmmoBag()))
        {
           return true;
        }else
        {return false;}
    }




    /**
     * @return the cost of buying this NormalWeapon so the cost of recharge without the first cube
     */
    public List<AmmoCube> getCost() {

        return cost.subList(1,cost.size());
    }

    /**
     *
     * @return the name of the NormalWeapon
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the list of macro-effects
     */
    public ArrayList<MacroEffect> getEffects() {
        return effects;
    }

    /**
     *
     * @param macroEffect is the effect that will be added to the NormalWeapon
     */
    public void addMacroEffect(MacroEffect macroEffect){

        this.effects.add(macroEffect);
    }



    /**
     * creates the static weaponsList
     */
    public static void weaponsCreator()
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/java/it/polimi/ingsw/model/Weaponary"))
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

    /**
     * reads the JSON and creates a NormalWeapon object and adds it to the list
     * @param micros
     */
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


        NormalWeapon w=new NormalWeapon(n,wpCost,mf);
        normalWeapons.add(w);
    }

    /**
     * creates the ammo from the JSON using the COlor class
     * @param wpCost
     * @param type
     * @return the cost in AmmoCubes
     */
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

    /**
     * generate a MacroEffects list
     * @param mf
     * @param typeEncoded
     * @return a MacroEffects list
     */
    public static ArrayList<MacroEffect> effectsAnalizer (ArrayList<MacroEffect> mf,int typeEncoded)
    {
        mf.add(MacroEffect.getMacroEffects().get(typeEncoded));
        return mf;
    }

    /**
     *
     * @param target
     * @param mE
     * @throws WeaponNotLoadedException
     */
    public void shoot(ArrayList<Player> target,ArrayList<MacroEffect> mE)throws WeaponNotLoadedException//neeed a player list !
    {
        try{
            if(this.isLoaded==false)
            {
                throw new WeaponNotLoadedException();//weapon not loaded zac
            }
            for(MacroEffect item : mE)//iterate macroeffects
            {
                if(mE.get(mE.indexOf(item)).getEffectCost()!=null)//if the effect costs 0
                {
                    if(canPay(mE.get(mE.indexOf(item)).getEffectCost(),this.isPossessedBy().getAmmoBag())==true)
                    {
                        for (AmmoCube ammo : mE.get(mE.indexOf(item)).getEffectCost())
                        {
                            this.isPossessedBy().pay(ammo.getColor());//pays the effects cost
                        }

                    }else{throw new NotEnoughAmmoException();}}
                //here i can shoot for real
                for(MicroEffect micro: item.getMicroEffects())//iterates microEffects
                {
                    micro.microEffectApplicator(target,this);//the method that applies the effects
                }

            }
        }catch(WeaponNotLoadedException e){e.printStackTrace();}
        catch (CardNotPossessedException e) { e.printStackTrace(); }
        catch(NotEnoughAmmoException e){ e.printStackTrace();}

        this.isLoaded=false;//the weapon is no longer loaded
    }

}