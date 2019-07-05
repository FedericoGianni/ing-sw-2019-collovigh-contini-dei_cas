package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_RESET;

/**
 * Simplified version of the Model Player class to store local information in clients
 */
public class Player {

    /**
     * integer representing the id of the player
     */
    private final int playerId;

    /**
     * color chosen by the player
     */
    private final PlayerColor playerColor;

    /**
     * name chosen by the player
     */
    private final String name;

    /**
     * reference to the player's stats (dmg taken, marks taken, current position)
     */
    private CachedStats stats;

    /**
     * reference to the player's CachedPowerUp bag
     */
    private CachedPowerUpBag powerUpBag;

    /**
     * reference to the player's Cached Weapon bag
     */
    private CachedWeaponBag weaponbag;

    /**
     * reference to the player's CachedAmmo bag
     */
    private CachedAmmoBag ammoBag;

    public Player(int playerId, String name, PlayerColor color) {
        this.playerId = playerId;
        this.name = name;
        this.playerColor = color;
    }

    public void update(CachedStats stats) {

        this.stats = stats;
    }

    public void update(CachedPowerUpBag powerUpBag) {

        this.powerUpBag = powerUpBag;
    }

    public void update(CachedAmmoBag ammoBag) {

        this.ammoBag = ammoBag;
    }

    public void update(CachedWeaponBag weaponbag) {

        this.weaponbag = weaponbag;
    }

    public CachedStats getStats() {
        return stats;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public CachedPowerUpBag getPowerUpBag() {
        return powerUpBag;
    }

    public CachedAmmoBag getAmmoBag() {
        return ammoBag;
    }

    public CachedWeaponBag getWeaponbag() {
        return weaponbag;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    @Override
    public String toString() {

        String s = "";

        s = s.concat("\nID: " + getPlayerId());
        s = s.concat("\nNome: " + getName());

        if (getStats() != null) {
            s = s.concat("\n" + CacheModel.showDmgTaken(getStats().getDmgTaken()));
            s = s.concat("\n" + CacheModel.showMarksTaken(getStats().getMarks()));
            s = s.concat("\nOnline: " + getStats().onlineToString());
            s = s.concat("\nMorti: " + getStats().getDeaths());
            s = s.concat("\nPunti: " + getStats().getScore());

        } else {
            s = s.concat("\nDanni" + "[]");
            s = s.concat("\nMarchi" + "[]");
            s = s.concat("\nOnline: " + " si");
        }

        if (getWeaponbag() != null) {
            s = s.concat("\nArmi: " + "\t" + getWeaponbag().toString());
        } else {
            s = s.concat("\nArmi: nessuna");
        }

        if (getAmmoBag() != null) {
            s = s.concat("\nMunizioni: " + getAmmoBag().toString());
         } else {
            s = s.concat("\nMunizioni: nessuna");
        }

        s = s.concat(ANSI_RESET.escape());

        return s;
    }
}
