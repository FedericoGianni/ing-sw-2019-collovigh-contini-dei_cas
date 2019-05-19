package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ToClientImpl implements ToClient {

    private final RMIClient client;

    public ToClientImpl(RMIClient client) throws RemoteException {

        this.client = client;

        UnicastRemoteObject.exportObject(this, 0);

    }


    // game startup functions

    /**
     * {@inheritDoc}
     */
    @Override
    public void NameAlreadyTaken(String name) throws RemoteException{


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ColorAlreadyTaken(PlayerColor color) throws RemoteException{

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean ping() throws RemoteException {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPid() throws RemoteException {

        return client.getPlayerId();
    }

    /**
     * @param gameId is the id of the game
     * @throws RemoteException
     */
    @Override
    public void initGame(int gameId) throws RemoteException {

        client.setGameId(gameId);
    }




    // methods from ToView interface ( game handling)

    /**
     * This methods starts the spawn Phase on the client ( 0)
     */
    @Override
    public void startSpawn() {

    }

    /**
     * This method starts the "use power Up" phase ( 1, 3, 5)
     */
    @Override
    public void startPowerUp() {

    }

    /**
     * This method starts the action phase on the client ( 2, 4)
     */
    @Override
    public void startAction() {

    }

    /**
     * This method starts the reload phase of the turn ( 6)
     */
    @Override
    public void startReload() {

    }

    /**
     * This method will be called on a player if he/she was shot in the previous phase and has grenades
     */
    @Override
    public void useGrenade() {

    }
}
