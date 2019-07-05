package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

import java.io.Serializable;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;

/**
 * Simplified version of the PowerUp contained in the Model, needed to store powerups in player's powerups bag
 */
public class CachedPowerUp implements Serializable {

    /**
     * color of the powerup
     */
    private final Color color;

    /**
     * type of the powerup
     */
    private final PowerUpType type;

    public CachedPowerUp(PowerUpType type, Color color){
        this.type = type;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public PowerUpType getType() {
        return type;
    }


    @Override
    public String toString() {

        String s;

        switch (color){

            case RED:
                s = ANSI_RED.escape();
                break;

            case BLUE:
                s = ANSI_BLUE.escape();
                break;

            case YELLOW:
                s = ANSI_YELLOW.escape();
                break;

            default:
                s = "";
                break;
        }

        switch (type){

            case NEWTON:
                s = s.concat("RAGGIO CINETICO");
                break;

            case TARGETING_SCOPE:
                s = s.concat("MIRINO");
                break;

            case TAG_BACK_GRENADE:
                s = s.concat("GRANATA");
                break;

            case TELEPORTER:
                s = s.concat("TELETRASPORTO");
                break;
        }

        return s + ANSI_RESET.escape();
    }
}
