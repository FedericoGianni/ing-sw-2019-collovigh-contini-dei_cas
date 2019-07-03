package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlameThrowerTest {

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

        // create the weapon

        FlameThrower weapon = new FlameThrower();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        List<List<Player>> targetList = Arrays.asList(Arrays.asList(target_1,target_2));


        // check exception playerAreNotInLine

        assertThrows(UncorrectDistanceException.class, () -> {

            weapon.preShoot(targetList, Arrays.asList(0), new ArrayList<>());
        });

        // check player

        assertThrows(NotCorrectPlayerNumberException.class, () -> {

            weapon.preShoot(new ArrayList<>(), Arrays.asList(0), new ArrayList<>());
        });

        // set the targets positions in line

        target_1.setPlayerPos(Model.getMap().getCell(0,1));
        target_2.setPlayerPos(Model.getMap().getCell(0,2));

        // check double effect specified

        assertThrows(UncorrectEffectsException.class, () -> {

            weapon.preShoot(targetList, Arrays.asList(0,1), null);
        });

        // same player

        assertThrows(DifferentPlayerNeededException.class, () -> {

            weapon.preShoot(Arrays.asList(Arrays.asList(target_1,target_1)), Arrays.asList(0), null);
        });

        // not Enough ammo for second effect

        assertThrows(DifferentPlayerNeededException.class, () -> {

            weapon.preShoot(Arrays.asList(Arrays.asList(target_1,target_1)), Arrays.asList(1), null);
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

        FlameThrower weapon = new FlameThrower();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        // set the targets positions in line

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(0,1));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(0,2));

        List<List<Player>> targetList = Arrays.asList(Arrays.asList(target_1,target_2));

        try{

            weapon.shoot(targetList,Arrays.asList(0),new ArrayList<>());

        } catch (Exception e){

            e.printStackTrace();
        }

        assertEquals(1,target_1.getDmg().size());

        assertEquals(1,target_2.getDmg().size());
    }

    @Test
    void shootFirstEffect() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");
        names.add("Peter");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);
        colors.add(PlayerColor.PURPLE);

        Model model = new Model(names,colors,2,8);

        // set the target to 1 distance from the vortex

        Cell target = Model.getMap().getCell(0,0);

        FlameThrower weapon = new FlameThrower();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        player.addCube(new AmmoCube(Color.YELLOW));

        player.addCube(new AmmoCube(Color.YELLOW));

        // set the targets positions in line

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(0,1));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(0,2));

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(Model.getMap().getCell(0,1));

        Player target_4 = Model.getPlayer(4);
        target_4.setPlayerPos(Model.getMap().getCell(0,2));

        List<List<Player>> targetList = Arrays.asList(Arrays.asList(target_1,target_2));

        try{

            weapon.shoot(targetList,Arrays.asList(1),new ArrayList<>());

        } catch (Exception e){

            e.printStackTrace();
        }

        assertEquals(2,target_1.getDmg().size());
        assertEquals(2,target_3.getDmg().size());

        assertEquals(1,target_2.getDmg().size());
        assertEquals(1,target_4.getDmg().size());
    }
}