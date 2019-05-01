package it.polimi.ingsw.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitingRoomTest {

    @Test
    void startTimer() {
        WaitingRoom w = new WaitingRoom();
        w.startTimer();
    }
}