
import java.util.*;
import java.util.function.Function;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class OsmXmlHandler extends DefaultHandler{

    private OsmMap map;


    // TODO: Use enum here
    // NOTE: 0 NONE 1 NODE 2 WAY
    private int state;
    private int pass;

    private Map<Long, List<OsmWay>> wantedNodes;

    private OsmNode currentNode;
    private OsmWay currentWay;

    private Function<OsmWay, Boolean> filter;

    public void setPass(int pass) {
        this.pass = pass;
    }

    public void Init(Function<OsmWay, Boolean> filter) {
        map = new OsmMap();
        wantedNodes = new HashMap<Long, List<OsmWay>>();
        this.filter = filter;
    }

    public void Finalize() {
        wantedNodes = null;
    }

    public OsmMap getMap() {
        return map;
    }

    @Override
    public void startDocument() throws SAXException {
        state = 0;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //System.out.println(qName);
        switch (qName) {
            case "node":
                if (pass != 1) return;

                if (state != 0) {
                    throw new SAXException("Unexpected statement in pass0 ");
                }

                long id = Long.parseLong(attributes.getValue("id"));
                double lon = Double.parseDouble(attributes.getValue("lon"));
                double lat = Double.parseDouble(attributes.getValue("lat"));

                currentNode = new OsmNode(id, lon, lat);
                state = 1;

                return;

            case "way":
                if (pass != 0) return;

                if (state != 0) {
                    throw new SAXException("Unexpected statement in pass1");
                }

                id = Long.parseLong(attributes.getValue("id"));
                currentWay = new OsmWay(id);
                state = 2;

                return;

            case "nd":
                if (pass != 0) return;

                if (state != 2) {
                    throw new SAXException("Unexpected <nd>");
                }

                long nodeId = Long.parseLong(attributes.getValue("ref"));

                if (currentWay.getNodes().size() != 0
                        && currentWay.getNodes().get(0).getId() == nodeId) {
                    currentWay.addNode(currentWay.getNodes().get(0));
                }
                else {
                    currentWay.addNode(new OsmNode(nodeId, Double.NaN, Double.NaN));


                    // Check if we have seen ways wanting the position of that node
                    // If not create new list, if yes just append
                    List<OsmWay> wantedList = wantedNodes.get(nodeId);
                    if (wantedNodes.get(nodeId) == null) {
                        wantedList = new ArrayList<OsmWay>();
                        wantedList.add(currentWay);
                        wantedNodes.put(nodeId, wantedList);
                    } else {
                        wantedList.add(currentWay);
                    }
                }
                return;


            case "tag":

                if (!((pass == 0 && state == 2) || (pass == 1 && state == 1))) return;

                String key = attributes.getValue("k");
                String value = attributes.getValue("v");

                if (state == 1) {
                    currentNode.setAtt(key, value);
                }
                else {
                    currentWay.setAttribute(key, value);
                }

                return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
            case "node":
                if (pass != 1) return;

                if (state != 1) {
                    throw new SAXException("Unexpected </node>");
                }

                long nodeId = currentNode.getId();
                List<OsmWay> wantedList = wantedNodes.get(nodeId);
                if (wantedList != null) {
                    double lon = currentNode.getLon();
                    double lat = currentNode.getLat();

                    for (OsmWay w : wantedList) {
                        for (OsmNode n : w.getNodes()) {
                            if (n.getId() == currentNode.getId()) {
                                // TODO: Node attributes are lost this way
                                n.setLon(currentNode.getLon());
                                n.setLat(currentNode.getLat());
                            }
                        }
                    }
                }

                currentNode = null;
                state = 0;
                return;

            case "way":
                if (pass != 0) return;

                if (state != 2) {
                    throw new SAXException("Unexpected </way>");
                }
                if (this.filter.apply(currentWay)) {
                    map.addWay(currentWay);
                }

                state = 0;
                return;

        }
    }
}
