package it.polimi.ingsw.network.serveronly.rmi;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the server interface for rmi
 */
public class RMIServer {

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.INFO;

    //attributes relative to client -> server flow
    /**
     * true if the rmi registry has already been created
     */
    private static Boolean registryCreated = false;
    /**
     * name of the ToServer remote object
     */
    private static final String LOCALNAME = "rmi_server";
    /**
     * rmi server registry port
     */
    private final int serverPort;
    /**
     * rmi local registry
     */
    private Registry local;
    /**
     * local toServer instance
     */
    private ToServerImpl skeleton;

    //attributes relative to server -> client flow
    /**
     * remote rmi registry
     */
    private Registry remote;
    /**
     * rmi registry client port
     */
    private final int clientPort;

    /**
     * Constructor
     */
    public RMIServer() {

        // use the default config ports

        this.serverPort = 22220;
        this.clientPort = 22221;

        // starts the remote objects

        this.createRegistry();

        this.createRemoteObject();

    }

    /**
     * Constructor
     * @param serverPort is the rmi server port
     * @param clientPort is the rmi client port
     */
    public RMIServer(int serverPort, int clientPort) {

        // use the default config ports

        this.serverPort = serverPort;
        this.clientPort = clientPort;

        // starts the remote objects

        this.createRegistry();

        this.createRemoteObject();

    }

    /**
     * this method creates a new Rmi registry on port 2020 if thi was not already created
     */
    private synchronized void createRegistry(){

        try{

            if (!registryCreated ) {

                local = LocateRegistry.createRegistry(serverPort);

                LOGGER.log(level, "[RMI-Server]created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), serverPort});


                if (local == null){
                    LocateRegistry.createRegistry(serverPort);
                    local = LocateRegistry.getRegistry(serverPort);
                }

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

            skeleton = new ToServerImpl(this, clientPort);

            local = LocateRegistry.getRegistry(serverPort);


            LOGGER.log(level, "[RMI-Server]located registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), serverPort});


            local.rebind(LOCALNAME, skeleton); // binding the string localName to hte instance in the registry
            LOGGER.log(level,"[RMI-Server]bound object name to registry");


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }



}
