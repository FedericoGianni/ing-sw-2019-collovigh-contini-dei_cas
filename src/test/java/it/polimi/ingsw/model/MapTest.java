package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.utils.CellColor;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapTest {


    @Test
    void getMapTypeShouldReturnActualMapType() {
        Map m = new Map(2);
        assertEquals(2, m.getMapType());
    }

    @Test
    void generateCellsShouldGenerateRightCellColor() {
        //Map m = Map.genMap(1);
        Map m = new Map(1);


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
        //m = Map.genMap(2);
        m = new Map(2);

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
        m = new Map(3);

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
        //Map m = Map.genMap(1);


        Model model = new Model(new ArrayList<>(),new ArrayList<>(),1,8);

        Map m = Model.getMap();

        assert (m.getCell(0, 0).getNorth() == null);
        assertEquals (m.getCell(0, 0).getSouth(), m.getCell(1, 0));
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

        model = new Model(new ArrayList<>(),new ArrayList<>(),2,8);

        m = Model.getMap();

        assert (m.getCell(0, 3).getNorth() == null);
        assert (m.getCell(0, 3).getSouth() == m.getCell(1, 3));
        assert (m.getCell(0, 3).getEast() == null);
        assert (m.getCell(0, 3).getWest() == m.getCell(0, 2));
    }

    @Test
    void cellToCoord() {
        //Map m = Map.genMap(1);
        Map m = new Map(1);

        assertEquals(m.cellToCoord(m.getCell(0, 0)), new Point(0, 0));
        assertEquals(m.cellToCoord(m.getCell(1, 2)), new Point(1, 2));
        assertEquals(null, m.cellToCoord(m.getCell(2, 0)));
    }

    @Test
    void getShootingDistShouldReturnRightDist() {



        ArrayList<String> pn=new ArrayList();
        pn.add("shooter");
        pn.add("visible");
        pn.add("unvisible");

        ArrayList <PlayerColor> c=new ArrayList();
        c.add(PlayerColor.GREY);
        c.add(PlayerColor.GREEN);
        c.add(PlayerColor.YELLOW);
        Model m =new Model(pn,c,2,8);

        Player p1=Model.getPlayer(0);
        Player p2=Model.getPlayer(1);
        Player p3=Model.getPlayer(2);
        p1.setPlayerPos(Model.getMap().getCell(1, 3));
        p2.setPlayerPos(Model.getMap().getCell(0, 3));
        p3.setPlayerPos(Model.getMap().getCell(0, 0));

            assertEquals(1, Model.getMap().getDist(p1, p2));


    }





}