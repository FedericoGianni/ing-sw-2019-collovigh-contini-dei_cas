package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.socket.SocketServer;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


public class Server  {

    /**
     * IP Address of the RemoteServer
     */
    private static String ip_address;

    /**
     * Reference to controller
     */
    private static Controller controller;

    /**
     * Reference to the WaitingRoom, which gathers user before game starts
     */
    private static WaitingRoom waitingRoom;

    /**
     * Number of clients connected to the main RemoteServer, both RMI or socket
     */
    private static AtomicInteger clientsNum;


    public Server() {
        //controller = WaitingRoom.getController();
        try {
            waitingRoom = new WaitingRoom(-1);  // NOTE: this need to be changed: this can only create a new game but load a saved one

            //clientsNum initialization (done this way since it is of type AtomicInteger to avoid problems with multi-threading
            //clientsNum.set(0);

        }catch (GameNonExistentException e){
            e.printStackTrace();
        }
        try {
            ip_address = Inet4Address.getLocalHost().getHostAddress();
            Logger.getLogger("infoLogging").info("RemoteServer is up and running on ip " + ip_address);
        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer());
        socketHandler.start();

        //Thread rmiHandler = new Thread(new RMIServer());
        //rmiHandler.start();
    }

    public static AtomicInteger getClientsNum() {
        return clientsNum;
    }

    public static String getIp_address() {
        return ip_address;
    }

    public static Controller getController() {
        return controller;
    }

    public static void setController(Controller controller) {
        Server.controller = controller;
    }

    public static WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

}
