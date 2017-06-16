import java.util.*;

public class OsmWay {

    private long id;

    private List<OsmNode> nodes;
    private Map<String, String> attribute;

    OsmWay(long id) {
        this.id = id;
        this.nodes = new ArrayList<OsmNode>();
        this.attribute = new HashMap<String, String>();
    }

    public long getId(){ return this.id; }
    public List<OsmNode> getNodes() {
        return this.nodes;
    }

    public String getAttribute(String key) {
        return this.attribute.get(key);
    }
    public Map<String, String> getAttrs() { return this.attribute; }
    public void setAtt(String key, String value) {
        this.attribute.put(key, value);
    }

    void addNode(OsmNode node) {
        this.nodes.add(node);
    }
}