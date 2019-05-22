package it.polimi.ingsw.view;

public interface UserInterface {

    /**
     *  This function starts the ui and ask the user which protocol wants to use
     */
    void startUI();

    // Utils

    void show(String s);



    // Login

    void gameSelection();

    void login();

    void retryLogin(String error);

    void retryLogin(Exception e);



    // Game Management

    void startSpawn();

    void startPowerUp();

    void startAction();

    void startReload();


}
