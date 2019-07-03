package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.NotCorrectPlayerNumberException;
import it.polimi.ingsw.customsexceptions.UncorrectDistanceException;
import it.polimi.ingsw.customsexceptions.UncorrectEffectsException;
import it.polimi.ingsw.customsexceptions.WeaponNotLoadedException;
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

class PowerGloveTest {

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

        Cell target = Model.getMap().getCell(0,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(0,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(0,1));

        // create the weapon

        PowerGlove weapon = new PowerGlove();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        // weapon not loaded

        assertThrows(WeaponNotLoadedException.class, ()-> {

            weapon.setLoaded(false);

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)),Arrays.asList(0),new ArrayList<>());
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

        assertThrows(NotCorrectPlayerNumberException.class, ()-> {

            weapon.shoot(Arrays.asList(new ArrayList<>()),Arrays.asList(0),new ArrayList<>());
        });

        // player not in line

        target_1.setPlayerPos(Model.getMap().getCell(2,2));

        assertThrows(UncorrectDistanceException.class, ()-> {

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)),Arrays.asList(0),new ArrayList<>());
        });

        target_1.setPlayerPos(Model.getMap().getCell(0,2));
    }

    @Test
    void baseShoot() {

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

        Cell target = Model.getMap().getCell(0,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(0,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(0,1));

        // create the weapon

        PowerGlove weapon = new PowerGlove();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        try{

            weapon.shoot(Arrays.asList(Arrays.asList(target_1)),Arrays.asList(0),new ArrayList<>());

        }catch (Exception e){

            e.printStackTrace();

        }

        assertEquals(target_1.getCurrentPosition(), player.getCurrentPosition());

        assertEquals(1, target_1.getDmg().size());
        assertEquals(2, target_1.getMarks().size());
    }

    @Test
    void secondShoot() {

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

        Cell target = Model.getMap().getCell(0,3);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(Model.getMap().getCell(0,2));

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(Model.getMap().getCell(0,1));

        // create the weapon

        PowerGlove weapon = new PowerGlove();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(target);

        player.addWeapon(weapon);

        // adds ammo

        player.addCube(new AmmoCube(Color.BLUE));

        try{

            weapon.shoot(Arrays.asList(Arrays.asList(target_1,target_2)),Arrays.asList(1),new ArrayList<>());

        }catch (Exception e){

            e.printStackTrace();

        }

        assertEquals(target_2.getCurrentPosition(), player.getCurrentPosition());

        assertEquals(2, target_1.getDmg().size());
        assertEquals(0, target_1.getMarks().size());

        assertEquals(2, target_2.getDmg().size());
        assertEquals(0, target_2.getMarks().size());

        assertEquals(0, player.getDmg().size());
        assertEquals(0, player.getMarks().size());
    }
}