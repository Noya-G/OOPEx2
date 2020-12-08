package api;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    directed_weighted_graph create_graph(int seed1, int seed2, int edges, int nodes, int seed_w){
        Random r1= new Random(seed1);
        Random r2=new Random(seed2);
        Random r3=new Random(seed_w);
        directed_weighted_graph graph =new DWGraph_DS();
        int key=0;
        //create the node and add them to the graph:
        for(int i=0; i<nodes; i++){
            node_data n=new NodeG(key++);
            graph.addNode(n);
        }
        //connecting nodes;
        while (graph.edgeSize()<edges){
            int es=graph.edgeSize();
            node_data rn1=graph.getNode(r1.nextInt(key));
            int n1=rn1.getKey();
            while (rn1==null){
                rn1=graph.getNode(r1.nextInt(key+1));
            }
            node_data rn2=graph.getNode(r1.nextInt(key));
            int n2=rn2.getKey();
            while (rn2==null){
                rn2=graph.getNode(r1.nextInt(key));
            }
            Double w=(double)r3.nextInt(10);
            graph.connect(rn1.getKey(),rn2.getKey(),w);

        }
        return  graph;
    }

    @Test
    void copy() {
        //test0:
        directed_weighted_graph g = create_graph(0,0,1,2,0);
        DWGraph_Algo gc=new DWGraph_Algo();
        gc.init(g);
        edge_data e= gc.copy().getEdge(0,1);
        assertEquals(8.0,e.getWeight());

        //test1:
        directed_weighted_graph g1 = create_graph(0,0,4,5,0);
        printGrapgEdges(g1);
        gc.init(g1);
        e= gc.copy().getEdge(0,3);
        assertEquals(9.0,e.getWeight());
        e= gc.copy().getEdge(4,2);
        assertEquals(8.0,e.getWeight());
        directed_weighted_graph gc_= gc.copy();
        System.out.println(gc_.edgeSize());
        printGrapgEdges(gc_);
        e= gc.copy().getEdge(3,2);
        assertEquals(1.0,e.getWeight());
        e= gc.copy().getEdge(0,4);
        assertEquals(1.0,e.getWeight());

    }

    @Test
    void isConnected() {
    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

    public void printGrapgEdges(directed_weighted_graph g){
        System.out.println("======================================");
        for(int i=0; i<g.nodeSize(); i++){
            ArrayList<edge_data> eg=(ArrayList<edge_data>) g.getE(i);
            System.out.println("---------"+i+" number of edge connecte it:"+g.getE(i).size());
            for (int j=0; j<eg.size(); j++){
                System.out.println("src: "+eg.get(j).getSrc()+", dest: "+eg.get(j).getDest()+", weighted: "+eg.get(j).getWeight());
            }
        }
    }
}