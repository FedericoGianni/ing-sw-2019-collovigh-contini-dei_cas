package it.polimi.ingsw.network.client.socket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocketClientReaderTest {

    String json = "{\"actionType\":\"GRAB\",\"playerId\":0,\"type\":\"TURN\"}";

    @Test
    void handleJson() {

        String[] update = json.split(",");

        System.out.println(update[ update.length - 1 ]);
    }

    @Test
    void handleTurnUpdate() {

        String[] update = json.split(",");

        System.out.println(update[ update.length - 3 ]);

        String type = (update[ update.length - 3 ].startsWith("{")) ? update[ update.length - 3 ].substring(1,update[ update.length - 3 ].length()) : update[ update.length - 3 ];

        System.out.println("TYPE : " + type);
    }
}