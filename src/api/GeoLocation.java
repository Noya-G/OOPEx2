package api;

import java.util.Objects;

public class GeoLocation implements geo_location{
    private double x,y,z;

    public GeoLocation(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public GeoLocation (GeoLocation p){
        this.x=p.x;
        this.y=p.y;
        this.z=p.z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {
        double xd=Math.pow(x-g.x(),2);
        double yd=Math.pow(y-g.y(),2);
        double zd=Math.pow(z-g.z(),2);
        return Math.sqrt(xd+yd+zd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}