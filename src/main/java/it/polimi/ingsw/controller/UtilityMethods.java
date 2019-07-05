package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP;

/**
 * This class contains some utility methods that are used by more than one controller's classes
 */
public class UtilityMethods {


    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    private static Level level = Level.INFO;

    /**
     * logger incipit
     */
    private static final String LOG_START = "[Controller] ";

    /**
     * Controller instance
     */
    private final Controller controller;

    /**
     * Constructor
     * @param controller is the class that instantiated this
     */
    public UtilityMethods(Controller controller) {
        this.controller = controller;
    }

    /**
     *
     * @param row is the start cell row
     * @param column is the start cell column
     * @param direction is the direction the player wants to move
     * @return true if the player can make a movement in this direction
     */
    public boolean askMoveValid(int row, int column, Directions direction) {

        //LOG

        LOGGER.log(level, () -> "[CONTROLLER] receiveed askMove valid from pos: " + row + ", " + column + " and direction : " + direction);

        if (Model.getMap().getCell(row, column) == null) {
            return false;
        }

        Cell startCell = Model.getMap().getCell(row, column);

        switch (direction){

            case NORTH:

                if (startCell.getNorth() != null ) return true;

                break;

            case EAST:

                if (startCell.getEast() != null ) return true;

                break;

            case WEST:

                if (startCell.getWest() != null ) return true;

                break;

            case SOUTH:

                if (startCell.getSouth() != null ) return true;

                break;

            default:

                break;
        }

        return false;
    }


    // move methods

    /**
     * this method moves the current player
     * @param directionsList is the list of the directions the player wants to move
     */
    public void move(List<Directions> directionsList){

        // gets the final position

        Cell finalPosition = simulateMovement(directionsList);

        // moves the player

        Model.getPlayer(controller.getCurrentPlayer()).setPlayerPos(finalPosition);

    }

    /**
     * This method simulate the actual movement and return the cell the player would reach if he moves
     * @param directions is the list of movements the player wants to do
     * @return the final cell the player would reach if he moves
     */
    public Cell simulateMovement(List<Directions> directions){

        Cell cell = Model.getPlayer(controller.getCurrentPlayer()).getCurrentPosition();

        for (Directions direction : directions){

            cell = getNextCell(direction,cell);
        }

        return cell;
    }

    /**
     * This method move the player in the given direction
     * @param direction the player wants to move to
     */
    private void moveCurrentPlayer(Directions direction) {

        // gets the id of the current player

        int playerId = controller.getCurrentPlayer();

        //gets the current position

        Cell position = Model.getPlayer(playerId).getCurrentPosition();

        // change the position var in the direction given

        position = (getNextCell(direction,position) != null) ? getNextCell(direction,position) : position;

        // sets the player in the new position

        Model.getPlayer(playerId).setPlayerPos(position);

        // logs the position change

        LOGGER.log(level, () ->  playerId + " moved " + direction + " in cell : " + Model.getMap().cellToCoord(Model.getPlayer(playerId).getCurrentPosition()));
    }

    /**
     *  This method compute the next cell given a direction
     * @param direction is the direction to move
     * @param startPoint is the cell from which starts
     * @return the cell in the direction specified
     */
    private Cell getNextCell(Directions direction, Cell startPoint){

        if (startPoint != null) {

            switch (direction) {

                case NORTH:

                    if (startPoint.getNorth() != null) return startPoint.getNorth();

                    break;

                case EAST:

                    if (startPoint.getEast() != null) return startPoint.getEast();

                    break;

                case WEST:

                    if (startPoint.getWest() != null) return startPoint.getWest();

                    break;

                case SOUTH:

                    if (startPoint.getSouth() != null) return startPoint.getSouth();

                    break;

                default:

                    break;
            }
        }

        return null;
    }


    //weapon locators

    /**
     * This method will return the weapon instance if there is a weapon with given name in the specified spawn or null
     * @param weaponName is the name specified
     * @param spawnCell is the Spawn Cell specified
     * @return the weapon instance or null if not found
     */
    public Weapon findWeaponInSpawnCell(String weaponName, SpawnCell spawnCell){

        List<Weapon> weaponList = spawnCell
                .getWeapons()
                .stream()
                .filter( x -> x.getName().equalsIgnoreCase(weaponName))
                .collect(Collectors.toList());

        return weaponList.isEmpty() ? null : weaponList.get(0);
    }

    /**
     * This method will return the weapon instance if there is a weapon with given name in the specified player's weaponBag or null
     * @param weaponName is the name of the weapon
     * @param playerId is the id of the player
     * @return the weapon instance or null if not found
     */
    public Weapon findWeaponInWeaponBag(String weaponName, int playerId){

        List<Weapon> weaponList = Model.getPlayer(playerId)
                .getCurrentWeapons()
                .getList()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(weaponName))
                .collect(Collectors.toList());

        return weaponList.isEmpty() ? null : weaponList.get(0);
    }

    /**
     * This method will return a list of ammo by considering the powerUp given as ammo
     * @param powerUps is a list of powerUp
     * @return a list of Ammo
     */
    public List<AmmoCube> powerUpToAmmoList(List<PowerUp> powerUps){

        return powerUps
                .stream()
                .map( powerUp -> new AmmoCube(powerUp.getColor()))
                .collect(Collectors.toList());
    }

    /**
     * This method will extract from the given powerUp list the one matching the Cached one or throw an exception if not found
     * @param cachedPowerUp is a cached powerUp
     * @param powerUpList is a list of real powerUp, will not be modified
     * @return an instance of a PowerUp contained in the list that match the Cached parameters
     * @throws CardNotPossessedException if the powerUp is not found
     */
    public PowerUp cachedToRealPowerUp(CachedPowerUp cachedPowerUp, List<PowerUp> powerUpList) throws CardNotPossessedException {

        List<PowerUp> matching = powerUpList
                .stream()
                .filter( x -> x.getType().equals(cachedPowerUp.getType()) && x.getColor().equals(cachedPowerUp.getColor()) )
                .collect(Collectors.toList());

        if (matching.isEmpty()) throw new CardNotPossessedException();
        else return matching.get(0);
    }


    /**
     *  THis method will check if the specified powerUps belongs to the player
     * @param powerUps is a list of cachedPowerUp received from the view
     * @return true if the player effectively has them
     */
    public Boolean checkIfPlayerPossessPowerUps(List<CachedPowerUp> powerUps){

        Boolean returnValue = true;

        try{

            getSpecifiedPowerUp(powerUps);

        }catch (CardNotPossessedException e){

            // log

            LOGGER.log(Level.WARNING,() -> LOG_START + " player does not possess all powerUps he declared ");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP);

            return false;
        }

        return returnValue;
    }

    /**
     * This method will get a list of PowerUp from a list of CachedPowerUp
     * @param powerUps is a list of cachedPowerUp
     * @return a list of PowerUp
     * @throws CardNotPossessedException if a powerUp was not found
     */
    public List<PowerUp> getSpecifiedPowerUp(List<CachedPowerUp> powerUps) throws CardNotPossessedException {

        // declare a list of powerUp

        List<PowerUp> powerUpList = new ArrayList<>();

        // get a copy of the list of the powerUp possessed by the model

        List<PowerUp> possessed = Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getList();

        // for each cachedPowerUp gets the correspondent one from the model and adds it to the new list

        for (CachedPowerUp cachedPowerUp : powerUps){

            PowerUp toRemove = cachedToRealPowerUp(cachedPowerUp,possessed);

            powerUpList.add(toRemove);

            // then removes it from the original list so that can not be picked again

            possessed.remove(toRemove);

        }

        return powerUpList;
    }

    /**
     * This method will check if a list of cachedPowerUp can be sold or if the player would have more than max AmmoCube
     * @param playerId is the id of the required player
     * @param cachedPowerUpList is the list of the powerUp to sold
     * @return true if the cachedPowerUp can be sold
     */
    public Boolean powerUpCanBeSold( Integer playerId, List<CachedPowerUp> cachedPowerUpList){


        List<Color> colorList = cachedPowerUpList
            .stream()
            .map(CachedPowerUp::getColor)
            .collect(Collectors.toList());

        for (Color color : Color.values() ){

            int requestPowerUpColorNum = (int) colorList
                    .stream()
                    .filter( x -> x.equals(color) )
                    .count();


            if ( Model.getPlayer(playerId).getAmmoBag().leftMaxAmmo(color) < requestPowerUpColorNum ) return false;
        }

        return true;
    }

    /**
     * This method will sell the sellable powerUp
     * @param playerId is the id of the player who wants to sell his powerUp
     * @param cachedPowerUpList is the list of the powerUp
     * @return a list of the powerUp that can't be sold
     * @throws CardNotPossessedException if the powerUp is not found
     */
    public List<CachedPowerUp> sellSellablePowerUp( Integer playerId, List<CachedPowerUp>  cachedPowerUpList ) throws  CardNotPossessedException{

        List< CachedPowerUp> remainingToSell = new ArrayList<>();

        if(cachedPowerUpList != null){

            for (CachedPowerUp cachedPowerUp : cachedPowerUpList) {

                List<CachedPowerUp> selectedAsList = new ArrayList<>();

                selectedAsList.add(cachedPowerUp);

                if (powerUpCanBeSold(playerId, selectedAsList)) {

                    PowerUp selected = getSpecifiedPowerUp(selectedAsList).get(0);

                    Model.getPlayer(playerId).sellPowerUp(selected);

                } else {

                    remainingToSell.add(cachedPowerUp);
                }
            }
        }

        return remainingToSell;
    }

    /**
     * This method translate a list of point into a list of cells
     * @param cells is a list of point
     * @return the correspondent list of cells
     */
    public List<Cell> PointToCell(List<Point> cells){

        return  (cells == null ) ? new ArrayList<>() : cells
                .stream()
                .map(x -> {
                    if (x == null ) return null;
                    else return Model.getMap().getCell(x.x, x.y);
                })
                .collect(Collectors.toList());
    }

}
