package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This class represent the single connection between clients and server.
 * Every time that a new client connects to the server a new socketConnection thread is started
 * to handle the single connection separately
 */
public class SocketConnection implements Runnable, Closeable {

    private Socket socket;

    public SocketConnection(Socket clientConn) {
        this.socket = clientConn;
    }

    @Override

    public void run() {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream);

            String msg;

            do {
                msg = in.readLine();

                if (msg != null && !msg.startsWith("disconnect")) {
                    System.out.println("<<< " + socket.getRemoteSocketAddress() + ": " + msg);
                    out.println("[Server] Received message: >>> " + msg);
                    processMessage(msg);
                    out.flush();
                }
            } while (msg != null && !msg.startsWith("disconnect"));

        } catch(IOException e){
            e.getMessage();
        } finally {
            if (inputStream != null && outputStream != null) {
                try {
                    inputStream.close();
                    outputStream.close();
                    Logger.getLogger("infoLogger").info("SocketClient " + socket.getRemoteSocketAddress() + " closing connection.");
                } catch(IOException e){
                    e.getMessage();
                }
            }
            try {
                socket.close();
            } catch(IOException e){
                e.getMessage();
            }
        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public void processMessage(String msg){
        //TODO creare una mappa di funzioni da associare alla stringa che rappresenta il comando
        if(msg.equals("login")) {
            //chiama funzione handleLogin
            handleLogin();
        }
    }

    public void handleLogin(){

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch(IOException e){
            e.getMessage();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(outputStream);
        String playerName = null;
        String playerColor = null;
        boolean loginCheck = false;

        try {
            do {

                out.println("[Server] Enter a name: >>> ");
                out.flush();
                try {
                    playerName = in.readLine();
                    loginCheck = Server.getController().isNameUnused(playerName);
                } catch(IOException e) {
                    e.getMessage();
                }
                if (!loginCheck) {
                    out.println("[Server] Name has already been chosen. ");
                    out.flush();
                }

            }while(!loginCheck);

            do {

                out.println("[Server] Pick a PlayerColor (GREEN, GREY,PURPLE, BLUE, YELLOW): >>> ");
                out.flush();
                playerColor = in.readLine();
                loginCheck = Server.getController().isColorUnused(playerColor);
                if(!loginCheck){
                    out.println("[Server] Color has already been chosen. ");
                    out.println("[Server] Retry");
                    out.flush();
                }

            } while(!loginCheck);

            out.println("[Server] Adding " + playerName + " to waitiing room. Press any key to continue.");
            out.flush();
            Server.getController().addPlayerToWaitingRoom(playerName,  PlayerColor.valueOf(playerColor.toUpperCase()));
            out.println("[Server] Added " + playerName + " to waiting room.");
            out.println("[Server] Your ID is: " + Server.getController().findPlayerByName(playerName));
            out.flush();

        } catch(IOException e){
            e.getMessage();
        }
    }
}