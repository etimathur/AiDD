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

public class Flipcardsp extends AppCompatActivity {
    ProgressBar pb,pb1;
    private int i=0,j=0;
    private TextView textView,textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipcardsp);
        pb=(ProgressBar) findViewById(R.id.progress1);
        textView=(TextView) findViewById(R.id.tst);
        textView1=(TextView) findViewById(R.id.ttxt);
        pb1=(ProgressBar) findViewById(R.id.progress2);

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
        graphA.getGridLabelRenderer().setHorizontalAxisTitle(" NO OF GAMES");
        graphA.getLegendRenderer().setVisible(true);
       // graphA.getGridLabelRenderer().setVerticalAxisTitle("MEMORY");
        graphA.getViewport().setMinY(0);
        graphA.getViewport().setMaxY(100);
        graphA.getViewport().setYAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 10),
                new DataPoint(2, 20),
                new DataPoint(3, 55),
                new DataPoint(4, 75),
                new DataPoint(5, 20),
//                new DataPoint(0, 70),
//                new DataPoint(0, 80),
//                new DataPoint(0, 60),
//                new DataPoint(0, 30)

        });

        graphA.addSeries(series4);
        graphA.getViewport().setScalableY(true);
        graphA.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphA.setBackgroundColor(Color.WHITE);
        series4.setThickness(7);
        series4.setTitle("MEMORY");
        series4.setColor(Color.RED);
        series4.setAnimated(true);
        series4.setDrawDataPoints(true);
        series4.setDataPointsRadius(10);

        pb.setMax(100);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i<=80){
                    pb.setProgress(i);
                    textView.setText(i+"%");
                    i++;
                    handler.postDelayed(this,10);


                }else {
                    handler.removeCallbacks(this);
                }

            }
        },10);


        pb1.setMax(100);
        final Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(j<=80){
                    pb1.setProgress(j);
                    textView1.setText(j+"%");
                    j++;
                    handler1.postDelayed(this,20);


                }else {
                    handler1.removeCallbacks(this);
                }

            }
        },20);





    }
}