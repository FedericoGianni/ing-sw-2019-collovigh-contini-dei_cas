package it.polimi.ingsw.view.virtualView.observers;

public class PlayerObserver {

    private final int playerId;
    private final Observers up;

    private StatsObserver statsObserver = null;
    private PowerUpBagObserver powerUpBagObserver = null;
    private AmmoBagObserver ammoBagObserver = null;
    private WeaponBagObserver weaponBagObserver = null;

    public PlayerObserver(int playerId, Observers up) {

        this.playerId = playerId;
        this.up = up;

        this.statsObserver = new StatsObserver(this);
        this.powerUpBagObserver = new PowerUpBagObserver(this);
        this.ammoBagObserver = new AmmoBagObserver(this);
        this.weaponBagObserver = new WeaponBagObserver(this);

    }




    //Observer

    public StatsObserver getStatsObserver() {
        return statsObserver;
    }

    public PowerUpBagObserver getPowerUpBagObserver() {
        return powerUpBagObserver;
    }

    public AmmoBagObserver getAmmoBagObserver() { return ammoBagObserver; }

    public int getPlayerId() {
        return playerId;
    }

    public Observers getTopClass() {
        return up;
    }

    public WeaponBagObserver getWeaponBagObserver() {
        return weaponBagObserver;
    }
}
