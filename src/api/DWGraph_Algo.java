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

        boolean or = isConnectedAid(copy()); //origin connection.
        if(or==false) return false;
        boolean op = isConnectedAid(opsit_graph);//opsit connection.
        if(or==true) return true; //if 'or' && 'op' both true return true.
        return false; //else return false.
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

    private boolean isConnectedAid(directed_weighted_graph g){
        ArrayList<node_data> nodeArray=(ArrayList<node_data>)g.getV();
        ArrayList<Integer> container=new ArrayList<>();
        int pointer=0;
        ArrayList<node_data> gn=(ArrayList<node_data>) g.getV();
        node_data node_pointer=gn.get(pointer);
        container.add(node_pointer.getKey());
        node_pointer.setTag(0);

        while(pointer<container.size()){
            int key=container.get(node_pointer.getKey());
            node_pointer=g.getNode(key);

            ArrayList<edge_data> node_edges=(ArrayList<edge_data>) g.getE(node_pointer.getKey());
            int neighborsSize=g.getE(node_pointer.getKey()).size();
            int i=0;

            //if we visit all the node neighbors:
            if(node_pointer.getTag()==1){
                i=Integer.MAX_VALUE;
                pointer++;
                continue;
            }


            //if we didnt visit the node at all:
            if(node_pointer.getTag()==Integer.MAX_VALUE){
                node_pointer.setTag(0);
            }

            for( i=0; i<neighborsSize; i++){
                int dest=node_edges.get(i).getDest();
                if(g.getNode(dest).getTag()==Integer.MAX_VALUE){
                    container.add(dest);
                }
            }
            pointer++;
        }

        if(container.size()<g.nodeSize()){
            return false;
        }

        return true;
    }

}

