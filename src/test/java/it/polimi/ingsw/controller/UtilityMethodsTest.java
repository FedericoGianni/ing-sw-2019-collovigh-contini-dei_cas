package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.powerup.TagbackGrenade;
import it.polimi.ingsw.model.powerup.TargetingScope;
import it.polimi.ingsw.model.weapons.Furnace;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilityMethodsTest {

    @Test
    void findWeaponInWeaponBag() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);

        Model model = new Model(names,colors,2,8);

        Player player = Model.getPlayer(0);

        Furnace weapon = new Furnace();

        player.addWeapon(weapon);

        assertEquals(player.getCurrentWeapons().getList(), Arrays.asList(weapon));

        UtilityMethods utilityMethods = new UtilityMethods(null);

        Weapon found = utilityMethods.findWeaponInWeaponBag(weapon.getName(),0);

        assertEquals(weapon,found);

        assertEquals(player.getCurrentWeapons().getList(), Arrays.asList(weapon));


    }

    @Test
    void cachedToRealPowerUp() {

        UtilityMethods utilityMethods = new UtilityMethods(null);

        List<PowerUp> powerUpList = new ArrayList<>();
        List<PowerUp> selected;
        List<PowerUp> found = new ArrayList<>();

        powerUpList.add(new TargetingScope(Color.BLUE));
        powerUpList.add(new Newton(Color.YELLOW));

        selected = new ArrayList<>(powerUpList);

        powerUpList.add(new TagbackGrenade(Color.RED));


        List<CachedPowerUp> cachedPowerUpList = new ArrayList<>();

        cachedPowerUpList.add(new CachedPowerUp(PowerUpType.TARGETING_SCOPE, Color.BLUE));
        cachedPowerUpList.add(new CachedPowerUp(PowerUpType.NEWTON, Color.YELLOW));

        for (CachedPowerUp powerUp : cachedPowerUpList){

            try {

                found.add(utilityMethods.cachedToRealPowerUp(powerUp,powerUpList));

            }catch (CardNotPossessedException e){

                e.printStackTrace();

            }
        }

        assertEquals(selected,found);
    }

    @Test
    void powerUpCanBeSold() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);

        Model model = new Model(names,colors,2,8);

        UtilityMethods utilityMethods = new UtilityMethods(null);

        assertTrue(utilityMethods.powerUpCanBeSold(0,Arrays.asList(new CachedPowerUp(PowerUpType.NEWTON,Color.BLUE))));

        assertTrue(utilityMethods.powerUpCanBeSold(0,Arrays.asList(new CachedPowerUp(PowerUpType.NEWTON,Color.BLUE), new CachedPowerUp(PowerUpType.NEWTON,Color.BLUE) )));
    }
}