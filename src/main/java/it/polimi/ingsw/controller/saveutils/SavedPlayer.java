package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.powerup.*;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SavedPlayer implements Serializable {

    private final String name;
    private final int id;
    private final PlayerColor color;
    private final List<String> weaponList;
    private final List<AmmoCube> ammoCubes;
    private final List<CachedPowerUp> powerUpList;

    public SavedPlayer(Player player) {

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
    }

    private Player getRealPlayer(){

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

        return player;
    }

    private PowerUp cachedPowerUpToReal( CachedPowerUp powerUp ){

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
}
