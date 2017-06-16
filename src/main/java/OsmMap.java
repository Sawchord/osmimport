
import java.util.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.function.Function;


public class OsmMap implements Iterable<OsmWay> {
    private Map<Long, OsmWay> wayMap;
    private Map<Long, OsmNode> nodeMap;

    OsmMap() {
        this.wayMap = new HashMap<>();
        this.nodeMap = new HashMap<>();
    }

    public OsmWayIterator iterator() {
        return new OsmWayIterator(wayMap);
    }


    public void addWay(OsmWay way) {
        this.wayMap.put(way.getId(), way);
        // Add all the nodes into the map to be easily accesible
        for (OsmNode n: way.getNodes()) {
            this.nodeMap.put(n.getId(), n);
        }
    }

    public OsmMap filter(Function<Map<String, String>, Boolean> filter) {
        OsmMap new_map = new OsmMap();
        OsmWayIterator it = this.iterator();
        it.SetFilter(filter);

        while (it.hasNext()) {
            new_map.addWay(it.next());
        }

        return new_map;
    }

    public OsmWay getWay(long id) {
        return this.wayMap.get(id);
    }

    public OsmNode getNode(long id) {
        return this.nodeMap.get(id);
    }

    public Set<String> getWayAttributeSet(String key) {
        Set<String> set = new HashSet<>();

        for (OsmWay w: this) {
            String att = w.getAttribute(key);
            if (att != null) {
                set.add(att);
            }
        }

        return set;
    }

    // TODO:  Make this function obsolete and then remove it
    public Map<Long, OsmWay> getWays() {
        return this.wayMap;
    }

    public static OsmMap importXml(String path) {
        return importXml(path, a -> true);
    }

    public static OsmMap importXml(String path, Function<Map<String, String>, Boolean> filter) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser parser = factory.newSAXParser();
            OsmXmlHandler handler = new OsmXmlHandler();

            handler.Init(filter);
            handler.setPass(0);
            parser.parse(path, handler);
            handler.setPass(1);
            parser.parse(path, handler);
            OsmMap m = handler.getMap();
            handler.Finalize();
            return m;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


