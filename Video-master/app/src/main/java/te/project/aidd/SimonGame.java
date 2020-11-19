package te.project.aidd;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import static java.lang.Thread.*;


public class SimonGame extends AppCompatActivity {

    private Chronometer time ;
    int[] myImageList;
    ArrayList<Integer> list;
    ImageView[] imageViews;
    ImageView[] selected=new ImageView[100];
    final Handler handler = new Handler();
    int blinkDelay=0;
    List clickedImageTags=new ArrayList();
    int lastIndex=0;
    ArrayList<Integer> selans=new ArrayList<>();
    ArrayList<Integer> answer=new ArrayList<>();
    private static final String TAG = "SimonGame";
    int outLoop=0;
    static int score=0;
    int blinkingOn=0;
    long elapsedMillis=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_game);
        levelcheck();
    }
    public void levelcheck() {
        generateImages();
        time=findViewById(R.id.time);
        time.start();
        time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override

            public void onChronometerTick(Chronometer chronometer) {
                showElapsedTime();
                if(elapsedMillis>60000) {
                    Log.i("Score",score+" Time Taken: "+elapsedMillis);
                    time.stop();
                    finish();
                    Intent go = new Intent(SimonGame.this, SimonInstruct.class);
                    startActivity(go);

                }

            }

        });
    }
    public void generateImages() {
            selans.clear();
            answer.clear();
            myImageList = new int[]{R.drawable.circleorange, R.drawable.circlegreen, R.drawable.circleyellow, R.drawable.circleorange, R.drawable.circlegreen, R.drawable.circleyellow, R.drawable.circleorange, R.drawable.circlegreen, R.drawable.circleyellow};
            list = new ArrayList<>();
            for (int i = 0; i < myImageList.length; i++) {
                list.add(myImageList[i]);
            }
            Collections.shuffle(list);
            imageViews = new ImageView[]{findViewById(R.id.v0), findViewById(R.id.v1), findViewById(R.id.v2), findViewById(R.id.v3), findViewById(R.id.v4), findViewById(R.id.v5), findViewById(R.id.v6), findViewById(R.id.v7), findViewById(R.id.v8)};
            for (int i = 0; i < 9; i++) {
                imageViews[i].setImageResource(list.get(i));
            }
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    outLoop++;
                    BlinkingImages(outLoop);
                    Log.d(TAG, "run: " + outLoop);
                }
            }, 1000);
        }

    public void select(View view)
    {
        if(blinkingOn==1) {
            ImageView counter = (ImageView) view;
            selected[lastIndex] = counter;
            int taggedCounter = Integer.parseInt(counter.getTag().toString());
            selans.add(taggedCounter);
            if (clickedImageTags.isEmpty()) {
                counter.setBackgroundResource(R.drawable.squareselect);
                clickedImageTags.add(taggedCounter);
                lastIndex++;
            } else {
                int x = lastIndex - 1;
                selected[x].setBackgroundColor(Color.WHITE);
                counter.setBackgroundResource(R.drawable.squareselect);
                clickedImageTags.add(taggedCounter);
                lastIndex++;

            }
            Log.d(TAG, "select:" + selans);
            Log.d(TAG, "answer:" + answer);
            for (int i = 0; i < selans.size(); i++) {
                if (selans.get(i) != answer.get(i)) {
                    Log.d(TAG, "select: not same");
                    time.stop();
                    showElapsedTime();
                    Log.i("Score",score+" Time Taken: "+elapsedMillis);
                    Toast.makeText(SimonGame.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                    Intent go = new Intent(SimonGame.this, SimonInstruct.class);
                    startActivity(go);
                    break;
                }
                if (selans.size() == answer.size()) {
                    score++;
                    Toast.makeText(SimonGame.this, "Correct answer", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            selected[lastIndex - 1].setBackgroundColor(Color.WHITE);
                        }
                    }, 1000);
                    outLoop++;
                    blinkingOn=0;
                    BlinkingImages(outLoop);
                }
            }
            showElapsedTime();
//            if(elapsedMillis>60000) {
//                time.stop();
//                Intent go = new Intent(SimonGame.this, SimonInstruct.class);
//                startActivity(go);
//            }
        }
    }

    private void showElapsedTime() {
        elapsedMillis = SystemClock.elapsedRealtime() - time.getBase();
        //Toast.makeText(SimonGame.this, "Elapsed milliseconds,score: " + elapsedMillis+"+"+score,Toast.LENGTH_SHORT).show();
    }
    public void BlinkingImages(int count)  {

//        if(elapsedMillis>60000) {
//            time.stop();
//            Intent go = new Intent(SimonGame.this, SimonInstruct.class);
//            startActivity(go);
//        }
        blinkingOn=0;
        int i;
        for(i=1;i<=count;i++){
            Random randomNum = new Random();
            final int num = randomNum.nextInt(8);
            answer.add(num);
            Log.i("Image", String.valueOf(num));
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    blink(num);
                }
            }, 1000 * i);
        }
//        Arrays.fill(answer, 0);
        if(i==count+1)
            blinkingOn=1;
    }
    public void blink(final int num) {
        Log.i("Blink","now"+String.valueOf(num));
        final ImageView image=imageViews[num];
        image.setAlpha(0);
        blinkDelay++;
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                image.setAlpha(255);
            }
        }, 500);
    }
}