package it.polimi.ingsw.model.ammo;


import it.polimi.ingsw.utils.Color;

import java.io.Serializable;

/**
 * this class represents the single cube that the game use for representing ammo
 * it has only one attribute that specify the color of it
 */
public class AmmoCube implements Serializable {

    private final Color color;

    /**
     *
     * @param color set the color of the cube
     */
    public AmmoCube(Color color) {

        this.color = color;


    }


    /**
     *
     * since this class is not dynamic it just need the get method
     *
     * @return the color of the cube
     */
    public Color getColor() {


        return color;
    }

    /**
     *
     * @return the color of the ammo
     */
    @Override
    public String toString() {
        return "Color = " + color;
    }


}