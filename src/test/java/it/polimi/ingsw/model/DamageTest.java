package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Damage;
import org.junit.jupiter.api.Test;

class DamageTest {

    @Test
    void populator() {
        Damage.populator();//creates the damages microeffects inside a damages List, just for tryng not useful
        for(int i=0;i<Damage.getDamagesList().size();i++)
        {
            System.out.println(Damage.getDamagesList().get(i).getDamages());//just testing to see if all th damages are read
        }
    }
}