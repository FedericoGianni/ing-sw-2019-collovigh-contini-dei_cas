package it.polimi.ingsw;

/**
 *
 */
public class AmmoCell extends Cell {

    /**
     * Default constructor
     */
    public AmmoCell() {
        super();
        ammoPlaced = AmmoCard.generateRandCard();
    }


    private AmmoCard ammoPlaced;


    /**
     * @return a randomly generated Ammo Card, based on the probability of the real on-board game deck
     * the ammoPlaced is generated randomly inside the creator so that the ammo is the same for all players and is not
     * generated randomly every time a Player wants to check what Ammo is placed inside this Cell
     */
    @Override
    public AmmoCard getAmmoPlaced() {

        return ammoPlaced;
    }

    /**
     * @return the Ammo picked up by Player inside this Cell, also it generates a new PowerUp to be placed inside Cell
     */
    @Override
    public AmmoCard pickAmmoPlaced() {

        AmmoCard tempAmmo = ammoPlaced;
        ammoPlaced = AmmoCard.generateRandCard();

        return tempAmmo;
    }



}
