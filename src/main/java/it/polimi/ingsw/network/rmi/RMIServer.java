package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.network.Server;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    //attributes relative to client -> server flow
    private static Boolean registryCreated = false;
    private static final String LOCALNAME = "rmi_server";
    private Registry local;
    private ToServerImpl skeleton;

    //attributes relative to server -> client flow
    private static ConcurrentHashMap<Integer,String> remoteClientAddress = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer,ToClient> remoteClients = new ConcurrentHashMap<>();
    private Registry remote;


    public RMIServer() {

        this.createRegistry();

        this.createRemoteObject();

    }

    /**
     * this method creates a new Rmi registry on port 2020 if thi was not already created
     */
    private synchronized void createRegistry(){

        try{

            if (!registryCreated ) {

                local = LocateRegistry.createRegistry(2020);

                LOGGER.log(level, "[RMI-Server]created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2020"});


                if (local == null){
                    LocateRegistry.createRegistry(2020);
                    local = LocateRegistry.getRegistry(2020);
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

            skeleton = new ToServerImpl(this);

            local = LocateRegistry.getRegistry(2020);


            LOGGER.log(level, "[RMI-Server]located registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2020"});


            local.rebind(LOCALNAME, skeleton); // binding the string localName to hte instance in the registry
            LOGGER.log(level,"[RMI-Server]bound object name to registry");


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }

    /**
     * This method will add the address of a new client remote registry to the server
     * @param address is the ip address of a new client
     */
    public void addClient(String address, int playerId, String name){

        try{

            // add the client rmi registry address to the server

            remoteClientAddress.put(playerId,address);

            // connect to the remote registry

            remote = LocateRegistry.getRegistry(address, 2021);

            // load the specified ToClient object and puts it in the map

            remoteClients.put(playerId,(ToClient) remote.lookup(name));

            LOGGER.log(Level.INFO, "Registered client with name: {0}", name);

            //starts a thread that pings that client

            new RMIPinger((ToClient) remote.lookup(name)).run();


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }


    public static void removeClient(int playerId){

        // remove the correspondent entry from the address map

        remoteClientAddress.remove(playerId);

        // remove the correspondent entry from the clients map

        remoteClients.remove(playerId);

        // call the method on the main server

        Server.removePlayer(playerId);

    }


    /**
     *
     * @return the number of clients that are bind to the remote server
     */
    public int getClientNumber(){

        return remoteClients.size();
    }

    /**
     * Unbinds all the clients previously bound if the client rmi registry is local
     * function for test uses only
     */
    public void resetClients(){

        remoteClients.clear();

    }



}
