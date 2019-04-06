package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapTest {

    @Test
    void getMapTypeShouldReturnActualMapType() {
        Map m = new Map();
        assertEquals(1, m.getMap(1).getMapType());
    }

    @Test
    private Map getMap(int t) {
        return getMap(t);
    }

    @Test
    void generateCells(int t) {
        return;
    }
    @Test
    void generateCellsShouldGenerateRightCellAttributes() {
        Map m = new Map();
        m.getMap(1);
        m.generateCells(1);
        assert (m.getCell(0, 0).getColor() == CellColor.BLUE);
        assert (m.getCell(1, 0).getColor() == CellColor.RED);

    }
}