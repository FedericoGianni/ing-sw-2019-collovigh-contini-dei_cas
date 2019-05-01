package it.polimi.ingsw.network.networkexceptions;

public class NameAlreadyTakenException extends Exception {

    private final String name;

    public NameAlreadyTakenException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
