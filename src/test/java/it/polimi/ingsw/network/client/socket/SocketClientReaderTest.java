package it.polimi.ingsw.network.client.socket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocketClientReaderTest {

    @Test
    void handleJson() {

        String json = "{\"weapon\":\"Gun\",\"actionType\":\"GRAB\",\"playerId\":0,\"type\":\"TURN\"}";

        String[] update = json.split(",");

        System.out.println(update[ update.length - 1 ]);
    }
}