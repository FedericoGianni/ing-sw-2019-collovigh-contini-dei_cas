package it.polimi.ingsw.view.virtualView;

public class PlayerObserver {

    private StatsObserver statsObserver = null;
    private PowerUpBagObserver powerUpBagObserver = null;

    public PlayerObserver() {

    }

    public StatsObserver getStatsObserver() {
        return statsObserver;
    }

    public void setStatsObserver(StatsObserver statsObserver) {
        this.statsObserver = statsObserver;
    }

    public PowerUpBagObserver getPowerUpBagObserver() {
        return powerUpBagObserver;
    }

    public void setPowerUpBagObserver(PowerUpBagObserver powerUpBagObserver) {
        this.powerUpBagObserver = powerUpBagObserver;
    }

}
