package it.polimi.ingsw.view.cachemodel.cachedmap;

/**
 * Simplified version of the map to store local information about cells such as ammo and waepon which can be bought
 * Note that the cached map doesn't have any cell adjacences, this can be retrieved by asking the server if from the
 * current position the direction is valid or not (there's a wall or the map border)
 */
public class CachedMap {

    /**
     * integer representing the game map type
     */
    private final int mapType;

    /**
     * matrix of cached cells representing the map
     */
    private CachedCell[][] map;

    private static final int MAP_R = 3;
    private static final int MAP_C = 4;


    public CachedMap(int mapType) {

        this.mapType = mapType;
        this.map = new CachedCell[MAP_R][MAP_C];
    }

    public void update(CachedCell cell){

        this.map[cell.getPosition().x][cell.getPosition().y] = cell;
    }

    public int getMapType() {
        return mapType;
    }

    public CachedCell getCachedCell(int r, int c) {
        return map[r][c];
    }

    public CachedCell[][] getCachedCellMatrix(){return map;}
}
