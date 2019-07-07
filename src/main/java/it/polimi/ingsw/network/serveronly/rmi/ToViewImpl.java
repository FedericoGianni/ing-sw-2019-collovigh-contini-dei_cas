package it.polimi.ingsw.network.serveronly.rmi;

import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.network.rmi.ToClient;
import it.polimi.ingsw.network.serveronly.Server;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the ToView for the rmi
 */
public class ToViewImpl implements ToView {

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.FINE;

    //real attributes

    /**
     * is the ToClient object
     */
    private final ToClient client;
    /**
     * is the address of the client
     */
    private final String ipAddress;
    /**
     * is the remote name of the client
     */
    private final String remoteName;
    /**
     * is the id of the client
     */
    private int playerId;

    /**
     * Constructor
     * @param ipAddress is the address of the client
     * @param remoteName is the remote name of the client
     * @param client is the ToClient object
     */
    public ToViewImpl(String ipAddress, String remoteName, ToClient client ) {
        this.client = client;
        this.ipAddress = ipAddress;
        this.remoteName = remoteName;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getRemoteName() {
        return remoteName;
    }

    // Updates

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUpdate(UpdateClass update) {

        try {

            client.sendUpdate(update);

        } catch (RemoteException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);

            Server.removePlayer(playerId);
        }

    }


    // Game Handling

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn() {

        try {

            LOGGER.log(level, "[RMI-ToView] forwarding startSpawn()");

            client.startSpawn();

            LOGGER.log(level,"[RMI-SERVER] sent startSpawn to player with address :{0}", ipAddress);

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);

            Server.removePlayer(playerId);

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp() {

        try {

            client.startPowerUp();

            LOGGER.log(level,"[RMI-SERVER] sent startPowerUp to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {

        try {

            client.startAction(isFrenzy, isBeforeFrenzyStarter);

            LOGGER.log(level,"[RMI-SERVER] sent startAction to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyAtomicShoot() {

        try{

            client.reDoFrenzyAtomicShoot();

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyReload() {

        try{

            client.doFrenzyReload();

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {

        try {

            client.startReload();

            LOGGER.log(level,"[RMI-SERVER] sent startReload to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {

        try {

            client.askGrenade();

            LOGGER.log(level,"[RMI-SERVER] sent useGrenade to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {

        try {

            client.startGame();

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(String s) {

        try {

            client.show(s);

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);
            Server.removePlayer(playerId);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {

        try {

            client.endGame();

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING,e.getMessage(),e);
            Server.removePlayer(playerId);

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {

        try{

            client.close();

        } catch (RemoteException e){

            LOGGER.log(Level.WARNING,e.getMessage(),e);
            Server.removePlayer(playerId);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> askMapAndSkulls() {
        try{

            return  client.askMapAndSkulls();

        } catch (RemoteException e){

            LOGGER.log(Level.WARNING,e.getMessage(),e);
            Server.removePlayer(playerId);
        }

        return null;
    }
}
