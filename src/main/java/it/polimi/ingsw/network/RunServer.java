package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.jsonconfig.Config;
import it.polimi.ingsw.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunServer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static Server server;


    public static void main(String[] args) {

        switch (args.length) {


            case 0:

                Gson gson = new Gson();


                try {

                    // creates a reader for the file

                    BufferedReader br = new BufferedReader(new FileReader(new File("resources/json/startupConfig/config.json").getAbsolutePath()));

                    // load the Config File

                    Config config = gson.fromJson(br, Config.class);

                    // LOG the load

                    LOGGER.log(level, "[RUN-CLIENT] Config successfully loaded ");

                    // starts the server

                    server = new Server(config.getSocketServerPort());

                } catch (Exception e) {
                    e.printStackTrace();

                    server = new Server(22222);
                }

                break;


            case 1:


                server = new Server(Integer.parseInt(args[0]));

                break;

            default:

                System.out.println("[ERROR] this args configurations has still not been implemented ");

                break;

        }
    }

}
