package it.polimi.ingsw.view.cachemodel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheModelParser {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String CACHED_FULL_WEAPON_PATH = "/json/cachedmodel/weapons.json";

    private static final Gson gson = new Gson();

    /**
     * Read a CachedFullWeaponList from parser
     * @return a list of cached full weapons
     */
    public static List<CachedFullWeapon> readCachedFullWeaponsFromList(){

        List<CachedFullWeapon> weaponList = new ArrayList<>();

        // gets input stream

        InputStream inputStream = CacheModelParser.class.getResourceAsStream(CACHED_FULL_WEAPON_PATH );

        // creates a reader for the file

        BufferedReader br = new BufferedReader( new InputStreamReader(inputStream));

        // reads the list

        Type listType = new TypeToken<ArrayList<CachedFullWeapon>>(){}.getType();

        weaponList = gson.fromJson(br,listType);

        // log

        LOGGER.log( level, "[CachedModel] load weapons successfully from json ");

        return weaponList;

    }

}