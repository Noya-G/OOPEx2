package gameClient;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.GeoLocation;
import api.game_service;
import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Ex2 {

    private class agentsArray{
        ArrayList<CL_Agent> agentsArray;


    }

    private class pokemonsArray{
        ArrayList<CL_Pokemon> pokemonsArray;

        public pokemonsArray(String info){

        }
    }

    private static CL_GameServer processJsonInfoFromGameServer(String json){
        CL_GameServer gameServer=new CL_GameServer();
        try{
            JSONObject p = new JSONObject(json);
            gameServer.setAgents(p.getInt("agents"));
            gameServer.setGame_level(p.getInt("game_level"));
            gameServer.setGrade(p.getInt("grade"));
            gameServer.setId(p.getInt("id"));
            gameServer.setIs_logged_in(p.getBoolean("is_logged_in"));
            gameServer.setMax_user_level(p.getInt("max_user_level"));
            gameServer.setMoves(p.getInt("moves"));
            gameServer.setPokemons(p.getInt("pokemons"));
            gameServer.setGraphfile(p.getString("graph"));//load the graph file
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return gameServer;
    }


    public static void main(String[] args) {

    }




}