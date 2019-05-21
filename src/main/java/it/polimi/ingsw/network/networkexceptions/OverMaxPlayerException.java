package it.polimi.ingsw.network.networkexceptions;

public class OverMaxPlayerException extends Exception {

    @Override
    public String getMessage() {
        return "Attempted login but players were already max";
    }
}
