package it.polimi.ingsw.controller;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will handle the timer for the clients action, the timer will be started when the action request is sent to the client and will be stop when the answer will be received
 *
 * if the answer will not be received in time will be called the :
 *
 * @see #defaultAnswer()
 */
public class Timer {

    /**
     * Logger instance
     */
    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    /**
     * Logger level
     */
    public static final Level level = Level.INFO;

    /**
     * this boolean will enable the timer ( if false nothing will happen if it expire)
     */
    private static final boolean TIMER_ACTIVE = true;

    /**
     * logger incipit
     */
    private static final String LOG_START = "[Timer]";

    /**
     * Controller instance
     */
    private final Controller controller;

    /**
     * is the Timer thread
     */
    private TimerThread thread;

    /**
     * Constructor
     * @param controller is the controller that instantiated this class
     */
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

        controller.setExpectingAnswer(true);

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

        controller.setExpectingAnswer(false);

        // stop the thread

        if (isActive()) {

            thread.stopTimer();

        }

    }

    /**
     * This method will be called when the timer expires
     */
    public void defaultAnswer(){

        LOGGER.log(level, () -> LOG_START + " would have called default Answer");

        if (TIMER_ACTIVE) {

            controller.getVirtualView(controller.getCurrentPlayer()).close();
        }
    }

    /**
     * This method will check is the timer is running
     * @return true if the timer is running false otherwise
     */
    public Boolean isActive(){

        if (thread != null) return thread.isActive();

        else return false;
    }
}
