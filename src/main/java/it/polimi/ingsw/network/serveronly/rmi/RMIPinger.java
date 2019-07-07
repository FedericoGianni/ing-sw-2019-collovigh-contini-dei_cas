package it.polimi.ingsw.network.serveronly.rmi;

import it.polimi.ingsw.network.rmi.ToClient;
import it.polimi.ingsw.network.serveronly.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class pings the client, used for detect disconnections
 */
public class RMIPinger implements Runnable{

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.FINE;

    /**
     * time interval for ping
     */
    private static final int WAIT_TIME = 1;

    /**
     * ToClient object
     */
    private final ToClient target;

    /**
     * id of the player
     */
    private int pId;


    /**
     * Constructor
     * @param client is the client to ping
     */
    public RMIPinger(ToClient client, int playerId) {

        this.target = client;

        this.pId = playerId;
    }

    /**
     * this method is an infinite loop that check if the specified client is still connected
     */
    @Override
    public void run() {

        String message = "[RMI-PING] Player with unknown id never joined the game";

        try {

            pId = target.getPid();
            message = "[RMI-PING] Player with Id: " + pId + " left the game";


            Boolean b;
            do{

                Thread.sleep(WAIT_TIME);



                LOGGER.log(level, "pinged client w/ id: {0}", pId);

                b = target.ping();

            }while (b);

            LOGGER.log(Level.WARNING, "lost client w/ id: {0}", pId);



        }catch (Exception e){

            LOGGER.log(Level.WARNING, message);

            Server.removePlayer(pId);

        }

    }
}
