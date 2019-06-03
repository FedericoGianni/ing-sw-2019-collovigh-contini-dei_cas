package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.actions.GrabAction;
import it.polimi.ingsw.view.actions.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ActionPhase {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private final Controller controller;

    public ActionPhase(Controller controller) {
        this.controller = controller;
    }


    /**
     * This methods will forward the action Request to the virtual View
     */
    public void handleAction(){

        int currentPlayer = controller.getCurrentPlayer();

        if (!controller.isPlayerOnline(currentPlayer)) {

            // if the player is not online skips the turn

            controller.incrementPhase();

        }else{

            // sends the startPhase command to the virtual view

            controller.getVirtualView(currentPlayer).startAction();
        }
    }

    /**
     * This methods will move the player
     * @param moveAction is the moveAction requested by the client
     */
    public void move(Move moveAction){

        // logs the action

        LOGGER.log(level,() -> "[CONTROLLER] player id " + controller.getCurrentPlayer() + "calling move");

        if (moveAction.getMoves().size() > 3){

            // if the player tried to move more than 3 steps recalls the action

            LOGGER.log(level, () -> "[Controller-MovePhase] received illegal Move Action Request # of moves req:" + moveAction.getMoves().size() );

            handleAction();

        }else {

            // reads the final position

            Point finalPos =  moveAction.getFinalPos();

            // gets the correspondent cell in the model

            Cell cell = Model.getMap().getCell(finalPos.x, finalPos.y);

            // move the player
            //TODO @Dav not working maybe you should have passed from getStats to change position?
            Model.getPlayer(controller.getCurrentPlayer()).getStats().setCurrentPosition(cell);

            // increment the phase

            controller.incrementPhase();
        }

    }


    public void moveGrab(GrabAction grabAction){

        // logs the action

        LOGGER.log(level,() -> "[CONTROLLER] player id " + controller.getCurrentPlayer() + "calling Grab");

        int playerId = controller.getCurrentPlayer();

        // if the player wants to move

        if (!grabAction.getDirection().isEmpty()){

            // the first move can be done by anyone

            moveCurrentPlayer(grabAction.getDirection().get(0));

            // the second one can be done only if you have more than 2 damage points

            if ((Model.getPlayer(playerId).getDmg().size() > 2) && (grabAction.getDirection().size() > 1)) {

                moveCurrentPlayer(grabAction.getDirection().get(1));

            }else {

                LOGGER.log(Level.WARNING, () -> "[Controller-GrabAction] Player w/ id: " + playerId + " tried to move more than 1 but only has damage : " + Model.getPlayer(playerId).getDmg().size() );

            }
        }

        grabStuffFromCurrPosition(grabAction.getNewWeaponName());

        // check if player has more than MAX weapon:

        if (Model.getPlayer(playerId).getCurrentWeapons().getList().size() > 3){

            if (grabAction.getDiscardedWeapon() == null){


            }
        }

    }


    public void grab(){
        //TODO
        System.out.println("[DEBUG] Grab called inside controller!");
    }


    public void shoot(int weapon, int target){
    }


    private void moveCurrentPlayer(Directions direction){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        Cell position = Model.getPlayer(playerId).getCurrentPosition();

        // change the position var in the direction given

        switch (direction){

            case NORTH:

                if (position.getNorth() != null ) position = position.getNorth();

                break;

            case EAST:

                if (position.getEast() != null ) position = position.getEast();

                break;

            case WEST:

                if (position.getWest() != null ) position = position.getWest();

                break;

            case SOUTH:

                if (position.getSouth() != null ) position = position.getSouth();

                break;

            default:

                break;
        }

        // sets the player in the new position

        Model.getPlayer(playerId).setPlayerPos(position);

        // logs the position change

        LOGGER.log(level,() -> "[Controller-GrabAction] Player w/ id: " + playerId + " moved " + direction + " in cell : " + Model.getMap().cellToCoord(Model.getPlayer(playerId).getCurrentPosition()) );
    }

    private void grabStuffFromCurrPosition(String weaponName){

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        Cell position = Model.getPlayer(playerId).getCurrentPosition();

        // if the cell is an ammoCell it picks up the ammo

        if (position.isAmmoCell()){

            Model.getPlayer(playerId).pickAmmoHere();

        }else{

            SpawnCell spawnCell = (SpawnCell) position;

            Weapon selected;

            // if the cell is a spawn cell it draws the weapon it is asked by the player

            if (weaponName == null){

                LOGGER.log(Level.WARNING,() ->"[Controller-GrabAction] Player w/ id: " + playerId + " tried to grab a weapon but specified no name: the cheaper one will be purchased if the player can pay for it" );

                selected = selectCheapestWeapon(spawnCell);

            }else {

                // serach the weapon with the specified name

                List<Weapon> namesCheckWeapons = spawnCell
                        .getWeapons()
                        .stream()
                        .filter(x -> x.getName().equalsIgnoreCase(weaponName))
                        .collect(Collectors.toList());

                if (namesCheckWeapons.isEmpty()) {

                    // Logs

                    LOGGER.log(Level.WARNING, () -> "[Controller-GrabAction] Player w/ id: " + playerId + " specified wrong weapon name: the cheaper one will be purchased if the player can pay for it");

                    // buy the cheaper weapon in the cell

                    selected = selectCheapestWeapon(spawnCell);

                }else {

                    //select the weapon

                    selected = namesCheckWeapons.get(0);


                }

                try {

                    // buy the selected weapon

                    spawnCell.buy(selected, Model.getPlayer(playerId));

                    // Log

                    LOGGER.log(Level.WARNING, () -> "[Controller-GrabAction] Player w/ id: " + playerId + " specified wrong weapon name: the cheaper one will be purchased if the player can pay for it");

                }catch (NotEnoughAmmoException e){

                    LOGGER.log(Level.WARNING, () -> "[Controller-GrabAction] Player w/ id: " + playerId + " could not pay for the weapon");
                }


            }
        }
    }

    private Weapon selectCheapestWeapon( SpawnCell spawnCell ){

        List<Weapon> weapons = new ArrayList<>(spawnCell.getWeapons());

        Collections.sort(weapons,( p1, p2 ) -> {

            if (p1.getCost().size() < p2.getCost().size()) return -1;

            else if (p1.getCost().size() > p2.getCost().size()) return 1;

            else return 0;
        });


        return weapons.get(0);

    }

    boolean askMoveValid(int row, int column, Directions direction){

        System.out.println("[CONTROLLER] receiveed askMove valid from pos: " + row + ", " + column + " and direction : " + direction );
        //System.out.println(" CELL: is ammocell: " + Model.getMap().getCell(row,column).isAmmoCell() + " have color : "+ Model.getMap().getCell(row,column).getColor());

        Cell startCell = Model.getMap().getCell(row,column);

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
