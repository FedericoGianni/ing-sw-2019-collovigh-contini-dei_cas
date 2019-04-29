package it.polimi.ingsw.model;

import customsexceptions.PlayerNotSeeableException;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapTest {


    @Test
    void getMapTypeShouldReturnActualMapType() {
        Map m = new Map();
        assertEquals(2, m.genMap(2).getMapType());
    }

    @Test
    void generateCellsShouldGenerateRightCellColor() {
        Map m = Map.genMap(1);

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
        m = Map.genMap(2);

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
        m = Map.genMap(3);

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
        Map m = Map.genMap(1);

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
        Map m = Map.genMap(1);

        assertEquals(m.cellToCoord(m.getCell(0, 0)), new Point(0, 0));
        assertEquals(m.cellToCoord(m.getCell(1, 2)), new Point(1, 2));
        assertEquals(null, m.cellToCoord(m.getCell(2, 0)));
    }

    @Test
    void getShootingDistShouldReturnRightDist() {

        Map map = Map.genMap(2);

        Player p1 = new Player("Shooter", 0, PlayerColor.GREY);
        p1.setPlayerPos(map.getCell(1, 3));

        Player p2 = new Player("Visible", 1, PlayerColor.GREEN);
        p2.setPlayerPos(map.getCell(0, 3));

        Player p3 = new Player("NotVisible", 2, PlayerColor.YELLOW);
        p3.setPlayerPos(map.getCell(0, 0));

        try {
            assertEquals(1, map.getShootingDist(p1, p2, map.getMapClone()));
            assertThrows(PlayerNotSeeableException.class,
                    () -> map.getShootingDist(p1, p3, map.getMapClone()));
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getShootingDistShouldReturnRightDist2() {
        //testing player with different positions

        Map map = Map.genMap(2);

        Player p1=new Player("Shooter", 0, PlayerColor.BLUE);
        p1.setPlayerPos(map.getCell(1,3));

        Player p2=new Player("Visible", 1, PlayerColor.PURPLE);
        p2.setPlayerPos(map.getCell(0,3));

        Player p3=new Player("NotVisible", 2, PlayerColor.YELLOW);
        p3.setPlayerPos(map.getCell(0,0));

        Player p4=new Player("Visible2",3, PlayerColor.GREEN);
        p4.setPlayerPos(map.getCell(2,1));

        Player p5=new Player("VisibleMeleeRange", 4, PlayerColor.GREY);//will be the first seen beacuse the players are ordinated in distance
        p5.setPlayerPos(map.getCell(1,3));

        try{
            assertEquals(1, map.getShootingDist(p1, p2, map.getMapClone()));
            assertEquals(0, map.getShootingDist(p1, p5, map.getMapClone()));
            assertEquals(3, map.getShootingDist(p1, p4, map.getMapClone()));
            assertThrows(PlayerNotSeeableException.class,
                    () -> map.getShootingDist(p1, p3, map.getMapClone()));

        } catch (PlayerNotSeeableException e){
            e.printStackTrace();
        }
    }





}