package it.polimi.ingsw.network.networkexceptions;

public class NameAlreadyTakenException extends Exception {

    private final String name;

    public NameAlreadyTakenException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {

        return "Attempted login with name: " + name + "but name was already used";
    }
}
