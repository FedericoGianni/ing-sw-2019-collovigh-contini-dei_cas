package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void pay() {

        Player player = new Player("Alex",0, PlayerColor.BLUE);

        for (AmmoCube cube : player.getAmmoBag().getList()){

            try {

                player.pay(cube.getColor());

            }catch (CardNotPossessedException e){

                e.printStackTrace();
            }
        }

        player.addCube(new AmmoCube(Color.RED));
        player.addCube(new AmmoCube(Color.RED));

        player.addCube(new AmmoCube(Color.YELLOW));

        player.addCube(new AmmoCube(Color.BLUE));

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.RED));

        cost.add(new AmmoCube(Color.YELLOW));

        cost.add(new AmmoCube(Color.BLUE));

        assertTrue(player.canPay(cost));

        try {

            player.pay(cost);

        }catch (CardNotPossessedException e){

            e.printStackTrace();
        }

        assertEquals(0, player.getAmmoBag().getList().size());
    }

    @Test
    void payShouldThrowException() {

        Player player = new Player("Alex",0,PlayerColor.BLUE);

        for (AmmoCube cube : player.getAmmoBag().getList()){

            try {

                player.pay(cube.getColor());

            }catch (CardNotPossessedException e){

                e.printStackTrace();
            }
        }

        player.addCube(new AmmoCube(Color.RED));
        player.addCube(new AmmoCube(Color.RED));

        player.addCube(new AmmoCube(Color.YELLOW));

        player.addCube(new AmmoCube(Color.BLUE));

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.RED));

        cost.add(new AmmoCube(Color.YELLOW));

        cost.add(new AmmoCube(Color.BLUE));

        cost.add(new AmmoCube(Color.BLUE));

        assertFalse(player.canPay(cost));

        assertThrows(CardNotPossessedException.class, () -> {

            player.pay(cost);

        });

        assertEquals(4, player.getAmmoBag().getList().size());
    }

    @Test
    void canSee() {

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        names.add("target1");
        names.add("target2");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.YELLOW);
        pc.add(PlayerColor.GREY);
        Model m=new Model(names,pc,2,8);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);
        Player target2=Model.getGame().getPlayers().get(2);
        Player target3=Model.getGame().getPlayers().get(3);
        shooter.setPlayerPos(Model.getMap().getCell(1,1));
        target1.setPlayerPos(Model.getMap().getCell(0,0));
        target2.setPlayerPos(Model.getMap().getCell(2,2));
        target3.setPlayerPos(Model.getMap().getCell(1,1));



        for(int i=0;i<shooter.canSee().size();i++)
        {
            //System.out.println(shooter.canSee().get(i).getPlayerName());
        }
    }

    @Test
    void canSee2(){

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2,8);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);

        shooter.setPlayerPos(Model.getMap().getCell(2,1));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        assert (shooter.canSee().contains(target1));


    }

    @Test
    void addDmg() {

        Player player = new Player("Frank",0,PlayerColor.BLUE);

        assertEquals(0, player.getMarks().size());
        assertEquals(0,player.getDmg().size());

        player.addDmg(1,2);

        assertEquals(2,player.getDmg().size());
        assertEquals(2,player.getStats().getDmgTaken().size());

        player.addDmg(2,1);

        assertEquals(3,player.getDmg().size());
        assertEquals(3,player.getStats().getDmgTaken().size());
    }

    @Test
    void sellPowerUp() {

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

        for (AmmoCube cube : player01.getAmmoBag().getList()){

            try {

                player01.pay(cube.getColor());

            }catch (CardNotPossessedException e){

                e.printStackTrace();
            }
        }

        assertEquals(0,player01.getAmmo().size());
        assertEquals(0,player01.getPowerUpBag().getList().size());

        player01.addPowerUp(new Newton(Color.BLUE));

        PowerUp powerUp = player01.getPowerUpBag().getList().get(0);

        player01.sellPowerUp(powerUp);

        assertEquals(0,player01.getPowerUpBag().getList().size());
        assertEquals(1,player01.getAmmo().size());
    }
}