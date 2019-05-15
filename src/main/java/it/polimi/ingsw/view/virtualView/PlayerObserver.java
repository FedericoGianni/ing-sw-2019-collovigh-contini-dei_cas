package it.polimi.ingsw.view.virtualView;

public class PlayerObserver {

    private StatsObserver statsObserver = null;
    private PowerUpBagObserver powerUpBagObserver = null;
    private AmmoBagObserver ammoBagObserver = null;

    public PlayerObserver() {

        this.statsObserver = new StatsObserver();
        this.powerUpBagObserver = new PowerUpBagObserver();
        this.ammoBagObserver = new AmmoBagObserver();

    }

    //Updater maybe not useful probably will be deleted

    /**
     * @deprecated 
     * @param statsObserver
     */
    @Deprecated
    public void update(StatsObserver statsObserver){

        this.statsObserver = statsObserver;
    }

    /**
     * @deprecated
     * @param powerUpBagObserver
     */
    @Deprecated
    public void update(PowerUpBagObserver powerUpBagObserver){

        this.powerUpBagObserver = powerUpBagObserver;
    }


    //Observer

    public StatsObserver getStatsObserver() {
        return statsObserver;
    }

    public PowerUpBagObserver getPowerUpBagObserver() {
        return powerUpBagObserver;
    }

    public AmmoBagObserver getAmmoBagObserver() { return ammoBagObserver; }


}
