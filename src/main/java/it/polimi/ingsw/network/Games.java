package it.polimi.ingsw.network;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;

import java.io.*;
import java.util.HashMap;

/**
 * this class bounds the gameId to the save path
 */
public class Games {

    private HashMap<Integer,String> hashMap;
    private final String savePath = new File("src/main/java/it/polimi/ingsw/savegames").getAbsolutePath();


    /**
     * Constructor: read the hashmap from a json to know if games were saved
     */
    public Games() {

        Gson gson = new Gson();


        try{

            BufferedReader br = new BufferedReader( new FileReader(savePath + "/games.json"));

            // load the HashMap
            hashMap = gson.fromJson(br,HashMap.class);



        }catch (JsonSyntaxException e){

            //this.hashMap = new HashMap<>();
        }catch (FileNotFoundException e){
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

        }catch(IOException e){

            e.printStackTrace();
        }


    }

    /**
     * add a new game to the hashmap and saves
     */
    public void addGame(){

        int gameId = hashMap.size() - 1; // the size is 1 by default

        hashMap.putIfAbsent(gameId,genSavePath(gameId));

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
