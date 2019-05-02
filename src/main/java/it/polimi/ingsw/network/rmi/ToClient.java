package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;

import java.rmi.Remote;

public interface ToClient extends Remote {

    void NameAlreadyTaken(String name);

    void ColorAlreadyTaken(PlayerColor color);

    Boolean ping();
}
