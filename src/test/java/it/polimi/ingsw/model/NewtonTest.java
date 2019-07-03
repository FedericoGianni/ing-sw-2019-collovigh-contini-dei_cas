package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.PlayerNonExistentException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewtonTest {

    @Test
    void use() {

        List<String> names = new ArrayList<>();

        names.add("Frank");
        names.add("Alex");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.PURPLE);



        Model kat = new Model(names,colors,2,8);

        Newton test = new Newton(Color.BLUE);

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,0));

        try {
            test.use(Model.getPlayer(0), Directions.NORTH, 2);
            assertEquals(Model.getMap().getCell(0,0),Model.getPlayer(0).getCurrentPosition());


        }catch (Exception e){

            e.printStackTrace();
        }

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,0));

        try {

            test.use(Model.getPlayer(0), Directions.SOUTH,1);
            assertEquals(Model.getMap().getCell(1,0),Model.getPlayer(0).getCurrentPosition());


        }catch (Exception e){

            e.printStackTrace();
        }

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,0));

        try {

            test.use(Model.getPlayer(0), Directions.WEST,2);
            assertEquals(Model.getMap().getCell(0,0),Model.getPlayer(0).getCurrentPosition());


        }catch (Exception e){

            e.printStackTrace();
        }

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,0));

        try {

            test.use(Model.getPlayer(0), Directions.EAST,2);
            assertEquals(Model.getMap().getCell(0,2),Model.getPlayer(0).getCurrentPosition());


        }catch (Exception e){

            e.printStackTrace();
        }

        // Cell 2 , 0 is not existent in mapType 2

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,0));


        Player err = new Player("Cheater",99,PlayerColor.BLUE);

        assertThrows(PlayerNonExistentException.class,() ->{

            test.use(err,Directions.SOUTH,2);
        });

    }
}