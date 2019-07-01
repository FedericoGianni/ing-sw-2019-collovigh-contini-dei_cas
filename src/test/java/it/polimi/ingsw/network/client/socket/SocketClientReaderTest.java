package it.polimi.ingsw.network.client.socket;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SocketClientReaderTest {

    String json = "{\"weapons\":[\"Gun\",\"rifle\"],\"powerUps\":[{\"color\":\"BLUE\",\"type\":\"TELEPORTER\"},{\"color\":\"YELLOW\",\"type\":\"TELEPORTER\"}],\"actionType\":\"RELOAD\"}";

    @Test
    void handleJson() {

        String[] update = json.split(",");

        System.out.println(update[ update.length - 1 ]);

        String type = update[ update.length - 1 ];

        String expected = "{\"actionType\":\"RELOAD\"}";

        assertEquals(expected,type);
    }

    @Test
    void handleTurnUpdate() {

        String[] update = json.split(",");

        System.out.println(update[ update.length - 3 ]);

        String type = (update[ update.length - 3 ].startsWith("{")) ? update[ update.length - 3 ].substring(1,update[ update.length - 3 ].length()) : update[ update.length - 3 ];

        System.out.println("TYPE : " + type);

    }
}