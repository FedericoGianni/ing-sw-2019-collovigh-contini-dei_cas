package it.polimi.ingsw;



import java.util.List;

/**
 * 
 */
public class FireMode {

    /**
     * Default constructor
     */
    public FireMode(int num) {//requires the number of targets
        targets = new int[num];
    }

    /**
     * 
     */
    private int number;

    /**
     * 
     */
    private List<AmmoCube> cost;

    /**
     * 
     */
    private int dmg;

    /**
     * 
     */
    private int marks;

    /**
     * 
     */
    private Boolean canHitInvTarget;

    /**
     * 
     */
    private int[] targets; //no size because we dont know how many targets

    /**
     * 
     */
    private int[] squareRadius = new int[2];

    /**
     * 
     */
    private int push;

    /**
     * 
     */
    private Boolean wholeRoom;

    /**
     * 
     */
    private Boolean wholeSquare;

    /**
     * 
     */
    private Boolean lineShot;

    /**
     * 
     */
    private Boolean ignoreWall;

    /**
     * 
     */
    private FireMode nextMode;


    /**
     * @return
     */
    public Boolean canBeUsed() {
        // TODO implement here
        return null;
    }

    /**
     * @param cell 
     * @return
     */
    public Boolean isReachable(Cell cell) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public FireMode getNextMode() {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public Boolean dmgToPlayer(Player p) {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public int marksToPlayer(Player p) {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public Boolean canHitMorePlayer() {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public List<Integer> dmgToPlayer(List<Player> p) {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public List<Integer> marksToPlayer(List<Player> p) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getNumber() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getCost() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public Boolean canPull() {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @param aimedCell 
     * @return
     */

    /*public Pair getNewPos(Player p, Pair aimedCell) {
        // TODO implement here
        return null;
    }*/

}