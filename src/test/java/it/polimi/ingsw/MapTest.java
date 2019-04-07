package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapTest {

    @Test
    void getMapTypeShouldReturnActualMapType() {
        Map m = new Map();
        assertEquals(1, m.getMap(1).getMapType());

        m = new Map();
        assertEquals(2, m.getMap(2).getMapType());

        m = new Map();
        assertEquals(3, m.getMap(3).getMapType());
    }

    @Test
    void generateCellsShouldGenerateRightCellColor() {
        Map m = new Map();
        m.getMap(1);
        m.generateCells(1);

        assert(m.getCell(0, 0).getColor() == CellColor.BLUE);
        assert(m.getCell(0, 1).getColor() == CellColor.BLUE);
        assert(m.getCell(0, 2).getColor() == CellColor.BLUE);
        assert(m.getCell(0, 3) == null);

        assert(m.getCell(1, 0).getColor() == CellColor.RED);
        assert(m.getCell(1, 1).getColor() == CellColor.RED);
        assert(m.getCell(1, 2).getColor() == CellColor.PURPLE);
        assert(m.getCell(1, 3).getColor() == CellColor.YELLOW);

        assert(m.getCell(2, 0) == null);
        assert(m.getCell(2, 1).getColor() == CellColor.WHITE);
        assert(m.getCell(2, 2).getColor() == CellColor.WHITE);
        assert(m.getCell(2, 3).getColor() == CellColor.YELLOW);

        //Now changin to MapType 2 to check if colors are different as it should be
        m = new Map();
        m.getMap(2);
        m.generateCells(2);

        assert(m.getCell(0,0).getColor() == CellColor.BLUE);
        assert(m.getCell(0,1).getColor() == CellColor.BLUE);
        assert(m.getCell(0,2).getColor() == CellColor.BLUE);
        assert(m.getCell(0,3).getColor() == CellColor.GREEN);

        assert(m.getCell(1, 0).getColor() == CellColor.RED);
        assert(m.getCell(1, 1).getColor() == CellColor.RED);
        assert(m.getCell(1, 2).getColor() == CellColor.YELLOW);
        assert(m.getCell(1, 3).getColor() == CellColor.YELLOW);

        assert(m.getCell(2, 0) == null);
        assert(m.getCell(2, 1).getColor() == CellColor.WHITE);
        assert(m.getCell(2, 2).getColor() == CellColor.YELLOW);
        assert(m.getCell(2, 3).getColor() == CellColor.YELLOW);

        //Now changing mapType to 3 to check if colors change as they should
        m = new Map();
        m.getMap(3);
        m.generateCells(3);

        assert(m.getCell(0,0).getColor() == CellColor.RED);
        assert(m.getCell(0,1).getColor() == CellColor.BLUE);
        assert(m.getCell(0,2).getColor() == CellColor.BLUE);
        assert(m.getCell(0,3).getColor() == CellColor.GREEN);

        assert(m.getCell(1, 0).getColor() == CellColor.RED);
        assert(m.getCell(1, 1).getColor() == CellColor.PURPLE);
        assert(m.getCell(1, 2).getColor() == CellColor.YELLOW);
        assert(m.getCell(1, 3).getColor() == CellColor.YELLOW);

        assert(m.getCell(2, 0).getColor() == CellColor.WHITE);
        assert(m.getCell(2, 1).getColor() == CellColor.WHITE);
        assert(m.getCell(2, 2).getColor() == CellColor.YELLOW);
        assert(m.getCell(2, 3).getColor() == CellColor.YELLOW);

    }

    @Test
    void getAdjShouldReturnRightCell(){
        Map m = new Map();
        m.getMap(1);
        m.generateCells(1);

        assert(m.getCell(0, 0).getNorth() == null);
        assert(m.getCell(0,0).getSouth() == m.getCell(1, 0));
        assert(m.getCell(0, 0).getEast() == m.getCell(0, 1));
        assert(m.getCell(0, 0).getWest() == null);

        assert(m.getCell(0, 1).getNorth() == null);
        assert(m.getCell(0,1).getSouth() == null);
        assert(m.getCell(0, 1).getEast() == m.getCell(0, 2));
        assert(m.getCell(0, 1).getWest() == m.getCell(0,0));

        assert(m.getCell(0, 2).getNorth() == null);
        assert(m.getCell(0,2).getSouth() == m.getCell(1, 2));
        assert(m.getCell(0, 2).getEast() == m.getCell(0, 3));
        assert(m.getCell(0, 2).getWest() == m.getCell(0,1));

        assert(m.getCell(0,3) == null);

        //Now changing Map type to 2 and checking if Cell[0][3] is not null anymore
        m = new Map();
        m.getMap(2);
        m.generateCells(2);

        assert(m.getCell(0,3).getNorth() == null);
        assert(m.getCell(0, 3).getSouth() == m.getCell(1,3));
        assert(m.getCell(0,3).getEast() == null);
        assert(m.getCell(0,3).getWest() == m.getCell(0,2));
    }

}