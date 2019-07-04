package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.network.rmi.ToClient;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
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
    public void nameAlreadyTaken(String name) throws RemoteException{


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void colorAlreadyTaken(PlayerColor color) throws RemoteException{

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
     * {@inheritDoc}
     */
    @Override
    public void initGame(int gameId) throws RemoteException {

        client.setGameId(gameId);
    }


    //Updates

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUpdate(UpdateClass update) throws RemoteException {

        client.getView().sendUpdates(update);
    }



    // methods from ToView interface ( game handling)

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn() {

        LOGGER.log(level, "[RMI-ToClient] received startSpawn()");
        client.getView().startSpawn();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp() {

        client.getView().startPowerUp();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {

        client.getView().startAction(isFrenzy, isBeforeFrenzyStarter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reDoFrenzyAtomicShoot() {

        client.getView().doFrenzyAtomicShoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyReload() throws RemoteException {

        client.getView().doFrenzyReload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {

        client.getView().startReload();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {

        client.getView().askGrenade();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() throws RemoteException {
        client.getView().startGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(String s) throws RemoteException {
        client.getView().show(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() throws RemoteException {

        client.getView().endGame();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws RemoteException {

        client.getView().close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> askMapAndSkulls() throws RemoteException {
        return client.getView().askMapAndSkulls();
    }
}
