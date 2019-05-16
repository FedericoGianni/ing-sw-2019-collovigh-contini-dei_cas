package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientWriter extends Client implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    /**
     * Reference to the socket stream, passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * PrintWriter to handle the output stream from socket
     */
    private PrintWriter output;

    /**
     * Constructor
     * @param socket to be initialized with
     */
    SocketClientWriter(Socket socket){
        this.socket = socket;
    }

    /**
     * Initialize socket output stream
     */
    @Override
    public void run(){

        try {
            output = new PrintWriter(socket.getOutputStream(), true);

        } catch(IOException e){
            e.getMessage();
        }
    }

    /**
     * Send a String to the SocketConnectionReader
     * @param message to be sent
     */
    public void send(String message) {
        output.println(message);
        output.flush();
    }

    @Override
    public int joinGame(String name, PlayerColor color) {
        send("login\f" + name + "\f" + color);
        //TODO check how to handle return type with socket?
        return -1; //temporary solution
    }

    @Override
    public void spawn(CachedPowerUp powerUp) {
        //TODO string to send PowerUp chosen by the player to discard at SPAWN phase
        //send();
    }
}
