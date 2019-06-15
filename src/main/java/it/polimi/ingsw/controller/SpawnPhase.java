package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpawnPhase {

    // LOGGER

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    // Controller reference

    private final Controller controller;

    public SpawnPhase(Controller controller) {
        this.controller = controller;
    }

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



        } else if (Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getList().isEmpty()) {

            //if currentPlayer already has 0 powerups in hand -> draw 2

            LOGGER.log(level, drawString);
            controller.drawPowerUp();
            LOGGER.log(level, drawString);
            controller.drawPowerUp();
            LOGGER.log(level, spawnString);
            controller.getVirtualView(controller.getCurrentPlayer()).startSpawn();

            // start timer

            controller.getTimer().startTimer(10);

        } else {

            //if currentPlayer already has more then 0 -> draw only 1

            LOGGER.log(level, drawString);
            controller.drawPowerUp();
            LOGGER.log(level, spawnString);
            controller.getVirtualView(controller.getCurrentPlayer()).startSpawn();

            // start timer

            controller.getTimer().startTimer(10);
        }
    }

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


    public void spawn(PowerUpType type, Color color){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level,"[CONTROLLER - Spawn] accessing Model to discard PowerUp for player: {0} ", currentPlayer );
        controller.discardPowerUp(type, color);
        Model.getPlayer(currentPlayer).setPlayerPos(Model.getMap().getSpawnCell(color));
        LOGGER.log(level, "[CONTROLLER - Spawn] incrementingGamePhase: {0} ", controller.getTurnPhase() );
        controller.incrementPhase();
        LOGGER.log(level,"[CONTROLLER - Spawn] new Phase: {0} ", controller.getTurnPhase());
    }
}
