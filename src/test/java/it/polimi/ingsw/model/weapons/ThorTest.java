package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.CacheModelParser;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThorTest {

    @Test
    void isLoaded() {
        Thor w=new Thor();
        assertTrue(w.isLoaded());
    }

    @Test
    void reload() throws NotAbleToReloadException {//tests that if the thor is not loaded it can be reloaded with the correct amount of ammos
        Thor w=new Thor();
        w.setLoaded(false);
        assertTrue(!w.isLoaded());
        ArrayList<String> playerNames=new ArrayList<>();
        playerNames.add("shooter");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(playerNames,pc,2,8);
        Player shooter = Model.getPlayer(0);
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.addWeapon(w);
        assertTrue(shooter.canPay(w.getReloadCost()));
        w.reload();
        assertTrue(w.isLoaded());
    }


    @Test
    void shoot() throws PlayerInSameCellException, FrenzyActivatedException, DeadPlayerException, SeeAblePlayerException, OverKilledPlayerException, PlayerInDifferentCellException, WeaponNotLoadedException, UncorrectEffectsException, PlayerNotSeeableException, UncorrectDistanceException, NotCorrectPlayerNumberException {
        Thor w=new Thor();
        ArrayList<String> playerNames=new ArrayList<>();
        ArrayList <PlayerColor>pc=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("Visible");
        playerNames.add("visible2");
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.GREEN);
        pc.add(PlayerColor.GREY);

        Model m=new Model(playerNames,pc,2,8);
        //generate players in a fixed position
        Player shooter=Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));

        Player p2=Model.getPlayer(1);
        p2.setPlayerPos(Model.getMap().getCell(0,0));

        Player p3=Model.getPlayer(2);
        p3.setPlayerPos(Model.getMap().getCell(2,3));

        //se danno >12 in un effetto son cazzi

        List <List<Player>> targetsLists =new ArrayList<>();
        List <Player> targets1=new ArrayList<>();
        List <Player> targets2=new ArrayList<>();
        targets1.add(p2);
        targets2.add(p3);
        targetsLists.add(targets1);
        targetsLists.add(targets2);

            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);
            mEf.add(1);
            shooter.addWeapon(w);
        try {
            shooter.getWeapons().get(0).shoot(targetsLists,mEf,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(p2.getStats().getDmgTaken());
        System.out.println(p3.getStats().getDmgTaken());


    }

    @Test
    void checkLinkCacheModel() {

        Thor thor = new Thor();

        List<CachedFullWeapon> weaponList = CacheModelParser.readCachedFullWeaponsFromList();

        CachedFullWeapon vortex = weaponList
                .stream()
                .filter( x -> x.getName().equalsIgnoreCase(thor.getName() ))
                .collect(Collectors.toList())
                .get(0);

        assertEquals( thor.getReloadCost().stream().map(AmmoCube::getColor).collect(Collectors.toList()), vortex.getFirstEffectCost());

        assertEquals( thor.getCost().stream().map(AmmoCube::getColor).collect(Collectors.toList()) , vortex.getFirstEffectCost().subList(1, vortex.getFirstEffectCost().size()));
    }

}