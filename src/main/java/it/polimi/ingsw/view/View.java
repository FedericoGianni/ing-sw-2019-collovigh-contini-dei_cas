package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.cachemodel.CacheModel;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

public class View implements ViewInterface {

    private UserInterface userInterface;
    private int playerId;
    private Client clientToVView;
    private CacheModel cacheModel = null;

    public View(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setVirtualView(Client clientToVView) {
        this.clientToVView = clientToVView;
    }

    //methods started from virtual view -> view
    @Override
    public void startSpawn(){
        userInterface.startSpawn();
    }

    @Override
    public void startPowerUp() {
       userInterface.startPowerUp();
    }

    @Override
    public void startAction() {
        userInterface.startAction();
    }

    @Override
    public void startReload() {
        userInterface.startReload();
    }

    //methods to be forwarded from view -> virtual view throught network
    //use clientToView which is an abstract class implemented both by rmi/socket in the client->server flow
    @Override
    public void spawn(CachedPowerUp powerUp) {
        clientToVView.spawn(powerUp);
    }

    @Override
    public void useNewton(Color color, int playerId, Directions directions, int amount) {
        clientToVView.useNewton(color, playerId, directions, amount);
    }

    @Override
    public void useTeleport(Color color, int r, int c) {
        clientToVView.useTeleport(color, r, c);
    }

    @Override
    public void useMarker(Color color, int playerId) {
        clientToVView.useMarker(color, playerId);
    }


    //why view of client needs this?
    @Override
    public void sendUpdates(UpdateClass update) {
        CacheModel.update(update);
    }




}
