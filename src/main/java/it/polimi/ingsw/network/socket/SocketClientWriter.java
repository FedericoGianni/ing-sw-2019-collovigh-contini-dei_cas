package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientWriter extends Thread {

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
}
