package it.polimi.ingsw.network.networkexceptions;

import it.polimi.ingsw.model.player.PlayerColor;

public class ColorAlreadyTakenException extends Exception{

    private final PlayerColor color;

    public ColorAlreadyTakenException(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }
}
