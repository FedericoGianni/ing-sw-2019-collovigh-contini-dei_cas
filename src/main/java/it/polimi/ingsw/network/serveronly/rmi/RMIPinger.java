package it.polimi.ingsw.network.serveronly.rmi;

import it.polimi.ingsw.network.rmi.ToClient;
import it.polimi.ingsw.network.serveronly.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIPinger implements Runnable{

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private static final int WAIT_TIME = 1;
    private final ToClient target;

    private int pId;


    public RMIPinger(ToClient client) {

        this.target = client;
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

            Server.removePlayer(pId);

            LOGGER.log(Level.WARNING, message);

            LOGGER.log(level, e.getMessage(), e);

        }

    }
}
