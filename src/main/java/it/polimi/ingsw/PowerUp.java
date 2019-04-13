package it.polimi.ingsw;

/**
 *  Abstract Class for PowerUps
 */
public abstract class PowerUp {

    private final Color color;

    /**
     *
     * @param color of the card and of the cube that will be obtained if sold
     */
    public PowerUp(Color color) {

        this.color = color;
    }


    /**
     * @return an AmmoCube of the color of the card
     */
    public AmmoCube sell() {

        return new AmmoCube(this.color);
    }

}