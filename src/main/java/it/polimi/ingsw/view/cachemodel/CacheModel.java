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

    private  List<Player> players = new ArrayList<>();
    private  int mapType;
    private  CachedGame game = null;


    public CacheModel() {


    }

    private void update(InitialUpdate update) {

        players = new ArrayList<>();

        for (int i = 0; i < update.getNames().size() ; i++) {

            players.add(new Player(i,update.getNames().get(i),update.getColors().get(i)));

        }

        mapType = update.getMapType();
    }


    public void update(UpdateClass updateClass){

        switch (updateClass.getType()){

            case INITIAL:

                update((InitialUpdate) updateClass.getUpdate());

                break;

            case STATS:

                players.get(updateClass.getPlayerId()).update((CachedStats) updateClass.getUpdate());

                break;

            case AMMO_BAG:

                players.get(updateClass.getPlayerId()).update((CachedAmmoBag) updateClass.getUpdate());

                break;

            case POWERUP_BAG:

                players.get(updateClass.getPlayerId()).update((CachedPowerUpBag) updateClass.getUpdate());

                break;

            case WEAPON_BAG:

                //TODO

                break;

            case GAME:

                this.game = (CachedGame) updateClass.getUpdate();

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

    public List<Player> getCachedPlayers(){

        return players;
    }

    public int getMapType() {

        return mapType;
    }

    public CachedGame getGame() {

        return game;
    }
}
