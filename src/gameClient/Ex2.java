package gameClient;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.game_service;
import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.GsonBuilder;
import com.google.gson.*;

import javax.swing.*;
import javax.swing.plaf.synth.SynthTableHeaderUI;
import javax.tools.Tool;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static int scenario_num,id;

    public static void main(String[] a) {
        Scanner input= new Scanner(System.in);
        System.out.print("Enter number ID and scenario number: ");
        String its=input.nextLine();
        int[] arr=new int[2];
        try {
            int i=0;
            for (String n : its.split(" ")) {
                arr[i] = Integer.parseInt(n);
                i++;
            }
        }catch (Exception e){
            System.out.print("invalid input!");
            System.exit(0);
        }
        id=arr[0];
        scenario_num=arr[1];
        Thread client = new Thread(new Ex2());
        client.start();
        client.setName("client thread");

    }

    @Override
    public void run() {
        //int scenario_num = 23;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //int id = 312321722;
        game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = jsonAdptorGraph(g);
        init(game);


        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;
        _win.setTitle("level number: "+scenario_num);
        long time=game.timeToEnd();

//        Thread mangerTreads=new Thread();
//        mangerTreads.setName("manager");
//        try {
//            mangerTreads.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%1==0) {_win.repaint(); _ar.setTime(game.timeToEnd());}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();

//		System.out.println("movse:"+game.move());
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        setPokEdges(ffs,gg);

//		System.out.println("pok: "+ffs.get(0).get_edge() );
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            Thread thread=new Thread( new SmurtMoves(ag,ffs,_ar,game));
            String name=""+i;
            thread.setName(name);
            thread.run();
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if(dest==-1) {
                dest = nextNode(gg, src);
                game.chooseNextEdge(ag.getID(), dest);
//                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
            }
        }
    }
    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size(); //
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
//            System.out.println(info);
//            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }

                game.addAgent(nn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void directedMoves(CL_Agent agent, CL_Pokemon poc, game_service game) {
        int agentId = agent.getID();
        int src = agent.get_curr_edge().getSrc();
        int dest = poc.get_edge().getDest();
        dw_graph_algorithms gAlgo = new DWGraph_Algo();
        gAlgo.init(_ar.getGraph());
        ArrayList<node_data> sortPathToPoc = (ArrayList<node_data>) gAlgo.shortestPath(src, dest);
        int i = 0;
        while (agent.get_curr_edge() != poc.get_edge()) {
            try {
                game.chooseNextEdge(agentId, sortPathToPoc.get(i).getKey());
            } catch (NullPointerException e) {
                return;
            }
        }
    }


    private static ArrayList<Object> chooseAgentToMove(List<CL_Agent> agents, List<CL_Pokemon> pokemons) {
        ArrayList<Object> ans = new ArrayList<>();
        CL_Pokemon destPoc = pokemons.get(0);
        CL_Agent srcAgent = agents.get(0);
        directed_weighted_graph gameGraphPointer = _ar.getGraph();
        dw_graph_algorithms gAlgo = new DWGraph_Algo();
        gAlgo.init(_ar.getGraph());
        ArrayList<node_data> SP = giveShorterPath(srcAgent, destPoc);

        for (int i = 0; i < agents.size(); i++) {
            CL_Agent agentPointer = agents.get(i);
            for (int j = 0; j < pokemons.size(); j++) {
                CL_Pokemon pokemonPointer = pokemons.get(j);
                if (SP == null) {
                    SP = giveShorterPath(srcAgent, destPoc);
                } else {
                    ArrayList<node_data> SP2 = giveShorterPath(agentPointer, pokemonPointer);
                    if (SP2 != null) {
                        if (SP.size() > SP2.size()) {
                            ans.clear();
                            SP = giveShorterPath(agentPointer, pokemonPointer);
                            ans.add(agentPointer);
                            ans.add(pokemonPointer);
                            ans.add(SP);
                        }
                    }
                }
            }
        }
        return ans;
    }


    /**
     * return shorter path.
     * return null if the is no path.
     * return null if the shorter Path didnt work
     *
     * @param agent
     * @param pokemon
     * @return
     */
    public static ArrayList<node_data> giveShorterPath(CL_Agent agent, CL_Pokemon pokemon) {
        int srcAgent = agent.get_curr_edge().getDest();
        int srcPokemon = pokemon.get_edge().getSrc();
        int destPokemon = pokemon.get_edge().getDest();

        DWGraph_Algo gAlgo = new DWGraph_Algo();
        gAlgo.init(_ar.getGraph());

        if (gAlgo.shortestPath(srcAgent, srcPokemon) != null) {
            if (gAlgo.shortestPath(srcPokemon, destPokemon) != null) {
                if (gAlgo.shortestPath(srcAgent, srcPokemon).size() == 1) {
                    return (ArrayList<node_data>) gAlgo.shortestPath(srcAgent, srcPokemon);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private edge_data pocEdge(CL_Pokemon pok) {
        edge_data ans = null;
        double geoX = pok.getLocation().x();
        double geoY = pok.getLocation().y();

        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                int srcN = e.getSrc();
                int destN = e.getDest();
                double srcX = gg.getNode(srcN).getLocation().x();
                double srcY = gg.getNode(srcN).getLocation().y();
                double destX = gg.getNode(destN).getLocation().x();
                double destY = gg.getNode(destN).getLocation().y();
                double m = (srcY - destY) / (srcX - destX);
                double line = srcY - (srcX * m);
                double c=srcY-(m*srcX);
                double fi = geoY - (m * geoX);
                double[] arr= new double[2];
                arr[0]=geoX;
                arr[1]=geoY;
                onTheLine(m,c,arr);
                if(onTheLine(m,c,arr)){
                    return e;
                }
            }
        }
        return ans;
    }

    private directed_weighted_graph jsonAdptorGraph(String s) {
        directed_weighted_graph graph = new DWGraph_DS();
        try {
            JSONObject obj = new JSONObject(s);
            JSONArray graph_nodes = obj.getJSONArray("Nodes");
            JSONArray graph_edges = obj.getJSONArray("Edges");

            //create all the nodes and add them to the graph accordingly the Gson file:
            for (int i = 0; i < graph_nodes.length(); i++) {
                JSONObject pp = graph_nodes.getJSONObject(i);
                int src = pp.getInt("id");
                NodeG n = new NodeG(src);
                String p = pp.getString("pos");
                double[] geoArr = new double[3];
                int j = 0;
                for (String geo : p.split(",")) {
                    geoArr[j] = Double.parseDouble(geo);
                    j++;
                }
                geo_location geo = new GeoLocation(geoArr[0], geoArr[1], geoArr[2]);
                n.setLocation(geo);
                graph.addNode(n);
            }

            //create the edges and add them to the graph accordingly the Gson file:
            for (int i=0; i<graph_edges.length(); i++){
                int src=graph_edges.getJSONObject(i).getInt("src");
                int dest=graph_edges.getJSONObject(i).getInt("dest");
                double w=graph_edges.getJSONObject(i).getDouble("w");
                graph.connect(src,dest,w);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
            return graph;
    }

    private boolean onTheLine(double m, double c, double[] point){
        double x0=point[0];
        double y0=point[1];
       if(Math.round(y0)==Math.round((m*x0)+c)){
            return true;
       }
        return false;
    }

    private void upDate(game_service game, directed_weighted_graph graph){
        String lg = game.move();

        List<CL_Agent> log = Arena.getAgents(lg, graph);
        _ar.setAgents(log);

        String fs =  game.getPokemons();
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);
        _ar.setPokemons(pokemons);

        for (int i = 0; i < pokemons.size(); i++) {
            pokemons.get(i).set_edge(graph);
        }
    }

    private static void setPokEdges(List<CL_Pokemon> pokemons, directed_weighted_graph graph){
        for(int i=0; i<pokemons.size(); i++ ){
            CL_Pokemon pointer=pokemons.get(i);
            pointer.set_edge(graph);
        }
    }

}
