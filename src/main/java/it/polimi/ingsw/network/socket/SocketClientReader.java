package it.polimi.ingsw.network.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketClientReader extends Thread {

    private BufferedReader in;
    private Socket socket;
    private SocketClientWriter scw;

    public SocketClientWriter getScw() {
        return scw;
    }

    SocketClientReader(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            scw = new SocketClientWriter(socket);
            scw.start();

            while(true) {
                String msg = receive();
                processMessage(splitCommand(msg));
            }


        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    private String[] splitCommand(String msg) {
        if(msg == null){
            return null;
        }
        return msg.split("\t");
    }

    public void processMessage(String[] commands) {

        String header = commands[0];

        switch (header) {

            case "login" :
                new Thread (() -> {
                    System.out.println("[Server] login reply: " + commands[1]);
                    if(!(commands[1].equals("OK")))
                        RunClient.getCli().retryLogin();
                }).start();
                break;

            case "retryLogin" :
                new Thread (() -> {

                }).start();
                break;

            case "ping" :
                new Thread (() -> {
                    //System.out.println("[DEBUG] Ricevuta ping request dal server.");
                    scw.send("pong\t" + scw.getName());
                }).start();
                break;

            default:
                new Thread(() -> {
                    System.out.println("[Server] Unknown command received from RemoteServer ");
                }).start();
        }
    }
}
