package api;

import java.util.*;


public class DWGraph_DS implements directed_weighted_graph, Iterator {
    HashMap<Integer,node_data> graph_keys_nodes;
    HashMap<Integer,HashMap<Integer,Edge>> graph_edges;
    ArrayList<edge_data> graph_Edges_collection;
    HashMap<Integer, ArrayList<edge_data>> graph_e;
    ArrayList<node_data> graph_n;
    int mc;
    NodeG pointer;
    int i=0;

    public DWGraph_DS(){
        this.graph_keys_nodes=new HashMap<>();
        this.graph_edges=new HashMap<>();
        this.graph_Edges_collection=new ArrayList<>();
        this.mc=0;
        this.graph_e=new HashMap<>();
        this.graph_n=new ArrayList<>();
    }

    @Override
    public node_data getNode(int key) {
        return graph_keys_nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(graph_edges.containsKey(src)==true && graph_edges.get(src).containsKey(dest)){
            Edge edge_pointer =graph_edges.get(src).get(dest);
            return edge_pointer;
        }
        return null;
    }

    @Override
    public void addNode(node_data n) {
        if(graph_keys_nodes.containsKey(n.getKey())==true){
            return;
        }
        mc++;
        graph_keys_nodes.put(n.getKey(),n);
        graph_edges.put(n.getKey(),new HashMap<>());
        graph_e.put(n.getKey(),new ArrayList<>());
        graph_n.add(n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(src==dest){
            return;
        }
        //if the two nodes are exist in the graph, else return.
        if(graph_keys_nodes.containsKey(src) && graph_keys_nodes.containsKey(dest)){
            //if the the two nodes are already connected:
            if(graph_edges.get(src).containsKey(dest)){
                edge_data edge_pointer= graph_edges.get(src).get(dest);
                graph_Edges_collection.remove(edge_pointer);
                graph_e.get(src).remove(graph_edges.get(src).get(dest));
                graph_edges.get(src).remove(dest);
            }
            mc++;
            Edge edge= new Edge(src,dest,w);
            graph_edges.get(src).put(dest,edge);
            graph_Edges_collection.add(edge);
            graph_e.get(src).add(edge);
        }
    }

    @Override
    public Collection<node_data> getV() {
        return graph_n;
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return graph_e.get(node_id);
    }

    @Override
    public node_data removeNode(int key) {
        //if the node is not exist:
        if(graph_keys_nodes.containsKey(key)==false){
            return null;
        }
        mc++;
        //else:
        node_data node_pointer = graph_keys_nodes.get(key);
        //delete all the edges which are connected to the node:
        for(int i=0; i<graph_edges.size(); i++){
            Edge edge = (Edge)graph_Edges_collection.get(i);
            //if the edge is point to the node we want to delete:
            if(edge.getDest()==key){
                removeEdge(edge.getSrc(),key);
            }
        }
        return node_pointer;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(graph_edges.containsKey(src)==true && graph_edges.containsKey(dest)){
            Edge edge = graph_edges.get(src).get(dest);
            //if the edge is point to the node we want to delete:
            graph_edges.remove(edge);
            graph_edges.get(src).remove(dest);
            mc++;
            return edge;
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return graph_keys_nodes.size();
    }

    @Override
    public int edgeSize() {
        return graph_Edges_collection.size();
    }

    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public boolean hasNext() {
        int i=0;
        if(graph_Edges_collection.get(i++)!=null){
            return true;
        }
        return false;
    }

    @Override
    public node_data next() {
        return null;
    }


    ////////////////////////////////////////////////////////////////////////
    /////////////////////////////Private Objects////////////////////////////
    ////////////////////////////////////////////////////////////////////////

    protected class Edge implements edge_data{
        private int src, dest, tag;
        private double weight;
        private String info;

        public Edge(int src, int dest, double weight){
            this.src=src;
            this.dest=dest;
            this.weight=weight;
            this.info=null;
            this.tag=Integer.MAX_VALUE;
        }

        @Override
        public int getSrc() {
            return src;
        }

        @Override
        public int getDest() {
            return dest;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public void setInfo(String s) {
            info=s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            tag=t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return src == edge.src &&
                    dest == edge.dest &&
                    tag == edge.tag &&
                    Double.compare(edge.weight, weight) == 0 &&
                    info.equals(edge.info);
        }

        @Override
        public int hashCode() {
            return Objects.hash(src, dest, tag, weight, info);
        }
    }

}
