package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedMap;
import it.polimi.ingsw.view.cachemodel.cachedmap.FileRead;
import it.polimi.ingsw.view.cachemodel.sendables.*;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CacheModel {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private List<Player> players = new ArrayList<>();
    private  CachedGame game = null;
    private final View view;
    private CachedMap cachedMap;

    public CacheModel(View view) {
        this.view = view;
    }


    private void update(InitialUpdate update) {

        LOGGER.log(level, " [CACHE-MODEL] Received initial update with players: " + update.getNames());

        players.clear();

        for (int i = 0; i < update.getNames().size() ; i++) {
            players.add(new Player(i, update.getNames().get(i), update.getColors().get(i)));
        }

        cachedMap = new CachedMap(update.getMapType());

        view.getClientToVView().setGameId(update.getGameId());
    }


    public void update(UpdateClass updateClass){

        switch (updateClass.getType()){

            case LOBBY:

                CachedLobby cachedLobby = (CachedLobby) updateClass;

                players.clear();


                for (int i = 0; i < cachedLobby.getNames().size(); i++) {
                    players.add(new Player(i, cachedLobby.getNames().get(i), null));

                }

                // notify the view

                view.getUserInterface().notifyUpdate(UpdateType.LOBBY, -1);

                LOGGER.log(level, "[Cache-Model] received LOBBY update w/ players: {0}", cachedLobby.getNames() );


                break;

            case INITIAL:

                update((InitialUpdate) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.INITIAL, updateClass.getPlayerId());

                //cli PlayerColors

                for(int i = 0; i < getCachedPlayers().size(); i++) {
                    FileRead.addPlayerColor(getCachedPlayers().get(i).getPlayerColor());
                }

                break;

            case STATS:

                players.get(updateClass.getPlayerId()).update((CachedStats) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.STATS, updateClass.getPlayerId());

                break;

            case AMMO_BAG:

                players.get(updateClass.getPlayerId()).update((CachedAmmoBag) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.AMMO_BAG, updateClass.getPlayerId());

                break;

            case POWERUP_BAG:

                players.get(updateClass.getPlayerId()).update((CachedPowerUpBag) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.POWERUP_BAG, updateClass.getPlayerId());

                break;

            case WEAPON_BAG:

                players.get(updateClass.getPlayerId()).update((CachedWeaponBag) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.WEAPON_BAG, updateClass.getPlayerId());

                break;

            case GAME:

                this.game = (CachedGame) updateClass;

                view.getUserInterface().notifyUpdate(UpdateType.GAME, updateClass.getPlayerId());

                break;

            case AMMO_CELL:

                this.cachedMap.update((CachedAmmoCell) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.AMMO_CELL,updateClass.getPlayerId());

                break;

            case SPAWN_CELL:

                this.cachedMap.update((CachedSpawnCell) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.SPAWN_CELL, updateClass.getPlayerId());

                break;

            case TURN:



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

    public CachedMap getCachedMap() {
        return cachedMap;
    }

    public CachedFullWeapon getWeaponInfo(String name) throws WeaponNotFoundException {

        // load the list from the parser

        List<CachedFullWeapon> weaponList = CacheModelParser.readCachedFullWeaponsFromList();

        // search the list of the weapon for the given name

        List<CachedFullWeapon> matchingWeapon = weaponList
                .stream()
                .filter( x -> x.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        return (matchingWeapon.isEmpty()) ? null : matchingWeapon.get(0);
    }

}
