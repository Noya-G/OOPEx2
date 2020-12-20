package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;
import api.*;

public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	double round=0.0000001;
	
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		_value = v;
//		set_edge(g,e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;


	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(directed_weighted_graph g) {
		geo_location geo=_pos;
		this._edge = findEdge(g,geo);
	}

	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}

	/**
	 * using triangle inequality, the algorithm search with BFS for an edge u,v such that dist(u,pos) + dist(pos,v) = dist(u,v)
	 * @param pos
	 * @return edge_data
	 */
	private edge_data findEdge(directed_weighted_graph g,geo_location pos) {
		for (node_data v : g.getV()) {
			int src = v.getKey();
			for (edge_data e : g.getE(v.getKey())) {
				int dest = e.getDest();
				node_data dest_node = g.getNode(dest);
				boolean a1=false;
				if(_type > 0){
					a1=true;
				}
				boolean a2=false;
				if(src < dest){
					a2=true;
				}
				if (a1 && a2) {
					geo_location src_loc = v.getLocation();
					geo_location dest_loc = dest_node.getLocation();
					double line = src_loc.distance(dest_loc);
					double line_through_pos = src_loc.distance(pos) + pos.distance(dest_loc);
					if (line > line_through_pos-round) {
						return e;
					}
				}
				boolean a3=false;
				if( _type < 0){
					a3=true;
				}
				boolean a4=false;
				if(src > dest){
					a4=true;
				}
				if(a3 && a4){
					geo_location src_loc = v.getLocation();
					geo_location dest_loc = dest_node.getLocation();
					double line = src_loc.distance(dest_loc);
					double line_through_pos = src_loc.distance(pos) + pos.distance(dest_loc);
					if (line > line_through_pos-round) {
						return e;
					}
				}
			}
		}
		return null;
	}



}
