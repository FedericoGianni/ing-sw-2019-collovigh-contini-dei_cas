package it.polimi.ingsw.network.serveronly.Socket;

import it.polimi.ingsw.network.serveronly.Server;
import it.polimi.ingsw.network.socket.SocketIdentifier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * This thread, started by the main Server, handles a SocketServer that keep listening for connection on the
 * specified port, and open a new socket stream for every client.
 */
public class SocketServer extends Thread {

    public static final int DEFAULT_MIN_CLIENTS = 3;
    public static final int DEFAULT_MAX_CLIENTS = 5;

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = INFO;

    private List<Socket> socketClients;

    private int port;

    public SocketServer(int port){
        this.port = port;
        socketClients = new ArrayList<>();
    }

    private static ConcurrentHashMap<Integer, SocketIdentifier> clients = new ConcurrentHashMap<>();

    @Override
    public void run() {
        LOGGER.log(INFO, "Starting SocketServer");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            if(Server.getWaitingRoom() != null){
                while (Server.getWaitingRoom().getTimerCount() > 0 && clients.size() <= DEFAULT_MAX_CLIENTS) {
                    Socket socket = serverSocket.accept();
                    new SocketConnectionReader(socket).start();
                    socketClients.add(socket);
                    System.out.println("[DEBUG] aggiunto client alla lista di connessinoi.");
                    for (Socket s : socketClients) {
                        System.out.println("inet address: " + s.getInetAddress());
                        System.out.println("local add: " + s.getLocalAddress());
                        System.out.println(s.getPort());
                    }
                    clients.put(clients.size(), new SocketIdentifier(socket.getInetAddress().toString(), socket.getPort()));
                }
            } else {
                while(true) {
                    Socket socket = serverSocket.accept();
                    new SocketConnectionReader(socket).start();
                    socketClients.add(socket);
                    System.out.println("[DEBUG] aggiunto client alla lista di connessinoi.");

                    clients.put(clients.size(), new SocketIdentifier(socket.getInetAddress().toString(), socket.getPort()));
                }
            }

        } catch (
                IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }


    public static ConcurrentHashMap<Integer, SocketIdentifier> getClients() {
        return clients;
    }
}