package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    //attributes relative to client -> server flow
    private static final String LOCALNAME = "rmi_server";
    private Registry local;
    private ToServerImpl skeleton;

    //attributes relative to server -> client flow
    private static final String REMOTENAME = "rmi_client";
    private static Registry remote;
    private String host;


    public RMIServer() {


        this.createRemoteObject();

    }

    private void createRemoteObject(){

        try {

            skeleton = new ToServerImpl(this);


            LocateRegistry.createRegistry(2020);

            local = LocateRegistry.getRegistry(2020);

            if (local == null){
                LocateRegistry.createRegistry(2020);
                local = LocateRegistry.getRegistry(2020);
            }

            LOGGER.log(level, "[RMI-Server]created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2020"});


            local.bind(LOCALNAME, skeleton); // binding the string localName to hte instance in the registry
            LOGGER.log(level,"[RMI-Server]bound object name to registry");


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }



    public void shutDown(){

        try {

            local.unbind(LOCALNAME);

            UnicastRemoteObject.unexportObject(skeleton, true);

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }
    }


}
