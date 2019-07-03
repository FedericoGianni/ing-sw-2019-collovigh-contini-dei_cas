package it.polimi.ingsw.network.networkexceptions;

import it.polimi.ingsw.utils.PlayerColor;

public class ColorAlreadyTakenException extends Exception{

    private final PlayerColor color;

    public ColorAlreadyTakenException(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }

    @Override
    public String getMessage() {
        return "Attempted login with color: " + color + "but color was already used";
    }
}
