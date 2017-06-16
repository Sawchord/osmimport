
import java.util.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.function.Function;


public class OsmNodeMap {
    private Map<Long, OsmWay> map;

    OsmNodeMap() {
        this.map = new HashMap<Long, OsmWay>();
    }

    public void addWay(OsmWay way) {
        this.map.put(way.getId(), way);
    }

    public OsmWay getWay(long id) {
        return this.map.get(id);
    }
    public Map<Long, OsmWay> getWays() { return this.map; }

    public static OsmNodeMap importXml(String path) {
        return importXml(path, a -> true);
    }
    public static OsmNodeMap importXml(String path, Function<Map<String, String>, Boolean> filter) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser parser = factory.newSAXParser();
            OsmXmlHandler handler = new OsmXmlHandler();

            handler.Init(filter);
            handler.setPass(0);
            parser.parse(path, handler);
            handler.setPass(1);
            parser.parse(path, handler);
            OsmNodeMap m = handler.getMap();
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


