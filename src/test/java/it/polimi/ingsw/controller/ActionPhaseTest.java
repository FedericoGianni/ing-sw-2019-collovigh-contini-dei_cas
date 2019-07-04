package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.actions.ShootAction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ActionPhaseTest {

    @Test
    void playerTransLation() {

        List<List<Integer>> targetList = new ArrayList<>();

        targetList.add( new ArrayList<>());

        targetList.get(0).add(1);

        targetList.add(new ArrayList<>());

        targetList.get(1).add(0);

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Player player01 = Model.getPlayer(0);
        Player player02 = Model.getPlayer(1);
        Player player03 = Model.getPlayer(2);

        ShootAction shootAction = new ShootAction(null,targetList,null,null,null,null);

        List<List<Player>> targets = new ArrayList<>();

        for (int i = 0; i < shootAction.getTargetIds().size(); i++) {

            List<Player> temp = new ArrayList<>();

            for (Integer id : shootAction.getTargetIds().get(i)) {

                temp.add(Model.getPlayer(id));
            }

            targets.add(temp);
        }

        for (List<Player> list : targets){

            for (Player target : list){

                System.out.println(target.getPlayerName());
            }
        }
    }
}