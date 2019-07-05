package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represent the RailGun special weapon
 *
 * @see SpecialWeapons
 */
public class RailGun extends SpecialWeapons {

    /**
     * name of the weapon
     */
    private static final String RAIL_GUN_NAME = "RAILGUN";

    /**
     * dmg given by the base effect
     */
    private static final int DMG_FIRST_EFFECT = 3;

    /**
     * dmg given by the second effect
     */
    private static final int DMG_SECOND_EFFECT = 2;

    /**
     * Reload cost
     */
    private final List<AmmoCube> costBaseEffect;

    /**
     * Second effect cost
     */
    private final List<AmmoCube> costSecondEffect;

    /**
     * Constructor
     */
    public RailGun( ) {
        super(RAIL_GUN_NAME);
        this.costBaseEffect = Arrays.asList(new AmmoCube(Color.YELLOW), new AmmoCube(Color.YELLOW),new AmmoCube(Color.BLUE));
        this.costSecondEffect = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws DifferentPlayerNeededException, WeaponNotLoadedException, PlayerAlreadyDeadException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        if ( (effects == null ) || (effects.size() != 1) || (!Arrays.asList(0,1).containsAll(effects)) ) throw new UncorrectEffectsException();

        if ( ! this.isLoaded() ) throw new WeaponNotLoadedException();

        if ( (targetLists == null) || ( (targetLists.stream().flatMap(List::stream).count() != 1) && (targetLists.stream().flatMap(List::stream).count() != 2) ) ) throw new NotCorrectPlayerNumberException();

        if ( effects.contains(0)) return preShootBase(targetLists.get(0));

        if ( effects.contains(1)) return preShootSecond(targetLists.get(0));

        checkPlayerAlreadyDead(targetLists);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerAlreadyDeadException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException, PrecedentPlayerNeededException {

        if (preShoot(targetLists, effects, cells)){

            if (effects.contains(0)) baseShoot(targetLists.get(0));

            if (effects.contains(1)) secondEffectShoot(targetLists.get(0));

            this.setLoaded(false);
        }

    }

    /**
     * This method will perform a base effect shot
     * @param targets is the list of targets
     */
    private void baseShoot(List<Player> targets){

        targets.get(0).addDmg(isPossessedBy().getPlayerId(),DMG_FIRST_EFFECT);
    }

    /**
     * This method will perform a second effect shot
     * @param targets is the list of targets
     */
    private void secondEffectShoot(List<Player> targets){

        for (Player player : targets){

            player.addDmg(isPossessedBy().getPlayerId(),DMG_SECOND_EFFECT);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {
        return costBaseEffect;
    }

    /**
     * This method will say if the player can do the base effect shot
     * @param targets are the specified targets
     * @return true if it can be done or false otherwise
     * @throws NotCorrectPlayerNumberException if there are more ore less than one target specified
     * @throws UncorrectDistanceException if players are not in line
     * @throws PlayerNotSeeableException if the player are beyond too many walls
     */
    private Boolean preShootBase(List<Player> targets ) throws NotCorrectPlayerNumberException,UncorrectDistanceException,PlayerNotSeeableException{

        if (targets.size() != 1) throw new NotCorrectPlayerNumberException();

        return playerAreInStraightLine(targets,1);

    }

    /**
     * This method will say if the player can do the second effect shot
     * @param targets are the specified targets
     * @return true if it can be done or false otherwise
     * @throws NotEnoughAmmoException if the player can not pay for the second effect
     * @throws UncorrectDistanceException if players are not in line
     * @throws PlayerNotSeeableException if the player are beyond too many walls
     */
    private Boolean preShootSecond(List<Player> targets ) throws NotEnoughAmmoException,UncorrectDistanceException,PlayerNotSeeableException{

        if (!isPossessedBy().canPay(costSecondEffect)) throw new NotEnoughAmmoException();

        return playerAreInStraightLine(targets,2);

    }

    /**
     * This method will say if the player and the target(s) are in a straight line
     * @param targets are the specified targets
     * @param walls are the maximum number of walls that can be between the player and the targets
     * @return true if the conditions are verified
     * @throws UncorrectDistanceException if the player are not in a straight line
     * @throws PlayerNotSeeableException if the player are beyond too many walls
     */
    public Boolean playerAreInStraightLine( List<Player> targets, int walls ) throws UncorrectDistanceException,PlayerNotSeeableException{

        Cell shooterPos = isPossessedBy().getCurrentPosition();

        Cell target1Pos = targets.get(0).getCurrentPosition();

        Directions direction = getDirection(shooterPos,target1Pos);

        List<Cell> cellList = genCellListInDirection(direction,shooterPos);

        if (!cellList.contains(target1Pos)) throw new UncorrectDistanceException();

        if (targets .size() > 1){

            Cell target2Pos = targets.get(1).getCurrentPosition();

            if (!cellList.contains(target2Pos)) throw new UncorrectDistanceException();

            if (cellList.indexOf(target2Pos) < cellList.indexOf(target1Pos)) throw new UncorrectDistanceException();

            cellList = cellList.subList(0,cellList.indexOf(target2Pos));

        } else {

            cellList = cellList.subList(0,cellList.indexOf(target1Pos));
        }

        if (wallCounter(cellList,direction) > walls ) throw new PlayerNotSeeableException();

        return true;

    }

    /**
     * This method will get the cardinal direction between two given cell
     * @param start is the "Origin" cell
     * @param dest is the other cell
     * @return the direction if the cells are in a straight line
     * @throws UncorrectDistanceException if the cells are not
     */
    private Directions getDirection(Cell start , Cell dest) throws UncorrectDistanceException{

        Point startPoint = Model.getMap().cellToCoord(start);

        Point destPoint = Model.getMap().cellToCoord(dest);

        if (startPoint.x == destPoint.x){

            if (startPoint.y > destPoint.y) return Directions.WEST;

            if (startPoint.y < destPoint.y) return Directions.EAST;
        }

        if (startPoint.y == destPoint.y){

            if (startPoint.x > destPoint.x) return Directions.NORTH;

            if (startPoint.x < destPoint.x) return Directions.SOUTH;
        }

        throw new UncorrectDistanceException();
    }

    /**
     * This method will generate a list of cell from a start cell in a given direction
     * @param direction is the direction
     * @param startCell is the start point
     * @return a list of cells
     */
    private List<Cell> genCellListInDirection(Directions direction, Cell startCell){

        Point point = Model.getMap().cellToCoord(startCell);

        List<Cell> cellList = new ArrayList<>();

        switch (direction){

            case WEST:

                for (int i = point.y; i >= 0 ; i--) {

                    cellList = addCellIfNotNull(point.x,i, cellList);
                }

                break;

            case EAST:

                for (int i = point.y; i < Map.MAP_C ; i++) {

                    cellList = addCellIfNotNull(point.x,i, cellList);
                }

                break;

            case SOUTH:

                for (int i = point.x; i < Map.MAP_R ; i++) {

                    cellList = addCellIfNotNull(i,point.y, cellList);
                }

                break;


            case NORTH:

                for (int i = point.x; i >= 0 ; i--) {

                    cellList = addCellIfNotNull(i,point.y, cellList);
                }

                break;

        }

        return cellList;
    }

    /**
     * This method will add a cell to a list if it is not null
     * @param x is the x coord of the cell to add
     * @param y is the y coord of the cell to add
     * @param list is the list the cell will be added to
     * @return a new list with the new cell if it was not null
     */
    private List<Cell> addCellIfNotNull(int x, int y, List<Cell> list){

        List<Cell> cellList = new ArrayList<>(list);

        Cell toAdd = Model.getMap().getCell(x,y);

        if (toAdd != null) {

            cellList.add(toAdd);

        }

        return cellList;

    }

    /**
     * This method will take a line of cell asa string and count the walls in it
     * @param cellList is the line of cell
     * @param direction is the direction in which the cell are connected
     * @return the number of walls
     */
    private int wallCounter(List<Cell> cellList, Directions direction){

        int walls = 0;

        for (Cell cell : cellList){

            if ( cell.getCellAdj(direction) == null ){

                walls++;

            }
        }

        return walls;
    }
}
