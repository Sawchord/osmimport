import java.util.*;

public class OsmNode {

    private long id;

    private double lon;
    private double lat;

    private Map<String,String> attributes;

    OsmNode(long id, double lon, double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;

        this.attributes = new HashMap<String, String>();
    }

    public void updatePos(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public long getId() {
        return this.id;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAtt(String key, String value) {
        this.attributes.put(key, value);
    }
}
