package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketConnectionWriter extends Thread {

    private Socket socket;
    private PrintWriter output;

    private SocketConnectionWriter(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            output = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.getMessage();
            Logger.getLogger("infoLogging").info("[DEBUG] Started SocketConnectionWriter " + this.getName());

        }
    }

    public void send(String message) {
        output.println(message);
        output.flush();
    }
}
