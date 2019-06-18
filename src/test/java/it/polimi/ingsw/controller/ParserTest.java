package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.saveutils.SavedMap;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void getMap1() {

        SavedMap savedMap= Parser.getMap(1);

        assertNotNull(savedMap);

        Cell[][] matrix = savedMap.getRealMap();

        System.out.println( "\nMAP 1 :");

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

            }
        }
    }

    @Test
    void getMap2() {

        SavedMap savedMap= Parser.getMap(2);

        assertNotNull(savedMap);

        Cell[][] matrix = savedMap.getRealMap();

        System.out.println( "\nMAP 2 :");

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

            }
        }
    }

    @Test
    void getMap3() {

        SavedMap savedMap= Parser.getMap(3);

        assertNotNull(savedMap);

        Cell[][] matrix = savedMap.getRealMap();

        System.out.println( "\nMAP 3 :");

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

            }
        }
    }

    @Test
    void shouldSaveAndRestoreMap(){

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Cell[][] matrix = Model.getMap().getMatrix();

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,2));

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(1,1));

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(2,2));

        Parser.addGame();

        Parser.saveMap();

        assertNotNull(Parser.readSavedMap().getRealMap());

        Cell[][] saved = Parser.readSavedMap().getRealMap();

        // check player

        assertEquals(matrix[0][2].getPlayers(), saved[0][2].getPlayers());

        //check Weapons

        assertEquals(matrix[0][2].getWeapons().stream().map(Weapon::getName).collect(Collectors.toList()), saved[0][2].getWeapons().stream().map(Weapon::getName).collect(Collectors.toList()));

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "[SOURCE] Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

                System.out.println((saved[i][j] == null) ? "[SAVE] Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + saved[i][j].getColor());


                System.out.println("");

            }

            System.out.println("\n");
        }

        Parser.clearGames();
    }



    @Test
    void containsGame() {

        Parser.clearGames();

        int gameId = Parser.addGame(); // game 0

        assertTrue(Parser.containsGame(gameId));

        Parser.clearGames();
    }

    @Test
    void addGame() {

        int size = Parser.getGamesSize();

        Parser.addGame();
        Parser.addGame();

        assertEquals(size + 2, Parser.getGamesSize());

        Parser.clearGames();
    }

    @Test
    void closeGame() {

        try {

            Parser.clearGames();

            int size = Parser.getGamesSize();

            int gameId = Parser.addGame();

            assertTrue(Parser.containsGame(gameId));

            Parser.closeGame(gameId);

            assertFalse(Parser.containsGame(gameId));

        }catch (GameNonExistentException e){

            e.printStackTrace();
        }

        Parser.clearGames();

    }

    @Test
    void clearGames() {

        Parser.clearGames();

        assertEquals(0,Parser.getGamesSize());

    }
}