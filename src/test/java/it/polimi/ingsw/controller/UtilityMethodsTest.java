package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.weapons.Furnace;
import it.polimi.ingsw.model.weapons.Weapon;
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
}