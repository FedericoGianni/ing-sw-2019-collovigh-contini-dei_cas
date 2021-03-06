package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecialWeaponsTest {

    @Test
    void playerAreInLine() {

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
        target_1.setPlayerPos(target.getEast());

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target.getEast().getEast());

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getEast());

        Player shooter = Model.getPlayer(0);

        shooter.setPlayerPos(target);

        List<Player> targets = Arrays.asList(target_1,target_2);

        assertTrue( SpecialWeapons.playerAreInLine(shooter,targets) );

    }
}