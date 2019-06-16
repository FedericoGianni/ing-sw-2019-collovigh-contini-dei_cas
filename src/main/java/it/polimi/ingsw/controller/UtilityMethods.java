package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UtilityMethods {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private final Controller controller;

    public UtilityMethods(Controller controller) {
        this.controller = controller;
    }

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
    public PowerUp CachedToRealPowerUp(CachedPowerUp cachedPowerUp, List<PowerUp> powerUpList) throws CardNotPossessedException {

        List<PowerUp> matching = powerUpList
                .stream()
                .filter( x -> x.getType().equals(cachedPowerUp.getType()) && x.getColor().equals(cachedPowerUp.getColor()) )
                .collect(Collectors.toList());

        if (matching.isEmpty()) throw new CardNotPossessedException();
        else return matching.get(0);
    }

}
