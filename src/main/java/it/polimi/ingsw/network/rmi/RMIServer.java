package it.polimi.ingsw.network.rmi;



import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RMIServer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    //attributes relative to client -> server flow
    private static Boolean registryCreated = false;
    private static final String LOCALNAME = "rmi_server";
    private Registry local;
    private ToServerImpl skeleton;

    //attributes relative to server -> client flow
    private static List<ToClient> remoteClients = new ArrayList<>();
    private static Registry remote;
    private static String host;


    public RMIServer() {

        RMIServer.createRegistry();

        this.createRemoteObject();

    }

    /**
     * this method creates a new Rmi registry on port 2020 if thi was not already created
     */
    private static synchronized void createRegistry(){

        try{

            if (!registryCreated ) {

                Registry local = LocateRegistry.createRegistry(2020);

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
     * this method updates the list of names bound to ToClient objects
     */
    public static void updateClientList(){

        try {

            host = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remote = LocateRegistry.getRegistry(host,2021);

            remoteClients.clear();

            List<String> n = Arrays.stream(remote.list()).sorted().collect(Collectors.toList());

            for (String name: n){

                remoteClients.add((ToClient) remote.lookup(name));
            }


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     *
     * @return the number of clients that are bind to the remote server
     */
    public int getClientNumber(){

        return remoteClients.size();
    }

    /**
     * Unbinds all the clients previously bound
     */
    public static void resetClients(){

        try {

            host = Inet4Address.getLocalHost().getHostAddress();  // to remove when full Network

            remote = LocateRegistry.getRegistry(host,2021);

            List<String> oldNames = Arrays.asList(remote.list());

            for (String name : oldNames){

                remote.unbind(name);
            }

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * this methods starts one thread of RMIPinger for each client connected and start it
     */
    public void pingAll(){

        for (ToClient client:remoteClients){

            new RMIPinger(client).run();

        }

        
    }



}
