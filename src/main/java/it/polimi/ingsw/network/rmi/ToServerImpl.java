package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.OverMaxPlayerException;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToServerImpl implements ToServer{

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private final RMIServer server;
    private int playerId;

    public ToServerImpl(RMIServer server) throws RemoteException{

        this.server = server;

        UnicastRemoteObject.exportObject(this,0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int joinGame(String name, PlayerColor color) throws RemoteException {

        try {

            LOGGER.log(level,"[RMI-Server] adding new player w/ name: {0}", name);

            playerId = Server.getWaitingRoom().addPlayer(name, color);

            return playerId;

        }catch (NameAlreadyTakenException e){
            LOGGER.log(Level.WARNING,"[RMI-Server]Attempted login with name: " +e.getName() + "but name was already used", e);
        }catch (ColorAlreadyTakenException e){
            LOGGER.log(Level.WARNING,"[RMI-Server]Attempted login with color: " +e.getColor() + "but color was already used", e);
        }catch (OverMaxPlayerException e){
            LOGGER.log(Level.WARNING,"[RMI-Server]Attempted login but players were already max");
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
    public void registerMe(String address, int playerId, String name) throws RemoteException {


        server.addClient(address, playerId, name);

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
    public int reconnect(String name) throws GameNonExistentException{

        //return Server.reconnect(name, null);
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
    public void useNewton(Color color, int playerId, Directions directions, int amount) {

    }

    @Override
    public void useTeleport(Color color, int r, int c) {

    }

    @Override
    public void useMarker(Color color, int playerId) {

    }


}
