package it.polimi.ingsw.view;

public interface UserInterface {

    /**
     *  This function starts the ui and ask the user which protocol wants to use
     */
    void startUI();

    void login();

    void retryLogin(String error);

    void startSpawn();

    void startPowerUp();

    void startAction();

    void startReload();


}
