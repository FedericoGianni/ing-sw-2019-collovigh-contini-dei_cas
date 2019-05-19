package it.polimi.ingsw.network;

//qua dovrò avviare il client da terminale passandogli gia come parametro ip del server porta cli/gui rmi/socket
//inizio direttamente dalla schermata di login del giocatore

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;

import static java.lang.Thread.sleep;

public class RunClient {

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




    /*
    public static void main(String args[]) {

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

    }

     */

    /**
     *
     * @param args ->
     *             arg1 = ip server
     *             arg2 = socket port
     *             arg3 = gui ( -gui for graphical interface or -cli for command line interface)
     */

    public static void main(String[] args) {

        if (args.length == 3) {

            startWithThree(args);


        }else if (args.length == 2){

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

        }else {

            System.out.println("[ERROR] this args config has still not been implemented ");
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

