package it.polimi.ingsw;

import customsexceptions.PlayerNotSeeableException;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {


    @Test
    void getMapTypeShouldReturnActualMapType() {
        Map m = new Map();
        assertEquals(2, m.genMap(2).getMapType());
    }

    @Test
    void generateCellsShouldGenerateRightCellColor() {
        Map m = new Map();
        m.genMap(1);
        m.generateCells(1);

        assert (m.getCell(0, 0).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 1).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 2).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 3) == null);

        assert (m.getCell(1, 0).getColor() == CellColor.RED);
        assert (m.getCell(1, 1).getColor() == CellColor.RED);
        assert (m.getCell(1, 2).getColor() == CellColor.RED);
        assert (m.getCell(1, 3).getColor() == CellColor.YELLOW);

        assert (m.getCell(2, 0) == null);
        assert (m.getCell(2, 1).getColor() == CellColor.GREY);
        assert (m.getCell(2, 2).getColor() == CellColor.GREY);
        assert (m.getCell(2, 3).getColor() == CellColor.YELLOW);

        //Now changing to MapType 2 to check if colors are different as it should be
        m = new Map();
        m.genMap(2);
        m.generateCells(2);

        assert (m.getCell(0, 0).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 1).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 2).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 3).getColor() == CellColor.GREEN);

        assert (m.getCell(1, 0).getColor() == CellColor.RED);
        assert (m.getCell(1, 1).getColor() == CellColor.RED);
        assert (m.getCell(1, 2).getColor() == CellColor.YELLOW);
        assert (m.getCell(1, 3).getColor() == CellColor.YELLOW);

        assert (m.getCell(2, 0) == null);
        assert (m.getCell(2, 1).getColor() == CellColor.GREY);
        assert (m.getCell(2, 2).getColor() == CellColor.YELLOW);
        assert (m.getCell(2, 3).getColor() == CellColor.YELLOW);

        //Now changing mapType to 3 to check if colors change as they should
        m = new Map();
        m.genMap(3);
        m.generateCells(3);

        assert (m.getCell(0, 0).getColor() == CellColor.RED);
        assert (m.getCell(0, 1).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 2).getColor() == CellColor.BLUE);
        assert (m.getCell(0, 3).getColor() == CellColor.GREEN);

        assert (m.getCell(1, 0).getColor() == CellColor.RED);
        assert (m.getCell(1, 1).getColor() == CellColor.PURPLE);
        assert (m.getCell(1, 2).getColor() == CellColor.YELLOW);
        assert (m.getCell(1, 3).getColor() == CellColor.YELLOW);

        assert (m.getCell(2, 0).getColor() == CellColor.GREY);
        assert (m.getCell(2, 1).getColor() == CellColor.GREY);
        assert (m.getCell(2, 2).getColor() == CellColor.YELLOW);
        assert (m.getCell(2, 3).getColor() == CellColor.YELLOW);

    }

    @Test
    void getAdjShouldReturnRightCell() {
        Map m = new Map();
        m.genMap(1);
        m.generateCells(1);

        assert (m.getCell(0, 0).getNorth() == null);
        assert (m.getCell(0, 0).getSouth() == m.getCell(1, 0));
        assert (m.getCell(0, 0).getEast() == m.getCell(0, 1));
        assert (m.getCell(0, 0).getWest() == null);

        assert (m.getCell(0, 1).getNorth() == null);
        assert (m.getCell(0, 1).getSouth() == null);
        assert (m.getCell(0, 1).getEast() == m.getCell(0, 2));
        assert (m.getCell(0, 1).getWest() == m.getCell(0, 0));

        assert (m.getCell(0, 2).getNorth() == null);
        assert (m.getCell(0, 2).getSouth() == m.getCell(1, 2));
        assert (m.getCell(0, 2).getEast() == m.getCell(0, 3));
        assert (m.getCell(0, 2).getWest() == m.getCell(0, 1));

        assert (m.getCell(0, 3) == null);

        //Now changing Map type to 2 and checking if Cell[0][3] is not null anymore
        m = new Map();
        m.genMap(2);
        m.generateCells(2);

        assert (m.getCell(0, 3).getNorth() == null);
        assert (m.getCell(0, 3).getSouth() == m.getCell(1, 3));
        assert (m.getCell(0, 3).getEast() == null);
        assert (m.getCell(0, 3).getWest() == m.getCell(0, 2));
    }

    @Test
    void cellToCoord() {
        Map m = new Map();
        m.genMap(1);
        m.generateCells(1);

        assertEquals(m.cellToCoord(m.getCell(0, 0)), new Point(0, 0));
        assertEquals(m.cellToCoord(m.getCell(1, 2)), new Point(1, 2));
        assertEquals(null, m.cellToCoord(m.getCell(2, 0)));
    }

    @Test
    void getShootingDistShouldReturnRightDist() {

        Map map = new Map();
        map.genMap(2);
        map.generateCells(2);

        Player p1 = new Player("Shooter", map.getCell(1, 3));
        Player p2 = new Player("Visible", map.getCell(0, 3));
        Player p3 = new Player("NotVisible", map.getCell(0, 0));
        try {
            assertEquals(1, map.getShootingDist(p1, p2, map.getMap()));
            assertThrows(PlayerNotSeeableException.class,
                    () -> map.getShootingDist(p1, p3, map.getMap()));
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getShootingDistShouldReturnRightDist2() {
        //testing player with different positions

        Map map = new Map();
        map.genMap(2);
        map.generateCells(2);

        Player p1 = new Player("Shooter", map.getCell(1,3));
        Player p2 = new Player("Visible", map.getCell(0,3));
        Player p3 = new Player("NotVisible", map.getCell(0,0));
        Player p4 = new Player("Visible2", map.getCell(2,1));
        Player p5 = new Player("VisibleMeleeRange", map.getCell(1,3));

        try{
            assertEquals(1, map.getShootingDist(p1, p2, map.getMap()));
            assertEquals(0, map.getShootingDist(p1, p5, map.getMap()));
            assertEquals(3, map.getShootingDist(p1, p4, map.getMap()));
            assertThrows(PlayerNotSeeableException.class,
                    () -> map.getShootingDist(p1, p3, map.getMap()));

        } catch (PlayerNotSeeableException e){
            e.printStackTrace();
        }
    }





}