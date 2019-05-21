package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.GUI.Gui;
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

    private String serverIp;
    private int port;


    // startup methods

    /**
     * @deprecated
     * @param userInterface
     */
    @Deprecated
    public View(UserInterface userInterface) {
        this.userInterface = userInterface;

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

        if (ui.equals("-cli")){

            this.userInterface = new CLI(this);
            this.userInterface.startUI();
        }

        if (ui.equals("-gui")){
            Gui gui = new Gui();
            this.userInterface = gui;
            gui.setView(this);
            this.userInterface.startUI();
        }


    }

    /**
     *  create a new client for handling connection to server
     * @param type
     */
    public void createConnection(ProtocolType type){

        if (type.equals(ProtocolType.RMI)){

            clientToVView = new RMIClient(serverIp,this);
        }

        if (type.equals(ProtocolType.SOCKET)){

            try {

                SocketClient sc = new SocketClient(serverIp, port);
                Thread t = new Thread(sc);
                t.start();

                sleep(2000);
                clientToVView = sc.getScw();

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        userInterface.login();
    }

    public void retryLogin(String error){

        userInterface.retryLogin(error);
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



    //methods to be forwarded from view -> virtual view throught network
    //use clientToView which is an abstract class implemented both by rmi/socket in the client->server flow



    @Override
    public void spawn(CachedPowerUp powerUp) {

        LOGGER.log(level, "[View] send spawn command to server with PowerUp: {0}", powerUp);
        clientToVView.spawn(powerUp);

    }

    @Override
    public void useNewton(Color color, int playerId, Directions directions, int amount) {
        clientToVView.useNewton(color, playerId, directions, amount);
    }

    @Override
    public void useTeleport(Color color, int r, int c) {
        clientToVView.useTeleport(color, r, c);
    }

    @Override
    public void useMarker(Color color, int playerId) {
        clientToVView.useMarker(color, playerId);
    }



    //Update flow


    public CacheModel getCacheModel() {
        return cacheModel;
    }


    @Override
    public void sendUpdates(UpdateClass update) {

        cacheModel.update(update);
    }




}
