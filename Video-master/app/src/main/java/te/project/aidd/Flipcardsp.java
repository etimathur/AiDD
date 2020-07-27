package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Flipcardsp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipcardsp);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 45),

                new DataPoint(2, 25),
                new DataPoint(3, 25),
                new DataPoint(4, 35),
                new DataPoint(5, 20)
        });
        //graph3.addSeries(series3);


        GraphView graphA = (GraphView) findViewById(R.id.graphmp);
// graph.getGridLabelRenderer().setVerticalAxisTitle("Match Value");
        graphA.getGridLabelRenderer().setHorizontalAxisTitle("GAMES");

        graphA.getViewport().setMinY(0);
        graphA.getViewport().setMaxY(100);

        graphA.getViewport().setYAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<DataPoint>(new DataPoint[]{
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

        graphA.addSeries(series4);
        graphA.getViewport().setScalableY(true);
        graphA.setBackgroundColor(Color.WHITE);
        series4.setThickness(5);
        series4.setColor(Color.RED);
        series4.setAnimated(true);
        series4.setDrawDataPoints(true);
        series4.setDataPointsRadius(10);

    }
}