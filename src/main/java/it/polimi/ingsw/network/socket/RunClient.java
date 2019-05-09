package it.polimi.ingsw.network.socket;

//qua dovrò avviare il client da terminale passandogli gia come parametro ip del server porta cli/gui rmi/socket
//inizio direttamente dalla schermata di login del giocatore

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.view.CLI;

public class RunClient {

    private RMIClient rmiClient;

    public static CLI cli;

    public static CLI getCli() {
        return cli;
    }

    public static void main(String args[]) {

        if (args[2].equals( "-r")){

            //TODO

        } else if(args[2].equals("-s")) {


            try {
                SocketClient sc = new SocketClient(args[0], Integer.parseInt(args[1]));
                Thread t = new Thread(sc);
                t.start();

                cli = new CLI(sc);
                cli.login();


            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

}