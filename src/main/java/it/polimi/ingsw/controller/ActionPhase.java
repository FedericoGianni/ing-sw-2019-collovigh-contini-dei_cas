package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.actions.GrabAction;
import it.polimi.ingsw.view.actions.Move;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ActionPhase {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private static final String LOG_START = "[Controller-GrabAction] Player w/ id: ";

    //Grab

    private static final int MAX_GRAB_MOVES = 1;
    private static final int MAX_GRAB_MOVES_PLUS = 2;
    private static final int DMG_FOR_PLUS = 2;
    private static final int MAX_WEAPONS = 3;

    private final Controller controller;

    public ActionPhase(Controller controller) {
        this.controller = controller;
    }


    /**
     * This methods will forward the action Request to the virtual View
     */
    public void handleAction() {

        int currentPlayer = controller.getCurrentPlayer();

        if (!controller.isPlayerOnline(currentPlayer)) {

            // if the player is not online skips the turn

            controller.incrementPhase();

        } else {

            // sends the startPhase command to the virtual view

            controller.getVirtualView(currentPlayer).startAction();
        }
    }


    // MOVE_ACTION

    /**
     * This methods will move the player
     *
     * @param moveAction is the moveAction requested by the client
     */
    public void moveAction(Move moveAction) {

        // logs the action

        LOGGER.log(level, () -> "[CONTROLLER] player id " + controller.getCurrentPlayer() + "calling move");

        if (moveAction.getMoves().size() > 3) {

            // if the player tried to move more than 3 steps recalls the action

            LOGGER.log(level, () -> "[Controller-MovePhase] received illegal Move Action Request # of moves req:" + moveAction.getMoves().size());

            handleAction();

        } else {

            // moves the current player in the directions specified in the list

            move(moveAction.getMoves());

            // increment the phase

            controller.incrementPhase();
        }

    }

    /**
     * this method moves the current player
     * @param directionsList is the list of the directions the player wants to move
     */
    private void move(List<Directions> directionsList){

        // move the player

        for (Directions direction : directionsList){

            moveCurrentPlayer(direction);
        }
    }

    /**
     * This method move the player in the given direction
     * @param direction the player wants to move to
     */
    private void moveCurrentPlayer(Directions direction) {

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        Cell position = Model.getPlayer(playerId).getCurrentPosition();

        // change the position var in the direction given

        position = (getNextCell(direction,position) != null) ? getNextCell(direction,position) : position;

        // sets the player in the new position

        Model.getPlayer(playerId).setPlayerPos(position);

        // logs the position change

        LOGGER.log(level, () -> LOG_START + playerId + " moved " + direction + " in cell : " + Model.getMap().cellToCoord(Model.getPlayer(playerId).getCurrentPosition()));
    }

    /**
     *  This method compute the next cell given a direction
     * @param direction is the direction to move
     * @param startPoint is the cell from which starts
     * @return the cell in the direction specified
     */
    private Cell getNextCell(Directions direction, Cell startPoint){

        if (startPoint != null) {

            switch (direction) {

                case NORTH:

                    if (startPoint.getNorth() != null) return startPoint.getNorth();

                    break;

                case EAST:

                    if (startPoint.getEast() != null) return startPoint.getEast();

                    break;

                case WEST:

                    if (startPoint.getWest() != null) return startPoint.getWest();

                    break;

                case SOUTH:

                    if (startPoint.getSouth() != null) return startPoint.getSouth();

                    break;

                default:

                    break;
            }
        }

        return null;
    }


    // GRAB_ACTION

    /**
     * This method represent the "Grab" action
     * @param grabAction is the class received from the view
     */
    public void grabAction(GrabAction grabAction) {

        // logs the action

        LOGGER.log(level, () -> "[CONTROLLER] player id " + controller.getCurrentPlayer() + " calling Grab");

        // check if the actions are possible

        if (checkGrabMove(grabAction) && checkGrab(grabAction, simulateMovement(grabAction))){

            // moves the player

            move(grabAction.getDirection());

            // gets the type of the cell the player is in

            if (Model.getPlayer(controller.getCurrentPlayer()).getCurrentPosition().isAmmoCell()){

                grabAmmoFromCurrPosition();

                // increment the phase

                controller.incrementPhase();

            }else {

                grabWeaponFromCurrPosition(grabAction);

                // increment the phase

                controller.incrementPhase();
            }

        }else {

            // if the checks fails recalls handleAction methods

            LOGGER.log(Level.WARNING, () -> LOG_START + controller.getCurrentPlayer() + " failed grab Action ");

            handleAction();

        }
    }

    /**
     * This method will check if the player can do the specified moves
     * @param grabAction is the class containing the list of moves
     * @return true if the moves are legal or false otherwise
     */
    private Boolean checkGrabMove(GrabAction grabAction){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        if (!grabAction.getDirection().isEmpty()) {

            if (grabAction.getDirection().size() > MAX_GRAB_MOVES_PLUS){

                LOGGER.log(Level.WARNING, () -> LOG_START + playerId + " tried to more than max movements");

                return false;
            }

            if ((grabAction.getDirection().size() > MAX_GRAB_MOVES) && (Model.getPlayer(playerId).getDmg().size() < DMG_FOR_PLUS)){

                LOGGER.log(Level.WARNING, () -> LOG_START + playerId + " tried to move more than one but has only damage : " + Model.getPlayer(playerId).getDmg().size() );

                return false;
            }
        }

        return true;
    }

    /**
     * this method will check if the player can grab in the specified cell
     * @param grabAction is the class containing the names of the weapon to buy or to discard
     * @param cell is the specified cell
     * @return true if the player can grab false otherwise
     */
    private Boolean checkGrab(GrabAction grabAction, Cell cell){

        if (cell == null) return false;

        if (!cell.isAmmoCell()) {

            if (grabAction.getNewWeaponName() == null) {

                // if the player did not specify the weapon to buy -> false

                LOGGER.log(Level.WARNING, () -> LOG_START + controller.getCurrentPlayer() + " tried to buy a weapon but did not specify the name ");

                return false;

            } else {

                if (findWeaponInSpawnCell(grabAction.getNewWeaponName(), (SpawnCell) cell) == null) {

                    LOGGER.log(Level.WARNING, () -> LOG_START + controller.getCurrentPlayer() + " the specified weapon was not found in the cell : " + grabAction.getNewWeaponName());

                    return false;

                } else {

                    if ((Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS) && (grabAction.getDiscardedWeapon() == null)) {

                        LOGGER.log(Level.WARNING, () -> LOG_START + controller.getCurrentPlayer() + " tried to buy a weapon but has already max weapon and did not specify weapon to delete");

                        return false;
                    }

                    return currentPlayerCanBuyWeapon(findWeaponInSpawnCell(grabAction.getNewWeaponName(), (SpawnCell) cell));
                }
            }
        }

        return true;
    }

    /**
     * This method will return the weapon instance if there is a weapon with given name in the specified spawn or null
     * @param weaponName is the name specified
     * @param spawnCell is the Spawn Cell specified
     * @return the weapon instance or null if not found
     */
    private Weapon findWeaponInSpawnCell(String weaponName, SpawnCell spawnCell){

        List<Weapon> weaponList = spawnCell
                .getWeapons()
                .stream()
                .filter( x -> x.getName().equalsIgnoreCase(weaponName))
                .collect(Collectors.toList());

        return weaponList.isEmpty() ? null : weaponList.get(0);
    }

    /**
     *
     * @param weapon is the weapon to buy
     * @return true if the player can buy it
     */
    private Boolean currentPlayerCanBuyWeapon(Weapon weapon){


        if ((weapon != null) && (Model.getPlayer(controller.getCurrentPlayer()).canPay(weapon.getCost()))){

            return true;

        }else {
            //default CANNOT_PAY_WEAPON
            String s =  " tried to buy a weapon but can not pay for it";
            LOGGER.log(Level.WARNING, () -> LOG_START + controller.getCurrentPlayer() + s);
            controller.getVirtualView(controller.getCurrentPlayer()).show(s);
            return false;
        }
    }

    /**
     * This method simulate the actual movement and return the cell the player would reach if he moves
     * @param grabAction is the action the controller gets
     * @return the final cell the player would reach if he moves
     */
    private Cell simulateMovement(GrabAction grabAction){

        Cell cell = Model.getPlayer(controller.getCurrentPlayer()).getCurrentPosition();

        for (Directions direction : grabAction.getDirection()){

            cell = getNextCell(direction,cell);
        }

        return cell;
    }

    /**
     * This method will pick the ammo card from the map
     */
    private void grabAmmoFromCurrPosition(){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        // pick the ammo in the current position

        Model.getPlayer(playerId).pickAmmoHere();
    }

    /**
     * This method will buy the specified weapon from the current cell
     *
     * @param grabAction is the class containing the weapon requested
     */
    private void grabWeaponFromCurrPosition(GrabAction grabAction){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        SpawnCell position = (SpawnCell) Model.getPlayer(playerId).getCurrentPosition();

        // buy the weapon it is asked by the player

        try{

            Model.getPlayer(controller.getCurrentPlayer()).buy(findWeaponInSpawnCell(grabAction.getNewWeaponName(), position));

            if (Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS ){

                List<Weapon> weaponList = Model.getPlayer(controller.getCurrentPlayer())
                        .getCurrentWeapons()
                        .getList()
                        .stream()
                        .filter( x-> x.getName().equalsIgnoreCase(grabAction.getDiscardedWeapon()))
                        .collect(Collectors.toList());

                Model.getPlayer(controller.getCurrentPlayer()).delWeapon(weaponList.get(0));
            }

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // SHOOT_ACTION

    public void shoot(int weapon, int target) {

        throw new UnsupportedOperationException();

        //TODO implement method
    }


    // UTILS

    public boolean askMoveValid(int row, int column, Directions direction) {

        //LOG

        LOGGER.log(level, () -> "[CONTROLLER] receiveed askMove valid from pos: " + row + ", " + column + " and direction : " + direction);

        if (Model.getMap().getCell(row, column) == null) {
            return false;
        }

        Cell startCell = Model.getMap().getCell(row, column);

        switch (direction){

            case NORTH:

                if (startCell.getNorth() != null ) return true;

                break;

            case EAST:

                if (startCell.getEast() != null ) return true;

                break;

            case WEST:

                if (startCell.getWest() != null ) return true;

                break;

            case SOUTH:

                if (startCell.getSouth() != null ) return true;

                break;

            default:

                break;
        }

        return false;
    }
}