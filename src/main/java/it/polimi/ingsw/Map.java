package it.polimi.ingsw;

import static it.polimi.ingsw.CellColor.*;

/**
 *
 */
public class Map {

    /**
     * Default constructor
     * mapType is set at 2 because this type if valid for every number of players
     */
    public Map() {
        this.mapType = 2;
        this.matrix = new Cell[3][4];
    }

    private Cell[][] matrix;

    private Map singleton = null;

    private int mapType;

    public Cell[][] getMatrix() {
        return matrix.clone();
    }

    /**
     *
     * @param row int between 0 and 2 representing index of row in the matrix
     * @param col int between 0 and 3 representing index of column in the matrix
     * @return the Cell placed in row and col inside the matrix representing the Map
     */
    public Cell getCell(int row, int col){
        return this.matrix[row][col];
    }

    /**
     *
     * @return actual type of Map: 1, 2, 3
     */
    public int getMapType() {
        return mapType;
    }

    /**
     *
     * @param mapType integer representing the map type which can be chosen at the creation of the Map
     * @return a Map created based on the current playing map type
     */
    public Map getMap(int mapType){

        switch(mapType) {

            case 1:

                if(singleton == null) {
                    singleton = new Map();
                    singleton.mapType = 1;
                    singleton.generateCells(1);
                }
                break;

            case 2:

                if(singleton == null) {
                    singleton = new Map();
                    singleton.mapType = 2;
                    singleton.generateCells(2);
                }
                break;

            case 3:

                if(singleton == null){
                    singleton = new Map();
                    singleton.mapType = 3;
                    singleton.generateCells(3);
                }
                break;
        }

        return singleton;

    }

    /**
     *
     * @param mapType an integer representing the type of Map, passed as a parameter to populate the matrix of Cells in different modes
     */
    public void generateCells(int mapType) {

        switch (mapType){

            case 1:

                (this.matrix[0][0]) = new Cell(BLUE, null, matrix[1][0], matrix[0][1], null);
                (this.matrix[0][1]) = new Cell(BLUE, null,  matrix[1][1], matrix[0][2], matrix[0][0]);
                (this.matrix[0][2]) = new Cell(BLUE, null, matrix[1][2], matrix[0][3], matrix[0][1]);
                (this.matrix[0][3]) = null;

                (this.matrix[1][0]) = new Cell(RED, matrix[0][0], null, matrix[1][1], null);
                (this.matrix[1][1]) = new Cell(RED, matrix[0][1], matrix[2][1], matrix[1][2], matrix[1][0]);
                (this.matrix[1][2]) = new Cell(PURPLE, matrix[0][2], matrix[2][2], matrix[1][3], matrix[1][1]);
                (this.matrix[1][3]) = new Cell(YELLOW, null, matrix[2][3], null, matrix[1][2]);

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new Cell(WHITE, matrix[1][1], null, matrix[2][2], null);
                (this.matrix[2][2]) = new Cell(WHITE, matrix[1][2], null, matrix[2][3], matrix[2][1]);
                (this.matrix[2][3]) = new Cell(YELLOW, matrix[1][3], null, null, matrix[2][2]);

                break;

            case 2:

                (this.matrix[0][0]) = new Cell(BLUE, null, matrix[1][0], matrix[0][1], null);
                (this.matrix[0][1]) = new Cell(BLUE, null,  matrix[1][1], matrix[0][2], matrix[0][0]);
                (this.matrix[0][2]) = new Cell(BLUE, null, matrix[1][2], matrix[0][3], matrix[0][1]);
                (this.matrix[0][3]) = new Cell(GREEN, null, matrix[1][3], null, matrix[0][2]);

                (this.matrix[1][0]) = new Cell(RED, matrix[0][0], null, matrix[1][1], null);
                (this.matrix[1][1]) = new Cell(RED, matrix[0][1], matrix[2][1], matrix[1][2], matrix[1][0]);
                (this.matrix[1][2]) = new Cell(YELLOW, matrix[0][2], matrix[2][2], matrix[1][3], matrix[1][1]);
                (this.matrix[1][3]) = new Cell(YELLOW, matrix[0][3], matrix[2][3], null, matrix[1][2]);

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new Cell(WHITE, matrix[1][1], null, matrix[2][2], null);
                (this.matrix[2][2]) = new Cell(YELLOW, matrix[1][2], null, matrix[2][3], matrix[2][1]);
                (this.matrix[2][3]) = new Cell(YELLOW, matrix[1][3], null, null, matrix[2][2]);

                break;

            case 3:

                (this.matrix[0][0]) = new Cell(RED, null, matrix[1][0], matrix[0][1], null);
                (this.matrix[0][1]) = new Cell(BLUE, null,  matrix[1][1], matrix[0][2], matrix[0][0]);
                (this.matrix[0][2]) = new Cell(BLUE, null, matrix[1][2], matrix[0][3], matrix[0][1]);
                (this.matrix[0][3]) = new Cell(GREEN, null, matrix[1][3], null, matrix[0][2]);

                (this.matrix[1][0]) = new Cell(RED, matrix[0][0], null, matrix[1][1], null);
                (this.matrix[1][1]) = new Cell(PURPLE, matrix[0][1], matrix[2][1], matrix[1][2], matrix[1][0]);
                (this.matrix[1][2]) = new Cell(YELLOW, matrix[0][2], matrix[2][2], matrix[1][3], matrix[1][1]);
                (this.matrix[1][3]) = new Cell(YELLOW, matrix[0][3], matrix[2][3], null, matrix[1][2]);

                (this.matrix[2][0]) = new Cell(WHITE, matrix[1][0], null, matrix[2][1], null);
                (this.matrix[2][1]) = new Cell(WHITE, matrix[1][1], null, matrix[2][2], matrix[2][0]);
                (this.matrix[2][2]) = new Cell(YELLOW, matrix[1][2], null, matrix[2][3], matrix[2][1]);
                (this.matrix[2][3]) = new Cell(YELLOW, matrix[1][3], null, null, matrix[2][2]);

                break;

        }
    }
}