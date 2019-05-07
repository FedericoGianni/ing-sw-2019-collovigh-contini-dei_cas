package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RMIClientTest {

    @Test
    void joinGame() {
        int port = 22222;

        RMIServer server = new RMIServer();
        RMIClient client = new RMIClient();

        Server mainServer = new Server(port);

        int a = client.joinGame("Alfred", PlayerColor.YELLOW);

        assertEquals(1, Server.getWaitingRoom().size() );

        assertTrue(Server.getWaitingRoom().isNameAlreadyTaken("Alfred"));
    }

    @Test
    void voteMap() {

        int port = 22222;

        RMIServer server = new RMIServer();
        RMIClient client = new RMIClient();

        Server mainServer = new Server(port);


        client.voteMap(1);

        assertEquals(1,Server.getWaitingRoom().getMapType());

        Server.getWaitingRoom().getGames().clear();
    }

}