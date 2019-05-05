package it.polimi.ingsw.network.socket;

import java.util.logging.Logger;

/**
 * This class implements a thread which will send every [DEFAULT_PING_INTERVAL] ms a string "ping" to the client to
 * check if it is still connected to the SocketServer. The client on his side reads the string "ping" and acts
 * accordingly to the function linked in the SocketClientReader handleMsg(String msg) function
 */
public class SocketPing implements Runnable {

    /**
     * The thread will send a "ping" string every DEFAULT_PING_INTERVAL millisecond
     */
    public static final int DEFAULT_PING_INTERVAL = 10000;

    /**
     * this flag can be set to false to stop the server ping requests, as the while loop continues until active is true
     */
    private boolean active = true;

    /**
     * Reference to SocketConnectionWriter needed to communicate from SocketServer to SocketClient
     */
    private SocketConnectionWriter scw;

    /**
     * Constructor
     * @param scw SocketConnectionWriter which represent the connection between SocketServer and one single client
     */
    SocketPing(SocketConnectionWriter scw){
        this.scw = scw;
    }

    /**
     * Setter to stop the thread ping
     * @param active if this parameter is set to false, the thread will stop
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Runs the SocketPing thread, pinging the Client every [DEFAULT_PING_INTERVAL] ms
     */
    @Override
    public void run() {
        while (active)
            try {
                Thread.sleep(DEFAULT_PING_INTERVAL);
                scw.send("ping\f" + scw.getId());
                Logger.getLogger("infoLogging").info("Sending ping message to client.");
            } catch (InterruptedException e) {
                //this.interrupt();
            }
    }






}
