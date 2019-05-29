package it.polimi.ingsw.view.actions;

import java.io.Serializable;

public abstract class JsonAction implements Serializable {

    private final ActionTypes actionType;

    public JsonAction(ActionTypes actionType) {

        this.actionType = actionType;
    }

    public ActionTypes getType(){

        return actionType;
    }
}
