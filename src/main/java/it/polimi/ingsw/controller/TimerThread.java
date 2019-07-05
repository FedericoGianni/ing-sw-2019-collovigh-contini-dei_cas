package it.polimi.ingsw.controller;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the thread for the timer, will be used by:
 *
 * @see it.polimi.ingsw.controller.Timer
 */
public class TimerThread implements Runnable {

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    public static final Level level = Level.INFO;

    /**
     * This int will hold the timer max time
     */
    private final int timerStart ;

    /**
     * is the current countdown state
     */
    private int timerCount;

    /**
     * logger incipit
     */
    private static final String LOG_START = "[Timer]";

    /**
     * is true if the timer is active
     */
    private boolean active = true;

    /**
     * is a Timer instance
     */
    private final Timer timerClass;

    /**
     * Constructor
     * @param timer is the class that instantiated this
     * @param timerStart is the max timer time
     */
    public TimerThread(Timer timer, int timerStart) {

        this.timerClass = timer;

        this.timerStart = timerStart;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        timerCount = timerStart;

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

                    timerClass.defaultAnswer();

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

    /**
     * This method stops the timer
     */
    public void stopTimer() {

        this.active = false;

    }

    /**
     *
     * @return true if the timer is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
}
