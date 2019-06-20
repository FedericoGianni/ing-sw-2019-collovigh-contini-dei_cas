package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent the main Server which is the common part shared by both Socket and RMI.
 */
public class Server  {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String LOG_START = "[Server] ";

    private static final int MIN_PLAYER_CONNECTED = 3;
    private static final boolean ONLINE_MIN_PLAYER_CHECK_ENABLE = false;

    /**
     * Reference to controller
     */
    private static Controller controller;

    /**
     * Reference to the WaitingRoom, which gathers user before game starts
     */
    private static WaitingRoom waitingRoom;

    private static ConcurrentHashMap<Integer, ToView> clients = new ConcurrentHashMap<>();

    private final RMIServer rmiServer;

    /**
     * Constructor
     * Initialize the main Server by first creating a WaitingRoom, then getting its local host address and assigning it to
     * the attribute ipAddress and finally starting the two type of Server.
     */
    public Server(int socketPort) {

        try {

            waitingRoom = new WaitingRoom(-1);  // TODO this need to be changed: this can only create a new game but load a saved one


        }catch (GameNonExistentException e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        try {

            String ipAddress = Inet4Address.getLocalHost().getHostAddress();

            LOGGER.info(() -> "RemoteServer is up and running on ip " + ipAddress);

        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer(socketPort));
        socketHandler.start();

        this.rmiServer = new RMIServer();
    }

    /**
     * Constructor w/ rmi ports
     *
     * Initialize the main Server by first creating a WaitingRoom, then getting its local host address and assigning it to
     * the attribute ipAddress and finally starting the two type of Server.
     *
     * @param clientPort is the port for the client rmi registry
     * @param serverPort is the port for the server rmi registry
     *
     * @param socketPort is the port used for creating the socket server
     */
    public Server(int socketPort,  int serverPort, int clientPort) {


        try {

            waitingRoom = new WaitingRoom(-1);  // NOTE: this need to be changed: this can only create a new game but load a saved one



        }catch (GameNonExistentException e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
        try {

            String ipAddress = Inet4Address.getLocalHost().getHostAddress();

            LOGGER.info(() -> "RemoteServer is up and running on ip " + ipAddress);

        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer(socketPort));
        socketHandler.start();

        this.rmiServer = new RMIServer(serverPort,clientPort);
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

            LOGGER.log(level, ()-> LOG_START + "Added player w/ id: " + playerId +" and name: " + name + " and color : " + playerColor);

            // Puts the new Player in the hashMap

            clients.put(playerId, toView);

            LOGGER.log(level, ()-> LOG_START + "Bounded player w/ id : " + playerId + " and toView: " + toView);

            // notify All Players

            waitingRoom.notifyNewPlayer();

            // Returns the PlayerId


            return playerId;

        }else {

            throw new GameAlreadyStartedException();
        }


    }

    /**
     * This function will be used for reconnection
     *
     * @param name is the name of the player
     * @param toView is the interface to reach him
     *
     * @throws GameNonExistentException if no games are running
     * @return the player id if found or -1
     */

    public static int reconnect(String name, ToView toView) throws GameNonExistentException {

        // if no games are on throws exception

        if (controller == null) throw new GameNonExistentException();

        // find the player id by the given name

        int playerId = controller.findPlayerByName(name);

        // if the player is found

        if (playerId != -1) {

            // puts the player in the clients table

            clients.put(playerId, toView);

            // sets the player online in the controller

            controller.setPlayerOnline(playerId);

            // logs the reconnection

            LOGGER.log(level, () -> LOG_START + "Reconnected player w/ id : " + playerId + " to View: " + toView);

        }

        return playerId;

    }




    /**
     * function that have to be called by the pingers if the client disconnects
     * @param playerId is the id of the player
     */
    public static void removePlayer(int playerId){

        // removes the player

        clients.remove(playerId);

        if ((waitingRoom.isActive()) && (WaitingRoom.getTimerCount() > 1)){

            // LOG the disconnection

            LOGGER.log(level, () -> LOG_START + "Player " + waitingRoom.getName(playerId) + " left the game" ) ;

            // remove the player from the waitingRoom

            waitingRoom.removePlayer(playerId);

            //updates the hashmap

            updateHashMap();

        }else {

            // wait to the the WaitingRoom to start the model

            do{

                LOGGER.log(Level.FINER, () -> LOG_START + "Waiting for waiting room to start the game ");

            }while (waitingRoom.isActive());

            // sets the player to offline

            controller.setPlayerOffline(playerId);

            if ( (ONLINE_MIN_PLAYER_CHECK_ENABLE) && (controller.getPlayerOnline().size() < MIN_PLAYER_CONNECTED) ){

                LOGGER.log(Level.WARNING, () -> LOG_START + "Connected player are less than " + MIN_PLAYER_CONNECTED + " the game will be terminated " );

                controller.endGame();
            }

            // LOG the player disconnection

            LOGGER.log(level, () -> LOG_START + "Player " + playerId + " left the game" ) ;

        }

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

    public RMIServer getRmiServer() {
        return rmiServer;
    }

    private static void updateHashMap(){

        LOGGER.log(level, () -> LOG_START + "hasMap before update: " + clients);

        for (int i = 0; i < clients.size(); i++) {

            if(clients.get(i) == null){


                for (int j = i + 1 ; j < clients.size() + 1 ; j++) {


                    clients.put(j-1, clients.get(j));


                }

                clients.remove(clients.size() - 1);
            }
        }

        LOGGER.log(level, () -> LOG_START + "hasMap after update: " + clients);
    }
}
