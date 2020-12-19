package gameClient;

import java.util.ArrayList;
import api.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SmurtMoves implements Runnable{
    private CL_Agent agent;
    private List<CL_Pokemon> pokemons;
    private CL_Pokemon pocTarget;
    private Arena _ar;
    private directed_weighted_graph gameGraph;
    private List<node_data> pathToCatchPoc;
    private game_service game;
    CL_Pokemon pokT;//**DEBUG**

    public SmurtMoves(CL_Agent agent, List<CL_Pokemon> pokemons, Arena _ar, game_service game){
        this.agent=agent;
        System.out.println("Smurth moves thread: "+Thread.currentThread().getName());
        this.pokemons=pokemons;
        this._ar=_ar;
        this.pocTarget=null;
        this.gameGraph=_ar.getGraph();
        this.game=game;
        this.pathToCatchPoc=new ArrayList<>();
        this.pokT=pokemons.get(0);//**DEBUG**

    }

    public synchronized void findTarget(){
        DWGraph_Algo algo=new DWGraph_Algo();
        algo.init(gameGraph);
        int s=-1;
        if(agent.get_curr_edge()==null){
            s=agent.getSrcNode();
        }
        else {
            s=agent.getNextNode();
        }
        pokT=pokemons.get(0);
        pathToCatchPoc=algo.shortestPath(s,pokemons.get(0).get_edge().getSrc());
        double dest=algo.shortestPathDist(s,pokemons.get(0).get_edge().getSrc());
        pathToCatchPoc.add(gameGraph.getNode(pokT.get_edge().getDest()));
        for (int i=0; i<pokemons.size(); i++){
            double tempDest=algo.shortestPathDist(s,pokemons.get(i).get_edge().getSrc());
            if(tempDest<dest){
                dest=tempDest;
                pathToCatchPoc=algo.shortestPath(s,pokemons.get(i).get_edge().getSrc());
                System.out.println();
                pokT=pokemons.get(i);
                pathToCatchPoc.add(gameGraph.getNode(pokT.get_edge().getDest()));
                System.out.println("##########DEBUG#######");
            }
        }
        System.out.println("**DEBUG**$$$$$$  agent: "+agent.getSrcNode()+", pok edge src: "+pokT.get_edge().getSrc()+" ,pok edge dest:"+pokT.get_edge().getDest()+" **DEBUG**");
        System.out.println("path size:"+pathToCatchPoc.size()+", the path:");
        for(int i=0; i<pathToCatchPoc.size(); i++){ //**DEBUG**
            System.out.print(pathToCatchPoc.get(i).getKey()+", ");
        }
        System.out.println();
    }


    @Override
    public void run() {
        findTarget();
        for (int i=0;i<pokemons.size(); i++){
            pokemons.get(i).set_edge(gameGraph);
            boolean a=false;
            if(pokemons.get(i)==pokT){
                a=true;
            }
            System.out.println("0000000000000000000n poc id:"+i+", pok x(): "+pokemons.get(i).getLocation().x()+", poc y(): "+pokemons.get(i).getLocation().y()+", target: "+a);
        }

        if(agent.getNextNode()==-1) {
            int d = nextNode(gameGraph, agent.getSrcNode());
            System.out.println("d: "+d);
            game.chooseNextEdge(agent.getID(), d);
            System.out.println("Agent: "+agent.getID()+", val: "+agent.getValue()+"   turned to node: "+agent.getNextNode());
        }
        System.out.println("agent: "+agent.getSrcNode()+", pok edge src: "+pokT.get_edge().getSrc()+" ,pok edge dest:"+pokT.get_edge().getDest());
        System.out.println("path size:"+pathToCatchPoc.size()+", the path:");
        for(int i=0; i<pathToCatchPoc.size(); i++){ //**DEBUG**
            System.out.print(pathToCatchPoc.get(i).getKey()+", ");
        }
        System.out.println();
        for (int i=0; i<pathToCatchPoc.size(); i++){
            System.out.println("Src: "+agent.getSrcNode()+", Dest: "+agent.getNextNode()+" path next node: "+pathToCatchPoc.get(i).getKey());
            game.chooseNextEdge(agent.getID(),pathToCatchPoc.get(i).getKey());

        }
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gameGraph);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);
        for (int i=0;i<pokemons.size(); i++){
            pokemons.get(i).set_edge(gameGraph);
            System.out.println("0000000000000000000n poc id:"+i+", pok x(): "+pokemons.get(i).getLocation().x()+", poc y(): "+pokemons.get(i).getLocation().y());
        }
        _ar.setPokemons(pokemons);
        pokT=null;
    }

    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size(); //
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;
        ans = itr.next().getDest();}
        return ans;
    }

    public void join() {
    }
}
