package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.player.PlayerColor;
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
    private SocketConnectionWriter socketConnectionWriter;

    BufferedReader input;

    public SocketConnectionReader(Socket socket) {
        this.socket = socket;
    }

    public SocketConnectionWriter getSocketConnectionWriter() {
        return socketConnectionWriter;
    }

    @Override
        public void run() {
            Logger.getLogger("infoLogger").info("Received Client Connection");

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                socketConnectionWriter = new SocketConnectionWriter(socket);
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
                        int id = Server.getWaitingRoom().addPlayer(commands[1], PlayerColor.valueOf(commands[2].toUpperCase()));
                        if(id >= 0){
                            socketConnectionWriter.send("login\tOK");
                        }

                    }catch (NameAlreadyTakenException e){       //NOTE: temporary solution by D, just to make it compile
                        socketConnectionWriter.send("login\tNAME_ALREADY_TAKEN");
                    }catch (ColorAlreadyTakenException e){
                        socketConnectionWriter.send("login\tCOLOR_ALREADY_TAKEN");
                    }

                }).start();
                break;

            case "pong" :
                new Thread (() -> {
                    Logger.getLogger("infoLogging").info("Client reply to ping: " + commands[1]);
                }).start();
                break;


            default :
                //this should never happen
                new Thread (() -> {
                    Logger.getLogger("infoLogging").warning("[DEBUG] Unknown command received from client.");
                }).start();
                break;

        }
    }
}




