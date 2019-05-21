package it.polimi.ingsw.network;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RunServer {

    public static void main(String[] args) {

        if (args.length < 1){

            System.out.println("[ERROR] this args config has still not been implemented: please specify Socket port ");
        }

        Logger LOGGER = Logger.getLogger("infoLogging");
        LOGGER.setLevel(Level.FINE);

        Server server = new Server(Integer.parseInt(args[0]));


    }

}
