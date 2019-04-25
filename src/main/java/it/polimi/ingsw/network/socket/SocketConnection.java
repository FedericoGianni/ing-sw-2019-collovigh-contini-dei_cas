package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketConnection implements Runnable, Closeable {

    private Socket socket;

    public SocketConnection(Socket clientConn) {
        this.socket = clientConn;
    }

    @Override

    public void run() {

        InputStream is = null;
        OutputStream os = null;

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            PrintWriter out = new PrintWriter(os);

            String msg;

            do {
                msg = in.readLine();

                /*trying to add Server response messages
                Scanner input = new Scanner(System.in);
                out.println("[Server] " + input.nextLine());
                out.flush();
                 */

                if (msg != null && !msg.startsWith("quit")) {
                    System.out.println("<<< " + socket.getRemoteSocketAddress() + ": " + msg);
                    out.println("[Server] Received message: >>> " + msg);
                    processMessage(msg);
                    // when you call flush you really send what
                    // you added to the buffer with println.
                    out.flush();
                }
            } while (msg != null && !msg.startsWith("quit"));

        } catch(IOException e){
            e.getMessage();
        } finally {
            if (is != null && os != null) {
                try {
                    is.close();
                    os.close();
                    Logger.getLogger("infoLogger").info("SocketClient2 " + socket.getRemoteSocketAddress() + " closing connection.");
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

        InputStream is = null;
        OutputStream os = null;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch(IOException e){
            e.getMessage();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        PrintWriter out = new PrintWriter(os);
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