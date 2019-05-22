package it.polimi.ingsw.network.networkexceptions;

public class GameAlreadyStartedException extends Exception {

    @Override
    public String getMessage() {

        return "Attempted login but game was already started";
    }
}
