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
    DatabaseHelper db;
    int list[]=new int[5];
    int analysis_list[]=new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatchp);
        pbparent=(ProgressBar) findViewById(R.id.progresss);
        tparent=(TextView) findViewById(R.id.parenttext);
        pbdoc=(ProgressBar) findViewById(R.id.docpro);
        tdoc=(TextView) findViewById(R.id.doctext);
        db=new DatabaseHelper(this);
        SessionManagement ses=new SessionManagement(Colormatchp.this);
        String naaam=ses.getnaaam();
        list=db.color_match_arr(naaam);
        // graph for calculating the score
        GraphView scoregraph = (GraphView) findViewById(R.id.graph2);
        scoregraph.getGridLabelRenderer().setHorizontalAxisTitle("NO OF GAMES");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1,list[0]),

                new DataPoint(2,list[1]),
                new DataPoint(3, list[2]),
                new DataPoint(4, list[3]),
                new DataPoint(5, list[4])
        });
        scoregraph.addSeries(series);
        scoregraph.setBackgroundColor(Color.WHITE);
        scoregraph.getLegendRenderer().setVisible(true);
        scoregraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        series.setTitle("SCORE");
        series.setThickness(7);
        series.setColor(Color.BLUE);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);







        //graph for calculating the speed
        GraphView timegraph = (GraphView) findViewById(R.id.graph);
        //graph1.getGridLabelRenderer().setVerticalAxisTitle("Speed progress");
        timegraph.getGridLabelRenderer().setHorizontalAxisTitle(" NO OF GAMES");

        timegraph.getViewport().setMinY(0);
        timegraph.getViewport().setMaxY(100);
        analysis_list=db.time_analysis_graph(naaam);

        timegraph.getViewport().setYAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, analysis_list[0]),
                new DataPoint(2, analysis_list[1]),
                new DataPoint(3, analysis_list[2]),
                new DataPoint(4, analysis_list[3]),
                new DataPoint(5, analysis_list[4]),


        });

        timegraph.addSeries(series1);
        timegraph.setBackgroundColor(Color.WHITE);
        timegraph.getViewport().setScalableY(true);
        timegraph.getLegendRenderer().setVisible(true);
        timegraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
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