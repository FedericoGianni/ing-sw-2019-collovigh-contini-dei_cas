package it.polimi.ingsw.network.jsonconfig;

import com.google.gson.Gson;

import java.io.FileWriter;

public class ConfigWriter {

    public static void main(String[] args) {

        try{

            Config config = new Config("192.168.1.33",22222,22222,false);

            Gson gson = new Gson();

            FileWriter writer = new FileWriter("resources/json/startupConfig/config.json");


            gson.toJson(config, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

}
