package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
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

    /**
     * This methods will move the player
     *
     * @param moveAction is the moveAction requested by the client
     */
    public void move(Move moveAction) {

        // logs the action

        LOGGER.log(level, () -> "[CONTROLLER] player id " + controller.getCurrentPlayer() + "calling move");

        if (moveAction.getMoves().size() > 3) {

            // if the player tried to move more than 3 steps recalls the action

            LOGGER.log(level, () -> "[Controller-MovePhase] received illegal Move Action Request # of moves req:" + moveAction.getMoves().size());

            handleAction();

        } else {

            // moves the current player in the directions specified in the list

            for (Directions direction : moveAction.getMoves()){

                moveCurrentPlayer(direction);
            }

            // increment the phase

            controller.incrementPhase();
        }

    }


    public void moveGrab(GrabAction grabAction) {

        boolean moveValid = true;

        boolean grabValid = false;

        // logs the action

        LOGGER.log(level, () -> "[CONTROLLER] player id " + controller.getCurrentPlayer() + " calling Grab");

        int playerId = controller.getCurrentPlayer();

        // if the player wants to move

        if (!grabAction.getDirection().isEmpty()) {

            // the first move can be done by anyone

            moveCurrentPlayer(grabAction.getDirection().get(0));

            // the second one can be done only if you have more than 2 damage points

            if ((Model.getPlayer(playerId).getDmg().size() > 2) && (grabAction.getDirection().size() > 1)) {

                moveCurrentPlayer(grabAction.getDirection().get(1));

            } else {

                LOGGER.log(Level.WARNING, () -> LOG_START + playerId + " tried to move more than 1 but only has damage : " + Model.getPlayer(playerId).getDmg().size());

                moveValid = false;

            }
        }

        // grab the correspondent item from the cell the player is in

        grabValid = grabStuffFromCurrPosition(grabAction.getNewWeaponName());

        // check if player has more than MAX weapon:

        if (Model.getPlayer(playerId).getCurrentWeapons().getList().size() > 3) {

            if (grabAction.getDiscardedWeapon() != null){

                // search in the player's weapon bag the specified weapon

                List<Weapon> matchingList = Model
                        .getPlayer(playerId)
                        .getCurrentWeapons()
                        .getList().stream()
                        .filter(x -> x.getName().equals(grabAction.getDiscardedWeapon()))
                        .collect(Collectors.toList());

                // if the weapon exist it deletes it

                if (!matchingList.isEmpty()){

                    try {

                        Model.getPlayer(playerId).delWeapon(matchingList.get(0));

                        LOGGER.log(level, () -> LOG_START + playerId + " discarded weapon w/ name: " + matchingList.get(0).getName());

                    }catch (CardNotPossessedException e){

                        LOGGER.log(Level.WARNING, e.getMessage(), e);
                    }

                }else{

                    // if the weapon do not exist it sets the validity bool to false

                    grabValid = false;

                    LOGGER.log(level,() -> LOG_START + playerId + " name specified for discarded weapon does not correspond to weapon");

                }

            }else {

                // if the weapon do not exist it sets the validity bool to false

                grabValid = false;

                LOGGER.log(level,() -> LOG_START + playerId + " no name specified for discarded weapon ");

            }

        }


        if (grabValid && moveValid){

            controller.incrementPhase();

            controller.handleTurnPhase();

            // TODO controller.updateInactivePlayers(n);

        }else {

            handleAction();
        }

    }


    public void shoot(int weapon, int target) {
    }


    private void moveCurrentPlayer(Directions direction) {

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        Cell position = Model.getPlayer(playerId).getCurrentPosition();

        // change the position var in the direction given

        switch (direction) {

            case NORTH:

                if (position.getNorth() != null) position = position.getNorth();

                break;

            case EAST:

                if (position.getEast() != null) position = position.getEast();

                break;

            case WEST:

                if (position.getWest() != null) position = position.getWest();

                break;

            case SOUTH:

                if (position.getSouth() != null) position = position.getSouth();

                break;

            default:

                break;
        }

        // sets the player in the new position

        Model.getPlayer(playerId).setPlayerPos(position);

        // logs the position change

        LOGGER.log(level, () -> LOG_START + playerId + " moved " + direction + " in cell : " + Model.getMap().cellToCoord(Model.getPlayer(playerId).getCurrentPosition()));
    }

    private boolean grabStuffFromCurrPosition(String weaponName){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        Cell position = Model.getPlayer(playerId).getCurrentPosition();

        // if the cell is an ammoCell it picks up the ammo

        if (position.isAmmoCell()) {

            Model.getPlayer(playerId).pickAmmoHere();

            return true;

        }else{

            SpawnCell spawnCell = (SpawnCell) position;

            Weapon selected;

            // if the cell is a spawn cell it draws the weapon it is asked by the player

            if (weaponName == null) {

                LOGGER.log(Level.WARNING,"[Controller-GrabAction] Player w/ id: {0} tried to grab a weapon but specified no name", playerId );

                return false;

            } else {

                // serach the weapon with the specified name

                List<Weapon> namesCheckWeapons = spawnCell
                        .getWeapons()
                        .stream()
                        .filter(x -> x.getName().equalsIgnoreCase(weaponName))
                        .collect(Collectors.toList());

                if (namesCheckWeapons.isEmpty()) {

                    // Logs

                    LOGGER.log(Level.WARNING, "[Controller-GrabAction] Player w/ id: {0} specified wrong weapon name", playerId);

                    return false;

                } else {

                    //select the weapon

                    selected = namesCheckWeapons.get(0);

                    try {

                        // buy the selected weapon

                        spawnCell.buy(selected, Model.getPlayer(playerId));

                        // Log

                        LOGGER.log(level, () -> LOG_START + playerId + " bought a new weapon : " + selected.getName());

                        return true;


                    }catch (NotEnoughAmmoException e){

                        LOGGER.log(Level.WARNING, "[Controller-GrabAction] Player w/ id: {0} could not pay for the weapon", playerId);

                        return false;
                    }

                }
            }
        }
    }

    boolean askMoveValid(int row, int column, Directions direction) {

        //LOG

        LOGGER.log(level, () -> "[CONTROLLER] receiveed askMove valid from pos: " + row + ", " + column + " and direction : " + direction);

        if (Model.getMap().getCell(row, column) == null) {
            return false;
        } else {
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

}