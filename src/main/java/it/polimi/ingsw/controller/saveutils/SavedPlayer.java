package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.model.powerup.*;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to serialize the Players in a file to be read, allowing game persistence
 */
public class SavedPlayer implements Serializable {

    private final String name;
    private final int id;
    private final PlayerColor color;
    private final List<String> weaponList;
    private final List<AmmoCube> ammoCubes;
    private final List<CachedPowerUp> powerUpList;
    private final Stats stats;

    public SavedPlayer(Player player) {

        //REMOVE OBSERVERS
        //player.getStats().cleanObservers();

        this.name = player.getPlayerName();
        this.id = player.getPlayerId();
        this.color = player.getColor();
        this.weaponList = player
                .getWeapons()
                .stream()
                .map(weapon -> weapon.getName() )
                .collect(Collectors.toList());

        this.ammoCubes = player
                .getAmmo();

        this.powerUpList = player
                .getPowerUpBag()
                .getList()
                .stream()
                .map( powerUp ->  new CachedPowerUp( powerUp.getType(),powerUp.getColor()) )
                .collect(Collectors.toList());

        this.stats = new Stats(player.getStats());

        //set player offline since reload is treated like a reconnection
        this.stats.setOnline(false);
    }

    public Player getRealPlayer(){

        Player player = new Player(name,id,color);

        for ( String weapon : weaponList ){

            player.getCurrentWeapons().addItem(Parser.getWeaponByName(weapon));
        }

        for ( AmmoCube ammoCube : ammoCubes ){

            player.getAmmoBag().addItem(ammoCube);

        }

        for ( CachedPowerUp powerUp : powerUpList ){

            player.getPowerUpBag().addItem(cachedPowerUpToReal(powerUp));
        }

        player.setStats(stats);


        return player;
    }

    private static PowerUp cachedPowerUpToReal( CachedPowerUp powerUp ){

        switch (powerUp.getType()){

            case NEWTON:

                return new Newton(powerUp.getColor());

            case TARGETING_SCOPE:

                return new TargetingScope(powerUp.getColor());

            case TAG_BACK_GRENADE:

                return new TagbackGrenade(powerUp.getColor());

            case TELEPORTER:

                return new Teleporter(powerUp.getColor());

            default:

                return null;
        }
    }

    public static List<PowerUp> cachedPowerUpDeckToList(List<CachedPowerUp> cachedPowerUps){

        List<PowerUp> powerUps = new ArrayList<>();

        for(CachedPowerUp c : cachedPowerUps){

            powerUps.add(cachedPowerUpToReal(c));
        }

        return powerUps;

    }


}
