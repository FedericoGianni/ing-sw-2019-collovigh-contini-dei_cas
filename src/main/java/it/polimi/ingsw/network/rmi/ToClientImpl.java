package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ToClientImpl implements ToClient {

    private final RMIClient client;

    public ToClientImpl(RMIClient client) throws RemoteException {

        this.client = client;

        UnicastRemoteObject.exportObject(this, 0);

    }

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

    /**
     * @param phaseNum is the phase number
     *                 0 -> Spawn
     *                 1 -> powerUp 1
     *                 2 -> action 1
     *                 3 -> powerUp 2
     *                 4 -> action 2
     *                 5 -> powerUp 3
     *                 6 -> reload
     * @throws RemoteException
     */
    @Override
    public void startPhase(int phaseNum) throws RemoteException {



    }

    /**
     * @return the grenade that will be used or null if not
     * @throws RemoteException
     */
    @Override
    public CachedPowerUp useGranade() throws RemoteException {

        return null;
    }


}
