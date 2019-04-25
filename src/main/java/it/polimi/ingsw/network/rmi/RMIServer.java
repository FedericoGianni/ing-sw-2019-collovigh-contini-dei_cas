package it.polimi.ingsw.network.rmi;

import java.util.logging.Logger;

public class RMIServer implements Runnable {

    @Override
    public void run() {
        Logger.getLogger("infoLogger").info("Starting RMIServer");
    }

}
