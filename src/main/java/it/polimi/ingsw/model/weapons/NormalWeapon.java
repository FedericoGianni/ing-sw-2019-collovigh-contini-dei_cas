package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NormalWeapon extends Weapon{

    private String name;
    private boolean isLoaded;
    private List<AmmoCube> cost;
    private List<MacroEffect> effects;


    public boolean isMoveBefore() {
        return moveBefore;
    }

    public void enableMoveBefore() {
        this.moveBefore =true;
    }
    public void disableMoveBefore() {
        this.moveBefore =false;
    }

    private boolean moveBefore;



    public void setName(String name) {
        this.name = name;
    }

    public void setCost(List<AmmoCube> cost) {
        this.cost = cost;
    }
    /**
     *
     * @return
     */
    public List<AmmoCube> getBuyCost()//tell me how much it costs to buy that weapon(reloadCost-first ammo cube)
    {
        ArrayList <AmmoCube> bC=new ArrayList();
        for(int i=1;i<this.cost.size();i++)
        {
            bC.add(this.cost.get(i));
        }
        return bC;
    }



    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    public void reload()throws NotAbleToReloadException {

        // check if the player can reload the weapon

        if(this.canBeReloaded()) {

            // try to pay the cost

            try {

                isPossessedBy().pay(cost);

            }catch (CardNotPossessedException e){

                throw new NotAbleToReloadException();
            }

            // if everything went fine set the weapon to loaded

            this.isLoaded = true;

        }else{

            throw new NotAbleToReloadException();
        }

    }




    /**
     * Constructor,
     *
     * isLoaded is set on true because Weapons are loaded when bought
     * effects are not filled in the creator
     */

    public NormalWeapon(String name, List<AmmoCube> cost, List<MacroEffect>l) {

        this.name = name;
        this.isLoaded = true;
        this.cost = cost;//rememeber that the firts is already payed
        effects=new ArrayList<>();
        effects=l;
    }

    public NormalWeapon(NormalWeapon clone){
        this.name = clone.name;
        this.isLoaded = clone.isLoaded;
        this.cost = clone.cost;
        this.effects = new ArrayList<>();

        for(MacroEffect e : clone.effects){
            this.effects.add(e);
        }
    }

    public boolean isSpecial()
    {
        return false;
    }

    /**
     * @return true only if the player has enough ammo for reloading the NormalWeapon and if it's not reloaded
     *
     */
    private boolean canBeReloaded() {

        return isPossessedBy().canPay(cost);
    }


    /**
     * @return the cost of buying this NormalWeapon so the cost of recharge without the first cube
     */
    public List<AmmoCube> getCost() {

        return cost.subList(1,cost.size());

    }

    @Override
    public List<AmmoCube> getReloadCost() {

        return new ArrayList<>(cost);

    }

    /**
     *
     * @return the name of the NormalWeapon
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @return the list of macro-effects
     */
    public List<MacroEffect> getEffects() {
        return effects;
    }

    /**
     *
     * @param macroEffect is the effect that will be added to the NormalWeapon
     */
    public void addMacroEffect(MacroEffect macroEffect){

        this.effects.add(macroEffect);
    }



    /**
     * creates the static weaponsList
     * @deprecated
     */
    @Deprecated
    public static  List<NormalWeapon>  weaponsCreator()
    {
        //----------------microEffects ecc creator
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();

        ArrayList<NormalWeapon> normalWeapons =new ArrayList<>();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("resources/json/Weaponary"))
        {//change to relative files paths
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray wps = (JSONArray) obj;

            for (int i = 0; i < wps.size(); i++) {
                normalWeapons.add(parseWeaponObject((JSONObject)wps.get(i)));
            }
            //for each Json input object

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return normalWeapons;
    }

    /**
     * reads the JSON and creates a NormalWeapon object and adds it to the list
     * @param micros
     * @deprecated
     */
    @Deprecated
    private static NormalWeapon parseWeaponObject(JSONObject micros)
    {
        //Get  object within list
        JSONObject employeeObject = (JSONObject) micros.get("Weapon");//Choose the class

        //get the damage amount
        String n = (String) employeeObject.get("name");
        //System.out.println(n);

        JSONArray types = (JSONArray) employeeObject.get("cost");//iterate the ammocubes cost codification
        ArrayList <AmmoCube> wpCost=new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {//read ammoCube type and differenciate it
            JSONObject type=(JSONObject)types.get(i);
            String typeEncoded= (String)type.get("ammoC");
            ammoAnalizer(wpCost,typeEncoded);//method that can decodify the ammos code---see documentatio
        }
        types = (JSONArray) employeeObject.get("macroEffects");//iterate the ammocubes cost codification
        ArrayList <MacroEffect> mf=new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {//read Every Effect type and differenciate it
            JSONObject type=(JSONObject)types.get(i);
            int typeEncoded=Integer.parseInt((String)type.get("num"));
            effectsAnalizer(mf,typeEncoded);//method that can decodify the microevfect code---see documentation
            //here changes microF
        }


        NormalWeapon w=new NormalWeapon(n,wpCost,mf);
       return w;
    }

    /**
     * creates the ammo from the JSON using the COlor class
     * @param wpCost
     * @param type
     * @return the cost in AmmoCubes
     * @deprecated
     */
    @Deprecated
    public static List<AmmoCube> ammoAnalizer(List<AmmoCube> wpCost,String type)
    {
            if(type=="BLUE")
            {
                wpCost.add(new AmmoCube(Color.BLUE));
            }else if(type=="RED") {
                wpCost.add(new AmmoCube(Color.RED));
            }else{
                wpCost.add(new AmmoCube(Color.YELLOW));
            }
            return wpCost;
    }

    /**
     * generate a MacroEffects list
     * @param mf
     * @param typeEncoded
     * @return a MacroEffects list
     * @deprecated
     */
    @Deprecated
    public static List<MacroEffect> effectsAnalizer (List<MacroEffect> mf,int typeEncoded)
    {
        mf.add(MacroEffect.getMacroEffects().get(typeEncoded));
        return mf;
    }

   /*
    @Override
    public void shoot(List<List<Player>>targetLists, List<Integer> effect, List<Cell> cells) throws PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, PlayerNotSeeableException, WeaponNotLoadedException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, NotCorrectPlayerNumberException {

            if(!this.isLoaded())//if actual weapon is not loaded
            {
                throw new WeaponNotLoadedException();//weapon not loaded
            }

            for(int macroCont=0;macroCont<effect.size();macroCont++)//iterate macroeffect
            {

                if (this.getEffects().get(macroCont).getEffectCost() != null)//if the effect costs 0 i don't need to pay
                {
                    if (canPay(this.getEffects().get(effect.get(macroCont)).getEffectCost(), this.isPossessedBy().getAmmoBag()) == true)//----need to add effects as payment
                    {
                        for (AmmoCube ammo : this.getEffects().get(effect.get(macroCont)).getEffectCost()) {
                            this.isPossessedBy().pay(ammo.getColor());//pays the effects cost need to be modified
                        }

                    } else {
                        throw new NotEnoughAmmoException();
                    }
                }
                //here i can shoot for real


                for (MicroEffect micro : this.getEffects().get(macroCont).getMicroEffects())//iterates microEffects
                {

                    if (micro.moveBefore() == true && moveBefore)//if i need to move before shooting
                    {
                        micro.microEffectApplicator(targetLists.get(macroCont), this, cells.get(macroCont));//contatore appostio forse perchè sposta gli ordini??
                        this.getEffects().get(macroCont).getMicroEffects().remove(micro);

                    }

                }

                for (MicroEffect micro : this.getEffects().get(macroCont).getMicroEffects())//iterates microEffects
                {
                    if (cells != null && !cells.isEmpty())//if you also have mover effects
                    {
                        micro.microEffectApplicator(targetLists.get(macroCont), this, cells.get(macroCont));
                    }//the method that applies the effects
                    else {
                       try{ micro.microEffectApplicator(targetLists.get(macroCont), this, null);}
                       catch(PlayerNotSeeableException e){
                           throw new PlayerNotSeeableException();

                       }
                    }
                }


            }
        this.isLoaded=false;
    }*/

    /**
     * Shoot and if the shoot doesn't go well restores the old state
     * @param targetLists
     * @param effect
     * @param cells
     * @throws PlayerInSameCellException
     * @throws DifferentPlayerNeededException
     * @throws SeeAblePlayerException
     * @throws PlayerNotSeeableException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws NotCorrectPlayerNumberException
     * @throws CardNotPossessedException
     * @throws WeaponNotLoadedException
     * @throws NotEnoughAmmoException
     */
    public void shoot(List<List<Player>> targetLists, List<Integer> effect, List<Cell> cells) throws PlayerInSameCellException, DifferentPlayerNeededException, SeeAblePlayerException, PlayerNotSeeableException, PlayerInDifferentCellException, UncorrectDistanceException, NotCorrectPlayerNumberException, CardNotPossessedException, WeaponNotLoadedException, NotEnoughAmmoException, PrecedentPlayerNeededException {

        List<List<Player>> targetsCopy=new ArrayList<>();
        //now i create the fake players
        for (List<Player> item : targetLists) {
            List<Player> pl=new ArrayList<>();
            for (Player p : item) {
                Player tmp=new Player(p.getPlayerName(),p.getPlayerId(),p.getColor());
                for (AmmoCube a : p.getAmmoBag().getList()) {
                    tmp.getAmmoBag().addItem(a);
                }
                tmp.getStats().setDmgTakenCopy(p.getStats().getDmgTaken());
                tmp.getStats().setMarksCopy(p.getStats().getMarks());
                tmp.setPlayerPosCopy(p.getCurrentPosition());
                pl.add(tmp);
            }
            //now i sort the players in order with playerID
            targetsCopy.add(pl);
        }

      try {
          if (!this.isLoaded())//if actual weapon is not loaded
          {
              throw new WeaponNotLoadedException();//weapon not loaded
          }

          for (int macroCont = 0; macroCont < effect.size(); macroCont++)//iterate macroeffect
          {

              System.out.println("Uso : " +this.getEffects().get(effect.get(macroCont)).getName());
              if (this.getEffects().get(effect.get(macroCont)).getEffectCost() != null)//if the effect costs 0 i don't need to pay
              {
                  if (canPay(this.getEffects().get(effect.get(macroCont)).getEffectCost(), this.isPossessedBy().getAmmoBag()) == true)//----need to add effects as payment
                  {
                      for (AmmoCube ammo : this.getEffects().get(effect.get(macroCont)).getEffectCost()) {
                          this.isPossessedBy().pay(ammo.getColor());//pays the effects cost need to be modified
                      }

                  } else {
                      restore(targetsCopy,targetLists);
                      throw new NotEnoughAmmoException();
                  }
              }
              //here i can shoot for real


              for (MicroEffect micro : this.getEffects().get(effect.get(macroCont)).getMicroEffects())//iterates microEffects
              {

                  if (micro.moveBefore() == true && moveBefore)//if i need to move before shooting
                  {
                      micro.microEffectApplicator(targetLists.get(macroCont), this, cells.get(macroCont),macroCont);//contatore appostio forse perchè sposta gli ordini??
                      this.getEffects().get(macroCont).getMicroEffects().remove(micro);

                  }

              }

              for (MicroEffect micro : this.getEffects().get(effect.get(macroCont)).getMicroEffects())//iterates microEffects
              {
                  if (cells != null && !cells.isEmpty())//if you also have mover effects
                  {
                      micro.microEffectApplicator(targetLists.get(macroCont), this, cells.get(macroCont),macroCont);
                  }//the method that applies the effects
                  else {
                          micro.microEffectApplicator(targetLists.get(macroCont), this, null,macroCont);
                  }
              }


          }

        }
      catch(PlayerInSameCellException e)
      {   restore(targetsCopy,targetLists);
          throw  new PlayerInSameCellException();
      } catch (NotEnoughAmmoException e) {
          restore(targetsCopy,targetLists);
          throw new NotEnoughAmmoException();
      } catch (WeaponNotLoadedException e) {
          restore(targetsCopy,targetLists);
          throw new WeaponNotLoadedException();
      } catch (PlayerInDifferentCellException e) {
          restore(targetsCopy,targetLists);
          throw new PlayerInDifferentCellException();
      } catch (CardNotPossessedException e) {
          restore(targetsCopy,targetLists);
          throw new CardNotPossessedException();
      } catch (UncorrectDistanceException e) {
          restore(targetsCopy,targetLists);
          throw new UncorrectDistanceException();
      } catch (PlayerNotSeeableException e) {
          restore(targetsCopy,targetLists);
          throw new PlayerNotSeeableException();
      } catch (NotCorrectPlayerNumberException e) {
          restore(targetsCopy,targetLists);
          throw new NotCorrectPlayerNumberException();
      } catch (DifferentPlayerNeededException e) {
          restore(targetsCopy,targetLists);
          throw new DifferentPlayerNeededException();
      } catch (SeeAblePlayerException e) {
          restore(targetsCopy,targetLists);
          throw new SeeAblePlayerException();
      } catch (PrecedentPlayerNeededException e) {
          restore(targetsCopy,targetLists);
          throw new PrecedentPlayerNeededException();
      }
        this.isLoaded=false;
    }


    /**
     * in case of failed shoot restores everything as before the shoot attempt
     * @param targetsCopy
     * @param targetLists
     */
    private void restore(List<List<Player>> targetsCopy,List<List<Player>> targetLists)
    {
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
        public void print()
    {

            System.out.println(this.getName());
            for(int j = 0; j< this.getEffects().size(); j++)
            {
                System.out.println(this.getEffects().get(j).getName());
                System.out.print("Cost: ");
                for(int h=0;h<this.getEffects().get(j).getEffectCost().size();h++)
                        System.out.println(getEffects().get(j).getEffectCost().get(h));
                if(this.getEffects().get(j).moveBeforShooting())
                {  this.getEffects().get(j).getMicroEffects().get(2);//you need to move before everything
                    for(int h=0;h<this.getEffects().get(j).getMicroEffects().size()-1;h++)
                    this.getEffects().get(j).getMicroEffects().get(h).print();}
                else{//shoot and other things then move
                    for(int h=0;h<this.getEffects().get(j).getMicroEffects().size()-1;h++)
                        this.getEffects().get(j).getMicroEffects().get(h).print();
                }
            }
    }

}