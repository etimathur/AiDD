package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Colormatchp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatchp);
        GraphView graph = (GraphView) findViewById(R.id.graph2);
        // graph.getGridLabelRenderer().setVerticalAxisTitle("Match Value");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Games");
        // graph.getViewport().setMinX(0);
        //graph.getViewport().setMaxX(5);
        graph.getViewport().setMinY(10);
        graph.getViewport().setMaxY(50);

        graph.getViewport().setYAxisBoundsManual(true);
        // graph.getViewport().setXAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 45),

                new DataPoint(2, 25),
                new DataPoint(3, 25),
                new DataPoint(4, 35),
                new DataPoint(5, 20)
        });
        graph.addSeries(series);
        series.setThickness(5);
        series.setColor(Color.BLUE);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);


        GraphView graph1 = (GraphView) findViewById(R.id.graph);
        //graph1.getGridLabelRenderer().setVerticalAxisTitle("Speed progress");
        graph1.getGridLabelRenderer().setHorizontalAxisTitle("GAMES");

        graph1.getViewport().setMinY(0);
        graph1.getViewport().setMaxY(100);

        graph1.getViewport().setYAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 10),
                new DataPoint(2, 20),
                new DataPoint(3, 55),
                new DataPoint(4, 75),
                new DataPoint(5, 90),
//                new DataPoint(0, 70),
//                new DataPoint(0, 80),
//                new DataPoint(0, 60),
//                new DataPoint(0, 30)

        });

        graph1.addSeries(series1);
        graph1.getViewport().setScalableY(true);
        series1.setThickness(5);
        series1.setColor(Color.RED);
        //series1.setDrawBackground(true);
        series1.setAnimated(true);
        series1.setDrawDataPoints(true);
        series1.setDataPointsRadius(10);
        //series1.setBackgroundColor(Color.CYAN);
        //series1.setDrawBackground(true);


    }
}