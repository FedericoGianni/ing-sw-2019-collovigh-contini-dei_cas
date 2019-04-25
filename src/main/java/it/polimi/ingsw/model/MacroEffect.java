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

public class MacroEffect {//----evetually add an attribute std or not if the weapon is one of the enlighted

    private String name;
    private boolean standerd;//if true is a vector of microeffects otherwise is a class of specialweapons
    private ArrayList<MicroEffect> microEffects;
    private static ArrayList<MacroEffect> macroEffects=new ArrayList<>();
    /**
     *
     */
    public MacroEffect (String n,ArrayList <MicroEffect> ef) {
        microEffects=new ArrayList<>();
        this.microEffects.addAll(ef);
        this.name=n;
    }
    //MISSING: ammocost of macreffects
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<MacroEffect> getMacroEffects() {
        return macroEffects;
    }

    public static void setMacroEffects(ArrayList<MacroEffect> macroEffects) {
        MacroEffect.macroEffects = macroEffects;
    }

    /**
     * this method uses the semplified JSON to populate the macroeffets Class that are in a known number
     */
    public static void effectCreator()//static beacuse no macroEffects  may exist before the first call of this method
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = new File("src/main/java/it/polimi/ingsw/model/macroEffects").getAbsolutePath();
        try (FileReader reader = new FileReader(path))
        {//change to relative files paths
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray macros = (JSONArray) obj;

            for (int i = 0; i < macros.size(); i++) {
                 parseDamageObject((JSONObject)macros.get(i));
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


        MacroEffect mf=new MacroEffect(n,microF);//create the macro effect by the list of micro Effects
        macroEffects.add(mf);


    }

    private static ArrayList<MicroEffect> differenciator(ArrayList<MicroEffect>microF,int type)
    {
        //maybe you need the object or something like that??
        //System.out.println(type);
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
