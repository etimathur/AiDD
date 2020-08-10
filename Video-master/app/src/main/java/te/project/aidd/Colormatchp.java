package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Colormatchp extends AppCompatActivity {
    ProgressBar pbparent;
    ProgressBar pbdoc;
    TextView tparent,tdoc;
    int i=0,j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatchp);
        pbparent=(ProgressBar) findViewById(R.id.progresss);
        tparent=(TextView) findViewById(R.id.parenttext);
        pbdoc=(ProgressBar) findViewById(R.id.docpro);
        tdoc=(TextView) findViewById(R.id.doctext);
        GraphView graph = (GraphView) findViewById(R.id.graph2);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("NO OF GAMES");
        graph.getViewport().setMinY(10);
        graph.getViewport().setMaxY(50);

        graph.getViewport().setYAxisBoundsManual(true);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 45),

                new DataPoint(2, 25),
                new DataPoint(3, 25),
                new DataPoint(4, 35),
                new DataPoint(5, 20)
        });
        graph.addSeries(series);
        graph.setBackgroundColor(Color.WHITE);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        series.setTitle("SCORE");
        series.setThickness(7);
        series.setColor(Color.BLUE);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);


        GraphView graph1 = (GraphView) findViewById(R.id.graph);
        //graph1.getGridLabelRenderer().setVerticalAxisTitle("Speed progress");
        graph1.getGridLabelRenderer().setHorizontalAxisTitle(" NO OF GAMES");

        graph1.getViewport().setMinY(0);
        graph1.getViewport().setMaxY(100);

        graph1.getViewport().setYAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 30),
                new DataPoint(2, 43),
                new DataPoint(3, 55),
                new DataPoint(4, 75),
                new DataPoint(5, 60),

        });

        graph1.addSeries(series1);
        graph1.setBackgroundColor(Color.WHITE);
        graph1.getViewport().setScalableY(true);
        graph1.getLegendRenderer().setVisible(true);
        graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        series1.setTitle("TIME");
        series1.setThickness(7);
        series1.setColor(Color.RED);
        series1.setAnimated(true);
        series1.setDrawDataPoints(true);
        series1.setDataPointsRadius(10);

        pbparent.setMax(100);
        pbparent.setProgress(60);
        tparent.setText(60+"%");

        pbdoc.setMax(100);
        pbdoc.setProgress(75);
        tdoc.setText(75+"%");




    }
}