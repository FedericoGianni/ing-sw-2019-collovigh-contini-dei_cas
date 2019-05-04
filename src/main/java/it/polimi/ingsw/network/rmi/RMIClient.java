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
    private static Boolean registryCreated = false;
    private String localName;
    private Registry localRegistry;



    public RMIClient() {

        RMIClient.createRegistry();

        createRemoteObject();

    }

    /**
     * this method creates a new Rmi registry on port 2021 if thi was not already created
     */
    private static synchronized void createRegistry(){

        try{

            if (!registryCreated){

                LocateRegistry.createRegistry(2021);

                registryCreated = true;
            }

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * this method create a new instance of toServerImpl and bind it to the local rmi registry
     */
    private void createRemoteObject(){

        try {

            ToClientImpl skeleton = new ToClientImpl(this);

            localRegistry = LocateRegistry.getRegistry(2021);

            LOGGER.log(level, "[RMI-Client]created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2021"});


            //creation of client name for binding

            int id = localRegistry.list().length;

            localName = "rmi_client" + id;

            //binding name to implementation in registry

            localRegistry.rebind(localName, skeleton); // binding the string localName to hte instance in the registry

            LOGGER.log(level, "[RMI-Client]Bound rmi client to registry");


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }

    /**
     *
     * @param name is the name chosen for the login
     * @param color is the color chosen for the player
     * @return the playerId of the player
     */
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

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return -1;
    }

    /**
     *
     * @param mapType is the map the player wants to choose
     */
    @Override
    public void voteMap(int mapType) {

        try {

            serverIp = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remoteRegistry = LocateRegistry.getRegistry(serverIp,2020);
            LOGGER.log(level,"[RMI-Client] registry located by client");

            ToServer server = (ToServer) remoteRegistry.lookup(REMOTE_OBJECT_NAME);

            server.voteMapType(mapType);

        }catch(Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

    }


}
