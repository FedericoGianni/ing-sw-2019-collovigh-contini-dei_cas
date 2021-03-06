package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.utils.Directions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void getAdjacencienceDirection() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);

        Model model = new Model(names,colors,2,8);

        Cell cell = Model.getMap().getCell(1,2);

        //north

        assertEquals(Directions.NORTH, cell.getAdjacencienceDirection(Model.getMap().getCell(0,2)));

        assertEquals(Directions.EAST, cell.getAdjacencienceDirection(Model.getMap().getCell(1,3)));

        assertEquals(Directions.SOUTH, cell.getAdjacencienceDirection(Model.getMap().getCell(2,2)));

    }

    @Test
    void getCellAdj() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);

        Model model = new Model(names,colors,2,8);

        Cell cell = Model.getMap().getCell(1,2);

        //north

        assertEquals(cell.getNorth(), cell.getCellAdj(Directions.NORTH));

        //south

        assertEquals(cell.getSouth(),cell.getCellAdj(Directions.SOUTH));

        //west

        assertEquals(cell.getWest(),cell.getCellAdj(Directions.WEST));

        //east

        assertEquals(cell.getEast(),cell.getCellAdj(Directions.EAST));

    }

    @Test
    void getPlayers() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);

        Model model = new Model(names,colors,2,8);

        Player player_01 = Model.getPlayer(0);
        Player player_02 = Model.getPlayer(1);

        Cell cell = Model.getMap().getCell(0,0);

        cell.addPlayerHere(player_01);
        cell.addPlayerHere(player_02);

        assertEquals(Arrays.asList(player_01,player_02),cell.getPlayers());

    }
}