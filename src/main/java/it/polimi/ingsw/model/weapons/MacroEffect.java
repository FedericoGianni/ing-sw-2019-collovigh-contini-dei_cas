package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * each weapon have 2 or 3 macroeffects that are the effects of the weapons.
 * each macroeffect have up to 3 microeffects
 */
public class MacroEffect {
    /**
     * name of the macroeffect
     */
    private String name;
    /**
     * contains the microEffects of this macroeffect
     */
    private ArrayList<MicroEffect> microEffects;


    /**
     *
     * @return the list of microEffects of this macroeffect
     */
    public ArrayList<MicroEffect> getMicroEffects() {
        return microEffects;
    }

    /**
     * get the cost of the effects
     * @return
     */
    public ArrayList<AmmoCube> getEffectCost() {
        return effectCost;
    }

    /**
     * contains the cost of the effect
     */
    private ArrayList<AmmoCube> effectCost;
    /**
     * list of macroEffects
     */
    private static ArrayList<MacroEffect> macroEffects=new ArrayList<>();
    /**
     *constructor
     */
    public MacroEffect (String n,ArrayList <MicroEffect> ef,ArrayList<AmmoCube>a) {
        microEffects=new ArrayList<>();
        effectCost=new ArrayList<>();
        this.microEffects.addAll(ef);
        this.name=n;
        effectCost.addAll(a);
    }

    /**
     * constructor
     * @param n
     * @param ef
     */
    public MacroEffect (String n,ArrayList <MicroEffect> ef) {
        microEffects=new ArrayList<>();
        effectCost=new ArrayList<>();//if empty means no cost
        this.microEffects.addAll(ef);
        this.name=n;
    }

    /**
     * return effect name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set the effects name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the list of macroeffects
     */
    public static ArrayList<MacroEffect> getMacroEffects() {
        return macroEffects;
    }



    /**
     * this method uses the semplified JSON to populate the macroeffets Class that are in a known number
     */
    public static void effectCreator()//static beacuse no macroEffects  may exist before the first call of this method
    {
        List<MacroEffect> macroEffectList = Parser.macroEffectReader();

        macroEffects.addAll(macroEffectList);
    }

    /**
     * read the JSON file and add the macroeffetcs to the static list
     * @param micros
     */
    private static void parseDamageObject(JSONObject micros)
    {
        //Get  object within list
        JSONObject employeeObject = (JSONObject) micros.get("MacroEffect");//Choose the class

        //get the damage amount
        String n = (String) employeeObject.get("name");
        //System.out.println(n);

        JSONArray types = (JSONArray) employeeObject.get("MicroEff");//iterate the MicroEffects codification
        ArrayList <MicroEffect>microF=new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {//read Every Effect type and differenciate it
           JSONObject type=(JSONObject)types.get(i);
            int typeEncoded=Integer.parseInt((String)type.get("type"));
            differenciator(microF,typeEncoded);//method that can decodify the microevfect code---see documentation
            //here changes microF
        }

        //ITERATE THE AMMOCOST OF THE MACROEFFECTS
        types = (JSONArray) employeeObject.get("cost");//iterate the MicroEffects codification
        ArrayList <AmmoCube> fc=new ArrayList<>();
        if(types!=null && types.size()!=0)
        {for (int i = 0; i < types.size(); i++) {//read Every Effect type and differenciate it
            JSONObject type=(JSONObject)types.get(i);
            String typeEncoded=(String)type.get("ammoC");
            fc.add(ammoAnalizer(typeEncoded));//method that can decodify the microevfect code

        }
            MacroEffect mf=new MacroEffect(n,microF,fc);//create the macro effect by the list of micro Effects
            macroEffects.add(mf);
        }else{
            fc=null;//this means the effect doesn't have cost
            MacroEffect mf=new MacroEffect(n,microF);//create the macro effect by the list of micro Effects ad empty AmmoCubes
            macroEffects.add(mf);
        }




    }

    /**
     * reads n microeffects and create the microeeffects array, one array for every macroeffect
     * @param microF
     * @param type
     * @return a micro effects array
     */
    private static ArrayList<MicroEffect> differenciator(ArrayList<MicroEffect>microF,int type) {
        if(type<201)//damage type effect from 100 to 200
        {
            type=type-101;
            microF.add(Damage.getDamagesList().get(type));//add the damage effect to the current microeffects list that will create the macroeffect
        }
        else if(type<301)//marker effect type from 200 to 300
        {
            type=type-201;
            microF.add(Marker.getMarkersArray().get(type));//same with the marker
        }else//movements from 300 to the ifinity and beyond
        {
            type=type-301;
            microF.add(Mover.getMoversArray().get(type));
        }
        return microF;
    }

    /**
     * directly converts the ammo into the enum type
     * @param color
     * @return an AmmoCube
     */
    private static AmmoCube ammoAnalizer(String color)
    {
        if(color.equals("RED"))
        {
            return new AmmoCube(Color.RED);
        }else if(color.equals("BLUE"))
        {
            return new AmmoCube(Color.BLUE);
        }else{
            return new AmmoCube(Color.YELLOW);
        }
    }

    /**
     *
     * @param microEffect is the effect to add to the macro-effect
     */
    public void addMicroEffect(MicroEffect microEffect){

        this.microEffects.add(microEffect.copy());
    }


    /**
     *
     * @param player is the player on who we want to apply this macro-effect
     */
    public void applyOn(Player player){

        //TODO
    }
}
