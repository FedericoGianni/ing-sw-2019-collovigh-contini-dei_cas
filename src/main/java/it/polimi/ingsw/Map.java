package it.polimi.ingsw;

import static it.polimi.ingsw.CellColor.*;

/**
 *
 */
public class Map {

    /**
     * Default constructor
     * mapType is set at 2 because this default type is valid for every number of players
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

                (this.matrix[0][0]) = new Cell();
                (this.matrix[0][1]) = new Cell();
                (this.matrix[0][2]) = new SpawnCell();
                (this.matrix[0][3]) = null;

                (this.matrix[1][0]) = new SpawnCell();
                (this.matrix[1][1]) = new Cell();
                (this.matrix[1][2]) = new Cell();
                (this.matrix[1][3]) = new Cell();

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new Cell();
                (this.matrix[2][2]) = new Cell();
                (this.matrix[2][3]) = new SpawnCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(this.matrix[1][0]);
                (this.matrix[0][0]).setAdjEast(this.matrix[0][1]);
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(BLUE);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(this.matrix[1][1]);
                (this.matrix[0][1]).setAdjEast(this.matrix[0][2]);
                (this.matrix[0][1]).setAdjWest(this.matrix[0][0]);
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(this.matrix[1][2]);
                (this.matrix[0][2]).setAdjEast(this.matrix[0][3]);
                (this.matrix[0][2]).setAdjWest(this.matrix[0][1]);
                (this.matrix[0][2]).setColor(YELLOW);

                (this.matrix[1][0]).setAdjNorth(this.matrix[0][0]);
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(this.matrix[1][1]);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(this.matrix[0][1]);
                (this.matrix[1][1]).setAdjSouth(this.matrix[2][1]);
                (this.matrix[1][1]).setAdjEast(this.matrix[1][2]);
                (this.matrix[1][1]).setAdjWest(this.matrix[1][0]);
                (this.matrix[1][1]).setColor(RED);

                (this.matrix[1][2]).setAdjNorth(this.matrix[0][2]);
                (this.matrix[1][2]).setAdjSouth(this.matrix[2][2]);
                (this.matrix[1][2]).setAdjEast(this.matrix[1][3]);
                (this.matrix[1][2]).setAdjWest(this.matrix[1][1]);
                (this.matrix[1][2]).setColor(PURPLE);

                (this.matrix[1][3]).setAdjNorth(null);
                (this.matrix[1][3]).setAdjSouth(this.matrix[2][3]);
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(this.matrix[1][2]);
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][0]) = null;

                (this.matrix[2][1]).setAdjNorth(this.matrix[1][1]);
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(this.matrix[2][2]);
                (this.matrix[2][1]).setAdjWest(this.matrix[2][0]);
                (this.matrix[2][1]).setColor(WHITE);

                (this.matrix[2][2]).setAdjNorth(this.matrix[1][2]);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(this.matrix[2][3]);
                (this.matrix[2][2]).setAdjWest(this.matrix[2][1]);
                (this.matrix[2][2]).setColor(WHITE);

                (this.matrix[2][3]).setAdjNorth(this.matrix[1][3]);
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(this.matrix[2][2]);
                (this.matrix[2][3]).setColor(YELLOW);

                break;

            case 2:

                (this.matrix[0][0]) = new Cell();
                (this.matrix[0][1]) = new Cell();
                (this.matrix[0][2]) = new SpawnCell();
                (this.matrix[0][3]) = new Cell();

                (this.matrix[1][0]) = new SpawnCell();
                (this.matrix[1][1]) = new Cell();
                (this.matrix[1][2]) = new Cell();
                (this.matrix[1][3]) = new Cell();

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new Cell();
                (this.matrix[2][2]) = new Cell();
                (this.matrix[2][3]) = new SpawnCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(this.matrix[1][0]);
                (this.matrix[0][0]).setAdjEast(this.matrix[0][1]);
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(BLUE);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(this.matrix[1][1]);
                (this.matrix[0][1]).setAdjEast(this.matrix[0][2]);
                (this.matrix[0][1]).setAdjWest(this.matrix[0][0]);
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(this.matrix[1][2]);
                (this.matrix[0][2]).setAdjEast(this.matrix[0][3]);
                (this.matrix[0][2]).setAdjWest(this.matrix[0][1]);
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[0][3]).setAdjNorth(null);
                (this.matrix[0][3]).setAdjSouth(this.matrix[1][3]);
                (this.matrix[0][3]).setAdjEast(null);
                (this.matrix[0][3]).setAdjWest(this.matrix[0][2]);
                (this.matrix[0][3]).setColor(GREEN);

                (this.matrix[1][0]).setAdjNorth(this.matrix[0][0]);
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(this.matrix[1][1]);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(this.matrix[0][1]);
                (this.matrix[1][1]).setAdjSouth(this.matrix[2][1]);
                (this.matrix[1][1]).setAdjEast(this.matrix[1][2]);
                (this.matrix[1][1]).setAdjWest(this.matrix[1][0]);
                (this.matrix[1][1]).setColor(RED);

                (this.matrix[1][2]).setAdjNorth(this.matrix[0][2]);
                (this.matrix[1][2]).setAdjSouth(this.matrix[2][2]);
                (this.matrix[1][2]).setAdjEast(this.matrix[1][3]);
                (this.matrix[1][2]).setAdjWest(this.matrix[1][1]);
                (this.matrix[1][2]).setColor(YELLOW);

                (this.matrix[1][3]).setAdjNorth(this.matrix[0][3]);
                (this.matrix[1][3]).setAdjSouth(this.matrix[2][3]);
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(this.matrix[1][2]);
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][1]).setAdjNorth(this.matrix[1][1]);
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(this.matrix[2][2]);
                (this.matrix[2][1]).setAdjWest(null);
                (this.matrix[2][1]).setColor(WHITE);

                (this.matrix[2][2]).setAdjNorth(this.matrix[1][2]);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(this.matrix[2][3]);
                (this.matrix[2][2]).setAdjWest(this.matrix[2][1]);
                (this.matrix[2][2]).setColor(YELLOW);

                (this.matrix[2][3]).setAdjNorth(this.matrix[1][3]);
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(this.matrix[2][2]);
                (this.matrix[2][3]).setColor(YELLOW);

                break;

            case 3:

                (this.matrix[0][0]) = new Cell();
                (this.matrix[0][1]) = new Cell();
                (this.matrix[0][2]) = new Cell();
                (this.matrix[0][3]) = new Cell();

                (this.matrix[1][0]) = new Cell();
                (this.matrix[1][1]) = new Cell();
                (this.matrix[1][2]) = new Cell();
                (this.matrix[1][3]) = new Cell();

                (this.matrix[2][0]) = new Cell();
                (this.matrix[2][1]) = new Cell();
                (this.matrix[2][2]) = new Cell();
                (this.matrix[2][3]) = new Cell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(this.matrix[1][0]);
                (this.matrix[0][0]).setAdjEast(this.matrix[0][1]);
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(RED);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(this.matrix[1][1]);
                (this.matrix[0][1]).setAdjEast(this.matrix[0][2]);
                (this.matrix[0][1]).setAdjWest(this.matrix[0][0]);
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(this.matrix[1][2]);
                (this.matrix[0][2]).setAdjEast(this.matrix[0][3]);
                (this.matrix[0][2]).setAdjWest(this.matrix[0][1]);
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[0][3]).setAdjNorth(null);
                (this.matrix[0][3]).setAdjSouth(this.matrix[1][3]);
                (this.matrix[0][3]).setAdjEast(null);
                (this.matrix[0][3]).setAdjWest(this.matrix[0][2]);
                (this.matrix[0][3]).setColor(GREEN);

                (this.matrix[1][0]).setAdjNorth(this.matrix[0][0]);
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(this.matrix[1][1]);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(this.matrix[0][1]);
                (this.matrix[1][1]).setAdjSouth(this.matrix[2][1]);
                (this.matrix[1][1]).setAdjEast(this.matrix[1][2]);
                (this.matrix[1][1]).setAdjWest(this.matrix[1][0]);
                (this.matrix[1][1]).setColor(PURPLE);

                (this.matrix[1][2]).setAdjNorth(this.matrix[0][2]);
                (this.matrix[1][2]).setAdjSouth(this.matrix[2][2]);
                (this.matrix[1][2]).setAdjEast(this.matrix[1][3]);
                (this.matrix[1][2]).setAdjWest(this.matrix[1][1]);
                (this.matrix[1][2]).setColor(YELLOW);

                (this.matrix[1][3]).setAdjNorth(this.matrix[0][3]);
                (this.matrix[1][3]).setAdjSouth(this.matrix[2][3]);
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(this.matrix[1][2]);
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][0]).setAdjNorth(this.matrix[1][0]);
                (this.matrix[2][0]).setAdjSouth(null);
                (this.matrix[2][0]).setAdjEast(this.matrix[2][1]);
                (this.matrix[2][0]).setAdjWest(null);
                (this.matrix[2][0]).setColor(WHITE);

                (this.matrix[2][1]).setAdjNorth(this.matrix[1][1]);
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(this.matrix[2][2]);
                (this.matrix[2][1]).setAdjWest(this.matrix[2][0]);
                (this.matrix[2][1]).setColor(WHITE);

                (this.matrix[2][2]).setAdjNorth(this.matrix[1][2]);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(this.matrix[2][3]);
                (this.matrix[2][2]).setAdjWest(this.matrix[2][1]);
                (this.matrix[2][2]).setColor(YELLOW);

                (this.matrix[2][3]).setAdjNorth(this.matrix[1][3]);
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(this.matrix[2][2]);
                (this.matrix[2][3]).setColor(YELLOW);

                break;

        }
    }
}