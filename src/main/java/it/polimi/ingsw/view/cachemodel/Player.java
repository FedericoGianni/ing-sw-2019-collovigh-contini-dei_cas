package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;

public class Player {

    private final int playerId;



    private final PlayerColor playerColor;
    private final String name;
    private CachedStats stats;
    private CachedPowerUpBag powerUpBag;
    private CachedWeaponBag weaponbag;
    private CachedAmmoBag ammoBag;

    public Player(int playerId, String name, PlayerColor color) {
        this.playerId = playerId;
        this.name = name;
        this.playerColor = color;
    }

    public void update(CachedStats stats){

        this.stats = stats;
    }

    public void update(CachedPowerUpBag powerUpBag){

        this.powerUpBag = powerUpBag;
    }

    public void update(CachedAmmoBag ammoBag){

        this.ammoBag = ammoBag;
    }

    public void update(CachedWeaponBag weaponbag){

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
    public String toString(){

        if(getStats() != null && getWeaponbag() != null){
            return "ID: " + getStats().getPlayerId() + "\n" +
                    "Nome: " + getName() + "\n" +
                    "Danni: " + getStats().getDmgTaken() + "\n" +
                    "Marchi: " + getStats().getMarks() + "\n" +
                    "Online: " + getStats().getOnline() +
                    "Armi: " + getWeaponbag().toString() + "\n";

        } else if(getStats() != null) {
            return "ID: " + playerId + "\n" +
                    "Nome: " + getName() + "\n" +
                    "Danni: " + getStats().getDmgTaken() + "\n" +
                    "Marchi: " + getStats().getMarks() + "\n" +
                    "Online: " + getStats().getOnline() + "\n" +
                    "Armi : " + " nessuna \n";
        } else {
            return "ID: " + playerId + "\n" +
                    "Nome: " + getName() + "\n" +
                    "Danni" + "[ ]" + "\n" +
                    "Marchi" + "[ ]" + "\n" +
                    "Online: " + " true" + "\n" +
                    "Armi : " + " nessuna \n";
        }
    }
}
