import org.jfree.chart.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.*;
import org.jfree.chart.plot.*;


import java.awt.*;
import java.util.Map;

public class OsmDisplay extends ApplicationFrame{

    public OsmDisplay(OsmMap map) {
        super ("Display");

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        int series = 0;

        //for (Map.Entry<Long, OsmWay> entry: map.getWays().entrySet()) {
        for (OsmWay entry: map) {

            XYSeries data = new XYSeries("Some Key");

            for (int i = 0; i < entry.getNodes().size(); i++) {
                data.add(entry.getNodes().get(i).getLon(),
                        entry.getNodes().get(i).getLat()
                        );
            }

            // Areas need to be completed by pointing back to the first point
            if (entry.isArea()) {
                data.add(entry.getNodes().get(0).getLon(),
                        entry.getNodes().get(0).getLat()
                );
            }

            dataset.addSeries(data);

            renderer.setSeriesLinesVisible(series, true);
            renderer.setSeriesShapesVisible(series, false);
            renderer.setSeriesPaint(series, Color.black);

            series ++;
        }


        JFreeChart chart = ChartFactory.createXYLineChart("Map", "Lon", "Lat",
                dataset, PlotOrientation.VERTICAL, false, true, true);
        chart.setBackgroundPaint(Color.white);

        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(renderer);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(800,600));
        this.setSize(800, 600);
        setContentPane(panel);


    }

}
