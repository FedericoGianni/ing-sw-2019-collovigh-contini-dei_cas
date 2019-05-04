package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RMIClientTest {

    @Test
    void joinGame() {


        RMIServer server = new RMIServer();
        RMIClient client = new RMIClient();

        Server mainServer = new Server();

        int a = client.joinGame("Alfred", PlayerColor.YELLOW);

        assertEquals(1, Server.getWaitingRoom().size() );

        assertTrue(Server.getWaitingRoom().isNameAlreadyTaken("Alfred"));
    }

    @Test
    void voteMap() {

        RMIServer server = new RMIServer();
        RMIClient client = new RMIClient();

        Server mainServer = new Server();


        client.voteMap(1);

        assertEquals(1,Server.getWaitingRoom().getMapType());

        Server.getWaitingRoom().getGames().clear();
    }
}