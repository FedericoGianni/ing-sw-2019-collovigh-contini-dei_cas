package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToClientImpl implements ToClient {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

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


    //Updates


    @Override
    public void sendUpdate(UpdateClass update) throws RemoteException {

        client.getView().getCacheModel().update(update);
    }



    // methods from ToView interface ( game handling)

    /**
     * This methods starts the spawn Phase on the client ( 0)
     */
    @Override
    public void startSpawn() {

        LOGGER.log(level, "[RMI-ToClient] received startSpawn()");
        client.getView().startSpawn();

    }

    /**
     * This method starts the "use power Up" phase ( 1, 3, 5)
     */
    @Override
    public void startPowerUp() {

        client.getView().startPowerUp();

    }

    /**
     * This method starts the action phase on the client ( 2, 4)
     */
    @Override
    public void startAction() {

        client.getView().startAction();

    }

    /**
     * This method starts the reload phase of the turn ( 6)
     */
    @Override
    public void startReload() {

        client.getView().startReload();

    }

    /**
     * This method will be called on a player if he/she was shot in the previous phase and has grenades
     */
    @Override
    public void useGrenade() {

        client.getView().askGrenade();

    }

    @Override
    public void startGame() throws RemoteException {
        client.getView().startGame(); //TODO check if it's ok @Dav (by Fede)
    }
}
