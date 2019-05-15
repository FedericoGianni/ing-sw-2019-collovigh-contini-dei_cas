package it.polimi.ingsw.view.virtualView;

import it.polimi.ingsw.view.cachemodel.sendables.CachedGame;

public class CurrentGameObserver implements Observer {

    private CachedGame game;

    @Override
    public void update(Object object) {

        this.game = (CachedGame) object;

        // send update
    }
}
