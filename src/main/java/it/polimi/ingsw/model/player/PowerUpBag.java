package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Subject;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.powerup.PowerUp;

import java.util.ArrayList;
import java.util.List;

public class PowerUpBag  extends Subject implements Bag<PowerUp> {

    private static final int MAX_POWER_UPS = 3;

    private List<PowerUp> powerUps;

    /**
     * Constructor
     */
    public PowerUpBag() {

        this.powerUps = new ArrayList<>();
    }

    /**
     * Returns a copy of the PowerUpBag, passed as a parameter
     * @param clone PowerUpBag to be cloned
     */
    public PowerUpBag(PowerUpBag clone){
        this.powerUps =  new ArrayList<>();
        for(PowerUp p : clone.getList()){
            this.powerUps.add(p);
        }
    }

    /**
     *
     * @return a copy of the PowerUp list
     */
    @Override
    public List<PowerUp> getList() {
        return new ArrayList<>(this.powerUps);
    }

    /**
     *
     *
     * @param item is the PowerUp that needs to be added to the list
     */
    @Override
    public void addItem(PowerUp item) {

        if (this.powerUps.size()<MAX_POWER_UPS){

            this.powerUps.add(item);
        }

    }

    /**
     *
     * @param item is the pointer of the powerUp to "use" note that it will be removed from the list
     * @return the ONLY pointer to the PowerUp
     */
    @Override
    public PowerUp getItem(PowerUp item) throws CardNotPossessedException {

        if (!this.powerUps.contains(item)) throw new CardNotPossessedException();

        else {
            this.powerUps.remove(item);

            return item;
        }
    }

    /**
     * this function will automatically thrash the powerUp
     * @param item is the item to convert in AmmoCubes
     * @return the AmmoCube the PowerUp value
     */
    public AmmoCube sellItem(PowerUp item){

        AmmoCube cube = item.sell();

        Model.getGame().discardPowerUp(item);

        this.powerUps.remove(item);

        return cube;
    }

    @Override
    public Boolean hasItem(PowerUp item) {


        return this.powerUps.contains(item);
    }
}
