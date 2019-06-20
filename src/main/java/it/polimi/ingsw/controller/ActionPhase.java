package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
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
    private static final String LOG_START_MOVE = "[Controller-MoveAction]";
    private static final String LOG_START_ID = "[CONTROLLER] player id ";

    private static final int TIMER_ACTION = 30;

    //Move

    private static final int MAX_MOVES = 3;
    private static final int MAX_FRENZY_MOVES = 4;

    //Grab

    private static final int MAX_GRAB_MOVES = 1;
    private static final int MAX_GRAB_MOVES_PLUS = 2;
    private static final int MAX_GRAB_FRENZY_MOVES_ENHANCED = 2;
    private static final int MAX_GRAB_FRENZY_MOVES = 3;
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

        boolean frenzyEnhanced = controller.getCurrentPlayer() > controller.getFrenzyStarter();

        if ((!controller.isPlayerOnline(currentPlayer)) || (controller.getFrenzy() && !frenzyEnhanced && (controller.getTurnPhase() == TurnPhase.ACTION2)) ) {

            // if the player is not online, or is after the first player in the frenzy round and has already done the first action, skips the turn

            controller.incrementPhase();

        } else {

            // sends the startPhase command to the virtual view

            controller.getVirtualView(currentPlayer).startAction(controller.getFrenzy(), frenzyEnhanced);

            // start the timer

            controller.setExpectingAnswer(true);

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

        LOGGER.log(level, () -> LOG_START_ID + controller.getCurrentPlayer() + "calling move");

        if (moveAction.getMoves().size() > MAX_MOVES) {

            // if the player tried to move more than 3 steps recalls the action

            LOGGER.log(level, () -> LOG_START_MOVE + " received illegal Move Action Request # of moves req:" + moveAction.getMoves().size());

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

        LOGGER.log(level, () -> LOG_START_ID + controller.getCurrentPlayer() + " calling Grab");

        // check if the actions are possible

        if (checkGrabMove(grabAction) && checkGrab(grabAction.getNewWeaponName(),grabAction.getDiscardedWeapon(), utilityMethods.simulateMovement(grabAction.getDirections()),grabAction.getPowerUpsForPay())){

            // moves the player

            utilityMethods.move(grabAction.getDirections());

            // sells the specified powerUps

            if ( (grabAction.getPowerUpsForPay() != null ) && (!grabAction.getPowerUpsForPay().isEmpty()) ){

                for (CachedPowerUp cachedPowerUp : grabAction.getPowerUpsForPay()){

                    PowerUp powerUp = Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().findItem(cachedPowerUp.getType(), cachedPowerUp.getColor());

                    Model.getPlayer(controller.getCurrentPlayer()).sellPowerUp(powerUp);

                }
            }

            // grabs

            grab(grabAction.getNewWeaponName(),grabAction.getDiscardedWeapon());

        }else {

            // if the checks fails recalls handleAction methods

            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " failed grab Action ");

            handleAction();

        }
    }

    private void grab(String newWeaponName,String discardedWeaponName) {

        // gets the type of the cell the player is in

        if (Model.getPlayer(controller.getCurrentPlayer()).getCurrentPosition().isAmmoCell()){

            grabAmmoFromCurrPosition();

            // update the inactive players

            controller.updateInactivePlayers(new GrabTurnUpdate(controller.getCurrentPlayer()));

            // increment the phase

            controller.incrementPhase();

        }else {

            grabWeaponFromCurrPosition(newWeaponName,discardedWeaponName);

            // update the inactive players

            controller.updateInactivePlayers(new GrabTurnUpdate(controller.getCurrentPlayer(),newWeaponName));

            // increment the phase

            controller.incrementPhase();
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

        if (!grabAction.getDirections().isEmpty()) {

            if (grabAction.getDirections().size() > MAX_GRAB_MOVES_PLUS){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than max movements");

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

                return false;
            }

            if ((grabAction.getDirections().size() > MAX_GRAB_MOVES) && (Model.getPlayer(playerId).getDmg().size() < DMG_FOR_PLUS)){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than one but has only damage : " + Model.getPlayer(playerId).getDmg().size() );

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT);

                return false;
            }
        }

        return true;
    }

    /**
     * this method will check if the player can grab in the specified cell
     * @param newWeaponName is the name of the weapon to buy
     * @param discardedWeaponName is the name of the weapon to discard
     * @param cell is the specified cell
     * @return true if the player can grab false otherwise
     */
    private Boolean checkGrab(String newWeaponName,String discardedWeaponName, Cell cell, List<CachedPowerUp> powerUpList){

        if (cell == null) return false;

        if (!cell.isAmmoCell()) {

            return checkWeaponGrab(newWeaponName,discardedWeaponName,cell, powerUpList);

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
     * @param newWeaponName is the name of the weapon to buy
     * @param discardedWeaponName is the name of the weapon to discard
     * @param cell is the specified cell
     * @return true if the player can grab false otherwise
     */
    private Boolean checkWeaponGrab(String newWeaponName,String discardedWeaponName, Cell cell, List<CachedPowerUp> powerUpList){

        if (newWeaponName == null) {

            // if the player did not specify the weapon to buy -> false

            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " tried to buy a weapon but did not specify the name ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_BUY_WEAPON_BUT_NO_NAME_SPECIFIED);

            return false;

        } else {

            if (utilityMethods.findWeaponInSpawnCell(newWeaponName, (SpawnCell) cell) == null) {

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " the specified weapon was not found in the cell : " + newWeaponName);

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_SPAWN);

                return false;

            } else {

                if ((Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS) && (discardedWeaponName == null)) {

                    LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " tried to buy a weapon but has already max weapon and did not specify weapon to delete");

                    controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD);

                    return false;
                }

                return currentPlayerCanBuyWeapon(utilityMethods.findWeaponInSpawnCell(newWeaponName, (SpawnCell) cell),powerUpList);
            }
        }
    }



    /**
     *
     * @param weapon is the weapon to buy
     * @return true if the player can buy it
     */
    private Boolean currentPlayerCanBuyWeapon(Weapon weapon, List<CachedPowerUp> powerUpList){

        List<AmmoCube> possessed = new ArrayList<>();

        try{

            possessed.addAll(controller.getUtilityMethods().powerUpToAmmoList(controller.getUtilityMethods().getSpecifiedPowerUp(powerUpList)));

            possessed.addAll(Model.getPlayer(controller.getCurrentPlayer()).getAmmoBag().getList());


        }catch (CardNotPossessedException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);
        }


        if ((weapon != null) && (checkIfPlayerCanPayWeapon(possessed,weapon))){

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
     * This method will check if the player can pay the reload cost with the parameters he specified
     * @param ammoCubeList is a list of ammoCubes
     * @param weapon is the weapon to buy
     * @return true if yes, false otherwise
     */
    private Boolean checkIfPlayerCanPayWeapon(List<AmmoCube> ammoCubeList, Weapon weapon){

        Boolean returnValue = true;

        List<Color> required = new ArrayList<>();



        required.addAll(weapon
                .getCost()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList()));



        List<Color> possessed = ammoCubeList
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        if( ! possessed.containsAll(required)){

            returnValue = false;

            // log

            LOGGER.log(Level.WARNING, ()->  "[Controller] player tried to buy weapon but did not have enough ammo");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_NO_ENOUGH_AMMO);
        }

        return returnValue;
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
     * @param newWeaponName is the name of the weapon to buy
     * @param discardedWeaponName is the name of the weapon to discard
     */
    private void grabWeaponFromCurrPosition(String newWeaponName,String discardedWeaponName){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        SpawnCell position = (SpawnCell) Model.getPlayer(playerId).getCurrentPosition();

        // buy the weapon it is asked by the player

        try{

            Model.getPlayer(controller.getCurrentPlayer()).buy(utilityMethods.findWeaponInSpawnCell(newWeaponName, position));

            LOGGER.log(level, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " bought a new weapon: " + newWeaponName );

            if (Model.getPlayer(controller.getCurrentPlayer()).getCurrentWeapons().getList().size() >= MAX_WEAPONS ){

                List<Weapon> weaponList = Model.getPlayer(controller.getCurrentPlayer())
                        .getCurrentWeapons()
                        .getList()
                        .stream()
                        .filter( x-> x.getName().equalsIgnoreCase(discardedWeaponName))
                        .collect(Collectors.toList());

                Model.getPlayer(controller.getCurrentPlayer()).delWeapon(weaponList.get(0));

                LOGGER.log(level, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " discarded a weapon: " + discardedWeaponName );

            }

        }catch (Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // SHOOT_ACTION

    /**
     * This method will make the player do a "shoot" action
     * @param shootAction is the class containing the list of moves
     */
    public void shootAction(ShootAction shootAction) {

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();


        if (!checkShoot(shootAction)) {

            handleAction();

        }

        try {

            shoot(shootAction);


        }catch (WeaponNotLoadedException weaponNonLoadedException){

                LOGGER.log(Level.WARNING, weaponNonLoadedException.getMessage(), weaponNonLoadedException);

                controller.getVirtualView(playerId).show(DEFAULT_WEAPON_NOT_LOADED);

                handleAction();

        }catch (PlayerInSameCellException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_SAME_CELL);

                handleAction();

        }catch (PlayerInDifferentCellException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_DIFFERENT_CELL);

                handleAction();

        }catch (UncorrectDistanceException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_DISTANCE);

                handleAction();

        }catch (SeeAblePlayerException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_SEEABLE_PLAYER);

                handleAction();

        }catch (UncorrectEffectsException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_EFFECTS);

                handleAction();

        }catch (NotCorrectPlayerNumberException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_NOT_CORRECT_PLAYER_NUMBER);

                handleAction();

        }catch (PlayerNotSeeableException e){

                LOGGER.log( Level.WARNING, e.getMessage(),e);

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_NOT_SEEABLE);

                handleAction();

        }catch (WeaponNotFoundException e){

                LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " weapon not found ");

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

                handleAction();

        } catch (CardNotPossessedException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " card not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            handleAction();

        } catch (NotEnoughAmmoException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " not enough ammo ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_NO_ENOUGH_AMMO);

            handleAction();

        } catch (DifferentPlayerNeededException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + "DifferentPlayerNeededException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            handleAction();

        }

        controller.incrementPhase();


    }

    private void shoot(ShootAction shootAction) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, WeaponNotFoundException, DifferentPlayerNeededException, NotEnoughAmmoException, CardNotPossessedException {

        // gets the selected weapon

        Weapon selected = utilityMethods.findWeaponInWeaponBag(shootAction.getWeaponName(),controller.getCurrentPlayer());

        if (selected != null) {

            // translate the list of point in a list of cell

            List<Cell> cells = shootAction
                    .getCells()
                    .stream()
                    .map(x -> Model.getMap().getCell(x.x, x.y))
                    .collect(Collectors.toList());

            List<List<Player>> targets = new ArrayList<>();

            // translate the lists of Integer in lists of Players

            for (int i = 0; i < shootAction.getTargetIds().size(); i++) {

                List<Player> temp = new ArrayList<>();

                for (Integer id : shootAction.getTargetIds().get(i)) {

                    temp.add(Model.getPlayer(id));
                }

                targets.add(temp);
            }


            selected.shoot(targets, shootAction.getEffects(), cells);

        }else {

            throw new WeaponNotFoundException();
        }
    }

    /**
     * This method will check if the parameter the player specified are suitable for shooting
     * @param shootAction is the class containing the list of moves
     * @return true if the action is doable
     */
    private Boolean checkShoot(ShootAction shootAction){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        // gets the specified weapon

        Weapon selected = utilityMethods.findWeaponInWeaponBag(shootAction.getWeaponName(),playerId);

        boolean returnValue = true;

        if(selected == null){

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            returnValue = false;

        } else if (!selected.isLoaded()){

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_LOADED);

            returnValue = false;
        }

        return returnValue;
    }


    // FRENZY

    /**
     * This function will make the player do a "frenzy move" action
     * @param frenzyMove is the class containing the requested parameters
     */
    public void frenzyMoveAction(Move frenzyMove){

        // look if the player is before the first player (0,1,2,3,4)

        boolean frenzyEnhanced = controller.getCurrentPlayer() > controller.getFrenzyStarter();

        if ( frenzyMove.getMoves().size() > MAX_FRENZY_MOVES ){

            //log

            LOGGER.log(Level.WARNING, () -> LOG_START_MOVE + " player tried to move more than " + MAX_FRENZY_MOVES + " in frenzy ");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

            // ask action again

            handleAction();

        } else if ((!frenzyEnhanced) && (frenzyMove.getMoves().size() > MAX_MOVES)){


            //log

            LOGGER.log(Level.WARNING, () -> LOG_START_MOVE + " player tried to move more than " + MAX_MOVES + " in frenzy, but was after first player ");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT);

            // ask action again

            handleAction();


        } else {

            // moves the current player in the directions specified in the list

            utilityMethods.move(frenzyMove.getMoves());

            // notify other player

            controller.updateInactivePlayers(new MoveTurnUpdate(controller.getCurrentPlayer()));

            // increment the phase

            controller.incrementPhase();

        }
    }


    /**
     * This function will make the player do a "frenzy grab" action
     * @param frenzyGrab is the class containing the requested parameters
     */
    public void frenzyGrabAction(GrabAction frenzyGrab){

        // logs the action

        LOGGER.log(level, () -> LOG_START_ID + controller.getCurrentPlayer() + " calling FrenzyGrab");

        // check if the actions are possible

        if (checkFrenzyGrabMove(frenzyGrab) && checkGrab(frenzyGrab.getNewWeaponName(),frenzyGrab.getDiscardedWeapon(), utilityMethods.simulateMovement(frenzyGrab.getDirections()), frenzyGrab.getPowerUpsForPay())){

            // moves the player

            utilityMethods.move(frenzyGrab.getDirections());

            // grab

            grab(frenzyGrab.getNewWeaponName(),frenzyGrab.getDiscardedWeapon());

        }else {

            // if the checks fails recalls handleAction methods

            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " failed grab Action ");

            handleAction();

        }
    }


    /**
     * This method will check if the player can do the specified moves
     * @param frenzyGrab is the class containing the list of moves
     * @return true if the moves are legal or false otherwise
     */
    private Boolean checkFrenzyGrabMove(GrabAction frenzyGrab){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        // look if the player is before the first player (0,1,2,3,4)

        boolean frenzyEnhanced = playerId > controller.getFrenzyStarter();

        if (!frenzyGrab.getDirections().isEmpty()) {

            if (frenzyGrab.getDirections().size() > MAX_FRENZY_MOVES){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than " + MAX_GRAB_FRENZY_MOVES + " movements");

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

                return false;

            } else if ((frenzyEnhanced) && (frenzyGrab.getDirections().size() > MAX_GRAB_FRENZY_MOVES_ENHANCED)){

                LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + playerId + " tried to move more than " + MAX_GRAB_FRENZY_MOVES_ENHANCED + " but is before the first player " );

                controller.getVirtualView(playerId).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

                return false;
            }
        }

        return true;
    }


}