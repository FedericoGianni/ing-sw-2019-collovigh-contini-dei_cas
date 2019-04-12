package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

class PlayerTest {

    @Test
    void canSee() {
        Map map = new Map();//geerate the map
        map.getMap(2);
        map.generateCells(2);//map type 2 gerata
        Player p1=new Player("Shooter",map.getCell(1,3));//genrate a player in a position
        Player p2=new Player("Visible",map.getCell(0,3));
        Player p3=new Player("NotVisible",map.getCell(0,0));
        //dico alle celle che hanno dentro i giocatori
        map.getCell(1,3).addPlayerHere(p1);
        map.getCell(0,3).addPlayerHere(p2);
        map.getCell(0,0).addPlayerHere(p3);
        List<Player> visibles=new ArrayList<>();
        try{
        visibles=p1.canSee();}
        catch(Exception e)
        {
            System.out.println("No players can be seen");
            e.printStackTrace();

        }
        assertTrue(visibles.get(0)==p2);
        assertTrue(visibles.size()==1);//tests that there's only p2 as a visible player

    }

    @Test //second test, more complex
    void canSee2() {
        Map map = new Map();//geerate the map
        map.getMap(2);
        map.generateCells(2);//map type 2 gerata
        Player p1=new Player("Shooter",map.getCell(1,3));//genrate a player in a position
        Player p2=new Player("Visible",map.getCell(0,3));
        Player p3=new Player("NotVisible",map.getCell(0,0));
        Player p4=new Player("Visible2",map.getCell(0,3));
        Player p5=new Player("VisibleMeleeRange",map.getCell(1,3));//will be the first seen beacuse the players are ordinated in distance
        //dico alle celle che hanno dentro i giocatori
        map.getCell(1,3).addPlayerHere(p1);
        map.getCell(0,3).addPlayerHere(p2);
        map.getCell(0,0).addPlayerHere(p3);
        map.getCell(0,3).addPlayerHere(p4);
        map.getCell(1,3).addPlayerHere(p5);//now there are 3 visible players, 2 in the same cell on the north of the current player and
        //another in his cell
        List<Player> visibles=new ArrayList<>();
        try{
            visibles=p1.canSee();}
        catch(Exception e)
        {
            //System.out.println("No players can be seen");
            e.printStackTrace();

        }

        assertTrue(visibles.get(0)==p5);//closest player
        assertTrue(visibles.get(1)==p2);
        assertTrue(visibles.get(2)==p4);//2 players in the same room
        assertTrue(visibles.size()==3);//tests that there are 3 players in the shooter's eye range

    }





}