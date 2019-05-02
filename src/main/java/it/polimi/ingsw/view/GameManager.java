package it.polimi.ingsw.view;

import it.polimi.ingsw.network.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameManager {

    public static void main(String[] args) {

        Logger LOGGER = Logger.getLogger("infoLogging");
        LOGGER.setLevel(Level.FINE);

        Server server = new Server();


    }

}
