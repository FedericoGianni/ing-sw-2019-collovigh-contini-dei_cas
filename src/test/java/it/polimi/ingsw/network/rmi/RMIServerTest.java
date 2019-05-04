package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Server;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class RMIServerTest {


    @Test
    void updateClientList() {

        RMIClient client = new RMIClient();
        RMIClient client1 = new RMIClient();

        RMIServer server = new RMIServer();

        server.resetClients();

        RMIClient client2 = new RMIClient();
        client2.setPlayerId(0);

        RMIClient client3 = new RMIClient();
        client3.setPlayerId(1);

        client2.reconnect();
        client3.reconnect();

        assertEquals(2,server.getClientNumber());


    }
}