package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

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

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;
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
    TextView score,timer;
    Interpreter interpreter;
    int  level_1_points,no_of_q,no_of_q1=0, points1=0;


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
        level_1_points=intent.getIntExtra("color_points",level_1_points);
        no_of_q=intent.getIntExtra("no_of_q",no_of_q);

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
            points1=points1+5;
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
                timeleft=0;
                if(decision!=1){
                popup.startpop();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cd.cancel();
                        SessionManagement ses=new SessionManagement(MakeColor.this);
                        String email=db.getEmailForChild(ses.getTableID());
                        int analysis=(int)doInference(level_1_points,no_of_q,points1,no_of_q1);
                        Log.i("hello","points:"+points1+"no of ques:"+no_of_q1+"anal:"+analysis);
                        db.addscore((level_1_points+points1),email);
                        db.time_analysis(analysis,email);
                        db.color_match_30(email,ses.getnaaam(),(level_1_points+points1),analysis);
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







}