package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.OverMaxPlayerException;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * This class represent the main Server which is the common part shared by both Socket and RMI.
 */
public class Server  {

    /**
     * IP Address of the Server
     */
    private static String ip_address;

    /**
     * Port of the Server
     */
    private static int port;

    /**
     * Reference to controller
     */
    private static Controller controller;

    /**
     * Reference to the WaitingRoom, which gathers user before game starts
     */
    private static WaitingRoom waitingRoom;

    private static ConcurrentHashMap<Integer,Integer> clients;

    private static AtomicInteger clientsNum = new AtomicInteger(0);

    private final RMIServer rmiServer;

    /**
     * Constructor
     * Initialize the main Server by first creating a WaitingRoom, then getting its local host address and assigning it to
     * the attribute ip_address and finally starting the two type of Server.
     */
    public Server(int port) {
        this.port = port;

        try {
            waitingRoom = new WaitingRoom(-1);  // NOTE: this need to be changed: this can only create a new game but load a saved one

            clients = new ConcurrentHashMap<>();

        }catch (GameNonExistentException e){
            e.printStackTrace();
        }
        try {
            ip_address = Inet4Address.getLocalHost().getHostAddress();
            Logger.getLogger("infoLogging").info("RemoteServer is up and running on ip " + ip_address);
        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer(port));
        socketHandler.start();

        this.rmiServer = new RMIServer();
    }

    /**
     * this function is used to propagate the login and handle the clients registration in the main server
     *
     * @param name is the name chosen by the player
     * @param playerColor is the color chosen by the player
     * @param connNum is the thread they use to communicate (-1 if rmi)
     * @return the id of the player
     * @throws NameAlreadyTakenException
     * @throws ColorAlreadyTakenException
     * @throws OverMaxPlayerException
     */
    public static int addPlayer(String name, PlayerColor playerColor, int connNum) throws NameAlreadyTakenException, ColorAlreadyTakenException, OverMaxPlayerException {

        if (waitingRoom.isActive()) {

            int playerId =  waitingRoom.addPlayer(name,playerColor);

            System.out.println("[DEBUG] Added player w/ id: " + playerId +" and name: " + name + "and color : " + playerColor);

            clients.put(playerId,connNum);

            System.out.println("[DEBUG] bounded player w/ id : " + playerId + "to connNum:" + connNum);

            return playerId;
        }

        return -1;
    }


    /**
     * function that have to be called by the pingers if the client disconnects
     * @param playerId is the id of the player
     */
    public static void removePlayer(int playerId){

        clients.remove(playerId);

        if ((waitingRoom.isActive()) && (WaitingRoom.getTimerCount() > 1)){

            waitingRoom.removePlayer(playerId);
        }

    }

    /**
     *  this function is used to reconnect a player after the game was started w/ the id
     * @param playerId is the id of the player that wants to reconnect
     * @param threadNum is the thread they use to communicate (-1 if rmi)
     */
    public static void reconnect(int playerId, int threadNum) throws GameNonExistentException{

        if (getWaitingRoom().isActive()) throw new GameNonExistentException();

        if(getController().getPlayerName(playerId) != null ){

            clients.put(playerId,threadNum);
        }
    }


    /**
     * this function is used to reconnect a player after the game was started w/ the name
     * @param name is the name of the player that wants to reconnect
     * @param threadNum is the thread they use to communicate (-1 if rmi)
     * @return the playerId
     */
    public static int reconnect(String name, int threadNum) throws GameNonExistentException{

        if (getWaitingRoom().isActive()) throw new GameNonExistentException();

        int playerId = getController().findPlayerByName(name);

        if ( playerId != -1){

            clients.put(playerId,threadNum);

            return playerId;
        }

        return -1;
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
