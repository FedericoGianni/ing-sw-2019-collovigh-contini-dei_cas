package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HellionTest {

    @Test
    void preShoot() {

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

        // set the target to 1 distance from the vortex

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target);

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getEast());

        // create the weapon

        Weapon weapon = new Hellion();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(0,1));

        player.addWeapon(weapon);

        // check exception if player is in same cell as target

        player.setPlayerPos(target);

        assertThrows(PlayerInSameCellException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(0), new ArrayList<>());
        });

        player.setPlayerPos(Model.getMap().getCell(0,1));

        // check exception if weapon is unloaded

        weapon.setLoaded(false);

        assertThrows(WeaponNotLoadedException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(0), new ArrayList<>());
        });

        weapon.setLoaded(true);

        // check exception if effects are incorrect

        assertThrows(UncorrectEffectsException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(0,2), new ArrayList<>());
        });

        assertThrows(UncorrectEffectsException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(0,1), new ArrayList<>());
        });

        assertThrows(UncorrectEffectsException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(2), new ArrayList<>());
        });

        // check exception if target is unseeable

        player.setPlayerPos(Model.getMap().getCell(2,3));

        assertThrows(PlayerNotSeeableException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(0), new ArrayList<>());
        });

        player.setPlayerPos(Model.getMap().getCell(0,1));

        // check exception if player can not pay effect 2

        for (AmmoCube cube : player.getAmmoBag().getList()){

            try {

                player.pay(cube.getColor());

            }catch (CardNotPossessedException e){

                e.printStackTrace();
            }
        }

        assertThrows(NotEnoughAmmoException.class, () -> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(1), new ArrayList<>());
        });

    }

    @Test
    void shootBaseEffect() {

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

        // set the target to 1 distance from the vortex

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target);

        // create the weapon

        Weapon weapon = new Hellion();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(0,1));

        player.addWeapon(weapon);

        assertEquals(0,target_1.getDmg().size());
        assertEquals(0,target_1.getMarks().size());

        assertEquals(0,target_2.getDmg().size());
        assertEquals(0,target_2.getMarks().size());

        try {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(0), new ArrayList<>());

        }catch (Exception e){

            e.printStackTrace();
        }

        assertEquals(1,target_1.getDmg().size());
        assertEquals(1,target_1.getMarks().size());

        assertEquals(0,target_2.getDmg().size());
        assertEquals(1,target_2.getMarks().size());

        assertEquals(0,player.getDmg().size());
        assertEquals(0,player.getMarks().size());

    }

    @Test
    void shootSecondEffect() {

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

        // set the target to 1 distance from the vortex

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target);

        // create the weapon

        Weapon weapon = new Hellion();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(0,1));

        player.addWeapon(weapon);

        player.addCube(new AmmoCube(Color.RED));

        assertEquals(0,target_1.getDmg().size());
        assertEquals(0,target_1.getMarks().size());

        assertEquals(0,target_2.getDmg().size());
        assertEquals(0,target_2.getMarks().size());

        try {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)), Arrays.asList(1), new ArrayList<>());

        }catch (Exception e){

            e.printStackTrace();
        }

        assertEquals(1,target_1.getDmg().size());
        assertEquals(2,target_1.getMarks().size());

        assertEquals(0,target_2.getDmg().size());
        assertEquals(2,target_2.getMarks().size());

        assertEquals(0,player.getDmg().size());
        assertEquals(0,player.getMarks().size());
    }
}