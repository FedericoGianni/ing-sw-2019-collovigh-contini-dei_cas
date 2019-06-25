package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Skull;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointCounter {

    // logger

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private static final String LOG_START = "[Point-Calc] ";

    private static final int[] pointVect = {8,6,4,2,2,1,1};

    private static final int[] pointFrenzyVect = {2,1,1,1};

    private final Controller controller;

    private boolean frenzy;

    private boolean frenzyForTest = false;

    public PointCounter(Controller controller) {
        this.controller = controller;
    }

    /**
     * This method will cycle through all dead players and assigns points for the kills
     */
    public void calculateTurnPoints(){

        frenzy = (controller == null ) ? frenzyForTest : controller.getFrenzy();

        //for each player who has died this turn
        //+1 point for the player who did him the first dmg
        //deaths: 1 -> 8 6 4 4 2 1 1 -> switch case 0
        //deaths: 2 -> 6 4 4 2 1 1 -> case 1
        //deaths: 3 -> 4 4 2 1 1 -> case 2
        //deaths: 4 -> 4 2 1 1 -> case 3
        //deaths: 5 -> 2 1 1 -> case 4
        //deaths: 6 -> 1 1 -> case 5
        //deaths: 7 -> 1 -> case 6

        for (int i = 0; i < Model.getGame().getPlayers().size(); i++) {

            if(Model.getPlayer(i).getStats().getDmgTaken().size() >= 11){

                int[] vect = (Model.getPlayer(i).getStats().isFrenzyBoard()) ? pointFrenzyVect : pointVect;

                //+1 for the player who did the first dmg ( if not has frenzy dmg board )

                if (!Model.getPlayer(i).getStats().isFrenzyBoard()) Model.getPlayer(Model.getPlayer(i).getStats().getDmgTaken().get(0)).addScore(1);

                // gets the list of the player ordered by how much damage they have made

                List<Integer> dmgList = calcDmgList(i);

                // gets the number of deaths

                int deaths = Model.getPlayer(i).getStats().getDeaths();

                // modify the vector to keep count of deaths

                int[] actualPointVect = IntStream.range(deaths - 1 ,vect.length)
                        .map( index -> vect[index])
                        .toArray();

                assignPoints(actualPointVect,dmgList);

                // reset the damage list

                Model.getPlayer(i).resetDmg();

                // if the player is in frenzy set the player dmg board to frenzy

                Model.getPlayer(i).getStats().setFrenzyBoard(frenzy);

            }
        }
    }

    /**
     *
     * @param playerId dead player
     * @return a list ordered by players who did dmg to the dead player, from top to lowest
     */
    private List<Integer> calcDmgList(int playerId){

        // gets the damage list of the player

        List<Integer> playerDmgList = Model.getPlayer(playerId).getStats().getDmgTaken();

        return sortArrayListByPlayerIdOccurrences(playerDmgList);

    }

    /**
     * This function will calculate the killShotTrack Points
     */
    public void calcGamePoints(){

        // convert the killShotTrack in a plain list and gets a list of the id of players who have done more kills

        List<Integer> sortedPlayerList = sortArrayListByPlayerIdOccurrences(convertKillShotTrack());

        // assign points

        assignPoints(pointVect,sortedPlayerList);

    }

    /**
     * This function will translate the killShotTrack from alist of skull in a list of playerIds and will keep count of overkill as double kills
     * @return a list of player Ids in which every id is a kill performed by the correspondent player
     */
    private List<Integer> convertKillShotTrack(){

        List<Integer> plainKillShotTrack = new ArrayList<>();

        for (Skull skull: Model.getGame().getKillShotTrack()){

            for (int i = 0; i < skull.getAmount(); i++) {

                plainKillShotTrack.add(skull.getKillerId());

            }
        }

        return plainKillShotTrack;
    }

    /**
     *
     * @param list is a list of id w/ multiple occurrences
     * @return a list of ids ordered by the occurrences of them in the input list
     */
    private List<Integer> sortArrayListByPlayerIdOccurrences(List<Integer> list){

        // define an HashMap that will contain as key the damages and as value the player responsible

        Map<Integer,Integer> idPointsBound = new HashMap<>();

        // define an array that will contain all the damages and will be sort

        List<Integer> damages = new ArrayList<>();


        for (Player player : Model.getGame().getPlayers()){

            // gets the occurrences of the player id in the damage list

            Integer occurrences = Collections.frequency(list,player.getPlayerId());

            // if he has done damage

            if (occurrences > 0) {

                // to avoid the overriding of an id if two or more players have done same damages

                if (idPointsBound.containsKey(occurrences)){

                    occurrences = occurrences - 1;
                }

                // insert them in the hashMap

                idPointsBound.put(occurrences, player.getPlayerId());

                // insert the damages in the array

                damages.add(occurrences);

            }

        }

        // sort the arrayList in reverse order

        Collections.sort(damages);

        Collections.reverse(damages);

        // return the list of the ids associated to the entries in the array

        return damages
                .stream()
                .map( x -> idPointsBound.get(x))
                .collect(Collectors.toList());

    }

    /**
     * This method will assign points in the Model
     * @param actualPointVect is an int vector containing the point to assign to players in relation on their position in the sorted player list
     * @param sortedPlayerList is an ordered list of player ids
     */
    private void assignPoints( int[] actualPointVect, List<Integer> sortedPlayerList){

        // adds the correspondents point to each players

        for (int j = 0; j < sortedPlayerList.size(); j++) {

            int points = (actualPointVect.length > j) ? actualPointVect[j] : 0;

            Model.getPlayer(sortedPlayerList.get(j)).addScore(points);

            String log = LOG_START + "added " + points + " to player: " + j;

            LOGGER.log( level, () -> log );
        }

    }

    public void setFrenzyForTest(boolean frenzyForTest) {
        this.frenzyForTest = frenzyForTest;
    }
}
