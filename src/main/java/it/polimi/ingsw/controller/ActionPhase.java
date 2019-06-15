package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.actions.GrabAction;
import it.polimi.ingsw.view.actions.Move;
import it.polimi.ingsw.view.actions.ShootAction;
import it.polimi.ingsw.view.updates.otherplayerturn.GrabTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.MoveTurnUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_ALREADY_PICKED_AMMO_HERE;
import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_CANNOT_BUY_WEAPON;

public class ActionPhase {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private static final String LOG_START_GRAB = "[Controller-GrabAction] Player w/ id: ";
    private static final String LOG_START_SHOOT = "[Controller-ShootAction]";

    private static final int TIMER_ACTION = 30;

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

            // start the timer

            controller.getTimer().startTimer(TIMER_ACTION);
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

            // notify other player

            controller.updateInactivePlayers(new MoveTurnUpdate(controller.getCurrentPlayer()));

            // increment the phase

            controller.incrementPhase();
        }

    }

    /**
     * this method moves the current player
     * @param directionsList is the list of the directions the player wants to move
     */
    private void move(List<Directions> directionsList){

        // gets the final position

        Cell finalPosition = simulateMovement(directionsList);

        // moves the player

        Model.getPlayer(controller.getCurrentPlayer()).setPlayerPos(finalPosition);

    }

    /**
     * This method simulate the actual movement and return the cell the player would reach if he moves
     * @param directions is the list of movements the player wants to do
     * @return the final cell the player would reach if he moves
     */
    private Cell simulateMovement(List<Directions> directions){

        Cell cell = Model.getPlayer(controller.getCurrentPlayer()).getCurrentPosition();

        for (Directions direction : directions){

            cell = getNextCell(direction,cell);
        }

        return cell;
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

        LOGGER.log(level, () -> LOG_START_GRAB + playerId + " moved " + direction + " in cell : " + Model.getMap().cellToCoord(Model.getPlayer(playerId).getCurrentPosition()));
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

        if (checkGrabMove(grabAction) && checkGrab(grabAction, simulateMovement(grabAction.getDirection()))){

            // moves the player

            move(grabAction.getDirection());

            // gets the type of the cell the player is in

            if (Model.getPlayer(controller.getCurrentPlayer()).getCurrentPosition().isAmmoCell()){

                grabAmmoFromCurrPosition();

                // update the inactive players

                controller.updateInactivePlayers(new GrabTurnUpdate(controller.getCurrentPlayer()));

                // increment the phase

                controller.incrementPhase();

            }else {

                grabWeaponFromCurrPosition(grabAction);

                // update the inactive players

                controller.updateInactivePlayers(new GrabTurnUpdate(controller.getCurrentPlayer(),grabAction.getNewWeaponName()));

                // increment the phase

                controller.incrementPhase();
            }

        }else {

            // if the checks fails recalls handleAction methods

            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " failed grab Action ");

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

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to more than max movements");

                return false;
            }

            if ((grabAction.getDirection().size() > MAX_GRAB_MOVES) && (Model.getPlayer(playerId).getDmg().size() < DMG_FOR_PLUS)){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than one but has only damage : " + Model.getPlayer(playerId).getDmg().size() );

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

            return checkWeaponGrab(grabAction,cell);

        }else {

            if ( cell.getAmmoPlaced() == null){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " tried to pick an ammoCard in a cell that was empty ");
                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_ALREADY_PICKED_AMMO_HERE);

                return false;
            }
        }

        return true;
    }

    /**
     * this method will check if the player can grab a weapon in the specified cell
     * @param grabAction is the class containing the names of the weapon to buy or to discard
     * @param cell is the specified cell
     * @return true if the player can grab false otherwise
     */
    private Boolean checkWeaponGrab(GrabAction grabAction, Cell cell){

        if (grabAction.getNewWeaponName() == null) {

            // if the player did not specify the weapon to buy -> false

            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " tried to buy a weapon but did not specify the name ");

            return false;

        } else {

            if (findWeaponInSpawnCell(grabAction.getNewWeaponName(), (SpawnCell) cell) == null) {

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " the specified weapon was not found in the cell : " + grabAction.getNewWeaponName());

                return false;

            } else {

                if ((Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS) && (grabAction.getDiscardedWeapon() == null)) {

                    LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " tried to buy a weapon but has already max weapon and did not specify weapon to delete");

                    return false;
                }

                return currentPlayerCanBuyWeapon(findWeaponInSpawnCell(grabAction.getNewWeaponName(), (SpawnCell) cell));
            }
        }
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
     * This method will return the weapon instance if there is a weapon with given name in the specified player's weaponBag or null
     * @param weaponName is the name of the weapon
     * @param playerId is the id of the player
     * @return the weapon instance or null if not found
     */
    private Weapon findWeaponInWeaponBag(String weaponName, int playerId){

        List<Weapon> weaponList = Model.getPlayer(playerId)
                .getCurrentWeapons()
                .getList()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(weaponName))
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

            String s = DEFAULT_CANNOT_BUY_WEAPON;
            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + s);
            controller.getVirtualView(controller.getCurrentPlayer()).show(s);

            return false;
        }
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

            LOGGER.log(level, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " bought a new weapon: " + grabAction.getNewWeaponName() );

            if (Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS ){

                List<Weapon> weaponList = Model.getPlayer(controller.getCurrentPlayer())
                        .getCurrentWeapons()
                        .getList()
                        .stream()
                        .filter( x-> x.getName().equalsIgnoreCase(grabAction.getDiscardedWeapon()))
                        .collect(Collectors.toList());

                Model.getPlayer(controller.getCurrentPlayer()).delWeapon(weaponList.get(0));

                LOGGER.log(level, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " discarded a weapon: " + grabAction.getDiscardedWeapon() );

            }

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // SHOOT_ACTION

    public void shootAction(ShootAction shootAction) {

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        // gets the specified weapon

        Weapon selected = findWeaponInWeaponBag(shootAction.getWeaponName(),playerId);

        // check if weapon exist

        if (selected != null){

            try {

                // translate the list of point in a list of cell

                List<Cell> cells = shootAction
                        .getCells()
                        .stream()
                        .map( x -> Model.getMap().getCell(x.x,x.y))
                        .collect(Collectors.toList());

                List<List<Player>> targets = new ArrayList<>();

                // translate the lists of Integer in lists of Players

                for (int i = 0; i < shootAction.getTargetIds().size() ; i++) {

                    List<Player> temp = new ArrayList<>();

                    for (Integer id : shootAction.getTargetIds().get(i)){

                        temp.add(Model.getPlayer(id));
                    }

                    targets.add(temp);
                }


                selected.shoot(targets,shootAction.getEffects(),cells);


            }catch (WeaponNotLoadedException weaponNonLoadedException){

                LOGGER.log(Level.WARNING, weaponNonLoadedException.getMessage(), weaponNonLoadedException);

                controller.getVirtualView(playerId).show("player tried to shoot with unloaded weapon !");

            }catch (PlayerInSameCellException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot to player in same cell !");

            }catch (PlayerInDifferentCellException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot to player in different cell !");

            }catch (UncorrectDistanceException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot to player with illegal distance !");

            }catch (SeeAblePlayerException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot to seeable player ? !");

            }catch (UncorrectEffectsException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot with illegal effects !");

            }catch (NotCorrectPlayerNumberException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot with illegal target number specified !");

            }catch (PlayerNotSeeableException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show("player tried to shoot to target that can not see !");

            }catch (DeadPlayerException e){

                LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " player w/ id: " + e.getPlayerId() + " has been killed ");

                // set hasSomeoneDied check to true

                controller.setHasSomeoneDied(true);

                // remove the player from the map

                Model.getPlayer(e.getPlayerId()).setPlayerPos(null);

            }catch (OverKilledPlayerException e){

                LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " player w/ id: " + e.getPlayerId() + " has been overkilled ");

                // set hasSomeoneDied check to true

                controller.setHasSomeoneDied(true);

                // remove the player from the map

                Model.getPlayer(e.getPlayerId()).setPlayerPos(null);

            }catch (FrenzyActivatedException e){

                LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " player w/ id: " + e.getPlayerId() + " activated frenzy ");
            }

            controller.incrementPhase();

        }else {

            LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " weapon not found ");

            handleAction();
        }
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