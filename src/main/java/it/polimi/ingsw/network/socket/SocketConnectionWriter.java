package it.polimi.ingsw.network.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.io.BufferedWriter;
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

    private Gson gson = new Gson();

    private static final String END_GAME = "endGame";
    private static final String START_GAME = "startGame";
    private static final String START_SPAWN = "startSpawn";
    private static final String START_POWER_UP = "startPowerUp";
    private static final String START_ACTION = "startAction";
    private static final String START_RELOAD = "startReload";
    private static final String ASK_GRENADE = "askGrenade";
    private static final String SHOW = "showMessage";

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
    public synchronized void send(String message) {
        try {
            output.write(message);
            output.newLine();
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
        send(START_SPAWN);
    }

    @Override
    public void startPowerUp() {
        LOGGER.info("Sending startPowerUp string to connected client");
        send(START_POWER_UP);
    }

    @Override
    public void startAction() {
        LOGGER.info("Sending startAction string to connected client");
        send(START_ACTION);
    }

    @Override
    public void startReload() {
        LOGGER.info("sending startReload string to connected client");
        send(START_RELOAD);
    }

    /**
     * This method will be called on a player if he/she was shot in the previous phase and has grenades
     */
    @Override
    public void askGrenade() {
        LOGGER.log(level, "[Socket-Conn-Writer] sending askGrenade string to client ");
        send(ASK_GRENADE);
    }

    @Override
    public void sendUpdate(UpdateClass update) {
        LOGGER.info("sending update string to connected client of type : " + update.getType());
        send(gson.toJson(update));
    }

    @Override
    public void startGame() {
        LOGGER.info("sending initGame to conneted client");
        send(START_GAME);
    }

    @Override
    public void show(String s) {
        LOGGER.info("sending show to connected client");
        String msg = SHOW + "\f" + s;
        send(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {

        LOGGER.log(level,"sending endGame to connected client");

        send(END_GAME);

    }

    public void disconnect(){
        LOGGER.log(WARNING, "Disconneting virtual view linked to this connection.");

        int playerId = -1 ;

        for (int i = 0; i < Server.getClients().size() ; i++) {

            if (Server.getClient(i).equals(this)){
                playerId = i;
            }

        }


        if (playerId > -1) Server.removePlayer(playerId);


        //closing socket stream
        try{
            System.out.println("[DEBUG] received disconnection from SocketPing. Calling socket.close()");
            socket.close();
        } catch (IOException e){
            LOGGER.log(WARNING, "ERROR when trying to close socket stream");
        }
    }
}
