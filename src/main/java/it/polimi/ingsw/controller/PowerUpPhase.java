package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.powerup.TagbackGrenade;
import it.polimi.ingsw.model.powerup.Teleporter;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.otherplayerturn.PowerUpTurnUpdate;

import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.*;
import static it.polimi.ingsw.utils.PowerUpType.*;

/**
 * This class is used for handling the methods in the powerUp phase
 */
public class PowerUpPhase {

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
     * Timer for the powerUp phase
     */
    private static final int TIMER_POWER_UP = 120;


    // Controller reference

    /**
     * Controller instance
     */
    private final Controller controller;

    /**
     * Default caonstructor
     * @param controller is the controller which instantiated this class
     */
    public PowerUpPhase(Controller controller) {
        this.controller = controller;
    }

    // Server -> Client flow

    /**
     * This methods will forward the action Request to the virtual View
     */
    public void handlePowerUp(){

        // if someone has been shot ask them to use grenades

        if (!controller.getShotPlayerThisTurn().isEmpty()) {

            askGrenadeToShot();

        }else {

            int currentPlayer = controller.getCurrentPlayer();

            if (!controller.isPlayerOnline(currentPlayer)) {

                // if the player is not online skips the turn

                controller.incrementPhase();

            } else if (!hasPowerUpPhase()) {

                //if current player hasn't got any usable PowerUp in hand  ( Newton or Teleporter ) -> skip this phase

                controller.incrementPhase();

            } else {

                // start the timer

                controller.getTimer().startTimer(TIMER_POWER_UP);

                // sends the action

                controller.getVirtualView(currentPlayer).startPowerUp();

            }
        }
    }

    /**
     * This method will ask to all the players who have been shot in this turn if they want to use a grenade on the shooter
     */
    public void askGrenadeToShot(){

        if (!controller.getShotPlayerThisTurn().isEmpty()) {

            int playerId = controller.getShotPlayerThisTurn().get(0);

            if ( ( controller.isPlayerOnline(playerId) ) && ( hasGrenade(playerId) && (playerId != controller.getCurrentPlayer())) ) {

                System.out.println(" asked grenade to player : " + playerId);

                controller.getVirtualView(playerId).askGrenade();

                // start the timer

                //controller.getTimer().startTimer(TIMER_POWER_UP);

                controller.setExpectingAnswer(true);

                controller.setExpectingGrenade(true);

            }else {

                // if the player does not have the grenades it gets removed from the list and skipped

                controller.getShotPlayerThisTurn().remove(0);

                handlePowerUp();
            }

        }

    }

    // Client -> Server flow

    /**
     * This method will make the player use a PowerUp by forwarding the request to different methods depending on the type of the powerUp
     *
     * TargetingScope powerUps will be handled differently in the shootAction
     *
     * @param powerUpAction is the jsonAction class used for powerUp phase actions
     *
     * @see it.polimi.ingsw.view.actions.usepowerup.PowerUpAction
     */
    public void usePowerUp(PowerUpAction powerUpAction){

        // stop timer

        controller.getTimer().stopTimer();

        controller.setExpectingAnswer(false);

        switch (powerUpAction.getPowerUpType()){

            case NEWTON:

                useNewton((NewtonAction) powerUpAction);

                break;

            case TELEPORTER:

                useTeleport((TeleporterAction) powerUpAction);

                break;

            case TARGETING_SCOPE:

                break;

            case TAG_BACK_GRENADE:

                useGrenade((GrenadeAction) powerUpAction);

                break;

            default:

                break;

        }
    }

    /**
     * This method will make the current player usa a newton powerUp
     * @param newtonAction is a jsonAction used to handle useNewton action
     */
    public void useNewton(NewtonAction newtonAction){

        int currentPlayer = controller.getCurrentPlayer();

        if (newtonAction.getTargetPlayerId() == currentPlayer){

            LOGGER.warning("[CONTROLLER - PowerUp] player tried to use Newton on himself");

            controller.getVirtualView(currentPlayer).show(DEFAULT_PLAYER_USED_NEWTON_ON_HIMSELF);

            handlePowerUp();
        }

        LOGGER.log(level,"[CONTROLLER - PowerUp] calling useNewton on player w/ id: {0}",currentPlayer);

        try {

            // Locate the powerUp in the model

            Newton newton = (Newton) Model.getPlayer(currentPlayer).getPowerUpBag().findItem(NEWTON, newtonAction.getColor());

            // calls the use() method in the powerUp

            newton.use(Model.getPlayer(newtonAction.getTargetPlayerId()), newtonAction.getDirection() , newtonAction.getAmount());

            // discard the powerUp

            discardPowerUp(newtonAction.getPowerUpType(),newtonAction.getColor());

            controller.updateInactivePlayers(new PowerUpTurnUpdate(currentPlayer,new CachedPowerUp(newtonAction.getPowerUpType(),newtonAction.getColor())));

            handlePowerUp();


        } catch(Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);

        }
    }

    /**
     * This method will make the current player usa a Teleporter powerUp
     * @param teleporterAction is a jsonAction used to handle useTeleporter action
     */
    public void useTeleport(TeleporterAction teleporterAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] calling useTeleport on player w/ id: {0} ", currentPlayer );

        // Gets the new Position

        Point newPos = teleporterAction.getCell();

        // gets the chosen Cell from the model

        Cell cell = Model.getMap().getCell( newPos.x, newPos.y);

        try {

            // Locate the powerUp in the model

            Teleporter t = (Teleporter) Model.getPlayer(currentPlayer).getPowerUpBag().findItem(TELEPORTER, teleporterAction.getColor());

            // calls the use() method in the powerUp

            t.use(cell);

            // discard the powerUp

            discardPowerUp( teleporterAction.getPowerUpType(), teleporterAction.getColor());

            controller.updateInactivePlayers(new PowerUpTurnUpdate(currentPlayer,new CachedPowerUp(teleporterAction.getPowerUpType(),teleporterAction.getColor())));

            handlePowerUp();


        } catch (CardNotPossessedException e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] player do not possess given powerUp ");

            // send show message

            controller.getVirtualView(currentPlayer).show(DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP);

            handlePowerUp();

        } catch (CellNonExistentException e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] cell does not exist ");

            // send show message

            controller.getVirtualView(currentPlayer).show(DEFAULT_CELL_NOT_EXISTENT);

            handlePowerUp();
        }
    }

    /**
     * This method will make the current player usa a grenade powerUp
     * @param grenadeAction is a jsonAction used to handle useGrenade action
     */
    public void useGrenade(GrenadeAction grenadeAction){

        controller.setExpectingGrenade(false);

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] player w/ id: {0} was shot and responded with a grenade", grenadeAction.getPossessorId() );

        if (grenadeAction.getColor() == null ){

            //if the player choose to not use the grenade the color will be set to null:

            // Remove the player from the shot list

            controller.getShotPlayerThisTurn().remove(0);

            // calls teh function to ask grenade to the next person in the list

            handlePowerUp();

        } else {

            try {

                // Locate the powerUp in the model

                TagbackGrenade grenade = (TagbackGrenade) Model.getPlayer(grenadeAction.getPossessorId()).getPowerUpBag().findItem(TAG_BACK_GRENADE, grenadeAction.getColor());

                // use the grenade on the current player

                grenade.applyOn(Model.getPlayer(currentPlayer), grenadeAction.getPossessorId());

                // discard the powerUp

                Model.getPlayer(grenadeAction.getPossessorId()).getPowerUpBag().getItem(grenade);

                // Remove the player from the shot list

                controller.getShotPlayerThisTurn().remove(0);

                // notify the other players

                controller.updateAllPlayersButOne(new PowerUpTurnUpdate(grenadeAction.getPossessorId(),new CachedPowerUp(grenadeAction.getPowerUpType(),grenadeAction.getColor())), grenadeAction.getPossessorId() );

                // calls teh function to ask grenade to the next person in the list

                handlePowerUp();

            } catch (Exception e) {

                LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] player w/ id {0} try to use a grenade but do not possess it", grenadeAction.getPossessorId());

                // send show message

                controller.getVirtualView(currentPlayer).show(DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP);

                LOGGER.log(Level.WARNING, e.getMessage(), e);

                handlePowerUp();
            }

        }

    }

    /**
     * This function will be used as default answer if the player timer expires after ask if wants to use a grenade
     */
    public void defaultGrenade(){

        controller.setExpectingGrenade(false);

        // Remove the player from the shot list

        controller.getShotPlayerThisTurn().remove(0);

        // calls teh function to ask grenade to the next person in the list

        askGrenadeToShot();
    }


    // Utils

    /**
     * This method will make the current player discard a powerUp
     * @param type is the powerUp type
     * @param color is the powerUp Color
     */
    public void discardPowerUp(PowerUpType type, Color color){

        int currentPlayer = controller.getCurrentPlayer();

        Model.getPlayer(currentPlayer).getPowerUpBag()
                .sellItem(Model.getPlayer(currentPlayer).getPowerUpBag().findItem(type, color));

    }

    /**
     * This method states if the player specified has grenades or not
     * @param playerId is the id of the queried player
     * @return true if has grenades, false otherwise
     */
    private Boolean hasGrenade(int playerId){

        List<PowerUp> list = Model
                .getPlayer(playerId)
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> x.getType().equals(TAG_BACK_GRENADE))
                .collect(Collectors.toList());

        return !(list.isEmpty());
    }

    /**
     * This method will states if the player has powerUps that can use in the powerUp phase
     * @return true if he has some, false otherwise
     */
    private Boolean hasPowerUpPhase(){

        List<Player> spawned = Model
                .getGame()
                .getPlayers()
                .stream()
                .filter( x -> (x.getStats().getCurrentPosition() != null ))
                .collect(Collectors.toList());

        List<PowerUp> list = Model
                .getPlayer(controller.getCurrentPlayer())
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> (x.getType().equals(TELEPORTER)) || (x.getType().equals(NEWTON)) )
                .collect(Collectors.toList());

        if (spawned.size() <= 1){

            list = list.stream()
                    .filter( x -> x.getType().equals(TELEPORTER) )
                    .collect(Collectors.toList());

        }

        return !(list.isEmpty());

    }

    /**
     * This method states if the currentPlayer has any targetingScope powerup
     * @return true if he has, false if he doesn't
     */
    public Boolean hasTargetingScope(){

        List<PowerUp> list = Model
                .getPlayer(controller.getCurrentPlayer())
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> x.getType().equals(TARGETING_SCOPE) )
                .collect(Collectors.toList());

        return !(list.isEmpty());
    }

}
