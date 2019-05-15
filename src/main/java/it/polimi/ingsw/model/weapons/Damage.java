package it.polimi.ingsw.model.weapons;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Damage extends MicroEffect {

    private int damage;//how much damage you can do
    private int playerNum;//some effects can deal damage to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players
    private boolean melee;//some weapons can deal damage to players only in your current cell
    private boolean differentPlayer;//in secondary effects sometimes you need to target different players from the first
    private boolean alreadyTargeted;//sometimes in secondary effects you have to choose between already targeted players of the first effect
    private int distMin;//some effects require a minimum distance, calculated by moves
    //particuarities: if both differentPlayer and alreadyTargeted are true you can choose to apply one or both effects(check machineGun III)
    // the player number is 100 you need to target every player in the target's square(check electroshyte-melee-  and grenade launcher)
    //player num is 10 you must target the number/10 every one from different cell
    //if the minimum distance is over 10 it means maximum distance, like 20 is at maximun distance of 2 (20/10-> 2)(check tractor beam)
    //if the distance is 1000 is like 1 , no more no less
    //if the minimum distance is 100 is an unseeble Player by default, you have to target an unseeable player(check heatseeker)
    //only the damage tag have the distance inside
    private static ArrayList <Damage> damages=new ArrayList<>();

    /**
     *
     * getters and setters
     */
    public int getDamage() {
        return this.damage;
    }




    public void setMelee(boolean melee) {
        this.melee = melee;
    }


    public boolean isDifferentPlayerNeeded() {
        return differentPlayer;
    }

    public void setDifferentPlayer(boolean differentPlayer) {
        this.differentPlayer = differentPlayer;
    }

    public boolean isAlreadyTargetedNeeded() {
        return alreadyTargeted;
    }

    public void setAlreadyTargetd(boolean alreadyTargetd) {
        this.alreadyTargeted = alreadyTargetd;
    }

    public int getDistMin() {
        return distMin;
    }

    public void setDistMin(int distMin) {
        this.distMin = distMin;
    }
    public static void insertDamage(Damage dm) {
        damages.add(dm);
    }

    public static ArrayList <Damage> getDamagesList()
    {
        return damages;
    }

    public Damage(int a, int b, boolean c, boolean d, boolean diff,int dm,boolean at) {
        this.damage=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.melee=d;
        this.differentPlayer=diff;
        this.distMin=dm;
        this.alreadyTargeted=at;
    }
    public Damage(Damage d)
    {
        this.damage=d.getDamage();
        this.playerNum=d.getPlayerNum();
        this.seeAbleTargetNeeded=isSeeAbleTargetNeeded();
        this.melee=d.isMeleeNeeded();
        this.differentPlayer=d.isDifferentPlayerNeeded();
        this.distMin=d.getDistMin();
        this.alreadyTargeted=d.isAlreadyTargetedNeeded();
    }


    public int getDamages()
    {
        return this.damage;
    }
    public void setDamage(int damages)
    {
        this.damage=damages;
    }
    public int getPlayerNum() {
        return playerNum;
    }

    public boolean isMeleeNeeded()
    {
        return this.melee;
    }
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public boolean isSeeAbleTargetNeeded() {
        return seeAbleTargetNeeded;
    }

    public void setSeeAbleTargetNeeded(boolean seeAbleTargetNeeded) {
        this.seeAbleTargetNeeded = seeAbleTargetNeeded;
    }

    public boolean isMeelee() {
        return melee;
    }

    public void setMeelee(boolean meelee) {
        this.melee = meelee;
    }


    /**
     * this method uses the semplified JSON to populate the microeffets-Damage Class that are in a known number
     */
    public static void populator()//static beacuse no damages types may exixst before the first call of this method
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = new File("resources/json/damageTypes").getAbsolutePath();
        try (FileReader reader = new FileReader(path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray damageTypes = (JSONArray) obj;

                for (int i = 0; i < damageTypes.size(); i++) {
                     parseDamageObject((JSONObject)damageTypes.get(i));
                }
            //for each Json input object

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * reads the JSON and create a Damage object with the values
     * @param damages
     */
    private static void parseDamageObject(JSONObject damages)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) damages.get("Damage");//Choose the class

        //get the damage amount
        String t = (String) employeeObject.get("damage");
        //System.out.println(t);

        //Get playerNum
        String d = (String) employeeObject.get("playerNum");
        //System.out.println(d);

        //Get seeAble
        String stn = (String) employeeObject.get("seeAbleTargetNeeded");
        //System.out.println(stn);

        //Get melee
        String melee = (String) employeeObject.get("melee");
        //System.out.println(melee);

        //Get melee
        String diffP = (String) employeeObject.get("differentPlayer");
        //System.out.println(melee);

        //Get melee
        String dist = (String) employeeObject.get("distMin");
        //System.out.println(melee);

        //Get melee
        String alTarg= (String) employeeObject.get("alreadyTargeted");
        //System.out.println(melee);
        Damage dd=new Damage(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(melee),Boolean.parseBoolean(diffP),Integer.parseInt(dist),Boolean.parseBoolean(alTarg));
        Damage.insertDamage(dd);
    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public Damage copy() {
        return this;
    }


    public void microEffectApplicator(ArrayList<Player> playerList, Weapon w, Cell c) throws OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, FrenzyActivatedException, NotCorrectPlayerNumberException {//w.isPossesedBy.getPlayer mi dice il giocatore che spara
        if(alreadyTargeted==true && differentPlayer==false)
        {
            w.getFirstTarget().addDmg(w.isPossessedBy().getPlayerId(),damage);//playerId=0 bcz only one player the same as the first shot
            return;
        }
        else if(alreadyTargeted==true && differentPlayer==true){//MG-3 you must shoot the previous target then you can target whatever you want

            w.getFirstTarget().addDmg(w.isPossessedBy().getPlayerId(),damage);
            playerList.remove(w.getFirstTarget());//shot the previous target
            for(int i=0;i<=playerList.size();i++)//if there are you can shot other targets now
            {
                Model.getPlayer(i).addDmg(w.isPossessedBy().getPlayerId(),damage);
            }
            return;
        }
         if(playerNum==100)
        {
            for(Player item : playerList)//check that everyone is in the same cells
            {
                sameCellCheck(item,playerList);
            }
            for(Player item:playerList)
            {
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
        else {
            if(playerList.size()!=playerNum){
                System.out.println(playerList.size()+" !!"+playerNum);
             throw new NotCorrectPlayerNumberException();
            }
            for(Player item : playerList)
            {
                distance(item,w.isPossessedBy());
            }

        }w.setFirstTarget(playerList.get(0));//it can be useful if you need to reshot the shame player otherwise it doesn't count nothing

    }

    @Override
    public boolean moveBefore() {
        return false;
    }

    private void distance(Player target,Player shooter) throws UncorrectDistanceException, SeeAblePlayerException, OverKilledPlayerException, DeadPlayerException, FrenzyActivatedException//distance, called for every player
    {
        if(melee==true)
        {
            if(target.getCurrentPosition()==shooter.getCurrentPosition())
            {
                target.addDmg(shooter.getPlayerId(),damage);
            }else{
                throw new UncorrectDistanceException();
            }
        }
        else if(distMin<10)
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
        }else{
            //a player that is exactly distMin/1000 away
            if(Map.getDist(target,shooter)==(distMin/1000)){
                target.addDmg(shooter.getPlayerId(),damage);
            }else{throw new UncorrectDistanceException();}
        }
    }

    private void differentCellsCheck(Player p,ArrayList<Player> playerList) throws PlayerInSameCellException
    {
        for(Player item:playerList)
        {
            if(p.getCurrentPosition()==item.getCurrentPosition())
                throw new PlayerInSameCellException();
        }
    }

    private void sameCellCheck(Player p,ArrayList<Player> playerList) throws PlayerInDifferentCellException, NotCorrectPlayerNumberException {
        if(!playerList.containsAll(p.getCurrentPosition().getPlayers())){//non all the player are in the number
            throw new NotCorrectPlayerNumberException();
        }
        for(Player item:playerList)
        {
            if(p.getCurrentPosition()!=item.getCurrentPosition())
                throw new PlayerInDifferentCellException();
        }
    }

    public void print()
    {
        System.out.println("damage: "+damage);
        System.out.println("seeAbletargetneededd: "+seeAbleTargetNeeded);
        System.out.println("alreadyTargetd: "+alreadyTargeted);
        System.out.println("Different player:" +differentPlayer);
        System.out.println("Minimun distance required:"+distMin);
    }
}
