import org.jfree.ui.RefineryUtilities;

public class OsmImport {

    public static void main (String [] args) {
        System.out.println("Hello World");

        OsmNodeMap map;

        //map = OsmNodeMap.importXml(args[0]);
        //System.out.println("Parsed " + map.getWays().size() + " ways");

        map = OsmNodeMap.importXml(args[0], x -> {
            String hw = x.get("highway");
            if (hw == null) return false;
            if (hw.equals("motorway")) return true;
            if (hw.equals("motorway_link")) return true;
            if (hw.equals("primary")) return true;
            if (hw.equals("secondary")) return true;
            if (hw.equals("tertiary")) return true;
            if (hw.equals("residential")) return true;
            return false;
            }
        );

        System.out.println("Parsed " + map.getWays().size() + " ways");
        OsmDisplay display = new OsmDisplay(map);
        RefineryUtilities.centerFrameOnScreen(display);
        display.setVisible(true);

    }

}
