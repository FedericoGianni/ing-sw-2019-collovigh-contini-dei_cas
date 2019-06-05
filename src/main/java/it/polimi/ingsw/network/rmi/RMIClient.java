package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIClient extends Client {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    //attributes relative to client -> server flow
    private final String remoteObjectName = "rmi_server";
    private Registry remoteRegistry;
    private final String serverIp;
    private final int serverPort;

    //attributes relative to server -> client flow
    private static Boolean registryCreated = false;
    private String localName;
    private View view;
    private final int clientPort;

    //view

    private static final String DEFAULT_LOGIN_OK_REPLY = "OK";
    private static final String DEFAULT_NAME_ALREADY_TAKEN_REPLY = "NAME_ALREADY_TAKEN";
    private static final String DEFAULT_COLOR_ALREADY_TAKEN_REPLY = "COLOR_ALREADY_TAKEN";
    private static final String DEFAULT_GAME_ALREADY_STARTED_REPLY = "GAME_ALREADY_STARTED";
    private static final String DEFAULT_MAX_PLAYER_READCHED = "MAX_PLAYER_REACHED";



    public RMIClient(String serverIp, View view) {

        this.serverIp = serverIp;
        this.view = view;

        // use default config

        this.serverPort = 22220;
        this.clientPort = 22221;

        // starts the remote objects

        createRegistry();

        createRemoteObject();


    }

    public RMIClient(String serverIp, View view, int serverPort, int clientPort) {

        this.serverIp = serverIp;
        this.view = view;

        // gets configs from json

        this.serverPort = serverPort;

        this.clientPort = clientPort;

        // starts the remote objects

        createRegistry();

        createRemoteObject();


    }


    // Utilities

    /**
     * this method creates a new Rmi registry on port 2021 if thi was not already created
     */
    private synchronized void createRegistry(){

        try{

            if (!RMIClient.registryCreated){

                LocateRegistry.createRegistry(clientPort);

                RMIClient.registryCreated = true;
            }

        }catch (Exception e){

            LOGGER.log(Level.WARNING, "[RMI-Client] registry was already created ");
        }
    }

    /**
     * this method create a new instance of toServerImpl and bind it to the local rmi registry
     */
    private void createRemoteObject(){

        try {

            ToClientImpl skeleton = new ToClientImpl(this);

            Registry localRegistry = LocateRegistry.getRegistry(clientPort);

            LOGGER.log(level, "[RMI-Client]located registry at address: {0}, port:{1}", new Object[]{Inet4Address.getLocalHost().getHostAddress(), clientPort});


            //creation of client name for binding if only one client for each pc all will be 0

            int id = localRegistry.list().length;

            localName = "rmi_client" + id;

            LOGGER.log(level,"created new client with name: {0}", localName);

            //binding name to implementation in registry

            localRegistry.rebind(localName, skeleton); // binding the string localName to hte instance in the registry

            LOGGER.log(level, "[RMI-Client]Bound rmi client to registry");


        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);

        }
    }


    // Getter

    private ToServer getServer(){

        try {

            // locate the server registry

            remoteRegistry = LocateRegistry.getRegistry(serverIp,serverPort);
            LOGGER.log(level,"[RMI-Client] registry located by client");

            //load the ToServer object from the registry

            return  (ToServer) remoteRegistry.lookup(remoteObjectName);

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return null;
    }

    public View getView() {
        return view;
    }



    // connection

    /**
     *
     * @param name is the name chosen for the login
     * @param color is the color chosen for the player
     * @return the playerId of the player
     */
    @Override
    public int joinGame(String name, PlayerColor color) {

        try {

            //load the ToServer object from the registry

            ToServer server = getServer();

            // Get the ip of the local machine

            String localIp = Inet4Address.getLocalHost().getHostAddress();

            // join the game, register the ip of the client rmiRegistry to the server, and store the pid

            int playerId = server.joinGame(localIp,localName,name,color);


            //set the pid parameter of the class

            this.setPlayerId(playerId);
            this.getView().setPlayerId(playerId);

            // temp x gui

            view.show(DEFAULT_LOGIN_OK_REPLY);

            //return the playerId

            return playerId;


        }catch (NameAlreadyTakenException e){

            // LOG the exception

            LOGGER.log(Level.WARNING,"[RMI-Client]Attempted login with name: " +e.getName() + "but name was already used", e);

            view.show(DEFAULT_NAME_ALREADY_TAKEN_REPLY);

            // Retry the login

            view.getUserInterface().retryLogin(e);

        }catch (ColorAlreadyTakenException e){

            // LOG the exception

            LOGGER.log(Level.WARNING,"[RMI-Client]Attempted login with color: " +e.getColor() + "but color was already used", e);

            view.show(DEFAULT_COLOR_ALREADY_TAKEN_REPLY);

            // Retry the login

            view.getUserInterface().retryLogin(e);

        }catch (OverMaxPlayerException e){

            // LOG the exception

            LOGGER.log(Level.WARNING,"[RMI-Client]Attempted login but players were already max");

            view.show(DEFAULT_MAX_PLAYER_READCHED);

            // Retry the login

            view.getUserInterface().retryLogin(e);

        }catch (GameAlreadyStartedException e){

            // LOG the exception

            LOGGER.log(Level.WARNING,"[RMI-Client]Attempted login but game was already started");

            view.show(DEFAULT_GAME_ALREADY_STARTED_REPLY);

            // Retry the login

            view.getUserInterface().retryLogin(e);

        }catch(Exception e){

            // LOG the exception

            LOGGER.log(Level.WARNING, e.getMessage(), e);

            // Retry the login

            view.getUserInterface().retryLogin(e);
        }

        return -1;
    }

    /**
     *
     * @param mapType is the map the player wants to choose
     */
    public void voteMap(int mapType) {

        try {

            remoteRegistry = LocateRegistry.getRegistry(serverIp,serverPort);
            LOGGER.log(level,"[RMI-Client] registry located by client");

            ToServer server = (ToServer) remoteRegistry.lookup(remoteObjectName);

            server.voteMapType(mapType);

        }catch(Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

    }

    /**
     * this method will reconnect the player to the server if the client was shut down
     *
     * @param name is the name chosen
     * @return the player id
     */

    public int reconnect(String name) {

        try {

            //load the ToServer object from the registry

            ToServer server = getServer();

            // Get the ip of the local machine

            String localIp = Inet4Address.getLocalHost().getHostAddress();

            // register the ip of the client rmiRegistry to the server


            return server.reconnect(name, localIp, localName );


        } catch (GameNonExistentException e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);


        } catch (Exception e) {

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return -1;
    }






    // game handling methods

    @Override
    public void spawn(CachedPowerUp powerUp) {

        try {

            //load the ToServer object from the registry

            ToServer server = getServer();

            //calls the method on the ToServer remote object

            server.spawn(powerUp);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING,e.getMessage(),e);
        }
    }

    @Override
    public void useMarker(Color color, int playerId) {
        //TODO forward this function to view to handle useMarker function invocation
    }

    @Override
    public void doAction(JsonAction jsonAction) {

        try {

            ToServer server = getServer();

            server.doAction(jsonAction);

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING,e.getMessage(),e);
        }
    }

    @Override
    public boolean askMoveValid(int row, int column, Directions direction) {

        boolean answer;

        try {

            ToServer server = getServer();

            answer= server.askMoveValid(row,column,direction);

            view.setValidMove(answer);

            return true;

        }catch (RemoteException e){

            LOGGER.log(Level.WARNING,e.getMessage(),e);
        }

        return false;
    }
}
