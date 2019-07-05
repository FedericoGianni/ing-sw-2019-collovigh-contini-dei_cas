package it.polimi.ingsw.view.actions.usepowerup;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

import java.awt.*;

/**
 * teleport class
 */
public class TeleporterAction extends PowerUpAction {

    private final Point cell;

    public TeleporterAction(Color color, Point cell) {
        super(color, PowerUpType.TELEPORTER);
        this.cell = cell;
    }

    public Point getCell() {
        return cell;
    }
}
