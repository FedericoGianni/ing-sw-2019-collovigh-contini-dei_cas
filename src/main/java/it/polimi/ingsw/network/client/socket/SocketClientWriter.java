package it.polimi.ingsw.network.client.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.runner.RunClient;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientWriter extends Client implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String RECONNECTION_MESSAGE_START = "reconnect";

    /**
     * Reference to the socket stream, passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * PrintWriter to handle the output stream from socket
     */
    private BufferedWriter output;

    private boolean booleanAnswer;

    /**
     * Used to read return types from method invocation
     */
    private int intAnswer;

    private static final int DEFAULT_INT_ANSWER_VALUE = -5;

    private Gson gson = new Gson();

    /**
     * Constructor
     * @param socket to be initialized with
     */
    public SocketClientWriter(Socket socket){
        this.socket = socket;
    }

    /**
     * Initialize socket output stream
     */
    @Override
    public void run(){

        try {
            output = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));

        } catch(IOException e){
            e.getMessage();
        }
    }

    /**
     * Send a String to the SocketConnectionReader
     * @param message to be sent
     */
    public void send(String message) {
        try {
            output.write(message);
            output.newLine();
            output.flush();
        }catch(IOException e){
            e.getMessage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int joinGame(String name, PlayerColor color) {

        intAnswer = DEFAULT_INT_ANSWER_VALUE;

        send("login\f" + name + "\f" + color);

        return -1; //temporary solution
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int reconnect(String name) {

        intAnswer = DEFAULT_INT_ANSWER_VALUE;

        send(RECONNECTION_MESSAGE_START + "\f" +  name);

        int playerId = waitIntAnswer();

        RunClient.getView().setPlayerId(playerId);

        return playerId;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spawn(CachedPowerUp powerUp) {
        send("spawn"+"\f"+powerUp.getType()+"\f"+powerUp.getColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAction(JsonAction jsonAction) {

        LOGGER.log(level,"sending ACTION string to connected client of type :  {0} ", jsonAction.getType());
        send(gson.toJson(jsonAction));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean askMoveValid(int row, int column, Directions direction) {
        send("askMoveValid\f" + row + "\f" + column + "\f" + direction);
        return true;
    }

    public void setBooleanAnswer(boolean booleanAnswer) {
        this.booleanAnswer = booleanAnswer;
    }

    public void setIntAnswer(int intAnswer) {
        this.intAnswer = intAnswer;
    }

    /**
     * wait for the socket client reader to read what the method invocation returns and then returns it
     * @return
     */
    private int waitIntAnswer(){

        while (intAnswer == DEFAULT_INT_ANSWER_VALUE){

            try{

                TimeUnit.MILLISECONDS.sleep(50);

            }catch (InterruptedException e){

                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }

        System.out.println(intAnswer);

        return intAnswer;
    }
}
