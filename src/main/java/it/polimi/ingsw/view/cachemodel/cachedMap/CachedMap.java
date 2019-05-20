package it.polimi.ingsw.view.cachemodel.cachedmap;

public class CachedMap {

    private final int mapType;
    private CachedCell[][] map;

    public CachedMap(int mapType) {

        this.mapType = mapType;
    }

    public void update(CachedCell cell){

        this.map[cell.getPosition().x][cell.getPosition().y] = cell;
    }

    public int getMapType() {
        return mapType;
    }
}
