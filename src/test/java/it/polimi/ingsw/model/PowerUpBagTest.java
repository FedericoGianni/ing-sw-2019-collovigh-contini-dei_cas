package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.PowerUpDeck.populatedDeck;
import static org.junit.jupiter.api.Assertions.*;

class PowerUpBagTest {

    @Test
    void addItemShoudAddItemToPowerUpBag() {
        Map m = Map.genMap(2);

        Player p = new Player("test", 0, PlayerColor.BLUE);

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();
        PowerUp c2 = d.getRandomCard();
        PowerUp c3 = d.getRandomCard();
        PowerUp c4 = d.getRandomCard();

        p.getPowerUpBag().addItem(c1);
        assertTrue(c1.equals(p.getPowerUpBag().getList().get(0)));
        p.getPowerUpBag().addItem(c2);
        assertTrue(c2.equals(p.getPowerUpBag().getList().get(1)));
        p.getPowerUpBag().addItem(c3);
        assertTrue(c3.equals(p.getPowerUpBag().getList().get(2)));

        //max powerUp bag size == 3. it shouldn't let add a 4th item to the PowerUp bag
        p.getPowerUpBag().addItem(c4);
        assertThrows(IndexOutOfBoundsException.class,
                () -> p.getPowerUpBag().getList().get(3));
    }

    @Test
    void getItemShouldRemovePowerUpFromPowerUpBag() {

        Map m = Map.genMap(2);

        Player p = new Player("test", 1, PlayerColor.BLUE);

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();
        PowerUp c2 = d.getRandomCard();
        PowerUp c3 = d.getRandomCard();
        PowerUp c4 = d.getRandomCard();

        p.getPowerUpBag().addItem(c1);
        assertTrue(c1.equals(p.getPowerUpBag().getList().get(0)));
        p.getPowerUpBag().addItem(c2);
        assertTrue(c2.equals(p.getPowerUpBag().getList().get(1)));
        p.getPowerUpBag().addItem(c3);
        assertTrue(c3.equals(p.getPowerUpBag().getList().get(2)));

        try {
            p.getPowerUpBag().getItem(c3);
            assertTrue(p.getPowerUpBag().getList().size() == 2);
            assertTrue(c1.equals(p.getPowerUpBag().getList().get(0)));
            assertTrue(c2.equals(p.getPowerUpBag().getList().get(1)));
        }catch(CardNotPossessedException e){

            e.printStackTrace();
        }

    }

    @Test
    void sellItemShouldReturnAmmoCube() {

        List<String> names = new ArrayList<>();

        names.add("Frank");
        names.add("Alex");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.PURPLE);

        Model kat = new Model(names,colors,2);

        Player p = Model.getPlayer(0);

        PowerUpDeck d = PowerUpDeck.populatedDeck();
        PowerUp c1 = d.getRandomCard();

        assertEquals(0,p.getPowerUpBag().getList().size());
        p.getPowerUpBag().addItem(c1);
        assertEquals(1,p.getPowerUpBag().getList().size());

        System.out.println(p.getPowerUpBag().getList());

        AmmoCube a = new AmmoCube(Color.RED);

        assertTrue(p.getPowerUpBag().hasItem(c1));
        //sellItem should remove PowerUp from Player's PowerUpBag

        p.sellPowerUp(c1);

        assertEquals(0,p.getPowerUpBag().getList().size());
        assertTrue(!p.getPowerUpBag().hasItem(c1));

        assertEquals(1,p.getAmmo().size());
    }

    @Test
    void hasItemShouldReturnTrue() {

        Player p = new Player("Mark",0,PlayerColor.BLUE);

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();

        p.getPowerUpBag().addItem(c1);
        assertTrue(p.getPowerUpBag().hasItem(c1));

    }

    @Test
    void cloneCreatorShouldBeEqual(){

        Player p = new Player("Mark",0,PlayerColor.YELLOW);

        PowerUpDeck d = populatedDeck();
        PowerUp c1 = d.getRandomCard();

        p.getPowerUpBag().addItem(c1);

        PowerUpBag copy = new PowerUpBag(p.getPowerUpBag());

        assertEquals(p.getPowerUpBag().getList(),copy.getList());



    }
}