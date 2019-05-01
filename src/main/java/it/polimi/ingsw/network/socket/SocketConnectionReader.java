package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketConnectionReader extends Thread {

    private Socket socket;
    private SocketClientWriter socketConnectionWriter;

    BufferedReader input;

    public SocketConnectionReader(Socket socket) {
        this.socket = socket;
    }

    public SocketClientWriter getSocketConnectionWriter() {
        return socketConnectionWriter;
    }

    @Override
        public void run() {
            Logger.getLogger("infoLogger").info("Received Client Connection");

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                socketConnectionWriter = new SocketClientWriter(socket);
                socketConnectionWriter.start();

                while(true) {

                    String msg = input.readLine();
                    if(msg != null)
                        processMessage(splitCommand(msg));
                }


            } catch(IOException e) {
                    e.getMessage();
            } finally {
                try {
                    socket.close();
                } catch(IOException e) {
                    e.getMessage();
                }
            }

        }

    private String[] splitCommand(String msg) {
        if(msg == null){
            return null;
        }
        return msg.split("\t");
    }

    public void processMessage(String[] commands){

        String header = commands[0];

        switch(header) {

            case "login" :
                new Thread (() -> {
                    try {
                        Server.getWaitingRoom().addPlayer(commands[1], PlayerColor.valueOf(commands[2].toUpperCase()));

                    }catch (NameAlreadyTakenException e){       //NOTE: temporary solution by D, just to make it compile
                        socketConnectionWriter.send("login\tWRONG_NAME");
                    }catch (ColorAlreadyTakenException e){
                        socketConnectionWriter.send("login\tWRONG_COLOR");
                    }
                    socketConnectionWriter.send("login\tOK");

                }).start();
                break;

            default :
                new Thread (() -> {
                    Logger.getLogger("infoLogging").warning("Unknown command received from client");
                }).start();
                break;

        }
    }
}




