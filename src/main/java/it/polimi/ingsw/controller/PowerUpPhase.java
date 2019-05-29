package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.customsexceptions.PlayerNonExistentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.model.powerup.Teleporter;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.model.powerup.PowerUpType.NEWTON;
import static it.polimi.ingsw.model.powerup.PowerUpType.TELEPORTER;

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

        if(Model.getPlayer(currentPlayer).getPowerUpBag().getList().isEmpty()){

            //if current player hasn't got any PowerUp in hand -> skip this phase

            controller.incrementPhase();

        }else{

            controller.getVirtualView(currentPlayer).startPowerUp();

        }
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

        } catch(PlayerNonExistentException e){

            //can be validated inside client so that he can send only existent players

        } catch(CellNonExistentException e){

            //same as before

        }
    }

    public void useTeleport(TeleporterAction teleporterAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] calling useTeleport on player w/ id: {0} ", currentPlayer );

        // Gets the new Position

        Point newPos = teleporterAction.getCell();

        // gets the chosen Cell from the model

        Cell cell = Model.getMap().getCell(newPos.y,newPos.x);  // TODO verificare

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


    public void discardPowerUp(PowerUpType type, Color color){

        int currentPlayer = controller.getCurrentPlayer();

        Model.getPlayer(currentPlayer).getPowerUpBag()
                .sellItem(Model.getPlayer(currentPlayer).getPowerUpBag().findItem(type, color));

    }
}
