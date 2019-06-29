package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SpawnCellTest {

    @Test
    void getWeapons() {

        int MAP_R = 3;

        int MAP_C = 4;

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Cell cell = Model.getMap().getCell(0,2);




        for (int i = 0; i < MAP_R; i++) {

            for (int j = 0; j < MAP_C; j++) {

                if ((Model.getMap().getCell(i,j) != null) && (!Model.getMap().getCell(i,j).isAmmoCell())){

                    //System.out.println(" spawn : ( " + i + " , " + j + " ) ");
                }
            }
        }

        assertFalse(cell.isAmmoCell());

        assertEquals(3,cell.getWeapons().size());
    }
}