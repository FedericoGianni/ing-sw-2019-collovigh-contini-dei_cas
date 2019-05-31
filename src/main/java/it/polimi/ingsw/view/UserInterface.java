package it.polimi.ingsw.view;

import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;

public interface UserInterface {

    /**
     *  This function starts the ui and ask the user which protocol wants to use
     */
    void startUI();

    // Utils

    void show(String s);
    void notifyUpdate(UpdateType updateType, int playerId);
    void startGame();



    // Login

    void gameSelection();

    void login();

    void retryLogin(String error);

    void retryLogin(Exception e);



    // Game Management

    void startSpawn();

    void startPowerUp();

    void askGrenade();

    void startAction();

    void startReload();



}
