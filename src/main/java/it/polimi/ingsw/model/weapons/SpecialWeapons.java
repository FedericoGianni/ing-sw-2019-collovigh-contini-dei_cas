package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.utils.Directions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SpecialWeapons extends Weapon{

    private final String name;

    public static final int KILL_DMG = 10;



    public SpecialWeapons(String name) {
        this.name = name;
    }

    /**
     * This method will check if the player can shoot
     *
     * @param targetLists is a list of list of target for each effect of the weapon
     * @param effects is a list containing int correspondents to which effect will be used
     * @param cells is a list of cell
     * @return true if the shoot can be preformed, false otherwise
     */
    public abstract Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws DifferentPlayerNeededException, WeaponNotLoadedException, PlayerAlreadyDeadException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException,NotEnoughAmmoException, CellNonExistentException;



    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void print() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getCost() {
        return getReloadCost().subList(1, getReloadCost().size());
    }


    //Utils

    /**
     *  This method checks if the targets are already dead
     * @param targetList is the list of list of Player
     * @throws PlayerAlreadyDeadException if the player is already dead
     */
    public static void checkPlayerAlreadyDead(List<List<Player>> targetList) throws PlayerAlreadyDeadException{

        if (targetList != null ) {

            for (Player player : targetList.stream().flatMap(List::stream).collect(Collectors.toList())) {

                if (player.getDmg().size() >= Stats.MAX_DMG - 1) {

                    throw new PlayerAlreadyDeadException();
                }
            }

        }
    }

    /**
     * This method will target all the player in a given cell and give them dmg / marks
     * @param cell is the selected cell
     * @param dmg is the amount of damage to give to each player
     * @param marks is the amount of marks to give to each player
     * @param shooterId is the id of the shooter
     */
    public static void toAllPlayerInCell(Cell cell, int dmg, int marks , int shooterId){

        if (Model.getMap().hasCell(cell)) {

            for (Player target : cell.getPlayers()) {

                target.addDmg(shooterId, dmg);

                target.addMarks(shooterId, marks);
            }

        }
    }

    /**
     * This method will say if the shooter is is in line with other max 2 players considering walls
     * @param shooter is the shooter
     * @param targets is a list of 1 or 2 targets
     * @return true if the specified players are in line, false otherwise
     */
    public static Boolean playerAreInLine(Player shooter, List<Player> targets){

        if (targets.size() == 1) return (shooter.getCurrentPosition().getAdjacencienceDirection(targets.get(0).getCurrentPosition()) != null );

        if (targets.size()==2) {

            Directions adjDir = shooter.getCurrentPosition().getAdjacencienceDirection(targets.get(0).getCurrentPosition());

            if (adjDir == null) return false;

            return  (targets.get(0).getCurrentPosition().getCellAdj(adjDir).equals(targets.get(1).getCurrentPosition())) ;

        }

        return false;
    }

    /**
     *
     * @param vortex is the vortex center
     * @return a list of cell that dist 1 from vortex
     */
    public static List<Cell> genCellSurrounding(Cell vortex){

        List<Cell> vortexSurrounding = new ArrayList<>();

        vortexSurrounding.add(vortex);

        vortexSurrounding.add(vortex.getNorth());
        vortexSurrounding.add(vortex.getEast());
        vortexSurrounding.add(vortex.getWest());
        vortexSurrounding.add(vortex.getSouth());

        return vortexSurrounding;
    }


}
