package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.PowerUpDeck.populatedDeck;
import static org.junit.jupiter.api.Assertions.*;

class PowerUpBagTest {

    @Test
    void addItemShoudAddItemToPowerUpBag() {
        Map m = new Map();
        m.genMap(2);
        m.generateCells(2);

        Player p = new Player("test", m.getCell(0,2));

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();
        PowerUp c2 = d.getRandomCard();
        PowerUp c3 = d.getRandomCard();
        PowerUp c4 = d.getRandomCard();

        p.getCurrentPowerUps().addItem(c1);
        assertTrue(c1.equals(p.getCurrentPowerUps().getList().get(0)));
        p.getCurrentPowerUps().addItem(c2);
        assertTrue(c2.equals(p.getCurrentPowerUps().getList().get(1)));
        p.getCurrentPowerUps().addItem(c3);
        assertTrue(c3.equals(p.getCurrentPowerUps().getList().get(2)));
        //max powerUp bag size == 3. it shouldn't let add a 4th item to the PowerUp bag
        p.getCurrentPowerUps().addItem(c4);
        assertThrows(IndexOutOfBoundsException.class,
                () -> p.getCurrentPowerUps().getList().get(3));
    }

    @Test
    void getItemShouldRemovePowerUpFromPowerUpBag() {

        Map m = new Map();
        m.genMap(2);
        m.generateCells(2);

        Player p = new Player("test", m.getCell(0,2));

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();
        PowerUp c2 = d.getRandomCard();
        PowerUp c3 = d.getRandomCard();
        PowerUp c4 = d.getRandomCard();

        p.getCurrentPowerUps().addItem(c1);
        assertTrue(c1.equals(p.getCurrentPowerUps().getList().get(0)));
        p.getCurrentPowerUps().addItem(c2);
        assertTrue(c2.equals(p.getCurrentPowerUps().getList().get(1)));
        p.getCurrentPowerUps().addItem(c3);
        assertTrue(c3.equals(p.getCurrentPowerUps().getList().get(2)));

        p.getPowerUpBag().getItem(c3);
        assertTrue(p.getPowerUpBag().getList().size() == 2);
        assertTrue(c1.equals(p.getCurrentPowerUps().getList().get(0)));
        assertTrue(c2.equals(p.getCurrentPowerUps().getList().get(1)));

    }

    @Test
    void sellItemShouldReturnAmmoCube() {

        Map m = new Map();
        m.genMap(2);
        m.generateCells(2);

        Player p = new Player("test", m.getCell(0,2));

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();

        assert(p.getCurrentPowerUps().getList().size() == 0);
        p.getCurrentPowerUps().addItem(c1);
        assert(p.getCurrentPowerUps().getList().size() == 1);

        AmmoCube a = new AmmoCube(Color.RED);

        assertEquals(p.getPowerUpBag().sellItem(c1).getClass(), a.getClass());
        //sellItem should remove PowerUp from Player's PowerUpBag
        p.getPowerUpBag().sellItem(c1);
        assert(p.getCurrentPowerUps().getList().size() == 0);
        assert(!p.getCurrentPowerUps().hasItem(c1));
    }

    @Test
    void hasItemShouldReturnTrue() {


        Map m = new Map();
        m.genMap(2);
        m.generateCells(2);

        Player p = new Player("test", m.getCell(0,2));

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();

        p.getCurrentPowerUps().addItem(c1);
        assertTrue(p.getPowerUpBag().hasItem(c1));

    }
}