package it.polimi.ingsw.view.actions.usepowerup;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.powerup.PowerUpType;

import java.awt.*;

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
