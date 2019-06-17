package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.saveutils.SavedMap;
import it.polimi.ingsw.model.map.Cell;
import org.junit.jupiter.api.Test;

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
}