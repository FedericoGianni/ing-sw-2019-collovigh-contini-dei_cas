package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.model.map.*;

import java.awt.*;
import java.io.Serializable;

import static it.polimi.ingsw.utils.CellColor.*;

/**
 * This class is used to serialize the Model game Map in a file to be read, allowing game persistence
 */
/**
 * This class is used to save / load maps from json: since in the model cell have reference to other cells them were not easily writable and could generate problem.
 * so this class is used to translate the Map cell matrix to a matrix that can easily saved to json
 */
public class SavedMap implements Serializable {

    /**
     * This is a const that states the max row of the matrix
     */
    private static final int MAP_R = Map.MAP_R;

    /**
     * This is a const that states the max column of the matrix
     */
    private static final int MAP_C = Map.MAP_C;

    /**
     * This is a matrix of SavedCell
     */
    private final SavedCell[][] matrix;

    /**
     * This is the map Type
     */
    private final int mapType;

    /**
     * Constructor: takes as param a matrix of Cells (Spawn or Ammo) and translate them into a matrix of SavedCell
     * @param map is a matrix of Cells (Spawn or Ammo)
     */
    public SavedMap(Cell[][] map, int mapType) {

        this.mapType = mapType;

        this.matrix = new SavedCell[MAP_R][MAP_C];

        for (int i = 0; i < MAP_R; i++) {

            for (int j = 0; j < MAP_C; j++) {

                this.matrix[i][j] = (map[i][j] == null) ? null : map[i][j].getSaveVersionOfCell();

            }
        }
    }

    public int getMapType() {
        return mapType;
    }

    /**
     * method that translate the SavedCell matrix stored in the class into a real Cell matrix
     * @return a matrix of Cells (Spawn or Ammo)
     */
    public Cell[][] getRealMap(){

        Cell[][] realMap = new Cell[MAP_R][MAP_C];

        for (int i = 0; i < MAP_R; i++) {

            for (int j = 0; j < MAP_C; j++) {

                realMap[i][j] = (matrix[i][j] == null) ? null : matrix[i][j].getRealCell();

            }
        }

        return realMap;
    }


}
