package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
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

import static it.polimi.ingsw.utils.DefaultReplies.*;

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

    private final UtilityMethods utilityMethods;

    public ActionPhase(Controller controller) {

        this.controller = controller;

        this.utilityMethods = controller.getUtilityMethods();
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

            utilityMethods.move(moveAction.getMoves());

            // notify other player

            controller.updateInactivePlayers(new MoveTurnUpdate(controller.getCurrentPlayer()));

            // increment the phase

            controller.incrementPhase();
        }

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

        if (checkGrabMove(grabAction) && checkGrab(grabAction, utilityMethods.simulateMovement(grabAction.getDirection()))){

            // moves the player

            utilityMethods.move(grabAction.getDirection());

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

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than max movements");

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

                return false;
            }

            if ((grabAction.getDirection().size() > MAX_GRAB_MOVES) && (Model.getPlayer(playerId).getDmg().size() < DMG_FOR_PLUS)){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than one but has only damage : " + Model.getPlayer(playerId).getDmg().size() );

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT);

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

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_BUY_WEAPON_BUT_NO_NAME_SPECIFIED);

            return false;

        } else {

            if (utilityMethods.findWeaponInSpawnCell(grabAction.getNewWeaponName(), (SpawnCell) cell) == null) {

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " the specified weapon was not found in the cell : " + grabAction.getNewWeaponName());

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_SPAWN);

                return false;

            } else {

                if ((Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS) && (grabAction.getDiscardedWeapon() == null)) {

                    LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " tried to buy a weapon but has already max weapon and did not specify weapon to delete");

                    controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD);

                    return false;
                }

                return currentPlayerCanBuyWeapon(utilityMethods.findWeaponInSpawnCell(grabAction.getNewWeaponName(), (SpawnCell) cell));
            }
        }
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

            Model.getPlayer(controller.getCurrentPlayer()).buy(utilityMethods.findWeaponInSpawnCell(grabAction.getNewWeaponName(), position));

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

        Weapon selected = utilityMethods.findWeaponInWeaponBag(shootAction.getWeaponName(),playerId);

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

                controller.getVirtualView(playerId).show(DEFAULT_WEAPON_NOT_LOADED);

            }catch (PlayerInSameCellException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_SAME_CELL);

            }catch (PlayerInDifferentCellException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_DIFFERENT_CELL);

            }catch (UncorrectDistanceException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_DISTANCE);

            }catch (SeeAblePlayerException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_SEEABLE_PLAYER);

            }catch (UncorrectEffectsException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_EFFECTS);

            }catch (NotCorrectPlayerNumberException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_NOT_CORRECT_PLAYER_NUMBER);

            }catch (PlayerNotSeeableException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_NOT_SEEABLE);

            }catch (DeadPlayerException e){

                LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " player w/ id: " + e.getPlayerId() + " has been killed ");

            }catch (OverKilledPlayerException e){

                LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " player w/ id: " + e.getPlayerId() + " has been overkilled ");

            }catch (FrenzyActivatedException e){

                LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " player w/ id: " + e.getPlayerId() + " activated frenzy ");
            }

            controller.incrementPhase();

        }else {

            LOGGER.log( Level.INFO, () -> LOG_START_SHOOT + " weapon not found ");

            controller.getVirtualView(playerId).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            handleAction();
        }
    }


    // FRENZY




}