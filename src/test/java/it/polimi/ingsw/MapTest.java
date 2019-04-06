package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void getMapTypeShouldReturnActualMapType() {

        Map m = new Map();
        assertEquals(1, m.getMap(1).getMapType());
    }

    private Map getMap(int t) {
        return getMap(t);
    }

    @Test
    void generateCells() {
    }

    @Test
    void getPlacedWeapons() {
    }

    @Test
    void getIstance() {
    }
}