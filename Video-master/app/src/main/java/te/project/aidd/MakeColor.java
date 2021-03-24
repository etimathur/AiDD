package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MakeColor extends AppCompatActivity {
    Button redb,yellowb,whiteb,blackb,blueb,greenb;
    ImageView yourcolor, givencolor;
    String colorvalue="";
    private int count=-1,decision;
    private static final long COUNTDOWN_IN=45000;
    private CountDownTimer cd;
    public long timeleft;
    String checktag="";
    pop popup=new pop(MakeColor.this);
    Random r=new Random();
    Button clear;
    DatabaseHelper db;
    int sheet_list[]=new int[6];
    TextView score,timer;
    Interpreter interpreter;
    long timetaken;
    int  level_1_results,no_of_q,no_of_q1=0, points1=0,level_1_points,level_no=2,l;
    ColourMatch obj=new ColourMatch();

    public static Integer[] questions;
    static {
        questions = new Integer[6];
        questions[0]=0;
        questions[1]=0;
        questions[2]=0;
        questions[3]=0;
        questions[4]=0;
        questions[5]=0;

    }

    public static Integer[] level_tell1;
    static {
        level_tell1 = new Integer[6];
        level_tell1[0]=0;
        level_tell1[1]=0;
        level_tell1[2]=0;
        level_tell1[3]=0;
        level_tell1[4]=0;
        level_tell1[5]=0;

    }

    public static Integer[] marks1;
    static {
        marks1 = new Integer[6];
        marks1[0]=0;
        marks1[1]=0;
        marks1[2]=0;
        marks1[3]=0;
        marks1[4]=0;
        marks1[5]=0;

    }
    public static Integer[] wakt1;
    static {
        wakt1 = new Integer[6];
        wakt1[0]=0;
        wakt1[1]=0;
        wakt1[2]=0;
        wakt1[3]=0;
        wakt1[4]=0;
        wakt1[5]=0;

    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_color);
        try {
            interpreter=new Interpreter(loadModelfile(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        db=new DatabaseHelper(this);
        redb=(Button) findViewById(R.id.redb);
        yellowb=(Button) findViewById(R.id.yellowb);
        greenb=(Button) findViewById(R.id.greenb);
        blackb=(Button) findViewById(R.id.blackb);
        blueb=(Button) findViewById(R.id.blueb);
        whiteb=(Button) findViewById(R.id.whiteb);
        yourcolor=findViewById((R.id.yourcolor));
        givencolor=findViewById((R.id.givencolor));
        score=findViewById(R.id.score);
        clear=findViewById(R.id.clear);
        timer=findViewById(R.id.time);
        Intent intent=getIntent();
        level_1_results=intent.getIntExtra("level_1_results",level_1_results);
        level_1_points=intent.getIntExtra("color_points",level_1_points);

        no_of_q=intent.getIntExtra("no_of_q",no_of_q);
        Log.d("questionss",no_of_q+"");
        questions=obj.total_questions;
        level_tell1=obj.level_tell;
        marks1=obj.marks;
        wakt1=obj.wakt;

        count=r.nextInt(Questions.colorss.length);
        newQuestion();
        timeleft=COUNTDOWN_IN;
        startCountDown();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="";
                yourcolor.setImageResource(R.drawable.plain);
            }
        });

        redb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue=colorvalue+"red";
                adding_color(colorvalue);

            }
        });
        yellowb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue=colorvalue+"yellow";
                adding_color(colorvalue);
            }
        });

        blueb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue=colorvalue+"blue";
                adding_color(colorvalue);
            }
        });

        greenb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue=colorvalue+"green";
                adding_color(colorvalue);
            }
        });
        blackb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue=colorvalue+"black";
                adding_color(colorvalue);
            }
        });
        whiteb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue=colorvalue+"white";
                adding_color(colorvalue);
            }
        });

    }
    public void adding_color(String colorvalue){
        switch (colorvalue){
            case "red":
                yourcolor.setImageResource(R.drawable.red);
                checktag="red";
                checkCombination();
                break;
            case "redyellow":
            case "yellowred":
                yourcolor.setImageResource(R.drawable.orange);
                checktag="orange";
                checkCombination();
                break;
            case "yellow":
                yourcolor.setImageResource(R.drawable.yelloe);
                checktag="yellow";
                checkCombination();
                break;
            case "blue":
                yourcolor.setImageResource(R.drawable.blue);
                checktag="blue";
                checkCombination();
                break;
            case "redblue":
            case "bluered":
                yourcolor.setImageResource(R.drawable.purple);
                checktag="purple";
                checkCombination();
                break;
            case "blackwhite":
            case "whiteblack":
                yourcolor.setImageResource(R.drawable.grey);
                checktag="grey";
                checkCombination();
                break;
            case "black":
                yourcolor.setImageResource(R.drawable.black);
                checktag="black";
                checkCombination();
                break;
            case "white":
                yourcolor.setImageResource(R.drawable.white);
                checktag="white";
                checkCombination();
                break;
            case "redwhite":
            case "whitered":
                yourcolor.setImageResource(R.drawable.pink);
                checktag="pink";
                checkCombination();
                break;
            case "bluewhite":
            case "whiteblue":
                yourcolor.setImageResource(R.drawable.lightblue);
                checktag="lightblue";
                checkCombination();
                break;
            case "green":
                yourcolor.setImageResource(R.drawable.green);
                checktag="green";
                checkCombination();
                break;
            case "redbluewhite":
            case "whitebluered":
            case "redwhiteblue":
            case "blueredwhite":
                yourcolor.setImageResource(R.drawable.lightpurple);
                checktag="lightpurple";
                checkCombination();
                break;
            case "greenblue":
            case "bluegreen":
                yourcolor.setImageResource(R.drawable.greenblue);
                checktag="greenblue";
                checkCombination();
                break;
            case "greenblack":
            case "blackgreen":
                yourcolor.setImageResource(R.drawable.darkgreen);
                checktag="darkgreen";
                checkCombination();
                break;
            case "blueblack":
            case "blackblue":
                yourcolor.setImageResource(R.drawable.darkblue);
                checktag="darkblue";
                checkCombination();
                break;
            case "greenred":
            case "redgreen":
                yourcolor.setImageResource(R.drawable.brown);
                checktag="brown";
                checkCombination();
                break;

            default:
                popup.startincorrect();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popup.dismissincorrect();
                        yourcolor.setImageResource(R.drawable.plain);
                        newQuestion();

                    }
                },1000);

                break;
        }
    }

    public void newQuestion(){
        no_of_q1++;
        int len=Questions.colorss.length;
        if(count==(len-1)){
            count=0;
        }
        else {
            count++;
        }
        givencolor.setImageResource(Questions.colorss[count]);
        givencolor.setTag(Questions.match[count]);
        colorvalue="";
    }

    public void checkCombination(){
        String t= givencolor.getTag().toString();
        if(t.equals(checktag)){
            points1=points1+2;
            score.setText(points1+"");
            popup.startcorrect();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.dismisscorrect();
                    yourcolor.setImageResource(R.drawable.plain);
                    newQuestion();

                }
            },1000);
        }
    }

    public void startCountDown(){
        cd=new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft=millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timetaken=45000-timeleft;
                timetaken=timetaken/1000;
                timeleft=0;
                if(decision!=1){
                popup.startpop();
                    final long finalTimetaken = timetaken;
                    new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cd.cancel();
                        SessionManagement ses=new SessionManagement(MakeColor.this);
                        String email=db.getEmailForChild(ses.getTableID());
                       // int analysis=(int)doInference(level_1_results,no_of_q,points1,no_of_q1);
                        Log.i("level 2  ","points: "+points1+level_1_points+"  no of ques:"+no_of_q1);
                        float level_2_results=  (points1 *100 )/(2*no_of_q1);
                        //level_2_results= level_2_results*100;
                        int analysis=(int) (level_1_results+level_2_results)/2;
                        Log.i("level 2__  ","analysissss "+analysis+"levelll"+level_2_results);
                        db.addscore((level_1_points+points1),email);
                        db.time_analysis(analysis,email);
                        db.color_match_30(email,ses.getnaaam(),(level_1_points+points1),analysis);
                        questions[0]=questions[1];
                        questions[1]=questions[2];
                        questions[2]=questions[3];
                        questions[3]=questions[4];
                        questions[4]=questions[5];
                        questions[5]=no_of_q1+no_of_q;


                        level_tell1[0]=level_tell1[1];
                        level_tell1[1]=level_tell1[2];
                        level_tell1[2]=level_tell1[3];
                        level_tell1[3]=level_tell1[4];
                        level_tell1[4]=level_tell1[5];
                        level_tell1[5]=level_no;

                        marks1[0]=marks1[1];
                        marks1[1]=marks1[2];
                        marks1[2]=marks1[3];
                        marks1[3]=marks1[4];
                        marks1[4]=marks1[5];
                        marks1[5]=points1+level_1_points;

                        wakt1[0]=wakt1[1];
                        wakt1[1]=wakt1[2];
                        wakt1[2]=wakt1[3];
                        wakt1[3]=wakt1[4];
                        wakt1[4]=wakt1[5];
                        wakt1[5]=(int)timetaken;


                        Intent swara=new Intent(MakeColor.this,ColourMatch.class);
                        swara.putExtra("array",questions);

                        addItemToSheet();
                        popup.dismisspop();
                        finish();
                    }
                },3000);}

            }
        }.start();

    }
    public void updateCountDownText(){
        int minutes=(int)(timeleft/1000)/60;
        int seconds=(int)(timeleft/1000)%60;
        String timeformat=String.format(Locale.getDefault(),"00:%02d",seconds);
        timer.setText(timeformat);
        if(timeleft<10000){
            timer.setTextColor(Color.RED);
        }else {
            timer.setTextColor(Color.BLACK);
        }
    }
    private MappedByteBuffer loadModelfile() throws IOException {
        AssetFileDescriptor assetFileDescriptor=this.getAssets().openFd("colormatch.tflite");
        FileInputStream fileInputStream=new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel=fileInputStream.getChannel();
        long startOffset=assetFileDescriptor.getStartOffset();
        long length=assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,length);

    }
    public  float doInference(int level1, int no_of_q,int level3, int no_of_q1){
        float[][] input= new float[1][4];
        input[0][0]=(float)(level1);
        input[0][1]=(float)(no_of_q);
        input[0][2]=(float)(level3);
        input[0][3]=(float)(no_of_q1);
        float[][] output=new float[1][1];
        interpreter.run(input,output);
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

        SessionManagement ses=new SessionManagement(MakeColor.this);
        final String email=db.getEmailForChild(ses.getTableID());
        final String child_name=ses.getnaaam();
        sheet_list=db.time_analysis_graph(email);
        final String game_1="Level:"+level_tell1[0]+"\n"+"Score:"+marks1[0]+"\n"+"No of questions attempted:"+questions[0]+"\n"+"Timetaken:"+wakt1[0]+"\n"+"Analysis:"+ sheet_list[0]+"";
        final String game_2="Level:"+level_tell1[1]+"\n"+"Score:"+marks1[1]+"\n"+"No of questions attempted:"+questions[1]+"\n"+"Timetaken:"+wakt1[1]+"\n"+ "Analysis:"+ sheet_list[1]+"";
        final String game_3="Level:"+level_tell1[2]+"\n"+"Score:"+marks1[2]+"\n"+"No of questions attempted:"+questions[2]+"\n"+"Timetaken:"+wakt1[2]+"\n"+ "Analysis:"+ sheet_list[2]+"";
        final String game_4="Level:"+level_tell1[3]+"\n"+"Score:"+marks1[3]+"\n"+"No of questions attempted:"+questions[3]+"\n"+"Timetaken:"+wakt1[3]+"\n"+ "Analysis:"+ sheet_list[3]+"";
        final String game_5="Level:"+level_tell1[4]+"\n"+"Score:"+marks1[4]+"\n"+"No of questions attempted:"+questions[4]+"\n"+"Timetaken:"+wakt1[4]+"\n"+ "Analysis:"+ sheet_list[4]+"";
        final  String game_6="Level:"+level_tell1[5]+"\n"+"Score:"+marks1[5]+"\n"+"No of questions attempted:"+questions[5]+"\n"+"Timetaken:"+wakt1[5]+"\n"+"Analysis:"+ sheet_list[5]+"";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbz91TkRELYJEBgNUI3Wj5zQfWsdon05SgfbWabEdjtmupLtPCqkJXmy4w/exec?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //loading.dismiss();

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