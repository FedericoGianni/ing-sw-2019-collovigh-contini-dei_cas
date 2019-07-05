package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.model.player.Skull;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SavedCurrentGame implements Serializable {

    private final int skulls;

    private final List<Skull> killShotTrack;

    private final List<String> weaponDeck;

    private final List<CachedPowerUp> powerUpDeck;

    private final List<CachedPowerUp> thrashPowerUpDeck;

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
