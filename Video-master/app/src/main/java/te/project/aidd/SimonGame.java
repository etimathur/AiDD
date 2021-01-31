package te.project.aidd;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    int blinkDelay=0,decision;
    List clickedImageTags=new ArrayList();
    int lastIndex=0;
    ArrayList<Integer> selans=new ArrayList<>();
    ArrayList<Integer> answer=new ArrayList<>();
    private static final String TAG = "SimonGame";
    int outLoop=0;
    static int score=0;
    int blinkingOn=0;
    long elapsedMillis=0;
    Interpreter interpreter;
    DatabaseHelper db;
    int analysis;
    int sheet_list[]=new int[6];
    int wrongAnswers=0;
    pop popup=new pop(SimonGame.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_game);
        db=new DatabaseHelper(this);
        try {
            interpreter = new Interpreter(loadModelfile(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if(elapsedMillis>75000) {
                    Log.i("Score",score+" Time Taken: "+elapsedMillis);
                    time.stop();
                    analysis= (int) doInference(score,wrongAnswers);
                    if(score==0){
                        analysis=0;
                    }
                    else if(analysis>100){
                        analysis= 96;
                    }
                    else{}
                    Log.i("Score", score +  "analysis" + analysis);
                    SessionManagement ses = new SessionManagement(SimonGame.this);
                    String email = db.getEmailForChild(ses.getTableID());
                    db.simon_analysis(analysis,email);
                    db.simon_30(email,ses.getnaaam(),score,analysis);
                    addItemToSheet();
                    Log.i("wrongAnswers: " ,wrongAnswers+" ");
                    if(decision!=1){

                        popup.startpop();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popup.dismisspop();
                                finish();
                            }
                        },4000);}

                    Intent go = new Intent(SimonGame.this, SimonInstruct.class);
                    startActivity(go);


                }

            }

        });
    }
    public void generateImages() {
        selans.clear();
        answer.clear();
        myImageList = new int[]{R.drawable.jerry, R.drawable.pikachu, R.drawable.tom, R.drawable.jerry, R.drawable.pikachu, R.drawable.tom, R.drawable.jerry, R.drawable.pikachu, R.drawable.tom};
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
            final ImageView counter = (ImageView) view;
            selected[lastIndex] = counter;
            int taggedCounter = Integer.parseInt(counter.getTag().toString());
            selans.add(taggedCounter);
            if (clickedImageTags.isEmpty()) {
                //counter.setBackgroundResource(R.drawable.squareselect);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        counter.setBackgroundResource(R.drawable.squareselect);
                    }
                },100);
                clickedImageTags.add(taggedCounter);
                lastIndex++;
            } else {
                int x = lastIndex - 1;
                selected[x].setBackgroundColor(Color.WHITE);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        counter.setBackgroundResource(R.drawable.squareselect);
                    }
                },100);
                clickedImageTags.add(taggedCounter);
                lastIndex++;

            }
            Log.d(TAG, "select:" + selans);
            Log.d(TAG, "answer:" + answer);
            for (int i = 0; i <selans.size(); i++) {
                if (selans.get(i) != answer.get(i)) {
                    Log.d(TAG, "select: not same" + selans.get(i) + " " + answer.get(i));

                    showElapsedTime();


                    Toast.makeText(SimonGame.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            counter.setBackgroundResource(R.drawable.squarered);
                        }
                    }, 100);
                    wrongAnswers++;
                    selans.clear();
                    answer.clear();
                    blinkingOn = 0;
                    BlinkingImages(outLoop);
                }
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
                    selans.clear();
                    answer.clear();
                    outLoop++;
                    blinkingOn=0;
                    BlinkingImages(outLoop);
                }

            showElapsedTime();
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

    private MappedByteBuffer loadModelfile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("simon.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);

    }

    public float doInference(int score, int wrongAnswers) {
        float[][] input = new float[1][2];
        input[0][0] = (float) (score);
        input[0][1] = (float) (wrongAnswers);
        float[][] output = new float[1][1];
        interpreter.run(input, output);
        return output[0][0];
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            decision=1;
            finish();


        }
        return super.onKeyDown(keyCode, event);
    }

    private void addItemToSheet() {

        //final ProgressDialog loading = ProgressDialog.show(this, "Adding Item", "Please wait");
        //final String name = editTextItemName.getText().toString().trim();
        //final String brand = editTextBrand.getText().toString().trim();
        SessionManagement ses=new SessionManagement(SimonGame.this);
        final String email=db.getEmailForChild(ses.getTableID());
        final String child_name=ses.getnaaam();
        sheet_list=db.simon_graph(email);
        final String game_1=sheet_list[0]+"";
        final String game_2=sheet_list[1]+"";
        final String game_3=sheet_list[2]+"";
        final String game_4=sheet_list[3]+"";
        final String game_5=sheet_list[4]+"";
        final  String game_6=sheet_list[5]+"";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbx6LCx7JFgOBOk4XwGxig21JtnuGotPjrG2DlOseAU13Gf-4B1K3MeqUw/exec?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //loading.dismiss();
                        //Toast.makeText(Additems.this, response, Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action", "addItem");
                parmas.put("child_name", child_name);
                parmas.put("email",email);
                parmas.put("game_1",game_1);
                parmas.put("game_2",game_2);
                parmas.put("game_3",game_3);
                parmas.put("game_4",game_4);
                parmas.put("game_5",game_5);
                parmas.put("game_6",game_6);

                return parmas;
            }
        };

        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }
}