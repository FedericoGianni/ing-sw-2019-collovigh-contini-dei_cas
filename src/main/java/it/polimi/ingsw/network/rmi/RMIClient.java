package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Client;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIClient extends Client {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static final String REMOTE_OBJECT_NAME = "rmi_server";
    private static final String LOCAL_OBJECT_NAME = "rmi_client";

    private Registry remoteRegistry;
    private Registry localRegistry;
    private String host;


    public RMIClient() {

        this.createRemoteObject();

    }

    @Override
    public int joinGame(String name, PlayerColor color) {

        try {

            host = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remoteRegistry = LocateRegistry.getRegistry(host,2020);
            LOGGER.log(Level.FINE,"[RMI] registry located by client");

            ToServerImpl server = (ToServerImpl) remoteRegistry.lookup(REMOTE_OBJECT_NAME);

            int playerId = server.joinGame(name,color);

            this.setPlayerId(playerId);

            return playerId;


        }catch(Exception e){

            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void voteMap(int mapType) {

        try {

            host = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remoteRegistry = LocateRegistry.getRegistry(host,2020);
            LOGGER.log(Level.FINE,"[RMI] registry located by client");

            ToServerImpl server = (ToServerImpl) remoteRegistry.lookup(REMOTE_OBJECT_NAME);

            server.voteMapType(mapType);

        }catch(Exception e){

            e.printStackTrace();
        }

    }

    private void createRemoteObject(){

        try {

            ToClientImpl Skeleton = new ToClientImpl(this);

            LocateRegistry.createRegistry(2020);

            LOGGER.log(Level.FINE, "created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2020"});

            localRegistry = LocateRegistry.getRegistry(2020);

            localRegistry.bind(LOCAL_OBJECT_NAME, Skeleton); // binding the string localName to hte instance in the registry


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }


}
