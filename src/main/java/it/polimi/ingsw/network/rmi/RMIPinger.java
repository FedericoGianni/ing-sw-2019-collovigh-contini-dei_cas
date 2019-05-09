package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.LostClientException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIPinger implements Runnable{

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final int WAIT_TIME = 1000;
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

        try {

            pId = target.getPid();


            Boolean b;
            do{

                Thread.sleep(WAIT_TIME);



                LOGGER.log(level, "pinged client w/ id: {0}", pId);

                b = target.ping();

            }while (b);

            LOGGER.log(Level.WARNING, "lost client w/ id: {0}", pId);

            throw new LostClientException(pId);

        }catch (LostClientException e){

            RMIServer.removeClient(pId);
        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

    }
}
