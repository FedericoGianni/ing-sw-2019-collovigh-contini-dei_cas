package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {

    @Test
    void getColor() {

        PowerUp up = new TagbackGrenade(Color.BLUE);
        PowerUp up1 = new Newton(Color.BLUE);

        assertEquals(up.getColor(),up1.getColor());
    }
}