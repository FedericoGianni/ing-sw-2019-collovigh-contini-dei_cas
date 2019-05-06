package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class SocketConnectionWriter extends Thread {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = INFO;

    /**
     * Reference to the socket representing the communication stream, passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * PrintWriter to manage the output stream from socket
     */
    private PrintWriter output;

    /**
     * Constructor
     * @param socket reference to the stream to be initialized with
     */
    SocketConnectionWriter(Socket socket){
        this.socket = socket;
    }

    /**
     * Initialize the output stream and start a SocketPing thread to keep checking if client is still connected
     */
    @Override
    public void run() {

        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            new Thread(new SocketPing(this)).start();


        } catch (IOException e) {
            e.getMessage();
            LOGGER.log(INFO, "[DEBUG] Started SocketConnectionWriter " + this.getName());

        }
    }

    /**
     * Send a String to the specified socket output stream
     * @param message to be sent
     */
    public void send(String message) {
        output.println(message);
        output.flush();
    }
}
