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

        if (!Model.getGame().getPlayers().contains(p)){

            throw new PlayerNonExistentException();
        }

        //be careful that Point type default x and y are float, not integers
        Point coord = cellCardinalMove(p.getCurrentPosition(),directions,amount);
        Cell dest = Model.getMap().getCell(coord.y,coord.x);

        if (!Model.getMap().hasCell(dest)){

            throw new CellNonExistentException();
        }else{

            p.setPlayerPos(dest);
        }


    }

    /**
     *
     * @param cell is the starting point
     * @param directions in which the player is moved
     * @param amount is the number of movement if it exit the matrix the value will remain the limit
     * @return the coord of the new position
     */
    private Point cellCardinalMove(Cell cell,Directions directions, int amount){

        Point base = Model.getMap().cellToCoord(cell);

        if (amount >= MAX_MOVE) amount = MAX_MOVE;

        if (directions == Directions.NORTH){

            base.y = max(base.y - amount,0);
        }

        if (directions == Directions.SOUTH){

            base.y = min(base.y + amount,2);
        }

        if (directions == Directions.EAST){

            base.x = min(base.x + amount,3);
        }

        if (directions == Directions.WEST){

            base.x = max(base.x - amount,0);
        }

        return base;
    }


}