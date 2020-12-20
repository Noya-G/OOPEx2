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
    ArrayList<CL_Pokemon> alreadyTargeted;

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
        alreadyTargeted=new ArrayList<>();

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
//            if(pokemons.get(i).getType()<0){
//                pathToCatchPoc=algo.shortestPath(s,pokemons.get(i).get_edge().getSrc());
//                System.out.println();
//                pokT=pokemons.get(i);
//                pathToCatchPoc.add(gameGraph.getNode(pokT.get_edge().getDest()));
//                return;
//            }
            if(tempDest<dest){
                dest=tempDest;
                pathToCatchPoc=algo.shortestPath(s,pokemons.get(i).get_edge().getSrc());
                System.out.println();
                pokT=pokemons.get(i);
                pathToCatchPoc.add(gameGraph.getNode(pokT.get_edge().getDest()));
//                System.out.println("##########DEBUG#######");
            }
        }
//        System.out.println("**DEBUG**$$$$$$  agent: "+agent.getSrcNode()+", pok edge src: "+pokT.get_edge().getSrc()+" ,pok edge dest:"+pokT.get_edge().getDest()+" **DEBUG**");
//        System.out.println("path size:"+pathToCatchPoc.size()+", the path:");
//        for(int i=0; i<pathToCatchPoc.size(); i++){ //**DEBUG**
//            System.out.print(pathToCatchPoc.get(i).getKey()+", ");
//        }
//        System.out.println();
    }


    @Override
    public void run() {
        findTarget();
        for(int i=0; i<pathToCatchPoc.size(); i++){
            if(i<(pathToCatchPoc.size()-1)){
                agent.set_curr_fruit(pokT);
                System.out.println("pok value: "+pokT.getValue());
            }
            game.chooseNextEdge(agent.getID(),pathToCatchPoc.get(i).getKey());
            String lg = game.move();
            List<CL_Agent> log = Arena.getAgents(lg, gameGraph);
            _ar.setAgents(log);
            String fs =  game.getPokemons();
            List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
            _ar.setPokemons(ffs);
        }
    }

    public void join() {
    }

    public void addAlreadyTargeted(CL_Pokemon pok){
        alreadyTargeted.add(pok);
        if(pokT==pok){
            changeTarget();
        }
    }

    private void changeTarget() {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(gameGraph);
        int s = -1;
        if (agent.get_curr_edge() == null) {
            s = agent.getSrcNode();
        } else {
            s = agent.getNextNode();
        }
        pokT = pokemons.get(0);
        pathToCatchPoc = algo.shortestPath(s, pokemons.get(0).get_edge().getSrc());
        double dest = algo.shortestPathDist(s, pokemons.get(0).get_edge().getSrc());
        pathToCatchPoc.add(gameGraph.getNode(pokT.get_edge().getDest()));
        for (int i = 0; i < pokemons.size(); i++) {
            boolean targeted=false;
            for(int j=0; j<alreadyTargeted.size(); j++){
                if(pokemons.get(i)==alreadyTargeted.get(j)){
                    targeted=true;
                }
            }
            if(targeted==false){
                double tempDest = algo.shortestPathDist(s, pokemons.get(i).get_edge().getSrc());
                if (tempDest < dest) {
                    dest = tempDest;
                    pathToCatchPoc = algo.shortestPath(s, pokemons.get(i).get_edge().getSrc());
                    System.out.println();
                    pokT = pokemons.get(i);
                    pathToCatchPoc.add(gameGraph.getNode(pokT.get_edge().getDest()));
//                System.out.println("##########DEBUG#######");
                }
            }
        }
    }

    public CL_Pokemon getPokT() {
        return pokT;
    }

    public void notifyManager(){
        notify();
    }
}
