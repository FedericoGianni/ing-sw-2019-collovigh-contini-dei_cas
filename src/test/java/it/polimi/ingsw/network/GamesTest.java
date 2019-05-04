package it.polimi.ingsw.network;

import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesTest {

    @Test
    void isEmpty() {

        Games games = new Games();

        games.addGame();
        games.addGame();

        games.clear();

        assertTrue(games.isEmpty());
    }

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

        Games games = new Games();
        String savePath = "resources/json/savegames";

        int gameId = games.addGame();

        try {

            assertEquals(savePath + "/" + gameId, games.getSavePath(gameId));
        }catch (GameNonExistentException e){

            e.printStackTrace();
        }

        assertDoesNotThrow( () ->{

            games.getSavePath(gameId);
        });

        games.clear();
    }

    @Test
    void closeGame() {

        Games games = new Games();

        games.addGame(); //game w/ id: 0
        int gameid = games.addGame(); //game w/ id: 1

        try {

            games.closeGame(1);

            assertEquals(false, games.contains(1));

        }catch(GameNonExistentException e){

            e.printStackTrace();
            }

        games.clear();

    }

    @Test
    void clear() {

        Games games = new Games();

        games.addGame();
        games.addGame();
        games.addGame();

        assertTrue(games.size() > 0);

        games.clear();

        assertEquals(0,games.size());

    }

    @Test
    void contains() {

        Games games = new Games();

        int gameId = games.addGame();

        assertTrue(games.contains(gameId));
    }

    @Test
    void size() {

        Games games = new Games();

        int size = 10;

        for (int i = 0; i < size; i++) {

            games.addGame();
            
        }

        assertEquals(size,games.size());

        games.clear();

    }


}