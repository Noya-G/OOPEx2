package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.*;


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
        opsitGraph(graph);

        boolean or = isConnectedAid(copy()); //origin connection.
        if(or==false) return false;
        boolean op = isConnectedAid(opsit_graph);//opsit connection.
        if(op==true) return true; //if 'or' && 'op' both true return true.
        return false; //else return false.
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return shorterPathAid0(src,dest,copy());
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return shorterPathAid(src,dest, copy());
    }

    @Override
    public boolean save(String file) {

        return true;
    }

    @Override
    public boolean load(String file) {

        return true;
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
        ArrayList<node_data> container=new ArrayList<node_data>();
        ArrayList<node_data> g_nodes= (ArrayList<node_data>)g.getV();
        container.add(g_nodes.get(0));
        g_nodes.get(0).setTag(0);

        int pointer=0;

        while (pointer<container.size()){
            node_data node_pointer=container.get(pointer);
            int src=node_pointer.getKey();
            ArrayList<edge_data> nodeEdges= (ArrayList<edge_data>) g.getE(src);


            for (int i=0; i<nodeEdges.size(); i++){
                edge_data edgePointer=nodeEdges.get(i);
                int dest=edgePointer.getDest();
                double weight=edgePointer.getWeight();
                node_data neighborPointer=g.getNode(dest);
                if(neighborPointer.getTag()==Integer.MAX_VALUE){
                    container.add(node_pointer);
                    neighborPointer.setTag(0);
                }
            }
            node_pointer.setTag(1);
            pointer++;
        }

        if(container.size()<g.nodeSize()){
            return false;
        }

        return true;
    }

    private ArrayList<node_data> shorterPathAid(int src, int dest, directed_weighted_graph g){
        ArrayList<node_data> ans=new ArrayList<>();
        HashMap<Integer,Double> paths=new HashMap<>();
        ArrayList<node_data> container=new ArrayList<node_data>();

        ArrayList<node_data> g_nodes= (ArrayList<node_data>)g.getV();
        paths.put(src,0.0);
        g.getNode(src).setTag(0);
        container.add(g.getNode(src));
        int pointer=0;

        while (pointer<container.size()){
            node_data node_pointer=container.get(pointer);
            int src_node=node_pointer.getKey();
            ArrayList<edge_data> nodeEdges= (ArrayList<edge_data>) g.getE(src_node);
            double path_node= paths.get(src_node);

            for (int i=0; i<nodeEdges.size(); i++){
                edge_data edgePointer=nodeEdges.get(i);
                int dest_node=edgePointer.getDest(); //point to the node with that key.
                double weight=edgePointer.getWeight();
                node_data neighborPointer=g.getNode(dest_node);

                if(neighborPointer.getTag()==0){
                    double p=path_node+weight; //new path of the node.
                    if(p< paths.get(dest_node)){
                        paths.remove(dest_node);
                        paths.put(dest_node,p);
                    }
                }

                if(neighborPointer.getTag()==Integer.MAX_VALUE){
                    container.add(g.getNode(dest_node));
                    neighborPointer.setTag(0);
                    double p=path_node+weight; //new path of the node.
                    paths.put(dest_node,p);
                }
            }
            node_pointer.setTag(1);
            pointer++;
        }

        if(paths.containsKey(dest)==false){
            return ans;
        }

        node_data node_pointer =g.getNode(dest);
        while(paths.containsKey(src)==false){
           int nKey= node_pointer.getKey();
           ArrayList<edge_data> node_edges=(ArrayList<edge_data>)g.getE(nKey);
          double pathOfNodePointer=paths.get(nKey);

           for (int i=0; i<node_edges.size(); i++){
               int nodeFrom =node_edges.get(i).getSrc();
               double edge_weight=node_edges.get(i).getWeight();
               double nodePath=paths.get(nodeFrom);
               if((edge_weight+nodePath)==pathOfNodePointer) {
                   ans.add(g.getNode(nodeFrom));
                   src=nodeFrom;
                   i=Integer.MAX_VALUE;
               }
           }
        }
        ArrayList<node_data> finalAns=new ArrayList<>();
        for (int i=ans.size()-1; i<-1; i--){
            finalAns.add(ans.get(i));
        }
        return ans;
    }

    private double shorterPathAid0(int src, int dest, directed_weighted_graph g){
        ArrayList<node_data> ans=new ArrayList<>();
        HashMap<Integer,Double> paths=new HashMap<>();
        ArrayList<node_data> container=new ArrayList<node_data>();

        ArrayList<node_data> g_nodes= (ArrayList<node_data>)g.getV();
        paths.put(src,0.0);
        g.getNode(src).setTag(0);
        container.add(g.getNode(src));
        int pointer=0;

        while (pointer<container.size()){
            node_data node_pointer=container.get(pointer);
            int src_node=node_pointer.getKey();
            ArrayList<edge_data> nodeEdges= (ArrayList<edge_data>) g.getE(src_node);
            double path_node= paths.get(src_node);

            for (int i=0; i<nodeEdges.size(); i++){
                edge_data edgePointer=nodeEdges.get(i);
                int dest_node=edgePointer.getDest(); //point to the node with that key.
                double weight=edgePointer.getWeight();
                node_data neighborPointer=g.getNode(dest_node);

                if(neighborPointer.getTag()==0){
                    double p=path_node+weight; //new path of the node.
                    if(p< paths.get(dest_node)){
                        paths.remove(dest_node);
                        paths.put(dest_node,p);
                    }
                }

                if(neighborPointer.getTag()==Integer.MAX_VALUE){
                    container.add(g.getNode(dest_node));
                    neighborPointer.setTag(0);
                    double p=path_node+weight; //new path of the node.
                    paths.put(dest_node,p);
                }
            }
            node_pointer.setTag(1);
            pointer++;
        }

        if(paths.containsKey(dest)==false){
            return -1.0;
        }

        return paths.get(dest);
    }
}

