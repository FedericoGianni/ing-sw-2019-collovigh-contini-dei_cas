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
    private static Level level = Level.FINE;

    //attributes relative to client -> server flow
    private static final String REMOTE_OBJECT_NAME = "rmi_server";
    private Registry remoteRegistry;
    private String serverIp;

    //attributes relative to server -> client flow
    private static final String LOCAL_OBJECT_NAME = "rmi_client";
    private Registry localRegistry;


    public RMIClient() {

        createRemoteObject();

    }

    private void createRemoteObject(){

        try {

            ToClientImpl skeleton = new ToClientImpl(this);

            LocateRegistry.createRegistry(2021);

            localRegistry = LocateRegistry.getRegistry(2021);

            LOGGER.log(level, "[RMI-Client]created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2021"});

            localRegistry.bind(LOCAL_OBJECT_NAME, skeleton); // binding the string localName to hte instance in the registry

            LOGGER.log(level, "[RMI-Client]Bound rmi client to registry");


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }

    @Override
    public int joinGame(String name, PlayerColor color) {

        try {

            serverIp = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remoteRegistry = LocateRegistry.getRegistry(serverIp,2020);
            LOGGER.log(level,"[RMI-Client] registry located by client");

            ToServer server = (ToServer) remoteRegistry.lookup(REMOTE_OBJECT_NAME);

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

            serverIp = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remoteRegistry = LocateRegistry.getRegistry(serverIp,2020);
            LOGGER.log(level,"[RMI-Client] registry located by client");

            ToServer server = (ToServer) remoteRegistry.lookup(REMOTE_OBJECT_NAME);

            server.voteMapType(mapType);

        }catch(Exception e){

            e.printStackTrace();
        }

    }



    public void shutDown(){

        try {

            localRegistry.unbind(LOCAL_OBJECT_NAME);

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }
    }


}
