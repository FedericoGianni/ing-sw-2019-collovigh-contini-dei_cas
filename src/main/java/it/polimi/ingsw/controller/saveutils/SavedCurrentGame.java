package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.model.player.Skull;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to serialize the attributes needed from CurrentGame in a file to be read, allowing game persistence
 */
public class SavedCurrentGame implements Serializable {

    /**
     * number of skulls placed in the killshot track at the beginning of the match
     */
    private final int skulls;

    /**
     * state of the killshottrack to be saved
     */
    private final List<Skull> killShotTrack;

    /**
     * state of the weapon deck to be saved, just as a list of weapon names
     */
    private final List<String> weaponDeck;

    /**
     * state of the powerup deck to be saved, just as a list of cached power ups
     */
    private final List<CachedPowerUp> powerUpDeck;

    /**
     * state of the thrashpowerup deck to be saved
     */
    private final List<CachedPowerUp> thrashPowerUpDeck;

    /**
     * Constructor which takes a real CurrentGame and extract the attributes it needs in this serializable class
     * @param c current game reference
     */
    public SavedCurrentGame(CurrentGame c) {

        this.skulls = c.getSkulls();

        this.killShotTrack = c.getKillShotTrack();

        this.weaponDeck = c.getWeaponDeck().getWeaponsList().stream()
                            .map(weapon -> weapon.getName())
                            .collect(Collectors.toList());

        this.powerUpDeck = c.getPowerUpDeck().getPowerUpList().stream()
                            .map(powerUp -> new CachedPowerUp(powerUp.getType(), powerUp.getColor()))
                            .collect(Collectors.toList());

        this.thrashPowerUpDeck = c.getThrashPowerUpDeck().getPowerUpList().stream()
                .map(powerUp -> new CachedPowerUp(powerUp.getType(), powerUp.getColor()))
                .collect(Collectors.toList());
    }

    public int getSkulls() {
        return skulls;
    }

    public List<Skull> getKillShotTrack() {
        return killShotTrack;
    }

    public List<String> getWeaponDeck() {
        return weaponDeck;
    }

    public List<CachedPowerUp> getPowerUpDeck() {
        return powerUpDeck;
    }

    public List<CachedPowerUp> getThrashPowerUpDeck() {
        return thrashPowerUpDeck;
    }
}
