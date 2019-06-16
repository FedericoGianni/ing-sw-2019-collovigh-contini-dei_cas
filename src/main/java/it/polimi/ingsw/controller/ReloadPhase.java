package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.actions.ReloadAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReloadPhase {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static final Level level = Level.INFO;

    private static final String LOG_START = "[Controller-ReloadPhase]";

    private final Controller controller;

    public ReloadPhase(Controller controller) {

        this.controller = controller;

    }

    public void handleReload(){

        // gets the current player

        int playerId = controller.getCurrentPlayer();

        if(!(Model.getGame().getPlayers().get(playerId).getWeapons().isEmpty())){

            controller.getVirtualView(playerId).startReload();

        }else {

            LOGGER.log(level,()->LOG_START + " player w/ id:  " + playerId + " skipped reload bc has no weapons ");

            // if the player has no weapon the phase get skipped automatically

            controller.incrementPhase();
        }
    }

    public void reload(ReloadAction reloadAction){


    }

    private List<Weapon> getWeapons(List<String> weaponNames) throws WeaponNotFoundException {

        // gets the current player

        int playerId = controller.getCurrentPlayer();

        try{

            List<Weapon> weaponList = weaponNames
                    .stream()
                    .map( name -> controller.getUtilityMethods().findWeaponInWeaponBag(name,playerId))
                    .collect(Collectors.toList());

            if (weaponList.contains(null)) throw new WeaponNotFoundException();

            return (weaponList.contains(null)) ? null : weaponList;

        }catch (Exception e){

            throw new WeaponNotFoundException();
        }
    }

    private Boolean checkIfPlayerPossessPowerUps(List<CachedPowerUp> powerUps){

        Boolean returnValue = true;

        List<PowerUp> possessed = Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getList();

        try{

            for (CachedPowerUp cachedPowerUp : powerUps){

                PowerUp toRemove = controller.getUtilityMethods().CachedToRealPowerUp(cachedPowerUp,possessed);

                possessed.remove(toRemove);

            }

        }catch (CardNotPossessedException e){

            LOGGER.log(Level.WARNING,() -> LOG_START + " player does not possess all powerUps he declared ");

            return false;
        }

        return returnValue;
    }
}
