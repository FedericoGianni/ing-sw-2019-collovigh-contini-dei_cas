package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ControllerTest {

    @Test
    void firstPhase() {
        List<String> playerList = new ArrayList<String>();
        playerList.add("Frank");
        playerList.add("Tim");
        List<PlayerColor> playerColor = new ArrayList<>();
        playerColor.add(PlayerColor.PURPLE);
        playerColor.add(PlayerColor.YELLOW);
        Controller c = new Controller(playerList, playerColor, 0);

        assert(c.getTurnPhase().equals(TurnPhase.SPAWN));
    }
}