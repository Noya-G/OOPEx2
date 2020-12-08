package api;

import java.util.ArrayList;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms{
    directed_weighted_graph graph;
    directed_weighted_graph opsit_graph;

    public DWGraph_Algo(){
        this.graph=new DWGraph_DS();
    }

    @Override
    public void init(directed_weighted_graph g) {
        graph=g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph g_c=new DWGraph_DS();

        //copy all the nodes:
        ArrayList<node_data> g_n= (ArrayList<node_data>)graph.getV();
        for(int i=0; i<graph.nodeSize(); i++){
            node_data original_node= g_n.get(i);//node key
            node_data n= new NodeG(original_node.getKey());
            n.setInfo(original_node.getInfo());
            n.setLocation(original_node.getLocation());
            n.setWeight(original_node.getWeight());
            //n.setTag(original_node.getTag());
            g_c.addNode(n);
            }

        //copy all the edges;
        for (int i=0; i< graph.nodeSize(); i++){
            node_data n_pointer = g_n.get(i); //original node pointer.
            int src=n_pointer.getKey();
            ArrayList<edge_data> n_e=(ArrayList<edge_data>) graph.getE(src);
            for (int j=0; j<n_e.size(); j++){
                int dest=n_e.get(j).getDest();
                double weight=n_e.get(j).getWeight();
                g_c.connect(src,dest,weight);
            }
        }

        return g_c;
    }

    @Override
    public boolean isConnected() {
        int nodeSize=graph.nodeSize();
        int edgeSize=graph.edgeSize();
        ArrayList<node_data> nodeArray=(ArrayList<node_data>)graph.getV();
        //copy
        opsitGraph(graph);
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }

    /////////////////////////////////////////////////////////////
    ///////////////////private method///////////////////////////
    ///////////////////////////////////////////////////////////

    private void opsitGraph(directed_weighted_graph graph) {
        opsit_graph=new DWGraph_DS();

        //copy all the nodes:
        ArrayList<node_data> g_n= (ArrayList<node_data>)graph.getV();
        for(int i=0; i<graph.nodeSize(); i++){
            node_data original_node= g_n.get(i);//node key
            node_data n= new NodeG(original_node.getKey());
            n.setInfo(original_node.getInfo());
            n.setLocation(original_node.getLocation());
            n.setWeight(original_node.getWeight());
            n.setTag(original_node.getTag());
            opsit_graph.addNode(n);
        }

        //copy all the edges;
        for (int i=0; i< graph.nodeSize(); i++){
            node_data n_pointer = g_n.get(i); //original node pointer.
            int src=n_pointer.getKey();
            ArrayList<edge_data> n_e=(ArrayList<edge_data>) graph.getE(src);
            for (int j=0; j<n_e.size(); j++){
                int dest=n_e.get(j).getDest();
                double weight=n_e.get(j).getWeight();
                opsit_graph.connect(dest,src,weight);
            }
        }
    }

}
