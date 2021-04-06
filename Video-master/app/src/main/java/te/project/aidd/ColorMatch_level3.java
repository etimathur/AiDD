package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class ColorMatch_level3 extends AppCompatActivity {
    private int question = 0,decision;
    Animation animation;
    private ImageView image;
    String colorvalue="";
    Button redb,yellowb,whiteb,blackb,blueb,greenb;
    private int score1=0;
    TextView points;
    private static final long COUNTDOWN_IN=45000;
    private CountDownTimer cd;
    public long timeleft;
    TextView timer;
    pop popup=new pop(ColorMatch_level3.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_match_level3);
        image=findViewById(R.id.color);

        redb=(Button) findViewById(R.id.redb);
        yellowb=(Button) findViewById(R.id.yellowb);
        greenb=(Button) findViewById(R.id.greenb);
        blackb=(Button) findViewById(R.id.blackb);
        blueb=(Button) findViewById(R.id.blueb);
        whiteb=(Button) findViewById(R.id.whiteb);
        points=findViewById(R.id.marks);
        timer=findViewById(R.id.time);

        updateQuestion();
        timeleft=COUNTDOWN_IN;
        startCountDown();


        redb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="red";
                checkAnswer(colorvalue);


            }
        });
        yellowb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="yellow";
                checkAnswer(colorvalue);

            }
        });

        blueb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="blue";
                checkAnswer(colorvalue);
            }
        });

        greenb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="green";
                checkAnswer(colorvalue);
            }
        });
        blackb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="black";
                checkAnswer(colorvalue);
            }
        });
        whiteb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorvalue="white";
                checkAnswer(colorvalue);

            }
        });

    }


    private void checkAnswer(String colorvalue){
        String answers=Questions.solution[question];
        if(answers.equals(colorvalue)){
            score1=score1+1;
            points.setText(score1+"");
            Log.d("score",score1+"");
        }
        else{
            score1=score1-1;
            points.setText(score1+"");
            Log.d("score",score1+"");
        }
        colorvalue="";
        updateQuestion();
    }

    private void updateQuestion() {
        Random ran = new Random();
        question = ran.nextInt(14);
        image.setImageResource(Questions.images[question]);
        animation = AnimationUtils.loadAnimation(ColorMatch_level3.this, R.anim.textanim);
        image.startAnimation(animation);
    }

    public void startCountDown() {
        cd = new CountDownTimer(timeleft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                long timetaken = 30000 - timeleft;
                timetaken = timetaken / 1000;
                timeleft = 0;
                if(decision!=1){

                    popup.startpop();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popup.dismisspop();
                            cd.cancel();
                            finish();
                        }
                    },4000);}
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