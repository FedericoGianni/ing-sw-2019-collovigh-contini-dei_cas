package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RailGunTest {

    @Test
    void shootFirst() {

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

        Cell target = Model.getMap().getCell(1,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(1,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(1,1));

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getEast());

        // create the weapon

        RailGun weapon = new RailGun();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        try{

            weapon.shoot(Arrays.asList(Arrays.asList(target_1,target_2)),Arrays.asList(1),new ArrayList<>());

        }catch (Exception e){

            e.printStackTrace();

        }

        assertEquals( 2, target_1.getDmg().size());

        assertEquals(2, target_2.getDmg().size());
    }

    @Test
    void shootBase() {

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

        Cell target = Model.getMap().getCell(1,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(1,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(1,1));

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getEast());

        // create the weapon

        RailGun weapon = new RailGun();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        try{

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)),Arrays.asList(0),new ArrayList<>());

        }catch (Exception e){

            e.printStackTrace();

        }

        assertEquals( 3, target_1.getDmg().size());
    }

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

        Cell target = Model.getMap().getCell(1,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(1,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(1,1));

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getEast());

        // create the weapon

        RailGun weapon = new RailGun();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        // throws WeaponNotLoadedException

        assertThrows(WeaponNotLoadedException.class, ()-> {

            weapon.setLoaded(false);

            weapon.shoot(Arrays.asList(Arrays.asList(target_1,target_2)),Arrays.asList(1),new ArrayList<>());
        });

        weapon.setLoaded(true);

        // throws UncorrectEffectException

        assertThrows(UncorrectEffectsException.class, ()-> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1,target_2)),Arrays.asList(0,1),new ArrayList<>());
        });

        // throws NotCorrectPlayerNumber

        assertThrows(NotCorrectPlayerNumberException.class, ()-> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1,target_2)),Arrays.asList(0),new ArrayList<>());
        });

        // player not in line

        target_1.setPlayerPos(Model.getMap().getCell(2,2));

        assertThrows(UncorrectDistanceException.class, ()-> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)),Arrays.asList(0),new ArrayList<>());
        });

        target_1.setPlayerPos(Model.getMap().getCell(1,2));



    }

    @Test
    void playerAreInStraightLine() {

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

        Cell target = Model.getMap().getCell(1,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(1,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(1,1));

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getEast());

        // create the weapon

        RailGun weapon = new RailGun();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        assertThrows(PlayerNotSeeableException.class, () -> {

            weapon.playerAreInStraightLine(Arrays.asList(target_1,target_2),0);
        });

        try{

            assertTrue(weapon.playerAreInStraightLine(Arrays.asList(target_1,target_2),1));

        }catch (Exception e){

            e.printStackTrace();
        }


    }
}