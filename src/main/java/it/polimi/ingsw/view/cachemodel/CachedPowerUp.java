package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.powerup.PowerUpType;

import java.io.Serializable;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;

public class CachedPowerUp implements Serializable {

    private final Color color;
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

        return s +"PowerUp {" +
                "color=" + color +
                ", type=" + type +
                '}' + ANSI_RESET.escape();
    }
}
