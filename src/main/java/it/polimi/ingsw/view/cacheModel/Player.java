package it.polimi.ingsw.view.cacheModel;

public class Player {

    private int playerId;
    private String name;
    private CachedStats stats;
    private CachedPowerUpBag powerUpBag;
    private CachedAmmoBag ammoBag;

    public Player(int playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public void getUpdate(PlayerAttributes attributes){

        this.stats = (attributes.getStats() == null)? this.stats : attributes.getStats();

        this.powerUpBag = (attributes.getPowerUpBag() == null)? this.powerUpBag : attributes.getPowerUpBag();

        this.ammoBag = (attributes.getAmmoBag() == null)? this.ammoBag : attributes.getAmmoBag();
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
}
