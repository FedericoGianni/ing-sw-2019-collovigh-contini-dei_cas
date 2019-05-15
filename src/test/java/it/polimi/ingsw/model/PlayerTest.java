package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

class PlayerTest {

    @Test
    void canSee() {

       //login stuff
        ArrayList<String> playerNames=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("visible");
        playerNames.add("NotVisible");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2);


        //generate a player with a name and its starting position
        //Player p1 = new Player("Shooter",map.getCell(1,3));
        Player p1 = Model.getPlayer(0);
        p1.setPlayerPos(Model.getMap().getCell(1,3));

        //Player p2 = new Player("Visible",map.getCell(0,3));
        Player p2 = Model.getPlayer(1);
        p2.setPlayerPos(Model.getMap().getCell(0,3));

        //Player p3 = new Player("NotVisible",map.getCell(0,0));
        Player p3 = Model.getPlayer(2);
        p3.setPlayerPos(Model.getMap().getCell(2,1));

        List<Player> visibles=new ArrayList<>();

        try {
            visibles=p1.canSee();
        } catch(Exception e) {
            System.out.println("No players can be seen");
            e.printStackTrace();
        }
        for(int i=0;i<visibles.size();i++)
        System.out.println(visibles.get(i).getPlayerName());
        //tests that there's only p2 as a visible player
        assertTrue(visibles.get(0) == p2);
        assertTrue(visibles.size() == 1);
    }

    @Test // second test,more features
    void canSee2() {
        ArrayList<String> playerNames=new ArrayList<>();
        ArrayList <PlayerColor>pc=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("visible");
        playerNames.add("notvisible");
        playerNames.add("visible2");
        playerNames.add("visiblemelee");
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.YELLOW);
        pc.add(PlayerColor.GREEN);
        pc.add(PlayerColor.GREY);

            Model m=new Model(playerNames,pc,2);
        //generate players in a fixed position
        Player p1=Model.getPlayer(0);
        p1.setPlayerPos(Model.getMap().getCell(1,3));

        Player p2=Model.getPlayer(1);
        p2.setPlayerPos(Model.getMap().getCell(0,0));

        Player p3=Model.getPlayer(2);
        p3.setPlayerPos(Model.getMap().getCell(1,2));

        Player p4=Model.getPlayer(3);
        p4.setPlayerPos(Model.getMap().getCell(0,3));

        Player p5=Model.getPlayer(4);
        p5.setPlayerPos(Model.getMap().getCell(1,3));
        //now there are 3 visible players, 2 in the same cell on the north of the current player and
        //another in his cell

        List<Player> visibles=new ArrayList<>();

        try{
            visibles=p1.canSee();
        } catch(Exception e) {
            //System.out.println("No players can be seen");
            e.printStackTrace();
        }
        for(int i=0;i<visibles.size();i++)
        {
            System.out.println(visibles.get(i).getPlayerName());
        }

        assertTrue(visibles.get(0)==p5);//closest player
        assertTrue(visibles.get(1)==p2);
        assertTrue(visibles.get(2)==p4);//2 players in the same room
        assertTrue(visibles.size()==3);//tests that there are 3 players in the shooter's eye range
    }
}