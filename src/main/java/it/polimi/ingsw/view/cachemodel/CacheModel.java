package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedMap;
import it.polimi.ingsw.view.cachemodel.sendables.*;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.ArrayList;
import java.util.List;

public class CacheModel {

    private  List<Player> players = new ArrayList<>();
    private  CachedGame game = null;
    private final View view;
    private CachedMap cachedMap;

    public CacheModel(View view) {
        this.view = view;
    }

    private void update(InitialUpdate update) {

        players = new ArrayList<>();

        for (int i = 0; i < update.getNames().size() ; i++) {

            players.add(new Player(i,update.getNames().get(i),update.getColors().get(i)));

        }

        cachedMap = new CachedMap(update.getMapType());

        view.getClientToVView().setGameId(update.getGameId());
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

                this.cachedMap.update((CachedAmmoCell) updateClass.getUpdate());

                break;

            case SPAWN_CELL:

                this.cachedMap.update((CachedSpawnCell) updateClass.getUpdate());

                break;

            default:

                break;



        }
    }

    public List<Player> getCachedPlayers(){

        return players;
    }

    public int getMapType() {

        return cachedMap.getMapType();
    }

    public CachedGame getGame() {

        return game;
    }
}
