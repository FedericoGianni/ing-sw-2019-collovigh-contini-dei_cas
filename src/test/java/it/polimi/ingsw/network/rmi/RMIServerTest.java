package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RMIServerTest {


    @Test
    void updateClientList() {

        RMIClient client = new RMIClient();
        RMIClient client1 = new RMIClient();

        RMIServer server = new RMIServer();
        server.resetClients();

        RMIClient client2 = new RMIClient();
        RMIClient client3 = new RMIClient();

        server.updateClientList();

        assertEquals(2,server.getClientNumber());


    }
}