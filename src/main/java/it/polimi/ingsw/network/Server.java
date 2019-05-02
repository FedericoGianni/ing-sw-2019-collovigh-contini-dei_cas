package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Logger;


public class Server  {

    /**
     * IP Address of the Server
     */
    private static String ip_address;

    /**
     * Reference to controller
     */
    private static Controller controller;

    private static WaitingRoom waitingRoom;

    /**
     * Number of clients connected to the main Server, both RMI or socket
     */
    private static int clientsNum = 0;

    public Server() {
        //controller = null
        try {
            waitingRoom = new WaitingRoom(-1);  // NOTE: this need to be changed: this can only create a new game but load a saved one

        }catch (GameNonExistentException e){

            e.printStackTrace();
        }
        try{
            ip_address = Inet4Address.getLocalHost().getHostAddress();
            Logger.getLogger("infoLogging").info("Server is up and running on ip " + ip_address);
        } catch(UnknownHostException e){
            e.getMessage();
        }

        Thread socketHandler = new Thread(new SocketServer());
        socketHandler.start();

        Thread rmiHandler = new Thread(new RMIServer());
        rmiHandler.start();
    }

    public static int getClientsNum() {
        return clientsNum;
    }

    public static void setClientsNum(int c) {
        clientsNum = c;
    }

    public static String getIp_address() {
        return ip_address;
    }

    public static Controller getController() {
        return controller;
    }



    public static WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

}
