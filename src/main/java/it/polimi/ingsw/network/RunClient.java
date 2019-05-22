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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class RunClient {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static RMIClient rmic ; // to be deleted

    public static UserInterface ui; // to be deleted

    public static CLI cli; // to be deleted

    private static View view;


    /**
     * @deprecated
     * @return
     */
    @Deprecated
    public static CLI getCli() {
        return cli;
    }

    /**
     * @deprecated
     * @param ui
     */
    @Deprecated
    public static void setUi(UserInterface ui) {
        RunClient.ui = ui;
    }

    /**
     * @deprecated
     * @return
     */
    @Deprecated
    public static UserInterface getUi() {
        return ui;
    }

    public static View getView() {
        return view;
    }

    /**
     *
     * @param args ->
     *             arg1 = ip server
     *             arg2 = socket port
     *             arg3 = gui ( -gui for graphical interface or -cli for command line interface)
     */

    public static void main(String[] args) {


        switch (args.length){


            case 0:

                Gson gson = new Gson();


                try{

                    // creates a reader for the file

                    BufferedReader br = new BufferedReader( new FileReader( new File("resources/json/startupConfig/config.json").getAbsolutePath()));

                    // load the Config File

                    Config config = gson.fromJson(br, Config.class);

                    // LOG the load

                    LOGGER.log(level,"[RUN-CLIENT] Config successfully loaded ");

                    // starts the game

                    view = new View(config.getServerIp(), config.getSocketClientPort(), config.getGui());

                }catch (Exception e){

                    e.printStackTrace();
                }

                break;

            case 3:

                startWithThree(args);

                break;

            case 2:

                try {
                    SocketClient sc = new SocketClient(args[0], Integer.parseInt(args[1]));
                    Thread t = new Thread(sc);
                    t.start();

                    ui = cli;
                    sleep(2000);
                    view = new View(cli);
                    cli = new CLI(view);
                    view.setVirtualView(sc.getScw());
                    cli.login();

                    //rmic = new RMIClient("localhost");
                    //rmic.joinGame("a", PlayerColor.GREEN);


                } catch (Exception e) {

                    e.printStackTrace();
                }

                break;

            default:

                System.out.println("[ERROR] this args configurations has still not been implemented ");

                break;


        }

    }

    /**
     * This function will start the client if the program is launched with three arguments
     * 1-> server Ip
     * 2-> server port for socket
     * 3-> -cli for cli / -gui for gui
     *
     * @param args are the three args
     */
    private static void startWithThree(String[] args){

        view = new View(args[0], Integer.parseInt(args[1]), args[2]);

    }
}

