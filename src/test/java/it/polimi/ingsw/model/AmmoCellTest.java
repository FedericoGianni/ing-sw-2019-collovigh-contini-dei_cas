package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AmmoCellTest {

    @Test
    void getColor() {
        Map m = Map.genMap(2);

        assertEquals(m.getCell(0, 0).getColor(), CellColor.BLUE);
    }

    @Test
    void getAmmoPlaced() {
        Map m = Map.genMap(2);

        AmmoCard card = AmmoCard.generateRandCard();

        assert (m.getCell(0, 0).getAmmoPlaced() != null);
        assertEquals(m.getCell(0, 1).getAmmoPlaced().getClass(), card.getClass());
    }

    @Test
    void pickAmmoPlaced() {
        Map m = Map.genMap(1);

        AmmoCard c1;
        AmmoCard c2;

        c1 = m.getCell(0, 0).pickAmmoPlaced();
        c2 = m.getCell(0, 0).pickAmmoPlaced();

        //Note that there's a chance that the method that generate a random PowerUp returns a randomly generated
        //PowerUp which is the same as the old one
        assertNotEquals(c1, c2);
    }

    @Test
    void cloneShouldBeEqual(){

        List<String> names=new ArrayList<String>();
        names.add("shooter");
        names.add("target");

        List<PlayerColor> pc=new ArrayList<PlayerColor>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);

        Model model=new Model(names,pc,1);

        Map m = Model.getMap();

        AmmoCell copy = new AmmoCell((AmmoCell)m.getCell(0,0));

        assertEquals(m.getCell(0,0).getColor(),copy.getColor());
        assertEquals(m.getCell(0,0).getPlayers(),copy.getPlayers());
        assertEquals(m.getCell(0,0).getEast(),copy.getEast());
        assertEquals(m.getCell(0,0).getWest(),copy.getWest());
        assertEquals(m.getCell(0,0).getNorth(),copy.getNorth());
        assertEquals(m.getCell(0,0).getSouth(),copy.getSouth());

        assertEquals(m.getCell(0,0).getAmmoPlaced(),copy.getAmmoPlaced());


    }
}