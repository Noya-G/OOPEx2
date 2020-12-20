package gameClient;

import api.DWGraph_DS;
import api.*;
import api.directed_weighted_graph;

public class CL_GameServer {
    private int pokemons, moves, grade, game_level, max_user_level, id, agents;
    private boolean is_logged_in;
    private directed_weighted_graph graph;
    private String graphfile;


    public void setPokemons(int pokemons) {
        this.pokemons = pokemons;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setGame_level(int game_level) {
        this.game_level = game_level;
    }

    public void setMax_user_level(int max_user_level) {
        this.max_user_level = max_user_level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAgents(int agents) {
        this.agents = agents;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public int getPokemons() {
        return pokemons;
    }

    public int getMoves() {
        return moves;
    }

    public int getGrade() {
        return grade;
    }

    public int getGame_level() {
        return game_level;
    }

    public int getMax_user_level() {
        return max_user_level;
    }

    public int getId() {
        return id;
    }

    public int getAgents() {
        return agents;
    }

    public boolean isIs_logged_in() {
        return is_logged_in;
    }

    public directed_weighted_graph getGraph() {
        return graph;
    }


    public void setGraphfile(String graphfile) {
        this.graphfile = graphfile;
        loadGraph(graphfile);
    }

    private void loadGraph(String graph_file) {
        dw_graph_algorithms gAlgo=new DWGraph_Algo();
        gAlgo.load(graph_file);
        this.graph=gAlgo.getGraph();
    }


}
