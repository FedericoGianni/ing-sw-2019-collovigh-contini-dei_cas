package it.polimi.ingsw.network.socket;

//qua dovrò avviare il client da terminale passandogli gia come parametro ip del server porta cli/gui rmi/socket
//inizio direttamente dalla schermata di login del giocatore

import it.polimi.ingsw.view.CLI;

public class RunClient {

    public static CLI cli;

    public static CLI getCli() {
        return cli;
    }

    public static void main(String args[]) {

        try {
            SocketClient sc = new SocketClient(args[0], Integer.parseInt(args[1]));
            Thread t = new Thread(sc);
            t.start();

            cli = new CLI(sc);
            cli.login();

        }catch(Exception e){

            e.printStackTrace();
        }
    }

}