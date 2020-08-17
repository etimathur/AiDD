package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

//import te.project.aidd.ui.exercises.ExercisesFragment;

public class ColourMatch extends AppCompatActivity {
    private TextView score;
    private ImageView image;
    private boolean answer;
    private Button yes,no;
    private int points=0;
    private int question=0;
    private int no_of_q=0;
    public float analysis;
    private TextView timer ;
    private static final long COUNTDOWN_IN=30000;
    private CountDownTimer cd;
    private long timeleft;
    DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_match);
        db=new DatabaseHelper(this);
        score=(TextView) findViewById(R.id.score);
        image=(ImageView) findViewById((R.id.color));
        yes=(Button) findViewById(R.id.btn_yes);
        no=(Button) findViewById(R.id.btn_no);
        timer=(TextView) findViewById(R.id.time);
        Random ran=new Random();
        question=ran.nextInt(15);
        updateQuestion();
        timeleft=COUNTDOWN_IN;
        startCountDown();




        //logic
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer){
                    points++;

                    updateScore(points);
                    Toast.makeText(ColourMatch.this, "Correct", Toast.LENGTH_SHORT).show();
                    // updateQuestion();
                    //perform this check before you update the question
                    if(question==Questions.answers.length){
                        question=0;
                        updateQuestion();
                    }
                    else{
                        updateQuestion();
                    }

                }
                else {
                    Toast.makeText(ColourMatch.this, "Wrong", Toast.LENGTH_SHORT).show();
                    points=points-1;
                    updateScore(points);
                    if(question==Questions.answers.length){
                        question=0;
                        updateQuestion();
                    }
                    else{
                        updateQuestion();
                    }

                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answer){
                    points++;
                    updateScore(points);
                    Toast.makeText(ColourMatch.this, "Correct", Toast.LENGTH_SHORT).show();
                    //updateQuestion();
                    //perform this check before you update the question
                    if(question==Questions.answers.length){
                        question=0;
                        updateQuestion();
                    }
                    else{
                        updateQuestion();
                    }

                }
                else {
                    Toast.makeText(ColourMatch.this, "Wrong", Toast.LENGTH_SHORT).show();
                    points=points-1;
                    updateScore(points);
                    if(question==Questions.answers.length){
                        question=0;
                        updateQuestion();
                    }
                    else{
                        updateQuestion();
                    }

                }
            }
        });




    }


    private void updateQuestion(){

        image.setImageResource(Questions.images[question]);
        answer=Questions.answers[question];
        question++;
        no_of_q++;

    }
    public void updateScore(int point){
        score.setText(""+points);
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
                // analysis of the game without model by 80% to 20% equation
                analysis= (float) ((0.8*points/no_of_q)+ (0.2*no_of_q/30));
                int results =(int) (analysis*100);
                SessionManagement ses=new SessionManagement(ColourMatch.this);
                String naaam=ses.getnaaam();
                db.addscore(points,naaam);
                db.time_analysis(results,naaam);


                cd.cancel();



                Intent homepage=new Intent(ColourMatch.this, Color_instruct.class);
                startActivity(homepage);


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
}
