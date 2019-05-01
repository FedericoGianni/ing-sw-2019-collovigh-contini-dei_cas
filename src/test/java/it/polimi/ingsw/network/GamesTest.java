package it.polimi.ingsw.network;

import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesTest {

    @Test
    void addGame() {

        Games games = new Games();

        games.addGame();
        games.addGame();

        try {
            System.out.println(games.getSavePath(0));
            System.out.println(games.getSavePath(1));
        }catch(GameNonExistentException e){

            e.printStackTrace();
        }


    }

    @Test
    void getSavePath() {
    }
}