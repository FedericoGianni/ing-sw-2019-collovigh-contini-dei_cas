package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.TagbackGrenade;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagbackGrenadeTest {

    @Test
    void applyOn() {

        List<String> names = new ArrayList<>();

        names.add("Frank");
        names.add("Alex");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.PURPLE);



        Model kat = new Model(names,colors,2,8);

        TagbackGrenade grenade = new TagbackGrenade(Color.BLUE);

        Model.getPlayer(0).addPowerUp(grenade);

        grenade.applyOn(Model.getPlayer(1), 0);


        assertEquals(0,Model.getGame().getPlayers().get(1).getStats().getMarks().get(0));
        assertEquals(1,Model.getGame().getPlayers().get(1).getMarks().size());
    }
}