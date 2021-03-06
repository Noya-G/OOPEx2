package api;

import java.util.Objects;

public class NodeG implements node_data {
    int key, tag;
    String info;
    double weighted;
    geo_location p;

    public NodeG(int key) {
        this.key = key;
        this.tag= Integer.MAX_VALUE;
        this.p=new GeoLocation(0,0,0);
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return p;
    }

    @Override
    public void setLocation(geo_location p) {
        this.p=p;
    }

    @Override
    public double getWeight() {
        return weighted;
    }

    @Override
    public void setWeight(double w) {
        weighted=w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {

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
        NodeG nodeG = (NodeG) o;
        return key == nodeG.key &&
                tag == nodeG.tag &&
                Double.compare(nodeG.weighted, weighted) == 0 &&
                info.equals(nodeG.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, tag, info, weighted);
    }
}

