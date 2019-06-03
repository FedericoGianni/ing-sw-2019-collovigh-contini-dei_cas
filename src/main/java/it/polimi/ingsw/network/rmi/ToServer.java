package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToServer extends Remote {

    /**
     *
     * @param name is the name chosen by the player
     * @param color is the color chosen by the player
     * @return the player id or -1 if the login was unsuccessful
     * @throws RemoteException
     */
    int joinGame(String address, String remoteName, String name, PlayerColor color) throws RemoteException, NameAlreadyTakenException, ColorAlreadyTakenException, OverMaxPlayerException, GameAlreadyStartedException;

    /**
     *
     * @param mapType is the map chosen
     * @throws RemoteException
     */
    void voteMapType(int mapType) throws RemoteException;

    Boolean ping() throws RemoteException;

    /**
     *
     * @param name is the name chosen
     * @return the id assigned to it
     */
    int reconnect(String name, String address, String remoteName) throws RemoteException, GameNonExistentException;


    //SPAWN

     void spawn(CachedPowerUp powerUp) throws RemoteException;

    //POWERUP

     void doAction(JsonAction jsonAction) throws RemoteException;

     boolean askMoveValid(int row, int column, Directions direction) throws RemoteException;


}
