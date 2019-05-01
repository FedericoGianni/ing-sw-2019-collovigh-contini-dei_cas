package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientWriter extends Thread {

    private Socket socket;
    private PrintWriter output;

    SocketClientWriter(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

        try {
            output = new PrintWriter(socket.getOutputStream(), true);

        } catch(IOException e){
            e.getMessage();
        }
    }

    public void send(String message) {
        output.println(message);
        output.flush();
    }
}
