package api;

import api.DWGraph_DS;
import org.junit.jupiter.api.BeforeAll;

import api.*;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    /**
     * this function create graph to the tests.
     * @param seed1
     * @param seed2
     * @param edges
     * @param nodes
     * @param seed_w
     * @return
     */
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
       while (graph.edgeSize()!=edges){
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


    @org.junit.jupiter.api.Test
    void getNode() {
        directed_weighted_graph g1=create_graph(0,0,0,1,0);
        boolean t1=false;
        if(g1.getNode(0)!=null){
            t1=true;
        }
        assertTrue(t1);
        directed_weighted_graph g2=create_graph(0,0,0,10,0);
        boolean t2=false;
        for (int i=0; i<10; i++){
            if(g2.getNode(i)!=null){
                t2=true;
            }
            assertTrue(t2);
            t2=false;
        }
        directed_weighted_graph g3=create_graph(0,0,0,50,0);
        boolean t3=false;
        for (int i=0; i<50; i++){
            if(g3.getNode(i)!=null){
                t3=true;
            }
            assertTrue(t3);
            t3=false;
        }
    }

    @org.junit.jupiter.api.Test
    void getEdge() {
       // directed_weighted_graph g1=create_graph(0,0,0,1,0);
        directed_weighted_graph g2=create_graph(0,0,5,10,0);
        assertEquals(0.0,g2.getEdge(0,8).getWeight());
        assertEquals(8.0,g2.getEdge(9,7).getWeight());
        assertEquals(9.0,g2.getEdge(5,3).getWeight());
        assertEquals(5.0,g2.getEdge(9,4).getWeight());
    }


    @org.junit.jupiter.api.Test
    void getV() {
        directed_weighted_graph g2=create_graph(0,0,0,10,0);
        for (int i=0; i<10; i++){

        }
    }

    @org.junit.jupiter.api.Test
    void getE() {
    }

    @org.junit.jupiter.api.Test
    void removeNode() {
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
    }

    @org.junit.jupiter.api.Test
    void hasNext() {
    }

    @org.junit.jupiter.api.Test
    void next() {
    }
}