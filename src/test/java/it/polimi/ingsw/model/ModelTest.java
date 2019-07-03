package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelTest {

    @Test
    void modelShouldInitializeCorrectly(){

        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_1");
        playerNames.add("Player_2");
        playerNames.add("Player_3");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //generating model
        Model currentModel = new Model(playerNames, playerColors, 2,8);

        //checking that Map is correctly generated from Model contructor
        assertEquals(2, Model.getMap().getMapType());
        assert (Model.getMap().getCell(0, 3).getNorth() == null);
        assert (Model.getMap().getCell(0, 3).getSouth() == Model.getMap().getCell(1, 3));
        assert (Model.getMap().getCell(0, 3).getEast() == null);
        assert (Model.getMap().getCell(0, 3).getWest() == Model.getMap().getCell(0, 2));

        assertEquals(Model.getPlayer(0).getPlayerName(), "Player_1");
        assertEquals(Model.getPlayer(0).getColor(), playerColors.get(0));

        //checking that PowerUp deck is correctly initialized
        for (int i = 0; i < 24; i++) {
            assertNotNull(Model.getGame().drawPowerUp());
        }



    }

}