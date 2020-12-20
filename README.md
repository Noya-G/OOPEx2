# OOP_Ex2

this code made as assignment at object oriented programming class.

by running this code you can create direced weighted graph(to read more about weighted graph: https://en.wikipedia.org/wiki/Graph_(discrete_mathematics))

this code support write the graph to text file and read it from Gson file. you also can get the shorter path from one node to anther, check if two nodes are connected and check if the graph is conncted.

the graph algoritims is used to get the best score in the pokemons game.

**_node used to represent vertex in a graph, here the methode that the class can perform: (NodeG class)_**

    /**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	 
public int getKey();
	
	/** Returns the location of this node, if
	 * none return null.
	 * 
	 * @return
	 */
	 
public geo_location getLocation();

	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	 
public void setLocation(geo_location p);

	/**
	 * Returns the weight associated with this node.
	 * @return
	 */
	 
public double getWeight();

	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	 
public void setWeight(double w);

	/**
	 * Returns the remark (meta data) associated with this node.
	 * @return
	 */

public String getInfo();

	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s
	 */

public void setInfo(String s);

	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */

public int getTag();

	/** 
	 * Allows setting the "tag" value for temporal marking an node - common
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	 
public void setTag(int t);

**_in this code here what you can do on the edges that are used in the graph: (Edge Class in the DWGraph_DS class)_**
    
    /**
	 * The id of the source node of this edge.
	 * @return
	 */
	 
public int getSrc();

	/**
	 * The id of the destination node of this edge
	 * @return
	 */

public int getDest();

	/**
	 * @return the weight of this edge (positive value).
	 */
	 
public double getWeight();
	
	/**
	 * Returns the remark (meta data) associated with this edge.
	 * @return
	 */
public String getInfo();
	
	/**
	 * Allows changing the remark (meta data) associated with this edge.
	 * @param s
	 */
public void setInfo(String s);
	
	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
public int getTag();
	
	/** 
	 * This method allows setting the "tag" value for temporal marking an edge - common
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
public void setTag(int t);

**_here what you can do in the graph: (DWGraph_DS class)_**

    /**
	 * returns the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
public node_data getNode(int key);

	/**
	 * returns the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return
	 */
public edge_data getEdge(int src, int dest);

	/**
	 * adds a new node to the graph with the given node_data.
	 * Note: this method should run in O(1) time.
	 * @param n
	 */
public void addNode(node_data n);

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
public void connect(int src, int dest, double w);

	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
public Collection<node_data> getV();

	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * Note: this method should run in O(k) time, k being the collection size.
	 * @return Collection<edge_data>
	 */
public Collection<edge_data> getE(int node_id);

	/**
	 * Deletes the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(k), V.degree=k, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */
public node_data removeNode(int key);

	/**
	 * Deletes the edge from the graph,
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
public edge_data removeEdge(int src, int dest);

	/** Returns the number of vertices (nodes) in the graph.
	 * Note: this method should run in O(1) time. 
	 * @return
	 */
public int nodeSize();

	/** 
	 * Returns the number of edges (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return
	 */
public int edgeSize();

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
public int getMC();

**_here it what you can do on the graph: (DWGraph_Algo class)_**
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
   public void init(directed_weighted_graph g);

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
   public directed_weighted_graph getGraph();
   
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
   public directed_weighted_graph copy();
   
    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return
     */
   public boolean isConnected();
   
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
   public double shortestPathDist(int src, int dest);
   
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
   public List<node_data> shortestPath(int src, int dest);

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
   public boolean save(String file);

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
   public boolean load(String file);
   
   
**_here wat you can do in the GeoLocation class:_**

    /**
     * This interface represents a geo location <x,y,z>, aka Point3D
     */
public double distance(geo_location g);



