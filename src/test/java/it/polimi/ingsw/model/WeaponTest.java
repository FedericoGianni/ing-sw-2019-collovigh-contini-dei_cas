package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.model.weapons.MacroEffect;
import it.polimi.ingsw.model.weapons.NormalWeapon;
import it.polimi.ingsw.model.weapons.Weapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    void isPossessedBy() {

        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_1");
        playerNames.add("Player_2");
        playerNames.add("Player_3");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //generating model
        Model currentModel = new Model(playerNames, playerColors, 2,8);

        Weapon w = new NormalWeapon("Gun", new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>());

        Model.getPlayer(0).addWeapon(w);

        assertEquals(Model.getPlayer(0),w.isPossessedBy());

        assertEquals(Model.getPlayer(0).getAmmoBag(),w.isPossessedBy().getAmmoBag());

    }
    @Test
    void printWeaponList()
    {

    }
}
