package it.polimi.ingsw.network.serveronly.Socket;

import com.google.gson.Gson;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.network.serveronly.Server;
import it.polimi.ingsw.network.socket.SocketPing;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private static final String SET_INT_ANSWER = "setIntAnswer";
    private static final String REDO_FRENZY_SHOOT = "doFrenzyAtomicShoot";
    private static final String DO_FRENZY_SHOOT_RELOAD = "doFrenzyShootReload";
    private static final String CLOSE = "close";
    private static final String ASK_MAP_AND_SKULLS = "askMapAndSkulls";

    /**
     * Reference to the socket representing the communication stream, passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * PrintWriter to manage the output stream from socket
     */
    private BufferedWriter output;

    private final Object lock = new Object();

    List<Integer> mapAndSkullsAnswer = new ArrayList<>();

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

    public void setMapAndSkullsAnswer(List<Integer> mapAndSkullsAnswer) {
        this.mapAndSkullsAnswer = mapAndSkullsAnswer;
    }

    /**
     * {@inheritDoc}
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn() {
        LOGGER.info("Sending startSpawn string to connected client");
        send(START_SPAWN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp() {
        LOGGER.info("Sending startPowerUp string to connected client");
        send(START_POWER_UP);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {
        LOGGER.info("Sending startAction string to connected client");
        send(START_ACTION  + "\f" + isFrenzy + "\f" + isBeforeFrenzyStarter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyAtomicShoot() {
        LOGGER.info("Sending doFrenzyAtomicShoot string to connected client");
        send(REDO_FRENZY_SHOOT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFrenzyReload() {
        LOGGER.log(level,"sending doFrenzyReload string to connected client");
        send(DO_FRENZY_SHOOT_RELOAD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {
        LOGGER.info("sending startReload string to connected client");
        send(START_RELOAD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {
        LOGGER.log(level, "[Socket-Conn-Writer] sending askGrenade string to client ");
        send(ASK_GRENADE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUpdate(UpdateClass update) {
        LOGGER.info("sending update string to connected client of type : " + update.getType());
        send(gson.toJson(update));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        LOGGER.info("sending initGame to conneted client");
        send(START_GAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(String s) {
        LOGGER.info("sending show to connected client");
        String msg = SHOW + "\f" + s;
        send(msg);
    }

    public void setIntAnswer(int intAnswer){

        LOGGER.log(level, "[Socket-Conn-Writer] sending setIntAnswer string to client ");

        send(SET_INT_ANSWER + "\f" + intAnswer);
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

            if(Server.getClient(i) != null) {

                if (Server.getClient(i).equals(this)) {
                    playerId = i;
                }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {

        LOGGER.log(level,"sending close to connected client");

        send(CLOSE);

    }

    private List<Integer> waitIntAnswer(){

        while (mapAndSkullsAnswer.isEmpty()){

            try{

                TimeUnit.SECONDS.sleep(50);

            }catch (InterruptedException e){

                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }

        System.out.println(mapAndSkullsAnswer);

        return mapAndSkullsAnswer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> askMapAndSkulls() {

        mapAndSkullsAnswer = new ArrayList<>();

        send(ASK_MAP_AND_SKULLS);

        return waitIntAnswer();
    }
}
