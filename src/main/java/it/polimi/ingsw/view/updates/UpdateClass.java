package it.polimi.ingsw.view.updates;


import java.io.Serializable;

public abstract class UpdateClass implements Serializable {

    private final int playerId; //Player whom the stats belong
    private final UpdateType type;



    public UpdateClass(UpdateType type, int playerId) {
        this.type = type;
        this.playerId = playerId;
    }

    public UpdateClass(UpdateType type) {
        this.type = type;
        this.playerId = -1;
    }

    public UpdateType getType() {
        return type;
    }

    public int getPlayerId() {
        return playerId;
    }
}
