package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.Teleporter;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeleporterTest {


    @Test
    void use() {

        List<String> names = new ArrayList<>();

        names.add("Frank");
        names.add("Alex");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.PURPLE);

        Model kat = new Model(names,colors,2);

        Cell cell = Model.getMap().getCell(1,0);

        Teleporter test = new Teleporter(Color.BLUE);

        assertThrows(CardNotPossessedException.class, () ->{

            test.use(cell);

        });

        Model.getPlayer(0).addPowerUp(test);

        try{

            test.use(cell);

        }catch(Exception e){

            e.printStackTrace();
        }

        assertEquals(cell,Model.getPlayer(0).getCurrentPosition());




    }
}