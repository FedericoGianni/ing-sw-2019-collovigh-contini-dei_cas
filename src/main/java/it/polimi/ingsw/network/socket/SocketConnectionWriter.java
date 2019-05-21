package it.polimi.ingsw.network.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

public class SocketConnectionWriter extends Thread implements ToView {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = INFO;

    Gson gson = new Gson();
    UpdateClass update;

    /**
     * Reference to the socket representing the communication stream, passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * PrintWriter to manage the output stream from socket
     */
    private BufferedWriter output;

    private final Object lock = new Object();

    /**
     * Constructor
     * @param socket reference to the stream to be initialized with
     */
    SocketConnectionWriter(Socket socket) {
        this.socket = socket;
    }

    public void signal() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void await() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    /**
     * Initialize the output stream and start a SocketPing thread to keep checking if client is still connected
     */
    @Override
    public void run() {

        try {
            //output = new PrintWriter(socket.getOutputStream(), true);
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new Thread(new SocketPing(output, this)).start();

            //this.signal();


        } catch (IOException e) {
            e.getMessage();
            LOGGER.log(INFO, "[DEBUG] Started SocketConnectionWriter " + this.getName());

        }
    }

    /**
     * Send a String to the specified socket output stream
     * @param message to be sent
     */
    public void send(String message) {
        try {
            output.write(message);
            output.flush();
        }catch (IOException e){
            /*
            disconnect();
            try{
                socket.close();
            } catch (IOException e2){
                LOGGER.log(WARNING, "ERROR trying to close socket stream");
            }
            this.interrupt();
            */
        }
    }

    @Override
    public void startSpawn() {
        LOGGER.info("Sending startSpawn string to connected client");
        send("startSpawn");
    }

    @Override
    public void startPowerUp() {
        LOGGER.info("Sending startPowerUp string to connected client");
        send("startPowerUp");
    }

    @Override
    public void startAction() {
        LOGGER.info("Sending startAction string to connected client");
        send("startAction");
    }

    @Override
    public void startReload() {
        LOGGER.info("sending startReload string to connected client");
        send("startReload");
    }

    /**
     * This method will be called on a player if he/she was shot in the previous phase and has grenades
     */
    @Override
    public void useGrenade() {

    }

    @Override
    public void sendUpdate(UpdateClass update) {
        LOGGER.info("sending update string to connected client");
        send(gson.toJson(update));
    }

    @Override
    public void useGrenade() {
        //TODO
    }

    public void disconnect(){
        LOGGER.log(WARNING, "Disconneting virtual view linked to this connection.");
        //TODO gestire la disconnessione nella lista di virtual view del controller
        //Server.getController().getVirtualView(1).disconnect();
        //closing socket stream
        try{
            System.out.println("[DEBUG] received disconnection from SocketPing. Calling socket.close()");
            socket.close();
        } catch (IOException e){
            LOGGER.log(WARNING, "ERROR when trying to close socket stream");
        }
    }
}
