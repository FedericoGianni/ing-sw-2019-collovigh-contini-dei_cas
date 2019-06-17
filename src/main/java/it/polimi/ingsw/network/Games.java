package it.polimi.ingsw.network;


import com.google.gson.Gson;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class bounds the gameId to it's own save path
 */
public class Games {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");

    private final String savePath = "resources/json/savegames";
    private HashMap<Integer,String> hashMap;



    /**
     * Constructor: read the hashmap from a json to know if games were saved
     */
    public Games() {

        Gson gson = new Gson();


        try{

            BufferedReader br = new BufferedReader( new FileReader( new File(savePath + "/games.json").getAbsolutePath()));

            // load the HashMap
            hashMap = gson.fromJson(br,HashMap.class);

            LOGGER.log(Level.FINE," [OK] games list loaded");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * saves the hashmap on the file
     */
    private void save(){

        Gson gson = new Gson();

        try {

            FileWriter writer = new FileWriter(savePath + "/games.json");

            gson.toJson(hashMap, writer);

            writer.flush();
            writer.close();

            LOGGER.log(Level.FINE," [OK] game list saved w/ {0} effective games",hashMap.size() -1);

        }catch(IOException e){

            e.printStackTrace();
        }


    }

    private HashMap<Integer, String> getHashMap() {
        return hashMap;
    }

    /**
     * add a new game to the hashmap and saves
     */
    public int addGame(){

        int gameId = hashMap.size() - 1; // the size is 1 by default

        hashMap.putIfAbsent(gameId,genSavePath(gameId));

        LOGGER.log(Level.FINE," [OK] added game w/ id: {0}", gameId);

        this.save();

        return gameId;


    }

    /**
     *
     * @param gameid is the id of the game to remove from the map
     */
    public void closeGame(int gameid) throws GameNonExistentException{

        if (!hashMap.containsKey(gameid)) throw  new GameNonExistentException();

        hashMap.remove(gameid);
        this.save();

    }

    /**
     * This method deletes all of the games from the map
     */
    public void clear(){

        hashMap.clear();
        hashMap.putIfAbsent(-1,"InitPath");
        this.save();
    }

    /**
     *
     * @param gameId is the id of the new game
     * @return a string containing the new save path for the new game
     */
    private String genSavePath(int gameId){

        return savePath + "/" + gameId;
    }

    /**
     *
     * @param gameId is the id of the game
     * @return the string containing the save path for the new game
     * @throws GameNonExistentException if the id do not correspond to any game
     */
    public String getSavePath(int gameId) throws GameNonExistentException{

        if (!hashMap.containsKey(gameId)) throw new GameNonExistentException();
        return hashMap.get(gameId);
    }

    /**
     *
     * @param gameId is the id of the asked game
     * @return true if the asked game exist
     */
    public Boolean contains(int gameId){

        return hashMap.containsKey(gameId);
    }

    /**
     *
     * @return the number of games
     */
    public int size(){
        return hashMap.size() - 1;
    }

    /**
     *
     * @return true if there are no games saved
     */
    public Boolean isEmpty(){
        return (hashMap.size() == 1);
    }

}
