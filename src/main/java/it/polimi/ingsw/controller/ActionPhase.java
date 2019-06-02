package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.view.actions.Move;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            LOGGER.log(level, () -> "[Controller-ActionPhase] received illegal Move Action Request # of moves req:" + moveAction.getMoves().size() );

            handleAction();

        }else {

            // reads the final position

            Point finalPos =  moveAction.getFinalPos();

            // gets the correspondent cell in the model

            Cell cell = Model.getMap().getCell(finalPos.y, finalPos.x);

            // move the player

            Model.getPlayer(controller.getCurrentPlayer()).setPlayerPos(cell);

            // increment the phase

            controller.incrementPhase();
        }

    }


    public void moveGrab(int r, int c){

    }

    public void grab(){
    }


    public void shoot(int weapon, int target){
    }

    Boolean askMoveValid(int row, int column, Directions direction){

        //TODO

        return false;
    }
}
