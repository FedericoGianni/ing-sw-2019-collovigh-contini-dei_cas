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
    private Client virtualView;
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

    @Override
    public void startSpawn(){
        userInterface.startSpawn();
    }

    @Override
    public void startPowerUp() {
       userInterface.startPowerUp();
    }

    @Override
    public void spawn(CachedPowerUp powerUp) {
        //TODO starts this method inside the virtual view passing throught network
    }

    @Override
    public void useNewton(Color color, int playerId, Directions directions, int amount) {
        //TODO starts this method inside the virtual view passing thorught network
    }

    @Override
    public void useTeleport(Color color, int r, int c) {
        //TODO starts this method inside the virtual view passing throught newtork
    }

    @Override
    public void useMarker(Color color, int playerId) {
        //TODO starts this method inside the virtual view passing thorought netowrk
    }

    @Override
    public void startAction() {
        userInterface.startAction();
    }

    @Override
    public void startReload() {
        userInterface.startReload();
    }

    //why view of client needs this?
    @Override
    public void sendUpdates(UpdateClass update) {
        CacheModel.update(update);
    }




}
