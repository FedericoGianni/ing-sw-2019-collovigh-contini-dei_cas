package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.saveutils.SavedMap;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.weapons.*;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.utils.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Parser {

    // logger

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String LOG_START = "[Parser] ";

    // paths

    private static final String DAMAGE_PATH = "resources/json/damageTypes";
    private static final String MACRO_EFFECT_PATH = "resources/json/macroEffects";
    private static final String MARKER_PATH = "resources/json/MarkerTypes";
    private static final String MOVER_PATH = "resources/json/mover";
    private static final String NORMAL_WEAPON_PATH = "resources/json/Weaponary";
    private static final String SAVED_MAP_RELATIVE_PATH = "_Map.json";
    private static final String MAP_ONE_PATH = "resources/json/maps/map_01.json";
    private static final String MAP_TWO_PATH = "resources/json/maps/map_02.json";
    private static final String MAP_THREE_PATH = "resources/json/maps/map_03.json";

    private static final String GAMES_PATH = "resources/json/savegames";


    // Json Simple

    private static JSONParser jsonParser = new JSONParser();

    // GSON

    private static Gson gson = new Gson();

    // saves

    private static int currentGame;

    private Parser() {

    }


    // weapon Methods

    /**
     * DAMAGE
     *
     * @return the list of the damage from the json
     */
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

            LOGGER.log(level, () -> LOG_START + "Damages successfully loaded from json");

        } catch (Exception e) {

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
    public static List<MacroEffect> macroEffectReader(){

        List<MacroEffect> macroEffectList= new ArrayList<>();

        //JSON parser object to parse read file

        String path = new File(MACRO_EFFECT_PATH).getAbsolutePath();

        try (FileReader reader = new FileReader(path)) {  //change to relative files paths

            //Read JSON file

            Object ob = jsonParser.parse(reader);

            JSONArray macros = (JSONArray) ob;

            //for each Json input object

            for (int i = 0; i < macros.size(); i++) {

                macroEffectList.add(parseMacroEffectObject((JSONObject)macros.get(i)));

            }

            LOGGER.log(level, () -> LOG_START + "Macro Effects successfully loaded from json");


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
        if(types!=null && !(types.isEmpty()))
        {for (int i = 0; i < types.size(); i++) {//read Every Effect type and differenciate it
            JSONObject type=(JSONObject)types.get(i);
            String typeEncoded=(String)type.get("ammoC");
            fc.add(ammoAnalizer(typeEncoded));//method that can decode the microeffect code

        }
            return new MacroEffect(n,microF,fc);//create the macro effect by the list of micro Effects


        }else{


            return new MacroEffect(n,microF);//create the macro effect by the list of micro Effects ad empty AmmoCubes


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
     * MARKER
     *
     * this method uses the semplified JSON to populate the microeffets-MArker Class that are in a known number
     */
    public static List<Marker> markerReader()
    {
        List<Marker> markerList = new ArrayList<>();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = new File(MARKER_PATH).getAbsolutePath();
        try (FileReader reader = new FileReader(path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray markerTypes = (JSONArray) obj;


            for (int i = 0; i < markerTypes.size(); i++) {
                markerList.add(parseMarkerObject((JSONObject)markerTypes.get(i)));
            }

            LOGGER.log(level, () -> LOG_START + "Markers successfully loaded from json");

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

        return markerList;

    }

    /**
     * create a Marker
     * @param markers is the json Object
     * @return the Marker object;
     */
    private static Marker parseMarkerObject(JSONObject markers)
    {
        //Get employee object within list
        JSONObject mObject = (JSONObject) markers.get("Marker");

        //get the damage amount
        String t = (String) mObject.get("markers");

        //Get playerNum
        String d = (String) mObject.get("playerNum");

        //Get seeAble
        String stn = (String) mObject.get("seeAbleTargetNeeded");

        //Get seeAble
        String dp = (String) mObject.get("differentPlayer");

        return new Marker(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(dp));

    }

    /**
     * MOVER
     *
     * this method uses the semplified JSON to populate the microeffets-Damage Class that are in a known number
     */
    public static List<Mover> moverReader() {

        List<Mover> moverList =new ArrayList<>();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = new File(MOVER_PATH).getAbsolutePath();
        try (FileReader reader = new FileReader(path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray damageTypes = (JSONArray) obj;

            for (int i = 0; i < damageTypes.size(); i++) {

                moverList.add( parseMoverObject((JSONObject)damageTypes.get(i)) );
            }
            //for each Json input object

        } catch (Exception e) {

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }

        return moverList;
    }

    /**
     * creates a Mover and adds it t the list
     * @param damages
     */
    private static Mover parseMoverObject(JSONObject damages)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) damages.get("Mover");//Choose the class

        //get the damage amount
        String t = (String) employeeObject.get("cellNumber");

        //Get playerNum
        String d = (String) employeeObject.get("beforeShooting");

        //Get seeAble
        String stn = (String) employeeObject.get("afterShooting");

        //Get melee
        String melee = (String) employeeObject.get("facoltative");

        //Get melee
        String diffP = (String) employeeObject.get("toCell");

        //Get melee
        String dist = (String) employeeObject.get("target");

        //Get melee
        String my = (String) employeeObject.get("mycell");

        return new Mover(Integer.parseInt(t),Boolean.parseBoolean(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee),Boolean.parseBoolean(diffP),Boolean.parseBoolean(dist),Boolean.parseBoolean(my));
    }


    /**
     * WEAPON
     *
     * creates the static weaponsList
     */
    public static  List<NormalWeapon>  normalWeaponReader()
    {
        //----------------microEffects ecc creator
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();

        List<NormalWeapon> normalWeapons =new ArrayList<>();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(NORMAL_WEAPON_PATH))
        {//change to relative files paths
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray wps = (JSONArray) obj;

            for (int i = 0; i < wps.size(); i++) {
                normalWeapons.add(parseWeaponObject((JSONObject)wps.get(i)));
            }
            //for each Json input object

        } catch (Exception e) {

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }

        return normalWeapons;
    }

    /**
     * reads the JSON and creates a NormalWeapon object and adds it to the list
     * @param micros
     */
    private static NormalWeapon parseWeaponObject(JSONObject micros)
    {
        //Get  object within list
        JSONObject employeeObject = (JSONObject) micros.get("Weapon");//Choose the class

        //get the damage amount
        String n = (String) employeeObject.get("name");

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


        return new NormalWeapon(n,wpCost,mf);
    }

    /**
     * creates the ammo from the JSON using the COlor class
     * @param wpCost
     * @param type
     * @return the cost in AmmoCubes
     */
    private static List<AmmoCube> ammoAnalizer(List<AmmoCube> wpCost,String type)
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
    private static List<MacroEffect> effectsAnalizer (List<MacroEffect> mf,int typeEncoded)
    {
        mf.add(MacroEffect.getMacroEffects().get(typeEncoded));
        return mf;
    }

    /**
     * This method will create a list of special weapons
     * @return a list of special weapons
     */
    private static List<Weapon> generateSpecialWeapon(){

        List<Weapon> specialWeaponList = new ArrayList<>();

        specialWeaponList.add(new Thor());

        //TODO add all the special weapons

        return specialWeaponList;
    }

    /**
     * This method will generate the full weapon list
     * @return the full weapon list
     */
    public static List<Weapon> getFullWeaponList(){

        List<Weapon> weaponList = new ArrayList<>();

        weaponList.addAll(normalWeaponReader());

        weaponList.addAll(generateSpecialWeapon());

        return weaponList;
    }

    /**
     * This method gets a real instance of a weapon from his name
     * @param weaponName is the weaponName
     * @return an instance of the requested weapon or null;
     */
    public static Weapon getWeaponByName(String weaponName){

        List<Weapon> matchingWeaponList = getFullWeaponList()
                .stream()
                .filter( weapon -> weapon.getName().equalsIgnoreCase(weaponName))
                .collect(Collectors.toList());

        return (matchingWeaponList.isEmpty()) ? null : matchingWeaponList.get(0);

    }

    /**
     * This method will load a map of the specified type
     * @param mapType is the type of map requested
     * @return an instance of SavedMap containing the map cell
     */
    public static SavedMap getMap(int mapType){

        String path;

        switch (mapType){

            case 1:

                path = MAP_ONE_PATH;

                break;

            case 2:

                path = MAP_TWO_PATH;

                break;

            case 3:

                path = MAP_THREE_PATH;

                break;

            default:

                path = MAP_TWO_PATH;

                break;

        }

        SavedMap savedMap = null;

        try {

            // creates a reader for the file

            BufferedReader br = new BufferedReader(new FileReader(new File(path).getAbsolutePath()));

            // load the Config File

            savedMap = gson.fromJson(br, SavedMap.class);

            // LOG the load

        }catch (Exception e){

            LOGGER.log(Level.WARNING, () -> LOG_START + " could not read the specified map ");
        }

        return savedMap;
    }


    // SAVES LOGIC


    //      EX-GAMES

    /**
     * @return the hashmap from a json to know if games were saved
     */
    private static HashMap<Integer,String> getGames(){

        List<String> pathList = new ArrayList<>();

        try{

            BufferedReader br = new BufferedReader( new FileReader( new File(GAMES_PATH  + "/games.json").getAbsolutePath()));

            // load the HashMap

            pathList.addAll( gson.fromJson(br,List.class));

            LOGGER.log(level,()-> LOG_START + " [OK] games list loaded");

        }catch (Exception e){

            LOGGER.log(Level.WARNING, ()-> LOG_START + " error while trying to load games list ");

            LOGGER.log(Level.WARNING,e.getMessage(),e);

            //TODO create directory if not present

        }

        return buildHashMapFromList(pathList);
    }

    /**
     * This method will get a list of path and turn it into a Hashmap
     * @param pathList is a list of string
     * @return a hashmap containing the paths linked with the id
     */
    private static HashMap<Integer,String> buildHashMapFromList(List<String> pathList){

        HashMap<Integer,String> hashMap = new HashMap<>();

        if (pathList.isEmpty()){

            pathList.add("defaultPath");

        }

        for (int i = 0; i < pathList.size(); i++) {

            hashMap.put(i- 1 ,pathList.get(i));

        }

        return hashMap;
    }

    /**
     * This method saves the list of games paired with the base path to save
     * @param games is the hashMap to save
     */
    private static void saveGamesList(HashMap<Integer,String> games){

        try {

            FileWriter writer = new FileWriter(GAMES_PATH + "/games.json");

            gson.toJson(getGameListToSave(games), writer);

            writer.flush();
            writer.close();

            int size = games.size() -1;

            LOGGER.log(level,() -> LOG_START + " [OK] game list saved w/ " + size + " effective games "  );

        }catch(IOException e){

            LOGGER.log(Level.WARNING, ()-> LOG_START + " error while trying to write games list ");

            LOGGER.log(Level.WARNING,e.getMessage(),e);
        }

    }

    private static List<String> getGameListToSave(HashMap<Integer,String> games){

        List<String> pathlist = new ArrayList<>();

        for (int i = -1 ; i < games.size() - 1 ; i++) {

            pathlist.add(games.get(i));
        }

        return pathlist;
    }

    /**
     *
     * @return the number of games
     */
    public static int getGamesSize(){

        HashMap<Integer,String> hashMap = getGames();

        return hashMap.size() - 1;

    }

    /**
     *
     * @param gameId is the id of the new game
     * @return a string containing the new save path for the new game
     */
    private static String genSavePath(int gameId){

        return GAMES_PATH + "/" + "game_Id_" + gameId;
    }

    /**
     *
     * @param gameId is the id of the asked game
     * @return true if the asked game exist
     */
    public static Boolean containsGame(int gameId){

        HashMap<Integer,String> hashMap = new HashMap<>();

        hashMap.putAll(getGames());

        return hashMap.containsKey(gameId);

    }

    /**
     * add a new game to the hashmap and saves
     */
    public static int addGame(){

        HashMap<Integer,String> hashMap = getGames();

        int gameId = hashMap.size() - 1; // the size is 1 by default

        hashMap.putIfAbsent(gameId, genSavePath(gameId));

        LOGGER.log(level, () -> LOG_START + " [OK] added game w/ id: " + gameId);

        saveGamesList(hashMap);

        currentGame = gameId;

        return gameId;
    }

    /**
     *
     * @param gameid is the id of the game to remove from the map
     */
    public static void closeGame(int gameid) throws GameNonExistentException {

        HashMap<Integer,String> hashMap = getGames();

        if (!hashMap.containsKey(gameid)) throw  new GameNonExistentException();

        hashMap.remove(gameid);

        saveGamesList(hashMap);

    }

    /**
     * This method deletes all of the games from the map
     */
    public static void clearGames(){

        HashMap<Integer,String> hashMap = getGames();

        hashMap.clear();

        hashMap.put(-1,"InitPath");

        saveGamesList(hashMap);
    }


    //          map


    /**
     * This method will read a saved Map
     * @return an instance of SavedMap containing the map cell
     */
    public static SavedMap readSavedMap(){

        String gamePath = getGames().get(currentGame);

        SavedMap savedMap = null;

        try {

            // creates a reader for the file

            BufferedReader br = new BufferedReader(new FileReader(new File(gamePath + SAVED_MAP_RELATIVE_PATH).getAbsolutePath()));

            // load the Config File

            savedMap = gson.fromJson(br, SavedMap.class);

            // LOG the load

        }catch (Exception e){

            LOGGER.log(Level.WARNING, () -> LOG_START + " could not read the saved map ");
        }

        return savedMap;
    }

    /**
     * This method will save the map currently used in the model
     */
    public static void saveMap(){

        String gamePath = getGames().get(currentGame);

        SavedMap savedMap = new SavedMap(Model.getMap().getMatrix());

        try {

            // creates a reader for the file

            FileWriter writer = new FileWriter(gamePath + SAVED_MAP_RELATIVE_PATH);

            // write the file

            gson.toJson(savedMap,writer);

            writer.flush();
            writer.close();

            // LOG the load

        }catch (IOException e){

            LOGGER.log(Level.WARNING, () -> LOG_START + " could not write the map ");

            LOGGER.log(Level.WARNING,e.getMessage(),e);
        }
    }




}
