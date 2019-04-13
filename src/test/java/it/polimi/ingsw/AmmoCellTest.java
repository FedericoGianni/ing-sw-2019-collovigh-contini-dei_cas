package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCellTest {

    @Test
    void getColor() {
        Map m = new Map();
        m.genMap(1);
        m.generateCells(1);

        assertEquals(m.getCell(0, 0).getColor(), CellColor.BLUE);
    }

    @Test
    void getAmmoPlaced() {
        Map m = new Map();
        m.genMap(1);
        m.generateCells(1);

        AmmoCard card = AmmoCard.generateRandCard();

        assert (m.getCell(0, 0).getAmmoPlaced() != null);
        assertEquals(m.getCell(0, 1).getAmmoPlaced().getClass(), card.getClass());
    }

    @Test
    void pickAmmoPlaced() {
        Map m = new Map();
        m.genMap(1);
        m.generateCells(1);

        AmmoCard c1;
        AmmoCard c2;
        m.getCell(0, 0).pickAmmoPlaced();

        c1 = m.getCell(0, 0).pickAmmoPlaced();
        c2 = m.getCell(0, 0).pickAmmoPlaced();

        //Note that there's a chance that the method that generate a random PowerUp returns a randomly generated
        //PowerUp which is the same as the old one
        assertNotEquals(c1, c2);
    }
}