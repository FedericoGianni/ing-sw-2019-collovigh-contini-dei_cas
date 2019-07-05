package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.WeaponBag;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.actions.usepowerup.ScopeAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import it.polimi.ingsw.view.updates.otherplayerturn.GrabTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.MoveTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.ShootTurnUpdate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.*;

/**
 * This class will handle the actions of the players ( Move, Move&Grab, Shoot, frenzyMove, FrenzyGrab, FrenzyShoot)
 */
public class ActionPhase {

    /**
     * Logger reference
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.INFO;

    /**
     * constants for log incipit
     */
    private static final String LOG_START_GRAB = "[Controller-GrabAction] Player w/ id: ";
    private static final String LOG_START_SHOOT = "[Controller-ShootAction]";
    private static final String LOG_START_MOVE = "[Controller-MoveAction]";
    private static final String LOG_START_ID = "[CONTROLLER] player id ";
    private static final String LOG_START_FRENZY_S = "[Controller-FrenzyShootAction] ";
    private static final String NO_DIRECTION_SPECIFIED = " player tried to move but did not specify any direction ";
    private static final String MOVE_MORE_THAN = " player tried to move more than ";

    /**
     * timer for the action phase ( amount of seconds the client have to answer the server request)
     */
    private static final int TIMER_ACTION = 90;

    //Move

    /**
     * max movement that player can do in non frenzy move
     */
    private static final int MAX_MOVES = 3;
    /**
     * max movements that player can do in frenzy
     */
    private static final int MAX_FRENZY_MOVES = 4;

    // shoot

    /**
     * damage the player has to have to be able to move in a shoot action
     */
    private static final int DMG_FOR_MOVE_SHOOT = 2;

    //Grab

    /**
     * max moves for vanilla grab action
     */
    private static final int MAX_GRAB_MOVES = 1;
    /**
     * max moves for grab with damage
     */
    private static final int MAX_GRAB_MOVES_PLUS = 2;
    /**
     * max moves for frenzy grab if player is before first player
     */
    private static final int MAX_GRAB_FRENZY_MOVES_ENHANCED = 2;
    /**
     * max moves for frenzy grab if player is after first player
     */
    private static final int MAX_GRAB_FRENZY_MOVES = 3;
    /**
     * damage the player has to have to be able to move more than standard in a grab action
     */
    private static final int DMG_FOR_PLUS = 2;
    /**
     * max weapons a player can have
     */
    private static final int MAX_WEAPONS = WeaponBag.MAX_WEAPONS;

    // frenzyShoot

    /**
     * max moves for frenzyShoot if the player is before the first player
     */
    private static final int MAX_FRENZY_SHOOT_ENHANCED_MOVES = 1;
    /**
     * max moves for frenzyShoot if the player is after the first player
     */
    private static final int MAX_FRENZY_SHOOT_MOVES = 2;

    /**
     * reference to controller main class
     * @see it.polimi.ingsw.controller.Controller
     */
    private final Controller controller;

    /**
     * reference to
     * @see it.polimi.ingsw.controller.UtilityMethods
     */
    private final UtilityMethods utilityMethods;
    /**
     * this list will be used for powerUp payments in shoot methods for restoring them in case of failed shoot
     */
    private List<PowerUp> discardedPowerUpForRestore = new ArrayList<>();
    /**
     * this list will be used for ammo payments in shoot methods for restoring them in case of failed shoot
     */
    private List<AmmoCube> ammoCubeForRestore = new ArrayList<>();

    /**
     * default constructor
     * @param controller is the reference to the controller class
     */
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

            // start the timer

            controller.getTimer().startTimer(TIMER_ACTION);

            // sends the startPhase command to the virtual view

            controller.getVirtualView(currentPlayer).startAction(controller.getFrenzy(), frenzyEnhanced);

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

        if (moveAction.getMoves() == null){

            //log

            LOGGER.log(Level.WARNING, () -> LOG_START_FRENZY_S + NO_DIRECTION_SPECIFIED );

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

            handleAction();

        }else if (moveAction.getMoves().size() > MAX_MOVES) {

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

            // grabs

            grab(grabAction.getNewWeaponName(),grabAction.getDiscardedWeapon(), grabAction.getPowerUpsForPay() );

        }else {

            // if the checks fails recalls handleAction methods

            LOGGER.log(Level.WARNING, () -> LOG_START_GRAB + controller.getCurrentPlayer() + " failed grab Action ");

            handleAction();

        }
    }

    /**
     * This method does the grab action
     * @param newWeaponName is the name of the weapon to buy
     * @param discardedWeaponName is the name of the weapon to discard
     */
    private void grab(String newWeaponName,String discardedWeaponName, List<CachedPowerUp> toSell ) {

        // sells the powerUp

        if ( (toSell != null ) && ( !toSell.isEmpty()) ){

            try {

                toSell = utilityMethods.sellSellablePowerUp(controller.getCurrentPlayer(), toSell);

            }catch (CardNotPossessedException e){

                LOGGER.log(Level.WARNING,e.getMessage(),e);
            }
        }

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

        // sell the remaining powerUp

        try{

            LOGGER.log( level,  "powerUps to discard: {0} ", toSell);

            toSell = controller.getUtilityMethods().sellSellablePowerUp(controller.getCurrentPlayer(),toSell);

            String message = LOG_START_GRAB + " controller could not sell this powerUp: " + toSell;

            if (!toSell.isEmpty()) LOGGER.log(Level.WARNING, ()-> message );

        }catch (CardNotPossessedException e){

            LOGGER.log(Level.WARNING,e.getMessage(),e);
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
     * This method will check if the player can pay the buy cost with the parameters he specified
     * @param ammoCubeList is a list of ammoCubes
     * @param weapon is the weapon to buy
     * @return true if yes, false otherwise
     */
    private Boolean checkIfPlayerCanPayWeapon(List<AmmoCube> ammoCubeList, Weapon weapon){

        boolean returnValue = true;

        List<Color> required = new ArrayList<>( weapon
                .getCost()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList()) );


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

        List<Integer> shotPlayerThisTurnBackup = new ArrayList<>(controller.getShotPlayerThisTurn());

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        try {

            shoot(shootAction);

            // notify other players

            notifyShotToOtherPlayer(shootAction);


        }catch (WeaponNotLoadedException weaponNonLoadedException){

            LOGGER.log(Level.WARNING, weaponNonLoadedException.getMessage(), weaponNonLoadedException);

            controller.getVirtualView(playerId).show(DEFAULT_WEAPON_NOT_LOADED);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (PlayerInSameCellException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_SAME_CELL);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (PlayerInDifferentCellException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_DIFFERENT_CELL);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (UncorrectDistanceException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_DISTANCE);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (SeeAblePlayerException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_SEEABLE_PLAYER);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (UncorrectEffectsException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_EFFECTS);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (NotCorrectPlayerNumberException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_NOT_CORRECT_PLAYER_NUMBER);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (PlayerNotSeeableException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_PLAYER_NOT_SEEABLE);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }catch (WeaponNotFoundException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " weapon not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (CardNotPossessedException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " card not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (NotEnoughAmmoException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " not enough ammo ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_NO_ENOUGH_AMMO);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (DifferentPlayerNeededException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " DifferentPlayerNeededException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_DIFFERENT_PLAYER_NEEDED);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (ArgsNotValidatedException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " ArgsNotValidatedException ");

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (CellNonExistentException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " cell specified in effect is null in model ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (PrecedentPlayerNeededException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " PrecedentPlayerNeededException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PRECEDENT_PLAYER_NEEDED);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        } catch (PlayerAlreadyDeadException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " PlayerAlreadyDeadException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_ALREADY_DEAD);

            restoreSellPowerUp();

            shootFailed(shootAction,shotPlayerThisTurnBackup);

            handleAction();

            return;

        }


        controller.incrementPhase();


    }

    /**
     * This method will do the atomic action shoot
     * forward exception if them are thrown in shoot
     *
     * @param shootAction is the class containing the list of moves
     * @throws ArgsNotValidatedException if the controller checks fails
     */
    private void shoot(ShootAction shootAction) throws WeaponNotLoadedException, PlayerAlreadyDeadException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, WeaponNotFoundException, DifferentPlayerNeededException, NotEnoughAmmoException, CardNotPossessedException, ArgsNotValidatedException, CellNonExistentException, PrecedentPlayerNeededException {

        // perform pre-check

        if (!checkShoot(shootAction)) throw new ArgsNotValidatedException();

        // gets the selected weapon

        Weapon selected = utilityMethods.findWeaponInWeaponBag(shootAction.getWeaponName(),controller.getCurrentPlayer());

        // discard selected powerUps

        List<CachedPowerUp> leftToSell = discardPowerUpShoot(shootAction.getPowerUpList());

        if (shootAction.getMove() != null){

            utilityMethods.move(Arrays.asList(shootAction.getMove()));

        }

        if (selected != null) {

            // translate the list of point in a list of cell

            List<Cell> cells = utilityMethods.PointToCell(shootAction.getCells());

            List<List<Player>> targets = new ArrayList<>();

            LOGGER.log(level, ()->"[Controller-shoot] list of target received : " + shootAction.getTargetIds() );

            // translate the lists of Integer in lists of Players

            for (int i = 0; i < shootAction.getTargetIds().size(); i++) {

                List<Player> temp = new ArrayList<>();

                for (Integer id : shootAction.getTargetIds().get(i)) {

                    temp.add(Model.getPlayer(id));
                }

                targets.add(temp);
            }

            selected.shoot(targets, shootAction.getEffects(), cells);

            // apply targeting Scope if requested

            targetingScope(shootAction.getTargetingScope());

            // discard the remained powerUp to discard

            leftToSell = utilityMethods.sellSellablePowerUp(controller.getCurrentPlayer(),leftToSell);

            String message = LOG_START_SHOOT + " controller could not sell this powerUp: " + leftToSell;

            if (!leftToSell.isEmpty()) LOGGER.log(Level.WARNING, ()-> message );

        }else {

            throw new WeaponNotFoundException();
        }
    }

    /**
     * This method will restore the player original position if the shoot failed and restore the shotPlayer list
     * @param shootAction is the class containing all the details of the shoot requested
     * @param shotPlayerThisTurnBackup is the backup list of the shotPlayer, saved before the shoot attempt
     */
    private void shootFailed(ShootAction shootAction,List<Integer> shotPlayerThisTurnBackup){

        if (shootAction != null && shootAction.getMove() != null && Model.getPlayer(controller.getCurrentPlayer()).getDmg().size() > DMG_FOR_MOVE_SHOOT ){

            utilityMethods.move(Arrays.asList(shootAction.getMove().getOpposite()));

        }

        controller.setShotPlayerThisTurn(shotPlayerThisTurnBackup);

    }

    /**
     * This method will check if the parameter the player specified are suitable for shooting
     * @param shootAction is the class containing the list of moves
     * @return true if the action is doable
     */
    private Boolean checkShoot(ShootAction shootAction){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        // checks the move

        if ( ( !controller.getFrenzy() ) && ( shootAction.getMove() != null ) && ( Model.getPlayer(playerId).getDmg().size() < DMG_FOR_MOVE_SHOOT) ){

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT);

            return false;
        }

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

        returnValue = returnValue && checkShootTarget(shootAction.getTargetIds());

        returnValue = returnValue &&checkShootCells(shootAction.getCells());



        return returnValue;
    }

    /**
     * This method will check if the player list of targets is valid
     * @param targetsIds is a list of list of target ids
     * @return true if the list is valid
     */
    private Boolean checkShootTarget(List<List<Integer>> targetsIds) {

        boolean returnValue = true;

        if (targetsIds == null || targetsIds.isEmpty()) {

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_NO_TARGETS_SPECIFIED);

            return false;

        }


        for (int i = 0; i < targetsIds.size(); i++) {

            for (Integer id : targetsIds.get(i)) {

                try {

                    Player player = Model.getPlayer(id);

                    if (player == null) returnValue = false;


                } catch (Exception e) {

                    LOGGER.log(Level.WARNING, e.getMessage(), e);

                    returnValue = false;
                }
            }

        }

        if (!returnValue){

            LOGGER.log(Level.WARNING, () -> LOG_START_SHOOT + " player specified players that were not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_INEXISTENT_TARGETS);

        }

        return returnValue;
    }

    /**
     * This method will check if the cell list is valid
     * @param cells is a list of points representing cell
     * @return true if the list is valid
     */
    private Boolean checkShootCells(List<Point> cells){

        if ( (cells != null ) && (!cells.isEmpty()) ){

            for (Point cell : cells){

                if (cell != null) {

                    try {

                        Cell realCell = Model.getMap().getCell(cell.x, cell.y);

                        if (realCell == null) {

                            LOGGER.log(Level.WARNING, () -> LOG_START_SHOOT + " Player specified non existent cell ");

                            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

                            return false;
                        }

                    } catch (Exception e) {

                        LOGGER.log(Level.WARNING, e.getMessage(), e);

                        LOGGER.log(Level.WARNING, () -> LOG_START_SHOOT + " Player specified non existent cell ");

                        controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

                        return false;
                    }

                }
            }
        }

        return true;
    }

    /**
     * This method apply a targetingScope on a target if the player have been shot
     * @param scopeAction is the class containing the parameters for the action
     */
    private void  targetingScope(ScopeAction scopeAction){

        if (scopeAction != null) {

            if (controller.getShotPlayerThisTurn().contains(scopeAction.getTargetId())) {

                try {

                    // delete the targetingScope if found

                    Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getItem(controller.getUtilityMethods().getSpecifiedPowerUp(Arrays.asList(new CachedPowerUp(PowerUpType.TARGETING_SCOPE, scopeAction.getColor()))).get(0));

                    // do the action

                    Model.getPlayer(scopeAction.getTargetId()).addDmg(controller.getCurrentPlayer(), 1);

                    // logs the action

                    LOGGER.log(level, () -> LOG_START_SHOOT + " used Targeting Scope on player: " + scopeAction.getTargetId());

                }catch (CardNotPossessedException e){

                    LOGGER.log(Level.WARNING,e.getMessage(),e);
                }

            } else {

                LOGGER.log(Level.WARNING, () -> LOG_START_SHOOT + " tried to use TargetingScope on player not targeted ");

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_TARGETING_SCOPE_ON_NON_TARGETED_PLAYER);
            }

        }

    }

    /**
     * This method notify the non current player of the shoot action
     * @param shootAction is the shoot action made
     */
    private void notifyShotToOtherPlayer(ShootAction shootAction){

        for (List<Integer> targetIdList : shootAction.getTargetIds()) {

            for (Integer targetId : targetIdList) {

                controller.updateInactivePlayers(new ShootTurnUpdate(controller.getCurrentPlayer(), targetId, shootAction.getWeaponName()));

            }
        }
    }

    /**
     * This method is used to sell PowerUp
     * @param cachedPowerUpList is the list of cachedPowerUp to sell
     * @return a list of remained powerUp to sell
     * @throws CardNotPossessedException if a powerUp is not found
     */
    private List<CachedPowerUp> discardPowerUpShoot(List<CachedPowerUp> cachedPowerUpList) throws CardNotPossessedException{

        List<CachedPowerUp> leftToDiscard = new ArrayList<>();

        for (CachedPowerUp cachedPowerUp : cachedPowerUpList){

            try {

                if ( utilityMethods.powerUpCanBeSold(controller.getCurrentPlayer(),Arrays.asList(cachedPowerUp)) ) {

                    PowerUp toDiscard = utilityMethods.getSpecifiedPowerUp(Arrays.asList(cachedPowerUp)).get(0);

                    discardedPowerUpForRestore.add(toDiscard);

                    ammoCubeForRestore.add(Model.getPlayer(controller.getCurrentPlayer()).sellPowerUp(toDiscard));

                } else {

                    leftToDiscard.add(cachedPowerUp);
                }

            }catch (CardNotPossessedException e){

                restoreSellPowerUp();

                LOGGER.log(Level.WARNING, () -> LOG_START_SHOOT + " Player tried to discard powerUp he does not possess ");
                LOGGER.log(Level.WARNING, e.getMessage(), e);

                throw new CardNotPossessedException();
            }
        }

        return leftToDiscard;
    }

    /**
     * This method uses the list of PowerUp discarded prior to shoot to restore the initial values if something went wrong
     */
    private void restoreSellPowerUp(){

        for (PowerUp powerUp : discardedPowerUpForRestore) {

            Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().addItem(powerUp);
        }

        for (AmmoCube ammoCube : ammoCubeForRestore){

            try {

                Model.getPlayer(controller.getCurrentPlayer()).pay(ammoCube.getColor());

            }catch ( CardNotPossessedException e){

                LOGGER.log(Level.WARNING,e.getMessage(),e);

            }
        }

        discardedPowerUpForRestore.clear();
        ammoCubeForRestore.clear();
    }


    // FRENZY

    /**
     * This function will make the player do a "frenzy move" action
     * @param frenzyMove is the class containing the requested parameters
     */
    public void frenzyMoveAction(Move frenzyMove){

        // logs the action

        LOGGER.log(level, () -> LOG_START_ID + controller.getCurrentPlayer() + " calling Move in frenzy mode ");

        // look if the player is before the first player (0,1,2,3,4)

        boolean frenzyEnhanced = controller.getCurrentPlayer() > controller.getFrenzyStarter();

        if (frenzyMove.getMoves() == null){

            //log

            LOGGER.log(Level.WARNING, () -> LOG_START_FRENZY_S + NO_DIRECTION_SPECIFIED);

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

            handleAction();

        }else if ( frenzyMove.getMoves().size() > MAX_FRENZY_MOVES ){

            //log

            LOGGER.log(Level.WARNING, () -> LOG_START_MOVE + MOVE_MORE_THAN + MAX_FRENZY_MOVES + " in frenzy ");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

            // ask action again

            handleAction();

        } else if ((!frenzyEnhanced) && (frenzyMove.getMoves().size() > MAX_MOVES)){


            //log

            LOGGER.log(Level.WARNING, () -> LOG_START_MOVE + MOVE_MORE_THAN + MAX_MOVES + " in frenzy, but was after first player ");

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

        LOGGER.log(level, () -> LOG_START_ID + controller.getCurrentPlayer() + " calling Grab in frenzy mode ");

        // check if the actions are possible

        if (checkFrenzyGrabMove(frenzyGrab) && checkGrab(frenzyGrab.getNewWeaponName(),frenzyGrab.getDiscardedWeapon(), utilityMethods.simulateMovement(frenzyGrab.getDirections()), frenzyGrab.getPowerUpsForPay())){

            // moves the player

            utilityMethods.move(frenzyGrab.getDirections());

            // grab

            grab(frenzyGrab.getNewWeaponName(),frenzyGrab.getDiscardedWeapon(), frenzyGrab.getPowerUpsForPay());

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

    /**
     * This function will make the player do a "frenzy shoot" action
     * @param frenzyShoot is the class containing the requested parameters
     */
    public void frenzyShootAction(FrenzyShoot frenzyShoot){

        // logs the action

        LOGGER.log(level, () -> LOG_START_ID + controller.getCurrentPlayer() + " calling frenzy shoot ");

        LOGGER.log(level, () -> LOG_START_FRENZY_S + " received frenzy Shoot : [ MOVE: " + frenzyShoot.getMoveAction() + " , RELOAD " + frenzyShoot.getReloadAction() + " , SHOOT: " + frenzyShoot.getShootAction() );

        // do the action

        switch (frenzyShoot.getFieldsNonNull()){

            case 1:

                doFrenzyShootMove(frenzyShoot);

                break;

            case 2:

                doFrenzyReload(frenzyShoot);

                break;

            case 3:

                if (frenzyShoot.getShootAction().getWeaponName() != null) {

                    doShootPartFrenzyShoot(frenzyShoot.getShootAction());

                }

                break;

            default:

                LOGGER.log(Level.WARNING, ()-> LOG_START_FRENZY_S + " received empty frenzy shoot action ");
        }
    }

    /**
     * This method checks the move part of the frenzy shoot
     * @param movePart is the move action contained in the FrenzyShoot Action
     * @return true if validated or false otherwise
     */
    private Boolean checkFrenzyShootMove(Move movePart){

        // look if the player is before the first player (0,1,2,3,4)

        boolean frenzyEnhanced = controller.getCurrentPlayer() > controller.getFrenzyStarter();
        if(movePart != null) {
            if (movePart.getMoves() == null) {

                //log

                LOGGER.log(Level.WARNING, () -> LOG_START_FRENZY_S + NO_DIRECTION_SPECIFIED);

                // show

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

                return false;

            } else if (movePart.getMoves().size() > MAX_FRENZY_SHOOT_MOVES) {

                //log

                LOGGER.log(Level.WARNING, () -> LOG_START_FRENZY_S + MOVE_MORE_THAN + MAX_FRENZY_SHOOT_MOVES + " in frenzy ");

                // show

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

                return false;

            } else if ((movePart.getMoves().size() > MAX_FRENZY_SHOOT_ENHANCED_MOVES) && (frenzyEnhanced)) {

                //log

                LOGGER.log(Level.WARNING, () -> LOG_START_FRENZY_S + MOVE_MORE_THAN + MAX_FRENZY_SHOOT_ENHANCED_MOVES + " in frenzy but was before first player ");

                // show

                controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX);

                return false;
            }
        }

        return true;
    }

    /**
     * This method will do the move part of the frenzy shoot action
     * @param frenzyShoot is a class containing the information needed
     */
    private void doFrenzyShootMove(FrenzyShoot frenzyShoot){

        if (checkFrenzyShootMove(frenzyShoot.getMoveAction())) {

            // does the move part

            utilityMethods.move(frenzyShoot.getMoveAction().getMoves());

            // starts the timer

            controller.getTimer().startTimer(TIMER_ACTION);

            // calls the reload part

            controller.getVirtualView(controller.getCurrentPlayer()).doFrenzyReload();

        }else {

            handleAction();
        }
    }

    /**
     * This method will do the reload part of the frenzy shoot action
     * @param frenzyShoot is a class containing the information needed
     */
    private void doFrenzyReload(FrenzyShoot frenzyShoot){

        if (controller.getReloadPhase().checkIfReloadIsValid(frenzyShoot.getReloadAction())) {

            // does the reload part

            controller.getReloadPhase().reload(frenzyShoot.getReloadAction());

            // starts the timer

            controller.getTimer().startTimer(TIMER_ACTION);

            // calls the shoot part

            controller.getVirtualView(controller.getCurrentPlayer()).doFrenzyAtomicShoot();

        } else {

            controller.getVirtualView(controller.getCurrentPlayer()).doFrenzyReload();
        }
    }

    /**
     * This method will perform the shoot part of the frenzy shoot action
     * @param jsonAction is a class containing the information needed
     */
    private void doShootPartFrenzyShoot(JsonAction jsonAction){

        if (!jsonAction.getType().equals(ActionTypes.SKIP)){

            ShootAction shootAction = (ShootAction) jsonAction;

            if(!frenzyAtomicShoot(shootAction)){

                // ask the player to redo only the shooting part

                controller.getVirtualView(controller.getCurrentPlayer()).doFrenzyAtomicShoot();

            }
        }

        controller.incrementPhase();
    }

    /**
     * This method will perform the atomic frenzy shoot action
     * @param shootAction is a class containing the information needed
     */
    private Boolean frenzyAtomicShoot(ShootAction shootAction){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        List<Integer> playerShotBackup = new ArrayList<>(controller.getShotPlayerThisTurn());

        try {

            shoot(shootAction);

            // notify other players

            notifyShotToOtherPlayer(shootAction);

        }catch (WeaponNotLoadedException weaponNonLoadedException){

            LOGGER.log(Level.WARNING, weaponNonLoadedException.getMessage(), weaponNonLoadedException);

            controller.getVirtualView(playerId).show(DEFAULT_WEAPON_NOT_LOADED);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (PlayerInSameCellException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_SAME_CELL);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (PlayerInDifferentCellException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_PLAYER_IN_DIFFERENT_CELL);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (UncorrectDistanceException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_DISTANCE);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (SeeAblePlayerException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_SEEABLE_PLAYER);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (UncorrectEffectsException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_UNCORRECT_EFFECTS);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (NotCorrectPlayerNumberException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_NOT_CORRECT_PLAYER_NUMBER);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (PlayerNotSeeableException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);

            controller.getVirtualView(playerId).show(DEFAULT_PLAYER_NOT_SEEABLE);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (WeaponNotFoundException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " weapon not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        } catch (CardNotPossessedException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " card not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        } catch (NotEnoughAmmoException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " not enough ammo ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_NO_ENOUGH_AMMO);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        } catch (DifferentPlayerNeededException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " DifferentPlayerNeededException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_DIFFERENT_PLAYER_NEEDED);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        } catch (ArgsNotValidatedException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " ArgsNotValidatedException ");

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            return false;

        }catch (CellNonExistentException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " cell specified in effect is null in model ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_CELL_NOT_EXISTENT);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            handleAction();

            return false;

        } catch (PrecedentPlayerNeededException e) {

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " PrecedentPlayerNeededException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PRECEDENT_PLAYER_NEEDED);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            handleAction();

        } catch (PlayerAlreadyDeadException e){

            LOGGER.log(Level.INFO, () -> LOG_START_SHOOT + " PlayerAlreadyDeadException ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_ALREADY_DEAD);

            shootFailed(null,playerShotBackup);

            restoreSellPowerUp();

            handleAction();
        }

        return true;
    }
}