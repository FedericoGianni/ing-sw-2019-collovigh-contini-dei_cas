package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * This class will start a SocketClient which starts another thread, SocketClientReader, which keep listening to
 * messages from SocketConnectionWriter. SocketClientReader thread also starts a SocketClientWriter thread, that is
 * used to send message from client to server.
 */
public class SocketClient implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    /**
     * Reference to the SocketClientReader bind to this client
     */
    private SocketClientReader scr;

    private SocketClientWriter scw;

    /**
     * If set to false, stops the while loop
     */
    private boolean active = true;

    /**
     * ip_address of the Server to be connected
     */
    private String ip;

    /**
     * port of the Socket Server
     */
    private int port;

    /**
     * Reference to the socket stream
     */
    private Socket socket;

    /**
     * Constructor
     * @param ip ip address of the Server which client wants to connect to
     * @param port port of the Server which client wants to connect to
     */
    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     *
     * @return a reference to the SocketClientReader created by this class run() method
     */
    public SocketClientReader getScr() {
        return scr;
    }

    /**
     *
     * @return a reference to the SocketClientWriter which is started inside SocketClientReader method
     */
    public SocketClientWriter getScw(){
        return scw;
    }

    /**
     *
     * @return a reference to the socket which represent the stream of the single client-server connection
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Sets the socket passed as a parameter to the socket attribute inside this class,
     * useful because the new Socket is created inside a try block and therefore can't be accessed
     * inside the SocketClient class because of the attribute scope.
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Starts a new socket on the specified ip address and port, creates its linked SocketClientReader thread,
     * which again starts a SocketClientWriter thread to handle the socket output stream.
     */
    @Override
    public void run() {

        try (Socket s = new Socket(ip, port)) {

            setSocket(s);

            scr = new SocketClientReader(socket);
            scr.start();
            scw = new SocketClientWriter(socket);
            scw.start();
            LOGGER.log(INFO, "Succesfuly started socketClientReader");
            scr.setScw(scw);

            while(active){
                //don't close this since it keeps socket open
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
