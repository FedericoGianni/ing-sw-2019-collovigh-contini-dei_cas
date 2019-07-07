package it.polimi.ingsw.network.serveronly.rmi;


import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.network.rmi.ToClient;
import it.polimi.ingsw.network.rmi.ToServer;
import it.polimi.ingsw.network.serveronly.Server;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class extends ToServer and will be loaded on the RMI registry on the server side
 */
public class ToServerImpl implements ToServer {

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.FINE;
    /**
     * rmi server reference
     */
    private final RMIServer server;
    /**
     * rmi client registry port
     */
    private final int rmiClientPort;
    /**
     * client player id
     */
    private int playerId;
    /**
     * client rmi registry
     */
    private Registry remote;

    /**
     * Constructor
     * @param server is the RmiServer class
     * @param rmiClientPort is the rmi client registry port
     * @throws RemoteException
     */
    public ToServerImpl(RMIServer server, int rmiClientPort) throws RemoteException{

        this.server = server;
        this.rmiClientPort = rmiClientPort;

        UnicastRemoteObject.exportObject(this,0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int joinGame(String address, String remoteName, String name, PlayerColor color) throws RemoteException, NameAlreadyTakenException, ColorAlreadyTakenException, OverMaxPlayerException, GameAlreadyStartedException {

        try {

            // connect to the remote registry

            LOGGER.log(level,() ->"[ToServer] ip: " + address);
            LOGGER.log(level,() ->"[ToServer] port " + rmiClientPort);

            remote = LocateRegistry.getRegistry(address, rmiClientPort);

            // create the Remote Object

            ToClient client = (ToClient) remote.lookup(remoteName);

            // register the client in the waitingRoom and gets the id

            ToViewImpl toView = new ToViewImpl(address, remoteName, client);

            playerId = Server.addPlayer(name, color, toView);

            toView.setPlayerId(playerId);

            // starts the pinger

            new Thread(new RMIPinger(client, playerId)).start();

            // LOG the action

            LOGGER.log(level,"[RMI-Server] adding new player w/ name: {0}", name);

            // Return the id

            return playerId;

        }catch (NotBoundException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void voteMapType(int mapType) throws RemoteException {

        Server.getWaitingRoom().setMapType(mapType);

        LOGGER.log(level, "A player voted for map: {0} ", mapType );

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
    public int reconnect(String name, String address, String remoteName) throws RemoteException, GameNonExistentException{

        try {

            // connect to the remote registry

            remote = LocateRegistry.getRegistry(address, rmiClientPort);

            // create the Remote Object

            ToClient client = (ToClient) remote.lookup(remoteName);

            // register the client in the Server and gets the id

            ToViewImpl toView = new ToViewImpl(address, remoteName, client);

            playerId = Server.reconnect(name, toView);

            toView.setPlayerId(playerId);

            // starts the pinger

            new Thread(new RMIPinger(client,playerId)).start();

            // Return the id

            return playerId;

        }catch (NotBoundException  e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spawn(CachedPowerUp powerUp) {

        Server
                .getController()
                .getVirtualView(playerId)
                .spawn(powerUp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAction(JsonAction jsonAction) throws RemoteException {

        Server.getController().doAction(jsonAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean askMoveValid(int row, int column, Directions direction) throws RemoteException {

        return Server.getController().getVirtualView(playerId).askMoveValid(row,column,direction);

    }


}
