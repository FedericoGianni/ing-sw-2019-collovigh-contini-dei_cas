package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.customsexceptions.PlayerNonExistentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.Player;

import java.awt.*;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * this Class represents the Newton PowerUp
 */
public class Newton extends PowerUp {

    private static final int MAX_MOVE = 2;

    public Newton(Color color) {

        super(color);

        this.setType(PowerUpType.NEWTON);
    }

    /**
     * need to be implemented
     * this class perform a check to see if the cell distance is under 2 and then moves the player
     *
     * @param p is the player to move
     * @param directions is the cardinal direction the player will be moved
     * @param amount is the number of movement if it exit the matrix the value will remain the limit
     */
    public void use(Player p, Directions directions, int amount) throws PlayerNonExistentException,CellNonExistentException{

        // checks if the player exist in the model

        if (!Model.getGame().getPlayers().contains(p)){

            // if not throws exception

            throw new PlayerNonExistentException();
        }

        // gets the player position

        Cell dest = p.getCurrentPosition();

        // gets the final player position

        for (int i = 0; i < min(MAX_MOVE,amount); i++) {

            dest = cellCardinalMove(dest,directions);

        }

        // checks if the computed position exist in the current map

        if (!Model.getMap().hasCell(dest)){

            throw new CellNonExistentException();

        }else{

            p.setPlayerPos(dest);
        }


    }

    /**
     *
     * @param cell is the starting point
     * @param direction in which the player is moved
     * @return the coord of the new position
     */
    private Cell cellCardinalMove(Cell cell,Directions direction){

        switch (direction){

            case NORTH:

                return (cell.getNorth() == null) ? cell : cell.getNorth();

            case SOUTH:

                return (cell.getSouth() == null) ? cell : cell.getSouth();

            case WEST:

                return (cell.getWest() == null) ? cell : cell.getWest();

            case EAST:

                return (cell.getEast() == null) ? cell : cell.getEast();

            default:

                return cell;
        }
    }


}