import org.jfree.ui.RefineryUtilities;

public class OsmImport {

    public static void main (String [] args) {
        System.out.println("Welcome to OsmImport test");

        OsmMap map;

        map = OsmMap.importXml(args[0]);
        System.out.println("Parsed " + map.getWays().size() + " ways");
        System.out.println("Possible Types of Highway are : " + map.getWayAttributeSet("highway"));

        map = map.filter(x -> {
            String hw = x.get("highway");

            if (hw == null) return false;
            if (hw.equals("unclassified")
                    || hw.equals("abandoned")
                    || hw.equals("track")
                    || hw.equals("pedestrian")
                    || hw.equals("footway")
                    || hw.equals("circleway")
                    || hw.equals("steps")
                    || hw.equals("razed")
                    || hw.equals("proposed")
                    ) return false;
            /*if (hw.equals("secondary")
                    || hw.equals("tertiary")
                    || hw.equals("residential")
                    ) return true;*/
            return true;
            }
        );

        //System.out.println("Possible Types of Highway : " + map.getWayAttributeSet("highway"));
        //System.out.println("Possible Names of Highway are : " + map.getWayAttributeSet("name"));
        System.out.println("After filtering " + map.getWays().size() + " ways remain");


        OsmDisplay display = new OsmDisplay(map);
        RefineryUtilities.centerFrameOnScreen(display);
        display.setVisible(true);

    }

}

