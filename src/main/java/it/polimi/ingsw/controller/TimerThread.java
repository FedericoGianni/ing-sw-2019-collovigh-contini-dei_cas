package it.polimi.ingsw.controller;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimerThread implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    public static final Level level = Level.INFO;

    private final int timerStart ;
    private int timerCount;

    private static final String LOG_START = "[Timer]";

    private boolean active = true;

    private final Timer timer;

    public TimerThread(Timer timer, int timerStart) {

        this.timer = timer;

        this.timerStart = timerStart;

        this.timerCount = timerStart;
    }

    @Override
    public void run() {

        active = true;

        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {

                LOGGER.log(level,() -> LOG_START + " remaining: " + timerCount);

                timerCount--;

                if (timerCount <= 0) {

                    LOGGER.warning(LOG_START + " Timer has expired ! ");

                    //calls default answer

                    LOGGER.log(Level.INFO,() -> LOG_START + " sending default answer");

                    timer.defaultAnswer();

                    //ends thread

                    this.cancel();
                }

                if (!active){

                    //ends thread

                    this.cancel();

                }

            }
        };

        java.util.Timer timer = new java.util.Timer("ActionTimer");
        timer.scheduleAtFixedRate(timerTask, 30, 1000);

    }

    public void stopTimer() {

        this.active = false;

    }
}
