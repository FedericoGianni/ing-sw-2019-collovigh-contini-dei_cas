package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * This thread, started by the main Server, handles a SocketServer that keep listening for connection on the
 * specified port, and open a new socket stream for every client.
 */
public class SocketServer extends Thread {

    @Override
    public void run() {
        Logger.getLogger("infoLogger").info("Starting SocketServer");
        try (ServerSocket serverSocket = new ServerSocket(22222)) {
            while (true) {
                new SocketConnectionReader(serverSocket.accept()).start();
            }
        } catch (
                IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }
}