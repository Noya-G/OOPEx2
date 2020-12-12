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
        DWGraph_Algo gAlgo = new DWGraph_Algo();

        //test0:
        directed_weighted_graph g0 = create_graph(0, 0, 1, 2, 0);
        gAlgo.init(g0);
        assertFalse(gAlgo.isConnected());

        //test1:
        directed_weighted_graph g1 = create_graph(0, 0, 4, 5, 0);
        gAlgo.init(g1);
        assertFalse(gAlgo.isConnected());

        directed_weighted_graph g2 = create_graph(0, 0, 2, 2, 0);
        gAlgo.init(g2);
        assertTrue(gAlgo.isConnected());

        //test3:
        directed_weighted_graph g3 = connectedGraph();
        assertTrue(gAlgo.isConnected());
    }

    @Test
    void shortestPathDist() {
       directed_weighted_graph g=connectedGraph2();
       DWGraph_Algo gAlgo=new DWGraph_Algo();
       gAlgo.init(g);
       assertEquals(2.0,gAlgo.shortestPathDist(0,1));
       assertEquals(3.0,gAlgo.shortestPathDist(0,2));
       assertEquals(3.0,gAlgo.shortestPathDist(0,5));
       assertEquals(3.0,gAlgo.shortestPathDist(3,5));
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
        directed_weighted_graph g3 = connectedGraph();
        DWGraph_Algo gAlgo=new DWGraph_Algo();
        gAlgo.init(g3);
        gAlgo.save("file2");
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

    private directed_weighted_graph connectedGraph(){
        DWGraph_DS g= new DWGraph_DS();
        for(int i=0; i<6; i++){
            NodeG n=new NodeG(i);
            g.addNode(n);
        }
        g.connect(0,1,0);
        g.connect(1,3,0);
        g.connect(3,5,0);
        g.connect(0,4,0);
        g.connect(5,0,0);
        g.connect(0,2,0);
        g.connect(2,5,0);
        g.connect(4,0,0);

        return g;
    }

    private directed_weighted_graph connectedGraph2(){
        DWGraph_DS g= new DWGraph_DS();
        for(int i=0; i<6; i++){
            NodeG n=new NodeG(i);
            g.addNode(n);
        }
        g.connect(0,1,2);
        g.connect(1,2,1);
        g.connect(2,5,1);
        g.connect(0,5,3);
        g.connect(0,3,1);
        g.connect(4,5,1);
        g.connect(4,2,3);
        g.connect(3,4,2);

        return g;
    }
}