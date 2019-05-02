package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static final String LOCALNAME = "rmi_server";
    private static final String REMOTENAME = "rmi_client";

    private Registry local;
    private Registry remote;
    private String host;


    public RMIServer() {

        this.createRemoteObject();

    }

    private void createRemoteObject(){

        try {

            ToServerImpl Skeleton = new ToServerImpl(this);

            LocateRegistry.createRegistry(2020);
            LOGGER.log(Level.FINE, "created registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), "2020"});

            local = LocateRegistry.getRegistry(2020);

            local.bind(LOCALNAME, Skeleton); // binding the string localName to hte instance in the registry


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }

    public int joinGame(String name, PlayerColor color){

        try {

            return Server.getWaitingRoom().addPlayer(name, color);

        }catch (NameAlreadyTakenException e){
            LOGGER.log(Level.WARNING,"Attempted login with name: " +e.getName() + "but name was already used", e);
        }catch (ColorAlreadyTakenException e){
            LOGGER.log(Level.WARNING,"Attempted login with color: " +e.getColor() + "but color was already used", e);
        }

        return -1;
    }

    public void voteMap(int mapId){

        Server.getWaitingRoom().setMapType(mapId);
    }
















}
