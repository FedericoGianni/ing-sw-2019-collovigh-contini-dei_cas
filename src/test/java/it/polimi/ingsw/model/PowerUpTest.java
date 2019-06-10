package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.powerup.TagbackGrenade;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerUpTest {

    @Test
    void getColor() {

        PowerUp up = new TagbackGrenade(Color.BLUE);
        PowerUp up1 = new Newton(Color.BLUE);

        assertEquals(up.getColor(),up1.getColor());
    }
}