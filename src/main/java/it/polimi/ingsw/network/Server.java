package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.socket.SocketServer;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * This class represent the main Server which is the common part shared by both Socket and RMI.
 */
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

    /**
     * Constructor
     * Initialize the main Server by first creating a WaitingRoom, then getting its local host address and assigning it to
     * the attribute ip_address and finally starting the two type of Server.
     */
    public Server() {
        //controller = WaitingRoom.getController();
        try {
            waitingRoom = new WaitingRoom(-1);  // NOTE: this need to be changed: this can only create a new game but load a saved one

            //clientsNum initialization (with default constructor initial value is set to 0)
            // AtomicInteger to avoid problems with multi-threading (this value can be accessed from multiple clients at the same time)
            clientsNum = new AtomicInteger();

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

        //TODO start RMI Server
        //Thread rmiHandler = new Thread(new RMIServer());
        //rmiHandler.start();
    }

    /**
     *
     * @return the current number of clients connected to the WaitingRoom
     */
    public static AtomicInteger getClientsNum() {
        return clientsNum;
    }

    /**
     *
     * @return current ip_address of the Server
     */
    public static String getIp_address() {
        return ip_address;
    }

    /**
     *
     * @return a static reference to the controller, used to call Controller methods inside network methods
     */
    public static Controller getController() {
        return controller;
    }

    /**
     * Sets the Controller passed as a parameter to the controller attribute inside this class
     * @param controller Controller to be passed as a parameter
     */
    public static void setController(Controller controller) {
        Server.controller = controller;
    }

    /**
     *
     * @return a reference to the WaitingRoom
     */
    public static WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

}
