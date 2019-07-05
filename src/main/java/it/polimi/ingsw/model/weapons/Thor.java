package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Skull;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the Thor special weapon
 *
 * @see SpecialWeapons
 */
public class Thor extends SpecialWeapons {

    /**
     * weapon name
     */
    private static final String THOR_NAME = "T.H.O.R.";

    /**
     * reload cost
     */
    private List<AmmoCube> weaponCost;//cost of the weapon

    /**
     * second effect cost
     */
    private List<AmmoCube> effectsCost;//only this beacuse every effects cst 1 ammoCube so get(0)->second effect cost and get(1) is the second

    /**
     *
     * @return the second effect cost
     */
    public  List<AmmoCube> getEffectsCost() {
        return this.effectsCost;
    }

    /**
     * Constructor
     */
    public Thor() {

        super(THOR_NAME);

        weaponCost=new ArrayList<>();
        effectsCost=new ArrayList<>();
        this.weaponCost.add(new AmmoCube(Color.BLUE));
        this.weaponCost.add(new AmmoCube(Color.RED));
        effectsCost.add(new AmmoCube(Color.BLUE));
        effectsCost.add(new AmmoCube(Color.BLUE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerAlreadyDeadException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, CellNonExistentException {


        //------------------------------------------restore things
            List <Skull> kstCopy = Model.getGame().getKillShotTrack();

            for (int i = 0; i < targetLists.size(); i++) {
                for(Player p : targetLists.get(i)){
                    if(p.getStats().getDmgTaken().size() > 10){
                        throw new PlayerAlreadyDeadException();
                    }
                }
            }

            Player shooterCopy=new Player(this.isPossessedBy().getPlayerName(),this.isPossessedBy().getPlayerId(),this.isPossessedBy().getColor());
            for (AmmoCube a : this.isPossessedBy().getAmmoBag().getList()) {
                shooterCopy.getAmmoBag().addItem(a);
            }

            shooterCopy.setPlayerPosCopy(this.isPossessedBy().getCurrentPosition());
            shooterCopy.getStats().setMarksCopy(this.isPossessedBy().getMarks());
            List<List<Player>> targetsCopy=new ArrayList<>();

            for (List<Player> item : targetLists) {
                List<Player> pl=new ArrayList<>();
                for (Player p : item) {
                    Player tmp=new Player(p.getPlayerName(),p.getPlayerId(),p.getColor());
                    tmp.getStats().setDmgTakenCopy(p.getStats().getDmgTaken());
                    tmp.getStats().setMarksCopy(p.getStats().getMarks());
                    tmp.setPlayerPosCopy(p.getCurrentPosition());
                    pl.add(tmp);
                }
                //now i sort the players in order with playerID
                targetsCopy.add(pl);
            }
            //--------------------------end restore things
        try {
            if(!isLoaded())
                throw new WeaponNotLoadedException();

            for (int i = 0; i < effects.size(); i++) {
                for (Player p : targetLists.get(i)) {
                    if (p.getStats().getDmgTaken().size() > KILL_DMG) {
                        throw new PlayerAlreadyDeadException();
                    }
                }
            }

            for (int i = 0; i < effects.size(); i++)//checks that i can actually shoot
            {
                if (i == 0) {
                    if (!isPossessedBy().canSee().contains(targetLists.get(i).get(0)))
                        throw new PlayerNotSeeableException();
                }
                if (i > 0) {

                    if (!targetLists.get(i - 1).get(0).canSee().contains(targetLists.get(i).get(0)))
                        throw new PlayerNotSeeableException();

                }

                if (effects.get(i) != i)
                    throw new UncorrectEffectsException();
            }

            for (int i = 0; i < effects.size(); i++) {

                if (i == 0)//first macroeffect
                {
                    targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(), 2);
                } else if (i == 1)//second macroeffect
                {
                    targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(), 1);
                } else if (i == 2)//third macroeffect
                {
                    targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(), 2);
                }
            }
        }
        catch(UncorrectEffectsException e)
        {
            restore(targetsCopy,targetLists,shooterCopy,this.isPossessedBy(),kstCopy);
            throw new UncorrectEffectsException();

        } catch (PlayerAlreadyDeadException e) {
            restore(targetsCopy,targetLists,shooterCopy,this.isPossessedBy(),kstCopy);
            throw new PlayerAlreadyDeadException();
        } catch (PlayerNotSeeableException e) {
            restore(targetsCopy,targetLists,shooterCopy,this.isPossessedBy(),kstCopy);
            throw new PlayerNotSeeableException();
        } catch(WeaponNotLoadedException e) {
            restore(targetsCopy,targetLists,shooterCopy,this.isPossessedBy(),kstCopy);
            throw new WeaponNotLoadedException();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {

        return this.weaponCost;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        throw new UnsupportedOperationException();
    }


    /**
     * in case of failed shoot restores everything as before the shoot attempt
     * @param targetsCopy
     * @param targetLists
     */
    private void restore(List<List<Player>> targetsCopy,List<List<Player>> targetLists,Player shooterCopy,Player shooter,List <Skull> kstCopy)
    {

        Model.getGame().setKillShotTrack(kstCopy);

        //for shooter i need to restore position and ammos
        shooter.setPlayerPos(shooterCopy.getCurrentPositionCopy());
        try {
            shooter.getStats().setMarks(shooterCopy.getMarks());
        } catch (OverMaxMarkException e) {//can't occur
            e.printStackTrace();
        }
        for (AmmoCube a : shooterCopy.getAmmoBag().getList()) {
            shooter.getAmmoBag().addItem(a);
        }
        //for target i need to restore life, marks and position
        for (int i=0;i<targetLists.size();i++)
        {
            for(int j=0;j<targetLists.get(i).size();j++)
            {
                targetLists.get(i).get(j).setPlayerPos(targetsCopy.get(i).get(j).getCurrentPositionCopy());

                try {
                    targetLists.get(i).get(j).getStats().setMarks(targetsCopy.get(i).get(j).getMarks());
                    targetLists.get(i).get(j).getStats().setDmgTaken(targetsCopy.get(i).get(j).getDmg());

                } catch (OverMaxMarkException e) {//this shit can't occur NEVER in this specifial case
                    e.printStackTrace();
                } catch (OverMaxDmgException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
