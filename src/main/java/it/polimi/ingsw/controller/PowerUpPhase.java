package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.powerup.*;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.List;

import static it.polimi.ingsw.model.powerup.PowerUpType.*;

public class PowerUpPhase {

    // LOGGER

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    // Controller reference

    private final Controller controller;

    public PowerUpPhase(Controller controller) {
        this.controller = controller;
    }

    // Server -> Client flow

    public void handlePowerUp(){

        int currentPlayer = controller.getCurrentPlayer();

        if(hasPowerUpPhase()){

            //if current player hasn't got any usable PowerUp in hand  ( Newton or Teleporter ) -> skip this phase

            controller.incrementPhase();

        }else{

            controller.getVirtualView(currentPlayer).startPowerUp();

        }
    }

    public void askGrenadeToShot(){

        for (Integer playerId: controller.getShotPlayerThisTurn()){

            if (hasGrenade(playerId)){

                //TODO controller.getVirtualView(playerId).askGrenade()
            }
        }

        // clears the list

        controller.getShotPlayerThisTurn().clear();
    }

    // Client -> Server flow

    public void usePowerUp(PowerUpAction powerUpAction){

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

    public void useNewton(NewtonAction newtonAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level,"[CONTROLLER - PowerUp] calling useNewton on player w/ id: {0}",currentPlayer);

        try {

            // Locate the powerUp in the model

            Newton newton = (Newton) Model.getPlayer(currentPlayer).getPowerUpBag().findItem(NEWTON, newtonAction.getColor());

            // calls the use() method in the powerUp

            newton.use(Model.getPlayer(newtonAction.getTargetPlayerId()), newtonAction.getDirection() , newtonAction.getAmount());

            // discard the powerUp

            discardPowerUp(newtonAction.getPowerUpType(),newtonAction.getColor());

        } catch(Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);

        }
    }

    public void useTeleport(TeleporterAction teleporterAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] calling useTeleport on player w/ id: {0} ", currentPlayer );

        // Gets the new Position

        Point newPos = teleporterAction.getCell();

        // gets the chosen Cell from the model

        Cell cell = Model.getMap().getCell( newPos.y, newPos.x);

        try {

            // Locate the powerUp in the model

            Teleporter t = (Teleporter) Model.getPlayer(currentPlayer).getPowerUpBag().findItem(TELEPORTER, teleporterAction.getColor());

            // calls the use() method in the powerUp

            t.use(cell);

            // discard the powerUp

            discardPowerUp( teleporterAction.getPowerUpType(), teleporterAction.getColor());


        } catch (CardNotPossessedException e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] player do not possess given powerUp ");

        } catch (CellNonExistentException e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] cell does not exist ");
        }
    }


    public void useGrenade(GrenadeAction grenadeAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] player w/ id: {0} was shot and responded with a grenade", grenadeAction.getPossessorId() );

        try {

            // Locate the powerUp in the model

            TagbackGrenade grenade = (TagbackGrenade) Model.getPlayer(grenadeAction.getPossessorId()).getPowerUpBag().findItem(TAG_BACK_GRENADE, grenadeAction.getColor());

            // use the grenade on the current player

            grenade.applyOn(Model.getPlayer(currentPlayer), grenadeAction.getPossessorId());

            // discard the powerUp

            Model.getPlayer(grenadeAction.getPossessorId()).getPowerUpBag().getItem(grenade);

        }catch (Exception e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] player w/ id {0} try to use a grenade but do not possess it", grenadeAction.getPossessorId());
            LOGGER.log(Level.WARNING,e.getMessage(),e);
        }

    }


    public void discardPowerUp(PowerUpType type, Color color){

        int currentPlayer = controller.getCurrentPlayer();

        Model.getPlayer(currentPlayer).getPowerUpBag()
                .sellItem(Model.getPlayer(currentPlayer).getPowerUpBag().findItem(type, color));

    }

    // Utils

    public Boolean hasGrenade(int playerId){

        List<PowerUp> list = Model
                .getPlayer(playerId)
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> x.getType().equals(TAG_BACK_GRENADE))
                .collect(Collectors.toList());

        return !(list.isEmpty());
    }

    public Boolean hasPowerUpPhase(){

        List<PowerUp> list = Model
                .getPlayer(controller.getCurrentPlayer())
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> (x.getType().equals(TELEPORTER)) || (x.getType().equals(NEWTON)) )
                .collect(Collectors.toList());

        return !(list.isEmpty());
    }

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
