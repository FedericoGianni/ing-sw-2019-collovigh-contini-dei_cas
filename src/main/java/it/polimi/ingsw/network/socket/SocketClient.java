package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketClient implements Runnable {

    private SocketClientReader scr;
    private SocketClientWriter scw;

    private String ip;
    private int port;
    private Socket socket;

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public SocketClientReader getScr() {
        return scr;
    }

    public SocketClientWriter getScw() {
        return scw;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (Socket s = new Socket(ip, port)) {

            setSocket(s);

            scr = new SocketClientReader(socket);
            scw = new SocketClientWriter(socket);
            scr.start();
            Logger.getLogger("infoLogging").info("Succesfuly started socketClientReader");
            scw.start();
            Logger.getLogger("infoLogging").info("Succesfuly started socketClientWriter.");

            while(true){
                //don't close this since it keeps socket open
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }


}
