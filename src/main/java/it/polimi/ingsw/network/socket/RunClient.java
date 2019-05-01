package it.polimi.ingsw.network.socket;

//qua dovrò avviare il client da terminale passandogli gia come parametro ip del server porta cli/gui rmi/socket
//inizio direttamente dalla schermata di login del giocatore

import it.polimi.ingsw.view.CLI;

public class RunClient {

    public static void main(String args[]) {

        SocketClient sc = new SocketClient("127.0.1.1", 22222);
        Thread t = new Thread(sc);
        t.start();

        CLI cli = new CLI(sc);
        cli.login();
    }

}