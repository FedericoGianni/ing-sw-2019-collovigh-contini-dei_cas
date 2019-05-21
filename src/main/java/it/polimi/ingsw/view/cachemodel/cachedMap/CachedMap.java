package it.polimi.ingsw.view.cachemodel.cachedmap;

public class CachedMap {

    private final int mapType;
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
}
