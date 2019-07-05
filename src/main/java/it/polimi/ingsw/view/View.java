package it.polimi.ingsw.view;

import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.rmi.RMIClient;
import it.polimi.ingsw.network.client.socket.SocketClient;
import it.polimi.ingsw.runner.RunClient;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.GUI.Gui;
import it.polimi.ingsw.view.actions.ActionTypes;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CacheModel;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.utils.Protocol.DEFAULT_LOGIN_OK_REPLY;
import static it.polimi.ingsw.utils.Protocol.DEFAULT_NAME_NOT_FOUND;
import static java.lang.Thread.sleep;

public class View implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    /**
     * Reference to the UserInterface linked to this class
     */
    private UserInterface userInterface;

    /**
     * Player ID of the player linked to this view
     */
    private int playerId = -1;

    /**
     * Reference to the interface Client (which can be both socket or rmi)
     */
    private Client clientToVView;

    /**
     * Reference to the cache Model, a lighter version of the model to store local changes
     */
    private CacheModel cacheModel;

    // for net configuration

    private String serverIp;
    private int port;
    private final int rmiServerPort;
    private final int rmiClientPort;




    // startup methods

    /**
     * @deprecated
     * @param userInterface
     */
    @Deprecated
    public View(UserInterface userInterface) {
        this.userInterface = userInterface;

        this.rmiServerPort = -1;
        this.rmiClientPort = -1;

    }


    public UserInterface getUserInterface() {
        return userInterface;
    }

    /**
     *  new mode
     * @param serverIp ip of the server
     * @param port port of the socket server
     * @param ui -cli for CLI or -gui for GUI
     */
    public View(String serverIp, int port,  String ui) {

        this.cacheModel = new CacheModel(this);
        this.serverIp = serverIp;
        this.port = port;
        this.rmiClientPort = -1;
        this.rmiServerPort = -1;

        if (ui.equals("-cli")){

            this.userInterface = new CLI(this);
            RunClient.setView(this);
            this.userInterface.startUI();
        }

        if (ui.equals("-gui")) {

            Gui gui = new Gui();
            gui.setView(this);
            this.userInterface = gui;
            new Thread(() -> this.userInterface.startUI()).start();


            //GuiMap guiMap = new GuiMap();
            //new Thread( () -> {guiMap.startUI();}).start();
            //GuiLobby guiLobby = new GuiLobby();
            //new Thread( () -> {guiLobby.startUI();}).start();
        }

    }

    /**
     *  constructor variant used if the user specified rmi ports
     *
     * @param serverIp ip of the server
     * @param port port of the socket server
     * @param ui -cli for CLI or -gui for GUI
     * @param rmiServerPort is the port for the rmi registry on the server
     * @param rmiClientPort is the port for the rmi registry on the client
     */
    public View(String serverIp, int port,  String ui , int rmiServerPort, int rmiClientPort) {

        this.cacheModel = new CacheModel(this);
        this.serverIp = serverIp;
        this.port = port;
        this.rmiClientPort = rmiClientPort;
        this.rmiServerPort = rmiServerPort;

        if (ui.equals("-cli")){

            this.userInterface = new CLI(this);
            this.userInterface.startUI();
        }

        if (ui.equals("-gui")) {

            Gui gui = new Gui();
            gui.setView(this);
            this.userInterface = gui;
            new Thread(() -> this.userInterface.startUI()).start();


            //GuiMap guiMap = new GuiMap();
            //new Thread( () -> {guiMap.startUI();}).start();
            //GuiLobby guiLobby = new GuiLobby();
            //new Thread( () -> {guiLobby.startUI();}).start();
        }

    }

    /**
     * creates a new client for handling connection to server
     * @param type protocolType (socket/rmi)
     */
    public void createConnection(ProtocolType type){

        if (type.equals(ProtocolType.RMI)){

            if (rmiServerPort == -1){

                // if the ports are not specified calls the default contructor w/ default ports

                clientToVView = new RMIClient(serverIp, this);

            } else {

                // if the ports are specified calls the correspondent constructor

                clientToVView = new RMIClient(serverIp,this,rmiServerPort,rmiClientPort);

            }

        }

        if (type.equals(ProtocolType.SOCKET)){
            SocketClient sc = new SocketClient(serverIp, port);
            Thread t = new Thread(sc);
            t.start();
            //SocketClientReader scr = new SocketClientReader(serverIp, port);
            //scr.start();

            /*
            try {

                sleep(1000);

            } catch (InterruptedException e){

                LOGGER.log(Level.WARNING, e.getMessage(), e);

            }*/

            while (sc.getScw() == null){

                try {

                    sleep(200);

                } catch (Exception e){

                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            }
            clientToVView = sc.getScw();
        }

        //userInterface.login();
        //TODO uncomment this to have game selection
        userInterface.gameSelection();
    }

    /**
     * Let the user retry login when invalid (i.e. name already taken)
     * @param error message to display to the user
     */
    public void retryLogin(String error){

        userInterface.retryLogin(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(String msg){
        new Thread( () -> userInterface.show(msg)).start();
    }


    /**
     *
     * @return id of the player linked to this view
     */
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setVirtualView(Client clientToVView) {
        this.clientToVView = clientToVView;
    }

    /**
     * Forward a login request to the server, with chosen name and color
     * @param name player name chosen
     * @param color color chosen
     */
    public void joinGame(String name, PlayerColor color) {
        clientToVView.joinGame(name, color);
    }

    public Client getClientToVView() {
        return clientToVView;
    }

    //methods started from virtual view -> view

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn(){
        userInterface.startSpawn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp() {
       userInterface.startPowerUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {
        userInterface.startAction(isFrenzy, isBeforeFrenzyStarter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {
        userInterface.startReload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {
        userInterface.askGrenade();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAction(JsonAction jsonAction) {

        if (jsonAction.getType().equals(ActionTypes.RELOAD)){

            System.out.println("called reload with : " + jsonAction);

        }


        clientToVView.doAction(jsonAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {

        userInterface.endGame();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        userInterface.startGame();
    }



    //methods to be forwarded from view -> virtual view throught network
    //use clientToView which is an abstract class implemented both by rmi/socket in the client->server flow


    /**
     * {@inheritDoc}
     */
    @Override
    public void spawn(CachedPowerUp powerUp) {

        LOGGER.log(level, "[View] send spawn command to server with PowerUp: {0}", powerUp);
        clientToVView.spawn(powerUp);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean askMoveValid(int row, int column, Directions direction) {

        return clientToVView.askMoveValid(row, column, direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyAtomicShoot() {

        userInterface.doFrenzyAtomicShoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyReload() {

        userInterface.doFrenzyReload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidMove(boolean b) {
        userInterface.setValidMove(b);
    }

    //Update flow
    //Update fl


    public CacheModel getCacheModel() {
        return cacheModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUpdates(UpdateClass update) {
        // LOG the update
        LOGGER.log(level, "[VIEW] recived UpdateClass type: {0}", update.getType());

        // forward it to the CacheModel
        cacheModel.update(update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        userInterface.close();
    }

    /**
     * This method reconnect the player to an active Game
     * @param name is the name the player used to log in
     * @return -1 if the name was not found or the player id if found
     */
    public int reconnect(String name){


        playerId = clientToVView.reconnect(name);

        System.out.println("reconnect playerid: " + playerId);

        if (playerId != -1){

            userInterface.show(DEFAULT_LOGIN_OK_REPLY);

        }else {

            userInterface.show(DEFAULT_NAME_NOT_FOUND);
        }

        //TODO check if it works fix by fed for gui reconnect
        //this.setPlayerId(playerId);

        return playerId;
    }

    /**
     *{@inheritDoc}
     */
    public List<Integer> askMapAndSkulls(){

        // pos 0 -> mapType pos 1 -> skull number
        List<Integer> mapAndSkulls = userInterface.askMapAndSkulls();

        return mapAndSkulls;
    }

}
