package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToServerImpl implements ToServer{

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private final RMIServer server;
    private final int rmiClientPort;
    private int playerId;

    private Registry remote;

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

            remote = LocateRegistry.getRegistry(address, rmiClientPort);

            // create the Remote Object

            ToClient client = (ToClient) remote.lookup(remoteName);

            // register the client in the waitingRoom and gets the id

            playerId = Server.addPlayer(name, color, new ToViewImpl(address, remoteName, client));

            // starts the pinger

            new Thread(new RMIPinger(client)).start();

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
     * @param name is the name chosen
     * @return the id assigned to it
     */
    @Override
    public int reconnect(String name, String address, String remoteName) throws GameNonExistentException{

        try {

            // connect to the remote registry

            remote = LocateRegistry.getRegistry(address, 2021);

            // create the Remote Object

            ToClient client = (ToClient) remote.lookup(remoteName);

            // register the client in the Server and gets the id

            playerId = Server.reconnect(name, new ToViewImpl(address, remoteName, client));

            // starts the pinger

            RMIPinger pinger = new RMIPinger(client);
            pinger.run();

            // Return the id

            return playerId;

        }catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public void spawn(CachedPowerUp powerUp) {

        Server
                .getController()
                .getVirtualView(playerId)
                .spawn(powerUp);
    }

    @Override
    public void doAction(JsonAction jsonAction) throws RemoteException {

        Server.getController().doAction(jsonAction);
    }

    @Override
    public boolean askMoveValid(int row, int column, Directions direction) throws RemoteException {

        return Server.getController().getVirtualView(playerId).askMoveValid(row,column,direction);

    }


}
