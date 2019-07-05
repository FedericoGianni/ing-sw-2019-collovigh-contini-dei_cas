package it.polimi.ingsw.network.serveronly;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.network.serveronly.Socket.SocketServer;
import it.polimi.ingsw.network.serveronly.rmi.RMIServer;
import it.polimi.ingsw.utils.PlayerColor;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent the main Server which is the common part shared by both Socket and RMI.
 */
public class Server  {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private static final String LOG_START = "[Server] ";

    private static final boolean ONLINE_MIN_PLAYER_CHECK_ENABLE = true;

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

            String ipAddress = Inet4Address.getLocalHost().getHostAddress();

            LOGGER.info(() -> "RemoteServer is up and running on ip " + ipAddress);

        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer(socketPort));
        socketHandler.start();

        this.rmiServer = new RMIServer();

        startGame();
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

            String ipAddress = Inet4Address.getLocalHost().getHostAddress();

            LOGGER.info(() -> "RemoteServer is up and running on ip " + ipAddress);

        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer(socketPort));
        socketHandler.start();

        this.rmiServer = new RMIServer(serverPort,clientPort);

        startGame();
    }

    private static void startGame(){

        int gameId = Parser.readConfigFile().getGame();

        String message = "[Server] read gameId: " + gameId;

        LOGGER.log(level,() -> message);

        if (gameId == -1){

            try {

                waitingRoom = new WaitingRoom();

            }catch (GameNonExistentException e){

                LOGGER.log(Level.WARNING, e.getMessage(), e);

            }

        }else{

            if(Parser.containsGame(gameId)){

                Parser.setCurrentGame(gameId);

                controller = Parser.readController();

                //currentGame, playrs, mappa

                Map map = new Map(Parser.readSavedMap().getRealMap(), Parser.readSavedMap().getMapType());

                List<Player> players = Parser.readPlayers();

                CurrentGame currentGame = new CurrentGame(Parser.readCurrentGame(), players, map);

                Model.setGame(currentGame);


                //RE-INITIALIZE OBSERVERS

                for (Player p : players) {
                    p.getStats().initObservers();
                }

                //restart the game flow

                controller.handleTurnPhase();


            } else {

                LOGGER.log(Level.WARNING, LOG_START + " game id not found. ");

            }
        }
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

        System.out.println("playerId del player: " + name + " ID: " + playerId);

        // if the player is found

        if (playerId != -1) {

            // puts the player in the clients table

            clients.put(playerId, toView);

            System.out.println("DEBUG clients.put" + playerId +" "+ toView);

            // sets the player online in the controller

            controller.setPlayerOnline(playerId);

            System.out.println("DEBUG controller.setPlayerOnlnie del polayer id " + playerId);

            // logs the reconnection

            LOGGER.log(level, () -> LOG_START + "Reconnected player w/ id : " + playerId + " to View: " + toView);

        }
        controller.getVirtualView(playerId).show("reconnectok");
        System.out.println("recconection sent");
        return playerId;

    }




    /**
     * function that have to be called by the pingers if the client disconnects
     * @param playerId is the id of the player
     */
    public static void removePlayer(int playerId){

        // removes the player

        clients.remove(playerId);

        if ((waitingRoom.isActive()) && (waitingRoom.getTimerCount() > 1)){

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

            LOGGER.log(Level.WARNING, "[DEBUG] playerOnline size: {0}",  controller.getPlayerOnline().size());

            if ( (ONLINE_MIN_PLAYER_CHECK_ENABLE) && (controller.getPlayerOnline().size() < WaitingRoom.DEFAULT_MIN_PLAYERS) ){

                LOGGER.log(Level.WARNING, () -> LOG_START + "Connected player are less than " + WaitingRoom.DEFAULT_MIN_PLAYERS + " the game will be TERMINATED " );

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
