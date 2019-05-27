package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent the main Server which is the common part shared by both Socket and RMI.
 */
public class Server  {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

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

    private static LinkedHashMap<Integer, ToView> clients;

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

            clients = new LinkedHashMap<>();

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
     * @param toView is the thread they use to communicate (-1 if rmi)
     * @return the id of the player
     * @throws NameAlreadyTakenException
     * @throws ColorAlreadyTakenException
     * @throws OverMaxPlayerException
     */
    public static int addPlayer(String name, PlayerColor playerColor, ToView toView) throws NameAlreadyTakenException, ColorAlreadyTakenException, OverMaxPlayerException, GameAlreadyStartedException {

        if (waitingRoom.isActive()) {

            // Adds the player in the Waiting Room

            int playerId =  waitingRoom.addPlayer(name,playerColor);

            System.out.println("[DEBUG] Added player w/ id: " + playerId +" and name: " + name + " and color : " + playerColor);

            // Puts the new Player in the hashMap

            clients.put(playerId, toView);

            System.out.println("[DEBUG] bounded player w/ id : " + playerId + " and toView: " + toView);

            // notify All Players

            waitingRoom.notifyNewPlayer();

            // Returns the PlayerId

            //updateHashMap();

            return playerId;

        }else {

            throw new GameAlreadyStartedException();
        }


    }

    /**
     * This function is used to add players to the HashMap but without forwarding anything to the WaitingRoom
     *
     * Will be also used for reconnections
     * @param name is the name of the player
     * @param toView is the interface to reach him
     */

    public static int reconnect(String name, ToView toView){

        int playerId = controller.findPlayerByName(name);

        clients.put(playerId,toView);

        String message = "[DEBUG] reconnected player w/ id :" + playerId + "to View: " +toView;

        LOGGER.log(level,message);

        return playerId;

    }




    /**
     * function that have to be called by the pingers if the client disconnects
     * @param playerId is the id of the player
     */
    public static void removePlayer(int playerId){


        System.out.println("SERVER DEBUG removePlayer hashmap BEFORE removePlayer");
        for (ToView client : clients.values()){
            System.out.println("Client: " + client.toString());
        }
        clients.remove(playerId);

        if ((waitingRoom.isActive()) && (WaitingRoom.getTimerCount() > 1)){

            // LOG the disconnection

            LOGGER.log(level, "Player {0} left the game", waitingRoom.getName(playerId));

            // remove the player from the waitingRoom

            waitingRoom.removePlayer(playerId);

            System.out.println("SERVER DEBUG removePlayer hashmap AFTER removePlayer");
            for (ToView client : clients.values()){
                System.out.println("Client: " + client.toString());
            }

            //updates the hashmap

            updateHashMap();

        }else {

            // wait to the the WaitingRoom to start the model

            do{

            }while (waitingRoom.isActive());

            // sets the player to offline

            controller.setPlayerOffline(playerId);

            // LOG the player disconnection

            LOGGER.log(level, "Player {0} left the game", playerId);

        }

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

    public static ToView getClient(int id) {
        return clients.get(id);
    }

    public static ConcurrentMap<Integer,ToView> getClients() {
        return new ConcurrentHashMap<>(clients);
    }

    private static void updateHashMap(){

        System.out.println("[SERVER] DEBUG updatehashMap. previous map: ");

        for (int i = 0; i < clients.size() +1 ; i++) {
            System.out.println("Client: " + i + " " + clients.get(i));
        }

        for (int i = 0; i < clients.size(); i++) {


            if(clients.get(i) == null){


                for (int j = i + 1 ; j < clients.size() + 1 ; j++) {


                    clients.put(j-1, clients.get(j));


                }
            }
        }

        clients.remove(clients.size() - 1);

        System.out.println("[SERVER] DEBUG new map: ");
        for (int i = 0; i < clients.size() +1 ; i++) {
            System.out.println("Client: " + i + " " + clients.get(i));
        }
    }
}
