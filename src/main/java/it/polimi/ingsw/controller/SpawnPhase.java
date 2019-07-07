package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will handle all the methods for the spawn phase of the turn
 */
public class SpawnPhase {

    // LOGGER

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.FINE;

    /**
     * Timer for the Spawn Phase
     */
    private static final int TIMER_SPAWN = 60;

    // Controller reference

    /**
     * controller instance
     */
    private final Controller controller;

    /**
     * Constructor
     * @param controller is the controller that instantiated this class
     */
    public SpawnPhase(Controller controller) {
        this.controller = controller;
    }

    /**
     * This method will ask the current player to spawn needed.
     *
     * If the player is offline the spawn will be performed automatically:
     * @see #defaultSpawn()
     */
    public void handleSpawn(){



        String drawString = "[CONTROLLER - Spawn] accessing Model to drawPowerUp for player: " + controller.getCurrentPlayer();
        String spawnString = "[CONTROLLER - Spawn] calling startPhase0 for virtual view id: " + controller.getCurrentPlayer();

        if (Model.getPlayer(controller.getCurrentPlayer()).getStats().getCurrentPosition() != null) {

            //if player has already spawned it skips this phase

            LOGGER.log(level,"[CONTROLLER - Spawn] skipping SPAWN phase for player:{0} ", controller.getCurrentPlayer());
            controller.incrementPhase();



        } else if (!controller.isPlayerOnline(controller.getCurrentPlayer())) {

            // if the player is not online skips the phase and do default spawn

            defaultSpawn();

            controller.incrementPhase();



        } else {

            //if currentPlayer already has 0 powerups in hand -> draw 2

            if (Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getList().isEmpty()){

                LOGGER.log(level, drawString);

                controller.drawPowerUp();

            }

            LOGGER.log(level, drawString);

            controller.drawPowerUp();

            LOGGER.log(level, spawnString);

            // start timer

            controller.getTimer().startTimer(TIMER_SPAWN);

            // sends the action

            controller.getVirtualView(controller.getCurrentPlayer()).startSpawn();



        }
    }

    /**
     * method for handling the spawn of a disconnected player, the player will draw one or two powerUp and discard the first one
     */
    public void defaultSpawn(){

        // if the player have 0 powerUps draw 2

        if (Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getList().isEmpty()) controller.drawPowerUp();

        controller.drawPowerUp();

        // discard the first one

        PowerUp powerUp = Model
                .getPlayer(controller.getCurrentPlayer())
                .getPowerUpBag()
                .getList()
                .get(0);

        spawn(powerUp.getType(), powerUp.getColor());
    }

    /**
     * This method is called by the view class and make the player spawn by discarding the selected powerUp
     * @param type is the powerUp type
     * @param color is the powerUp color
     */
    public void spawn(PowerUpType type, Color color){

        int currentPlayer = controller.getCurrentPlayer();

        controller.setExpectingAnswer(false);

        LOGGER.log(level,"[CONTROLLER - Spawn] accessing Model to discard PowerUp for player: {0} ", currentPlayer );

        controller.discardPowerUp(type, color);

        Model.getPlayer(currentPlayer).setPlayerPos(Model.getMap().getSpawnCell(color));

        LOGGER.log(level, "[CONTROLLER - Spawn] incrementingGamePhase: {0} ", controller.getTurnPhase() );

        controller.incrementPhase();

        LOGGER.log(level,"[CONTROLLER - Spawn] new Phase: {0} ", controller.getTurnPhase());
    }
}
