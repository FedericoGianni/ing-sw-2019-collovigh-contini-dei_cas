package it.polimi.ingsw.network.socket;

//qua dovrò avviare il client da terminale passandogli gia come parametro ip del server porta cli/gui rmi/socket
//inizio direttamente dalla schermata di login del giocatore

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.view.CLI;

public class RunClient {

    private static RMIClient rmic ;

    public static CLI cli;

    public static CLI getCli() {
        return cli;
    }

    public static void main(String args[]) {

        try {
            //SocketClient sc = new SocketClient(args[0], Integer.parseInt(args[1]));
            //Thread t = new Thread(sc);
            //t.start();

            //cli = new CLI(sc);
            //cli.login();

            rmic = new RMIClient("localhost");
            rmic.joinGame("a", PlayerColor.GREEN);


        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}

