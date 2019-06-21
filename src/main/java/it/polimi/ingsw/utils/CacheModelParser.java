package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheModelParser {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String CACHED_FULL_WEAPON_PATH = "resources/json/cachedmodel/weapons.json";

    private static final Gson gson = new Gson();


    public static List<CachedFullWeapon> readCachedFullWeaponsFromList(){

        List<CachedFullWeapon> weaponList = new ArrayList<>();

        try {

            // creates a reader for the file

            BufferedReader br = new BufferedReader(new FileReader(new File(CACHED_FULL_WEAPON_PATH).getAbsolutePath()));

            // reads the list

            Type listType = new TypeToken<ArrayList<CachedFullWeapon>>(){}.getType();

            weaponList = gson.fromJson(br,listType);

            // log

            LOGGER.log( level, "[CachedModel] load weapons successfully from json ");


        }catch (FileNotFoundException e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return weaponList;

    }

}