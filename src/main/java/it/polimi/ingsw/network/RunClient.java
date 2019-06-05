package it.polimi.ingsw.network;

//qua dovrò avviare il client da terminale passandogli gia come parametro ip del server porta cli/gui rmi/socket
//inizio direttamente dalla schermata di login del giocatore

import com.google.gson.Gson;
import it.polimi.ingsw.network.jsonconfig.Config;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class RunClient {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final int DEFAULT_SOCKET_PORT = 22222;
    private static final String DEFAULT_UI_CHOICE = "-cli";
    private static final String DEFAULT_SERVER_IP = "localhost";

    private static View view;

    // for saves
    private static final String CONFIG_PATH = "resources/json/startupConfig/config.json";
    private static Gson gson = new Gson();
    private static Config config;





    /**
     *
     * @param args ->
     *             arg0 = ip server
     *             arg1 = socket port
     *             arg2 = gui ( -gui for graphical interface or -cli for command line interface)
     *             arg3 = rmi serverPort
     *             arg4 = rmi clientPort
     */

    public static void main(String[] args) {


        switch (args.length){


            case 0:

                Gson gson = new Gson();


                try{

                    // creates a reader for the file

                    BufferedReader br = new BufferedReader( new FileReader( new File(CONFIG_PATH).getAbsolutePath()));

                    // load the Config File

                    Config config = gson.fromJson(br, Config.class);

                    // LOG the load

                    LOGGER.log(level,"[RUN-CLIENT] Config successfully loaded ");

                    // starts the game

                    view = new View(config.getServerIp(), config.getSocketPort(), config.getGui());

                }catch (FileNotFoundException e){

                    LOGGER.log(Level.WARNING, "[RunClient] jsonFile not found, start client assuming server is local");

                    // creates a new config file

                    config = new Config(DEFAULT_SERVER_IP, DEFAULT_SOCKET_PORT, DEFAULT_UI_CHOICE);

                    // saves it

                    saveCurrentConfig(config);

                    // starts the view w/ default values

                    view = new View(DEFAULT_SERVER_IP, DEFAULT_SOCKET_PORT, DEFAULT_UI_CHOICE);

                }

                break;

            case 1:

                // creates a new config file

                config = new Config(args[0], DEFAULT_SOCKET_PORT, DEFAULT_UI_CHOICE);

                // saves it

                saveCurrentConfig(config);

                // starts the view w/ default values

                view = new View(args[0], DEFAULT_SOCKET_PORT, DEFAULT_UI_CHOICE);

                break;

            case 3:

                view = new View(args[0], Integer.parseInt(args[1]), args[2]);

                // creates a new config file

                config = new Config(args[0], Integer.parseInt(args[1]), args[2]);

                // saves it

                saveCurrentConfig(config);

                break;


            case 5:

                view = new View(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

                // creates a new config file

                config = new Config(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

                // saves it

                saveCurrentConfig(config);

                break;



            default:

                LOGGER.log(Level.WARNING, "[RunClient] args number not matching any of our criteria: \n 0-> json files in path {0} \n 1-> ipAddress \n 2-> ipAddress, socketPort, \"-cli\" / \"-gui\" \n 3-> ipAddress, socketPort, \"-cli\" / \"-gui\", rmiServerPort, rmiClientPort ", CONFIG_PATH );

                break;


        }

    }


    /**
     *  this function saves the configuration files
     * @param config is the config class
     */
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

    // getter

    public static View getView() {
        return view;
    }
}

