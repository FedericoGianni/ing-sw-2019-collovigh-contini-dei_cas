package it.polimi.ingsw.view.actions.usepowerup;

import com.google.gson.Gson;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.actions.JsonAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class TEST {


    public static void main(String[] args) {

        classToJson();

        NewtonAction j = (NewtonAction) jsonToClass();

        System.out.println("AMOUNT : " + j.getAmount());
        System.out.println("DIRECTION : " + j.getDirection());
        System.out.println("TARGET : " + j.getTargetPlayerId());

    }

    public static void classToJson(){

        Gson gson = new Gson();

        try {

            JsonAction jsonAction = new NewtonAction(Color.BLUE,0,2, Directions.NORTH);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/usePowerUp.json");


            gson.toJson(jsonAction, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            System.out.println("Die ");
        }
    }

    public static JsonAction jsonToClass(){

        Gson gson = new Gson();

        JsonAction jsonAction;

        try {

            BufferedReader br = new BufferedReader(new FileReader("resources/json/jsonComunication/usePowerUp.json"));

            jsonAction = gson.fromJson(br,NewtonAction.class);

            return jsonAction;

        }catch (Exception e){

            e.printStackTrace();

            return null;
        }
    }
}
