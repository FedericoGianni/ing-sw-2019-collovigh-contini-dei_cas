package it.polimi.ingsw.network;

import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesTest {

    @Test
    void addGame() {

        Games games = new Games();

        int size = games.size();

        games.addGame();
        games.addGame();

        assertEquals(size + 2, games.size());

        games.clear();

    }

    @Test
    void getSavePath() {
    }
}