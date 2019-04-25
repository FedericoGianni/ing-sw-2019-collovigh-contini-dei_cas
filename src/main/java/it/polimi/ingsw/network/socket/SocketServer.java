package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class SocketServer implements Runnable {

    private static final int DEFAULT_SOCKET_PORT = 22222;
    public static final int DEFAULT_MAX_CLIENTS = 5;
    
    private ServerSocket serverSocket;
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_MAX_CLIENTS);

    @Override
    public void run() {
        Logger.getLogger("infoLogger").info("Starting SocketServer...");
        try {
            this.serverSocket = new ServerSocket(DEFAULT_SOCKET_PORT, 0, Inet4Address.getLocalHost());
            Logger.getLogger("infoLogger").info("SocketServer: ready at " + Server.getIp_address() + " port " + serverSocket.getLocalPort());
        } catch(IOException e){
            e.getMessage();
        }

        while(true){
            try {
                Socket socket = serverSocket.accept();
                Logger.getLogger("infoLogger").info("Received client connection by " + socket.getLocalAddress());
                Server.setClientsNum(Server.getClientsNum() + 1);
                Logger.getLogger("infoLogger").info("ClientsNum = " + Server.getClientsNum());

                if(Server.getClientsNum() < DEFAULT_MAX_CLIENTS) {
                    executor.execute(new SocketConnection(socket));
                    Logger.getLogger("infoLogger").info("Socket thread id: " + executor.getActiveCount());

                }
            } catch(IOException e){
                e.getMessage();
            }
        }
    }
}
