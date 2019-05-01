package it.polimi.ingsw.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitingRoomTest {

    @Test
    void startTimer() {

        final int TIMER = 30;
        WaitingRoom w = new WaitingRoom();
        w.startTimer();
        assertEquals(TIMER, w.getTimerCount());

        /*this part waits for the timer set time and assert that at the end of selected time the counter
         *has actually been decreased to 0. it is commented to avoid slowing down test exections
        try {
            Thread.sleep(31000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        assertEquals(0, w.getTimerCount());
        */
    }
}