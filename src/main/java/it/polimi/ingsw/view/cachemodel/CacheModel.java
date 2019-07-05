package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.view.UiHelpers;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedMap;
import it.polimi.ingsw.view.cachemodel.cachedmap.FileRead;
import it.polimi.ingsw.view.cachemodel.sendables.*;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Simplified version of the model used to store local updates for clients
 */
public class CacheModel {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    /**
     * List of cached Player containing all infos about in game players
     */
    private static List<Player> players = new ArrayList<>();

    /**
     * Game infos like killshot track
     */
    private  CachedGame game = null;

    /**
     * Referecen to the view
     */
    private final View view;

    /**
     * Simplified version of the game map (i.e. without adjacences)
     */
    private CachedMap cachedMap;

    public CacheModel(View view) {
        this.view = view;
    }

    /**
     * Initial update containing the name of the players, maptype
     * @param update InitialUpdate
     */
    private void update(InitialUpdate update) {

        LOGGER.log(level, " [CACHE-MODEL] Received initial update with players: " + update.getNames());

        players.clear();

        for (int i = 0; i < update.getNames().size() ; i++) {
            players.add(new Player(i, update.getNames().get(i), update.getColors().get(i)));
        }

        cachedMap = new CachedMap(update.getMapType());

        view.getClientToVView().setGameId(update.getGameId());
    }

    /**
     * Handle every other update type
     * @param updateClass update received by the client
     */
    public void update(UpdateClass updateClass){

        switch (updateClass.getType()){

            case LOBBY:

                CachedLobby cachedLobby = (CachedLobby) updateClass;

                players.clear();


                for (int i = 0; i < cachedLobby.getNames().size(); i++) {
                    players.add(new Player(i, cachedLobby.getNames().get(i), null));

                }

                // notify the view

                view.getUserInterface().notifyUpdate(UpdateType.LOBBY, -1,null);

                LOGGER.log(level, "[Cache-Model] received LOBBY update w/ players: {0}", cachedLobby.getNames() );


                break;

            case INITIAL:

                update((InitialUpdate) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.INITIAL, updateClass.getPlayerId(),null);

                //cli PlayerColors

                for(int i = 0; i < getCachedPlayers().size(); i++) {
                    FileRead.addPlayerColor(getCachedPlayers().get(i).getPlayerColor());
                }

                break;

            case STATS:

                players.get(updateClass.getPlayerId()).update((CachedStats) updateClass);

                new Thread( () -> view.getUserInterface().notifyUpdate(UpdateType.STATS, updateClass.getPlayerId(),null)).start();

                break;

            case AMMO_BAG:

                players.get(updateClass.getPlayerId()).update((CachedAmmoBag) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.AMMO_BAG, updateClass.getPlayerId(),null);

                break;

            case POWERUP_BAG:

                players.get(updateClass.getPlayerId()).update((CachedPowerUpBag) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.POWERUP_BAG, updateClass.getPlayerId(),null);

                break;

            case WEAPON_BAG:

                players.get(updateClass.getPlayerId()).update((CachedWeaponBag) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.WEAPON_BAG, updateClass.getPlayerId(),null);

                break;

            case GAME:

                this.game = (CachedGame) updateClass;

                view.getUserInterface().notifyUpdate(UpdateType.GAME, updateClass.getPlayerId(),null);

                break;

            case AMMO_CELL:

                this.cachedMap.update((CachedAmmoCell) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.AMMO_CELL,updateClass.getPlayerId(),null);

                break;

            case SPAWN_CELL:

                this.cachedMap.update((CachedSpawnCell) updateClass);

                view.getUserInterface().notifyUpdate(UpdateType.SPAWN_CELL, updateClass.getPlayerId(),null);

                break;

            case TURN:

                view.getUserInterface().notifyUpdate(UpdateType.TURN, updateClass.getPlayerId(), (TurnUpdate) updateClass );

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

    public CachedMap getCachedMap() {
        return cachedMap;
    }

    /**
     *
     * @param name of the weapon to be returned
     * @return the CachedFullWeapon passed as with name
     * @throws WeaponNotFoundException if the weapon is not found inside the CachedFullWeaponList read from json
     */
    public CachedFullWeapon getWeaponInfo(String name) throws WeaponNotFoundException {

        // load the list from the parser

        List<CachedFullWeapon> weaponList = CacheModelParser.readCachedFullWeaponsFromList();

        // search the list of the weapon for the given name

        List<CachedFullWeapon> matchingWeapon = weaponList
                .stream()
                .filter( x -> x.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());


        if(matchingWeapon.isEmpty()){

            throw new WeaponNotFoundException();

        }else return matchingWeapon.get(0);
    }

    public static String showDmgTaken(List<Integer> dmgTaken){

        String header = "Danni";
        return showColoredIntList(dmgTaken, header);
    }

    public static String showMarksTaken(List<Integer> playerMarks){

        String header = "Marchi";
        return showColoredIntList(playerMarks, header);

    }

    public static String showKillShotTrack(List<Point> killShotTrak){
        String header = "Teschi: ";
        return showColoredPointList(killShotTrak, header);
    }

    public static String showColoredPointList(List<Point> list, String header){

        String s = header + ": [";
        int cont = 0;
        int t = 0;

        for (int i = 0; i < list.size(); i++) {

            for (int j = 0; j < 2; j++) {
                if(cont == 0) {
                    s = s.concat("[ ID: ");
                    t = list.get(i).x;
                    s = s.concat(handleSingleIntShow(t));
                    cont++;
                } else if(cont == 1) {
                    s  = s.concat("SEGNALINI: ");
                    t = list.get(i).y;
                    s = s.concat(handleSingleIntShow(t));
                    s = s.concat("]");
                    cont = 0;
                }
                s = s.concat(", ");


            }


        }
        s = s.concat("]");
        return s;
    }

    public static String handleSingleIntShow(int t){

        String s = "";

        switch (t){
            case 0:
                s = s.concat(UiHelpers.colorAsciiTranslator(players.get(t).getPlayerColor()).escape());
                s = s.concat("0");
                s = s.concat(AsciiColor.ANSI_RESET.escape());
                break;

            case 1:
                s = s.concat(UiHelpers.colorAsciiTranslator(players.get(t).getPlayerColor()).escape());
                s = s.concat("1");
                s = s.concat(AsciiColor.ANSI_RESET.escape());
                break;

            case 2:
                s = s.concat(UiHelpers.colorAsciiTranslator(players.get(t).getPlayerColor()).escape());
                s = s.concat("2");
                s = s.concat(AsciiColor.ANSI_RESET.escape());
                break;

            case 3:
                s = s.concat(UiHelpers.colorAsciiTranslator(players.get(t).getPlayerColor()).escape());
                s = s.concat("3");
                s = s.concat(AsciiColor.ANSI_RESET.escape());
                break;

            case 4:
                s = s.concat(UiHelpers.colorAsciiTranslator(players.get(t).getPlayerColor()).escape());
                s = s.concat("4");
                s = s.concat(AsciiColor.ANSI_RESET.escape());
                break;

            case 5:
                s = s.concat(UiHelpers.colorAsciiTranslator(players.get(t).getPlayerColor()).escape());
                s = s.concat("5");
                s = s.concat(AsciiColor.ANSI_RESET.escape());
                break;
        }

        return s;
    }





    public static String showColoredIntList(List<Integer> list, String header){
        String s = header + ": [";

        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i)){
                case 0:
                    s = s.concat(UiHelpers.colorAsciiTranslator(players.get(list.get(i)).getPlayerColor()).escape());
                    s = s.concat("0");
                    s = s.concat(AsciiColor.ANSI_RESET.escape());
                    break;

                case 1:
                    s = s.concat(UiHelpers.colorAsciiTranslator(players.get(list.get(i)).getPlayerColor()).escape());
                    s = s.concat("1");
                    s = s.concat(AsciiColor.ANSI_RESET.escape());
                    break;

                case 2:
                    s = s.concat(UiHelpers.colorAsciiTranslator(players.get(list.get(i)).getPlayerColor()).escape());
                    s = s.concat("2");
                    s = s.concat(AsciiColor.ANSI_RESET.escape());
                    break;

                case 3:
                    s = s.concat(UiHelpers.colorAsciiTranslator(players.get(list.get(i)).getPlayerColor()).escape());
                    s = s.concat("3");
                    s = s.concat(AsciiColor.ANSI_RESET.escape());
                    break;

                case 4:
                    s = s.concat(UiHelpers.colorAsciiTranslator(players.get(list.get(i)).getPlayerColor()).escape());
                    s = s.concat("4");
                    s = s.concat(AsciiColor.ANSI_RESET.escape());
                    break;

                case 5:
                    s = s.concat(UiHelpers.colorAsciiTranslator(players.get(list.get(i)).getPlayerColor()).escape());
                    s = s.concat("5");
                    s = s.concat(AsciiColor.ANSI_RESET.escape());
                    break;
            }

            s = s.concat(", ");
        }

        s = s.concat("]");

        return s;
    }

}
