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

    public SmurtMoves(CL_Agent agent, List<CL_Pokemon> pokemons, Arena _ar, game_service game){
        this.agent=agent;
        System.out.println("Smurth moves thread: "+Thread.currentThread().getName());
        this.pokemons=pokemons;
        this._ar=_ar;
        this.pocTarget=null;
        this.gameGraph=_ar.getGraph();
        this.game=game;
        this.pathToCatchPoc=new ArrayList<>();

    }

    public synchronized void findTarget(){

//        System.out.println("while thread: "+Thread.currentThread().getName());
        // int firstSrc=agent.getSrcNode();
        if(agent.getNextNode()==-1){
            int nodeKey=agent.getSrcNode();
            nodeKey=nextNode(gameGraph,agent.getSrcNode());
            game.chooseNextEdge(agent.getID(),nodeKey);
        }

        int secondSrc=agent.getSrcNode();

        int almostDest=pokemons.get(0).get_edge().getSrc();
        int pes=pokemons.get(0).get_edge().getSrc();
        int ped=pokemons.get(0).get_edge().getDest();
//        System.out.println("pok pos edge src X: "+gameGraph.getNode(pes).getLocation().x()+"pok pos edge src Y:"
//                +gameGraph.getNode(pes).getLocation().y()+"pok pos edge dest X:"+gameGraph.getNode(ped).getLocation().x()+
//                "pok pos edge dest Y: "+gameGraph.getNode(ped).getLocation().y());
        //int finalDest=pokemons.get(0).get_edge().getDest();
        //int path=Integer.MAX_VALUE;
        dw_graph_algorithms gAlgo=new DWGraph_Algo();
        gAlgo.init(gameGraph);
        int path=gAlgo.shortestPath(secondSrc,almostDest).size();
//        System.out.println("path size: "+path);
        pathToCatchPoc=gAlgo.shortestPath(secondSrc,almostDest);
        for (int i=1; i<pokemons.size(); i++){
            almostDest=pokemons.get(i).get_edge().getSrc();
            List<node_data> tempList=gAlgo.shortestPath(secondSrc,almostDest);
            if(tempList.size()<path) {
                pocTarget = pokemons.get(i);
                pathToCatchPoc=gAlgo.shortestPath(secondSrc,almostDest);;
            }
        }
    }


    @Override
    public void run() {
        if(agent.getNextNode()==-1) {
            int d = nextNode(gameGraph, agent.getSrcNode());
            System.out.println("d: "+d);
            game.chooseNextEdge(agent.getID(), d);
            System.out.println("Agent: "+agent.getID()+", val: "+agent.getValue()+"   turned to node: "+agent.getNextNode());
        }
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gameGraph);
        _ar.setAgents(log);
        findTarget();
//        System.out.println("while thread: "+Thread.currentThread().getName());
//        System.out.println("pathToCatchPoc: "+pathToCatchPoc.size());
        for (int i=0; i<pathToCatchPoc.size(); i++){

//            for(int j=0; j<log.size(); j++){
//                if(log.get(j).getID()==agent.getID()){
//                    agent=log.get(j);
//                }
//            }
            System.out.println("Src: "+agent.getSrcNode()+", Dest: "+agent.getNextNode()+" path next node: "+pathToCatchPoc.get(i).getKey());
            game.chooseNextEdge(agent.getID(),pathToCatchPoc.get(i).getKey());
            game.move();
            lg = game.move();
            log = Arena.getAgents(lg, gameGraph);
            _ar.setAgents(log);
        }
    }

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


}
