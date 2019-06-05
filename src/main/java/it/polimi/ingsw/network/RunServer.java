package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.jsonconfig.Config;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunServer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final String CONFIG_PATH = "resources/json/startupConfig/config.json";

    private static final int DEFAULT_SOCKET_PORT = 22222;

    private static Server server;

    private static Gson gson = new Gson();
    private static Config config;


    /**
     *
     * @param args ->
     *             arg0 = socket port
     *             arg1 = rmi serverPort (opt)
     *             arg2 = rmi clientPort (opt)
     */
    public static void main(String[] args) {

        switch (args.length) {


            case 0:


                try {

                    // creates a reader for the file

                    BufferedReader br = new BufferedReader(new FileReader(new File(CONFIG_PATH).getAbsolutePath()));

                    // load the Config File

                    config = gson.fromJson(br, Config.class);

                    // LOG the load

                    LOGGER.log(level, "[RUN-CLIENT] Config successfully loaded ");

                    // starts the server

                    server = new Server(config.getSocketPort());

                } catch (FileNotFoundException e) {

                    // creates a new config file

                    config = new Config(DEFAULT_SOCKET_PORT);

                    // saves it

                    saveCurrentConfig(config);

                    // starts the server w/ default config

                    server = new Server(DEFAULT_SOCKET_PORT);

                    // log the exception

                    LOGGER.log(Level.WARNING, e.getMessage(),e );
                }

                break;


            case 1:

                // if the player only specified the socket port

                server = new Server(Integer.parseInt(args[0]));

                // creates a new config file

                config = new Config(Integer.parseInt(args[0]));

                // saves it

                saveCurrentConfig(config);

                break;


            case 3:

                // if user specified the rmi ports

                server = new Server(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                // creates a new config file

                config = new Config(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                // saves it

                saveCurrentConfig(config);

                break;

            default:

                System.out.println("[ERROR] this args configurations has still not been implemented ");

                break;

        }
    }

    private static void saveCurrentConfig( Config config ){

        try{

            gson = new Gson();

            FileWriter writer = new FileWriter(CONFIG_PATH);

            gson.toJson(config, writer);

            writer.flush();
            writer.close();

        }catch (IOException e){

            LOGGER.log(Level.WARNING, e.getMessage(),e );
        }
    }

}
