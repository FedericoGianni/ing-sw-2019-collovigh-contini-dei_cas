package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CellColor;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Furnace  extends SpecialWeapons{

    private static final String FURNACE_NAME = "FURNACE";

    private final List<AmmoCube> costBaseEffect;

    private final List<AmmoCube> costSecondEffect;

    public Furnace() {

        super(FURNACE_NAME);

        this.costBaseEffect = new ArrayList<>();
        costBaseEffect.add(new AmmoCube(Color.RED));
        costBaseEffect.add(new AmmoCube(Color.BLUE));

        this.costSecondEffect = new ArrayList<>();
    }



    /**
     * This weapon need:
     * effect 0 -> a cell of the room targeted
     * effect 1 -> the cell targeted
     *
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException {

        if (preShoot(targetLists,effects,cells)){

            if (effects.contains(0)){

                shootFirstEffect(cells.get(0));

            } else {

                shootSecondEffect(cells.get(0));

            }

            this.setLoaded(false);
        }
    }

    /**
     * This method does the first-effect-Shot
     * @param cell is the cell belonging to the room in which the player wants to shoot
     */
    private void shootFirstEffect(Cell cell){

        for (Cell roomCell : getRoom(cell.getColor())){

            SpecialWeapons.toAllPlayerInCell(roomCell,1,0,isPossessedBy().getPlayerId());
        }
    }

    /**
     * This method does the second-effect-Shot
     * @param cell is the cell in which the player wants to shoot
     */
    private void shootSecondEffect(Cell cell){

        SpecialWeapons.toAllPlayerInCell(cell,1,1,isPossessedBy().getPlayerId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {
        return new ArrayList<>(costBaseEffect);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        for (int i = 0; i < effects.size(); i++) {
            for(Player p : targetLists.get(i)){
                if(p.getStats().getDmgTaken().size() > KILL_DMG){
                    throw new PlayerAlreadyDeadException();
                }
            }
        }

        if (cells.isEmpty()) throw new CellNonExistentException();

        if (effects.contains(0) && !effects.contains(1)){

            return checkBaseEffect(cells.get(0));

        } else if ( effects.contains(1) && ! effects.contains(0)){

            return checkSecondEffect(cells.get(0));

        } else {

            throw new UncorrectEffectsException();
        }
    }

    /**
     * This Method will check if the base effect is doable with the given parameters
     * @param cell is the cell required by the effect
     * @return true if the parameters are validated
     * @throws CellNonExistentException if the cell is not specified or is null in the model
     * @throws PlayerInSameCellException if the player is in the specified room
     */
    private Boolean checkBaseEffect(Cell cell) throws CellNonExistentException, PlayerInSameCellException{

        if ( ( cell == null ) || (!Model.getMap().hasCell(cell)) ) throw new CellNonExistentException();

        if (getRoom(cell.getColor()).contains(isPossessedBy().getCurrentPosition())) throw new PlayerInSameCellException();

        return true;
    }

    /**
     * This Method will check if the second effect is doable with the given parameters
     * @param cell is the cell required by the effect
     * @return true if the parameters are validated
     * @throws CellNonExistentException if the cell is not specified or is null in the model
     * @throws PlayerInSameCellException if the player is in the specified cell
     */
    private Boolean checkSecondEffect(Cell cell) throws CellNonExistentException, PlayerInSameCellException, NotEnoughAmmoException{

        if (!isPossessedBy().canPay(costSecondEffect)) throw new NotEnoughAmmoException();

        if ( ( cell == null ) || (!Model.getMap().hasCell(cell)) ) throw new CellNonExistentException();

        if (isPossessedBy().getCurrentPosition().equals(cell)) throw new PlayerInSameCellException();

        return true;
    }

    /**
     * This method will get all the cells of a room of a given color
     * @param color is the color of the room
     * @return a list of all the cells of the room
     */
    private List<Cell> getRoom(CellColor color){

        List<Cell> roomCells = new ArrayList<>();

        for (int i = 0; i < Map.MAP_R; i++) {

            for (int j = 0; j < Map.MAP_C; j++) {

                if( (Model.getMap().getCell(i,j) != null ) && (Model.getMap().getCell(i,j).getColor().equals(color)) ) roomCells.add(Model.getMap().getCell(i,j));
            }
        }

        return roomCells;
    }
}
