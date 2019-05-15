package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedGame;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.ArrayList;
import java.util.List;

public class CacheModel {

    private static List<Player> players = new ArrayList<>();
    private static int mapType;
    private static CachedGame game = null;


    private CacheModel() {


    }

    private static void populateModel(InitialUpdate update) {

        new CacheModel();

        players = new ArrayList<>();

        for (int i = 0; i < update.getNames().size() ; i++) {

            players.add(new Player(i,update.getNames().get(i),update.getColors().get(i)));

        }

        mapType = update.getMapType();
    }

    public static List<Player> getCachedPlayers(){

        return players;
    }

    public static int getMapType() {
        return mapType;
    }

    public static CachedGame getGame() {
        return game;
    }

    public static  void update(UpdateClass updateClass){

        switch (updateClass.getType()){

            case INITIAL:

                populateModel((InitialUpdate) updateClass.getUpdate());

                break;

            case STATS:

                getCachedPlayers().get(updateClass.getPlayerId()).update((CachedStats) updateClass.getUpdate());

                break;

            case AMMO_BAG:

                getCachedPlayers().get(updateClass.getPlayerId()).update((CachedAmmoBag) updateClass.getUpdate());

                break;

            case POWERUP_BAG:

                getCachedPlayers().get(updateClass.getPlayerId()).update((CachedPowerUpBag) updateClass.getUpdate());

                break;

            case WEAPON_BAG:

                //TODO

                break;

            case GAME:

                game = (CachedGame) updateClass.getUpdate();

                break;

            case AMMO_CELL:

                //TODO

                break;

            case SPAWN_CELL:

                //TODO

                break;

            default:

                break;



        }
    }
}
