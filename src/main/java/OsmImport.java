import org.jfree.ui.RefineryUtilities;

public class OsmImport {

    public static void main (String [] args) {
        System.out.println("Hello World");

        OsmMap map;

        map = OsmMap.importXml(args[0]);
        System.out.println("Parsed " + map.getWays().size() + " ways");
        System.out.println("Possible Types of Highway are : " + map.getWayAttributeSet("highway"));
        System.out.println("Possible Names of Highway are : " + map.getWayAttributeSet("name"));

        map = map.filter(x -> {
            String hw = x.get("highway");

            if (hw == null) return false;
            if (hw.equals("unclassified")
                || hw.equals("abandoned")
                || hw.equals("track")
                || hw.equals("pedestrian")
                || hw.equals("steps")
                    ) return false;
            return true;
            }
        );

        System.out.println("After filtering " + map.getWays().size() + " ways remain");


        OsmDisplay display = new OsmDisplay(map);
        RefineryUtilities.centerFrameOnScreen(display);
        display.setVisible(true);

    }

}
