package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.weapons.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    // logger

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String LOG_START = "[Parser]";

    // paths

    private static final String DAMAGE_PATH = "resources/json/damageTypes";
    private static final String MACRO_EFFECT_PATH = "resources/json/macroEffects";


    // Json Simple

    private static JSONParser jsonParser = new JSONParser();



    // weapon Methods

    public static List<Damage> damageReader(){

        List<Damage> damageList = new ArrayList<>();

        // Locate the file

        String path = new File(DAMAGE_PATH).getAbsolutePath();

        try (FileReader reader = new FileReader(path))

        {
            //Read JSON file

            Object obj = jsonParser.parse(reader);

            JSONArray damageTypes = (JSONArray) obj;

            //for each Json input object

            for (int i = 0; i < damageTypes.size(); i++) {

                damageList.add(parseDamageObject((JSONObject)damageTypes.get(i)));

            }

        } catch (FileNotFoundException e) {

            LOGGER.log(Level.WARNING, e.getMessage(),e);

        } catch (IOException e) {

            LOGGER.log(Level.WARNING, e.getMessage(),e);

        } catch (ParseException e) {

            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

        return damageList;
    }

    /**
     * reads the JSON and create a Damage object with the values
     * @param damages is the json Object
     * @return the object created
     */
    private static Damage parseDamageObject(JSONObject damages)
    {
        //Get employee object within list

        JSONObject employeeObject = (JSONObject) damages.get("Damage"); //Choose the class

        //get the damage amount

        String t = (String) employeeObject.get("damage");

        //Get playerNum

        String d = (String) employeeObject.get("playerNum");

        //Get seeAble

        String stn = (String) employeeObject.get("seeAbleTargetNeeded");

        //Get melee

        String melee = (String) employeeObject.get("melee");

        //Get melee

        String diffP = (String) employeeObject.get("differentPlayer");

        //Get melee

        String dist = (String) employeeObject.get("distMin");

        //Get melee

        String alTarg= (String) employeeObject.get("alreadyTargeted");

        // return the object

        return new Damage(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee),Boolean.parseBoolean(diffP),Integer.parseInt(dist),Boolean.parseBoolean(alTarg));
    }


    /**
     *  MACRO_EFFECT
     *
     *  this method uses the simple JSON to populate the macroEffect Class that are in a known number
     */
    public static List<MacroEffect> effectCreator(){

        List<MacroEffect> macroEffectList= new ArrayList<>();

        //JSON parser object to parse read file

        String path = new File(MACRO_EFFECT_PATH).getAbsolutePath();

        try (FileReader reader = new FileReader(path)) {  //change to relative files paths

            //Read JSON file

            Object ob = jsonParser.parse(reader);

            JSONArray macros = (JSONArray) ob;

            //for each Json input object

            for (int i = 0; i < macros.size(); i++) {

                parseMacroEffectObject((JSONObject)macros.get(i));

            }


        } catch (Exception e) {

            LOGGER.log(Level.WARNING, e.getMessage(),e);

        }

        return macroEffectList;

    }

    /**
     * read the JSON file and add the macroeffetcs to the static list
     * @param micros
     */
    private static MacroEffect parseMacroEffectObject(JSONObject micros)
    {
        //Get  object within list

        JSONObject employeeObject = (JSONObject) micros.get("MacroEffect");

        //get the damage amount

        String n = (String) employeeObject.get("name");

        JSONArray types = (JSONArray) employeeObject.get("MicroEff"); //iterate the MicroEffects codification

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
            fc.add(ammoAnalizer(typeEncoded));//method that can decode the microeffect code

        }
            MacroEffect mf=new MacroEffect(n,microF,fc);//create the macro effect by the list of micro Effects

            return mf;

        }else{

            fc=null;//this means the effect doesn't have cost

            MacroEffect mf=new MacroEffect(n,microF);//create the macro effect by the list of micro Effects ad empty AmmoCubes

            return mf;

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



}
