package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.view.GUI.GuiLobbyController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedMap;
import it.polimi.ingsw.view.cachemodel.sendables.*;
import it.polimi.ingsw.view.cachemodel.updates.InitialUpdate;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheModel {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private List<Player> players = new ArrayList<>();
    //private ObservableList<Player> playersObsList = FXCollections.observableArrayList();
    private  CachedGame game = null;
    private final View view;
    private CachedMap cachedMap;

    public CacheModel(View view) {
        this.view = view;
    }


    private void update(InitialUpdate update) {

        LOGGER.log(level, " [CACHE-MODEL] Received initial update with players: " + update.getNames());

        players.clear();

        GuiLobbyController.clearLobbyPlayers();
        /*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                playersObsList.clear();
            }
        });*/

        for (int i = 0; i < update.getNames().size() ; i++) {
            players.add(new Player(i, update.getNames().get(i), update.getColors().get(i)));
            GuiLobbyController.addLobbyPlayers(update.getNames().get(i));
        }

        /*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < update.getNames().size() ; i++) {
                playersObsList.add(new Player(i, update.getNames().get(i), update.getColors().get(i)));
                }
            }
        });*/

        cachedMap = new CachedMap(update.getMapType());

        view.getClientToVView().setGameId(update.getGameId());
    }


    public void update(UpdateClass updateClass){

        switch (updateClass.getType()){

            case LOBBY:

                CachedLobby cachedLobby = (CachedLobby) updateClass.getUpdate();

                players.clear();
                GuiLobbyController.clearLobbyPlayers();
                /*
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        playersObsList.clear();
                    }
                });*/
                /*
                for (String name: cachedLobby.getNames()) {
                    players.add(new Player(cachedLobby.getNames().indexOf(name), name, null));
                    System.out.println("Player connessi: \n" + cachedLobby.getNames());
                    GuiLobbyController.addLobbyPlayers(cachedLobby.getNames().get(name.indexOf(name)));
                }*/

                for (int i = 0; i < cachedLobby.getNames().size(); i++) {
                    players.add(new Player(i, cachedLobby.getNames().get(i), null));
                    System.out.println("Player connessi: \n" + cachedLobby.getNames());
                    GuiLobbyController.addLobbyPlayers(cachedLobby.getNames().get(i));
                }
                /*
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (String name: cachedLobby.getNames()) {
                            players.add(new Player(cachedLobby.getNames().indexOf(name), name, null));
                            //System.out.println("Player connessi: \n" + cachedLobby.getNames());
                        }
                    }
                });*/




                break;

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

                players.get(updateClass.getPlayerId()).update((CachedWeaponBag) updateClass.getUpdate());

                break;

            case GAME:

                this.game = (CachedGame) updateClass.getUpdate();

                break;

            case CELL_AMMO:

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

    /*
    public ObservableList<Player> getLobbyPlayers() {
        return lobbyPlayers;
    }

     */
}
