package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.GUI.Gui;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CacheModel;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class View implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private UserInterface userInterface;
    private int playerId;
    private Client clientToVView;
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
            this.userInterface.startUI();
        }

        if (ui.equals("-gui")) {

            Gui gui = new Gui();
            gui.setView(this);
            this.userInterface = gui;
            new Thread(() -> {
                this.userInterface.startUI();
            }).start();
            //this.userInterface.startUI();


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
            new Thread(() -> {
                this.userInterface.startUI();
            }).start();
            //this.userInterface.startUI();


            //GuiMap guiMap = new GuiMap();
            //new Thread( () -> {guiMap.startUI();}).start();
            //GuiLobby guiLobby = new GuiLobby();
            //new Thread( () -> {guiLobby.startUI();}).start();
        }

    }

    /**
     *  create a new client for handling connection to server
     * @param type
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
            try {
                sleep(1000);
            } catch (InterruptedException e){

            }
            clientToVView = sc.getScw();
        }

        userInterface.login();
    }

    public void retryLogin(String error){

        userInterface.retryLogin(error);
    }

    public void show(String msg){
        //new Thread( () -> {userInterface.show(msg);}).start();
        userInterface.show(msg);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setVirtualView(Client clientToVView) {
        this.clientToVView = clientToVView;
    }

    public void joinGame(String name, PlayerColor color) {
        clientToVView.joinGame(name, color);
    }

    public Client getClientToVView() {
        return clientToVView;
    }

    //methods started from virtual view -> view


    @Override
    public void startSpawn(){
        userInterface.startSpawn();
    }

    @Override
    public void startPowerUp() {
       userInterface.startPowerUp();
    }

    @Override
    public void startAction() {
        userInterface.startAction();
    }

    @Override
    public void startReload() {
        userInterface.startReload();
    }

    @Override
    public void askGrenade() {
        userInterface.askGrenade();
    }

    @Override
    public void doAction(JsonAction jsonAction) {
        clientToVView.doAction(jsonAction);
    }


    @Override
    public void startGame() {
        userInterface.startGame();
    }



    //methods to be forwarded from view -> virtual view throught network
    //use clientToView which is an abstract class implemented both by rmi/socket in the client->server flow



    @Override
    public void spawn(CachedPowerUp powerUp) {

        LOGGER.log(level, "[View] send spawn command to server with PowerUp: {0}", powerUp);
        clientToVView.spawn(powerUp);

    }

    @Override
    public void useMarker(Color color, int playerId) {
        clientToVView.useMarker(color, playerId);
    }

    @Override
    public boolean askMoveValid(int row, int column, Directions direction) {

        return clientToVView.askMoveValid(row, column, direction);
    }

    @Override
    public void setValidMove(boolean b) {
        userInterface.setValidMove(b);
    }

    //Update flow
    //Update fl


    public CacheModel getCacheModel() {
        return cacheModel;
    }


    @Override
    public void sendUpdates(UpdateClass update) {
        // LOG the update
        LOGGER.log(level, "[VIEW] recived UpdateClass type: {0}", update.getType());

        // forward it to the CacheModel
        cacheModel.update(update);
    }




}
