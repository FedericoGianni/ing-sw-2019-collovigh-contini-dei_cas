package it.polimi.ingsw.view;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.cacheModel.CachedPowerUp;

public class View implements ViewInterface {

    private UserInterface userInterface;
    private int playerId;
    private Client virtualView;

    public View(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void startPhase0() {

       userInterface.startPhase0();

    }

    @Override
    public void spawn(CachedPowerUp powerUp) {



    }


}
