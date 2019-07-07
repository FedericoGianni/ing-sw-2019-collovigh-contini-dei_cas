package it.polimi.ingsw.model.weapons;
import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * this class give the damages to the targeted players
 */
public class Damage extends MicroEffect {
    /**
     * the damage
     */
    private int damage;//how much damage you can do
    /**
     * some effects can deal damage to more than 1 player
     * so player num have this thing inside
     *
     */
    private int playerNum;
    /**
     * if the effect need a a seeable target
     */
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players
    /**
     * if the weapon is a melee weapon
     */
    private boolean melee;//some weapons can deal damage to players only in your current cell
    /**
     * if you need different players targeted
     */
    private boolean differentPlayer;//in secondary effects sometimes you need to target different players from the first
    /**
     * if you need already targeted players
     */
    private boolean alreadyTargeted;//sometimes in secondary effects you have to choose between already targeted players of the first effect
    /**
     * if you have some kind of distances costraints
     */
    private int distMin;//some effects require a minimum distance, calculated by moves
    //particuarities: if both differentPlayer and alreadyTargeted are true you can choose to apply one or both effects(check machineGun III)

    // the player number is 100 you need to target every player in the target's square(check electroshyte-melee-  and grenade launcher 2)
    //player num is 10 you must target the number/10 every one from different cell
    //player number pure that number
    //player number 1000 or more up to playerNum/1000
    //if the minimum distance is over 10 it means maximum distance, like 20 is at maximun distance of 2 (20/10-> 2)(check tractor beam)
    //if the distance is 1000 is like 1 , no more no less,exactly dist/1000
    //if the minimum distance is 100 is an unseeble Player by default, you have to target an unseeable player(check heatseeker)
    //only the damage tag have the distance inside
    private static List <Damage> damages = new ArrayList<>();

    /**
     *
     * getters and setters
     */
    public int getDamage() {
        return this.damage;
    }


    /**
     * directly inserts damage
     * @param dm
     */
    public static void insertDamage(Damage dm) {
        damages.add(dm);
    }

    /**
     * get the damage
     * @return
     */
    public static List <Damage> getDamagesList()
    {
        return damages;
    }

    /**
     * contructor
     * @param a
     * @param b
     * @param c
     * @param d
     * @param diff
     * @param dm
     * @param at
     */
    public Damage(int a, int b, boolean c, boolean d, boolean diff,int dm,boolean at) {
        this.damage=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.melee=d;
        this.differentPlayer=diff;
        this.distMin=dm;
        this.alreadyTargeted=at;
    }


    /**
     * set the damage directly 2
     * @param damages
     */
    public void setDamage(int damages)
    {
        this.damage=damages;
    }


    /**
     * this method uses the semplified JSON to populate the microeffets-Damage Class that are in a known number
     */
    public static void populator() //static beacuse no damages types may exixst before the first call of this method
    {
        List<Damage> damageList= Parser.damageReader();

        for (Damage damage : damageList){

            Damage.insertDamage(damage);
        }

    }

    @Override
    public void applyOn(Player player) {

    }

    /**
     * copy the object
     * @return
     */
    @Override
    public Damage copy() {
        return this;
    }

    /**
     * Thi method do do the damages and the controls , launches excpetions if there are problems somewhere
     * damage given to player list  , given from weapon w
     * @param playerList
     * @param w
     * @param c requires cell where to move in case you move, is null if you use a non mover microeffect
     * @param n
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws NotCorrectPlayerNumberException
     * @throws PlayerNotSeeableException
     * @throws PrecedentPlayerNeededException
     * @throws DifferentPlayerNeededException
     */
    @Override
    public void microEffectApplicator(List<Player> playerList, Weapon w, Cell c,int n) throws PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, NotCorrectPlayerNumberException, PlayerNotSeeableException, PrecedentPlayerNeededException, DifferentPlayerNeededException {//w.isPossesedBy.getPlayer mi dice il giocatore che spara

       if(playerList.size()==0 && alreadyTargeted)//plasmaGun effetto opz 2, don't need target
       {
           w.getFirstTargets().get(0).addDmg(w.isPossessedBy().getPlayerId(),damage);
           return;
       }

        //System.out.println("------------------------------------");
        //System.out.println("Damage Effect! life now: " +playerList.get(0).getStats().getDmgTaken().size());
        //print();
        //System.out.println("------------------------------------");
       for(int i=0;i<playerList.size();i++)//i can't shoot more times to the same target
       {
           for(int j=0;j<playerList.size();j++)
           {
               if(i!=j && playerList.get(i).getPlayerId()== playerList.get(j).getPlayerId())
                   throw new DifferentPlayerNeededException();
           }

       }
        if(alreadyTargeted==true && differentPlayer==false)//1 target, one of the firstTargets
        {

            if(!(w.getFirstTargets().contains(playerList.get(0))))
             throw new PrecedentPlayerNeededException();

            for(Player p: playerList ) {
                if (!w.isPossessedBy().canSee().contains(p))
                    throw new PlayerNotSeeableException();
            }
            for(Player p:w.getFirstTargets())
            {
                if(p==playerList.get(0))
                {
                    playerList.get(0).addDmg(w.isPossessedBy().getPlayerId(),damage);
                    w.removeFromFirstTargets(p);
                }
            }

            return;
        }
        else if(alreadyTargeted && differentPlayer){//MG-3 you must shoot the previous target and/or you can target whover you want

            if(w.getFirstTargets().contains(playerList.get(0)) ||w.getFirstTargets().contains(playerList.get(1)))//if i have the previous target shoot him
            {
                for(Player p:w.getFirstTargets())
                {
                    for(int i=0;i< playerList.size();i++) {

                        if (p == playerList.get(i)) {
                            p.addDmg(w.isPossessedBy().getPlayerId(), damage);
                            playerList.remove(p);//shot the previous target
                        }
                    }
                }

            }
            //then you can target someone else

            for(int i=0;i<playerList.size();i++)//here you can shoot one other target
            {
                if(playerList.size()>1)
                throw new NotCorrectPlayerNumberException();

                if(!w.isPossessedBy().canSee().contains(playerList.get(i)))
                    throw new PlayerNotSeeableException();


                playerList.get(i).addDmg(w.isPossessedBy().getPlayerId(),damage);
            }
            return;
        }
        if(differentPlayer && melee)//cyberblade last effect
        {

            if(playerList.size()!=1)
                throw new NotCorrectPlayerNumberException();
            if(w.getFirstTargets().contains(playerList.get(0)))
                throw new DifferentPlayerNeededException();
            if(!playerList.get(0).getCurrentPosition().equals(w.isPossessedBy().getCurrentPosition()))
                throw new PlayerInDifferentCellException();


            playerList.get(0).addDmg(w.isPossessedBy().getPlayerId(),damage);
           return;

        }


        if(seeAbleTargetNeeded)
        {
            for(Player item : playerList)//check that everyone is in the same cells
            {
                if(!w.isPossessedBy().canSee().contains(item))
                    throw new PlayerNotSeeableException();
            }

        }
            
         if(playerNum==100)
        {

            if(melee==true)// only electroshyte I e II
            {
                for(Player item:playerList)
                {
                    if(!item.getCurrentPosition().equals(w.isPossessedBy().getCurrentPosition()))
                    {
                        throw new PlayerInDifferentCellException();
                    }
                }

                for(Player p:w.isPossessedBy().getCurrentPosition().getPlayers())
                {
                    if(!p.equals(w.isPossessedBy()))
                    p.addDmg(w.isPossessedBy().getPlayerId(),damage);
                }
                return;
            }
            if(c!=null)//Granade launcher 2, e rccket launcher has cell different from 0
            {
                System.out.println("aaaaaa");
                if(c.getPlayers().size()==0)
                    throw new NotCorrectPlayerNumberException();
                if(!w.isPossessedBy().canSee().contains(c.getPlayers().get(0)))//you cant see the cella and the ttagets
                    throw new PlayerNotSeeableException();

                for(Player item : c.getPlayers())
                {
                    System.out.println("giocatore "+item.getPlayerName());
                    if(!item.equals(w.isPossessedBy()))
                    item.addDmg(w.isPossessedBy().getPlayerId(),damage);
                }
                return;
            }
            for(Player item : playerList)
            {

                sameCellCheck(item,playerList);
            }

            for(Player item:playerList)
            {
                if(!item.equals(w.isPossessedBy()))
                distance(item,w.isPossessedBy());
            }
        }else if(playerNum>=10 && playerNum <100)
        {
            if(playerList.size()!=playerNum/10){//checks that the number is really the one said
                throw new NotCorrectPlayerNumberException();
            }
            for(Player item : playerList)//check that everyone is in a different cell
            {
                differentCellsCheck(item,playerList);
            }
            for(Player item:playerList)
            {
                distance(item,w.isPossessedBy());
            }

        }
         else if(playerNum>=1000)//up to playerNum/1000 targets
         {
             if(playerList.size()>playerNum/1000)
                 throw new NotCorrectPlayerNumberException();
             for(Player item:playerList)
             {
                 distance(item,w.isPossessedBy());
             }
         }
         else if(playerNum==9999999)//special if you are sure eberything is ok
         {
             for(Player item : playerList)
             {
                 item.addDmg(w.isPossessedBy().getPlayerId(),damage);
             }
         }
        else {//player number is pure

            if(playerList.size()!=playerNum){

             throw new NotCorrectPlayerNumberException();
            }
            for(Player item : playerList)
            {
                distance(item,w.isPossessedBy());
            }

        }
        if((n==0 && playerList.size()!=0) || (n==1 && w.getFirstTargets()==null)) {

            w.setFirstTarget(playerList);//it can be useful if you need to reshot the shame player otherwise it doesn't count nothing
        }
    }

    /**
     * ask if you need to move before do effects
     * @return
     */
    @Override
    public boolean moveBefore() {
        return false;
    }

    /**
     * check if the distance is correct
     * @param target
     * @param shooter
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws PlayerInDifferentCellException
     */
    private void distance(Player target,Player shooter) throws UncorrectDistanceException, SeeAblePlayerException, PlayerInDifferentCellException //distance, called for every player
    {
        if(distMin==0)//no more controls neeeded
        {

            target.addDmg(shooter.getPlayerId(),damage);
            return;
        }
        else if(melee)
        {
            if(target.getCurrentPosition()==shooter.getCurrentPosition())
            {
                target.addDmg(shooter.getPlayerId(),damage);
            }else{
                throw new PlayerInDifferentCellException();
            }
        }
        else if(distMin<10 && distMin!=0)//0 means uninportant
        {
            if(Map.getDist(target,shooter)>=distMin){//--------wait

                target.addDmg(shooter.getPlayerId(),damage);
            }else{throw new UncorrectDistanceException();}

        }else if(distMin>=10 && distMin <100)
        {
            //distMin/10 is the maximun distance
            if(Map.getDist(target,shooter)<=(distMin/10)){
                target.addDmg(shooter.getPlayerId(),damage);

            }else{throw new UncorrectDistanceException();}
        }else if(distMin==100)//if the shooter can't see the target
        {
            if(!shooter.canSee().contains(target)){
                target.addDmg(shooter.getPlayerId(),damage);
            }else{throw new SeeAblePlayerException();}
        }else if(distMin>=1000 && distMin<9999){
            //a player that is exactly distMin/1000 away
            if(Map.getDist(target,shooter)==(distMin/1000)){
                target.addDmg(shooter.getPlayerId(),damage);
            }else{throw new UncorrectDistanceException();}
        }
    }

    /**
     * check if player are in the same cell or not
     * @param p
     * @param playerList
     * @throws PlayerInSameCellException
     */
    private void differentCellsCheck(Player p,List<Player> playerList) throws PlayerInSameCellException
    {
        for(Player item:playerList)
        {
            if(p.getCurrentPosition()==item.getCurrentPosition())
                throw new PlayerInSameCellException();
        }
    }

    /**
     * check if player are in the same cell or not
     * @param p
     * @param playerList
     * @throws PlayerInDifferentCellException
     * @throws NotCorrectPlayerNumberException
     */
    private void sameCellCheck(Player p,List<Player> playerList) throws PlayerInDifferentCellException, NotCorrectPlayerNumberException {
        if(!playerList.containsAll(p.getCurrentPosition().getPlayers())){//non all the player are in the number
            throw new NotCorrectPlayerNumberException();
        }
        for(Player item:playerList)
        {
            if(p.getCurrentPosition()!=item.getCurrentPosition())
                throw new PlayerInDifferentCellException();
        }
    }

    /**
     * print
     */
    public void print()
    {
        /*
        System.out.println("damage: "+damage);
        System.out.println("seeAbletargetneededd: "+seeAbleTargetNeeded);
        System.out.println("alreadyTargetd: "+alreadyTargeted);
        System.out.println("Different player:" +differentPlayer);
        System.out.println("Minimun distance required:"+distMin);
        System.out.println("Melee:"+melee);
        System.out.println("Player number="+playerNum);
System
         */
    }
}
