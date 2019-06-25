package it.polimi.ingsw.controller;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Timer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    public static final Level level = Level.INFO;

    private static final String LOG_START = "[Timer]";

    private final Controller controller;

    private TimerThread thread;

    public Timer(Controller controller) {
        this.controller = controller;
    }

    /**
     * This method will start the timer for the actions
     *
     * @param timerTime is the countdown start value
     */
    public void startTimer(int timerTime){

        LOGGER.log(level, () -> LOG_START + " started timer ");

        // creates a new thread for the timer

        thread = new TimerThread(this, timerTime);

        Thread t = new Thread(thread);

        // starts the thread

        t.start();

    }

    /**
     * This method will stop the timer for the actions
     */
    public void stopTimer(){

        // stop the thread

        thread.stopTimer();

    }

    public void defaultAnswer(){

        //controller.defaultAnswer();
    }
}
