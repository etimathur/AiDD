package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

class images{
    String colour;
    String shape;
    int index;
    images(String colour,String shape)
    {
        this.colour=colour;
        this.shape=shape;
    }

    @Override
    public String toString() {
        return "images{" +
                "colour='" + colour + '\'' +
                ", shape='" + shape + '\'' +
                '}';
    }
}
class Pair{
    String type;
    int count=0;
    List index=new ArrayList();
    String colourCode;
    Pair(String colour)
    {
        this.type=colour;
        count=0;
    }
}
public class FindTheMatch extends AppCompatActivity {

    Interpreter interpreter;
    int analysis;
    String [] colours={"black","blue","brown","grey","orange","purple","red","yellow","green"};
    String [] colourCode={"#000000","#0000FF","#660000","#CCCCCC","#FF6600","#9900FF","#FF0000","#FFFF00","#339900"};
    int noOfColours=colours.length;
    Pair[] colourList=new Pair[noOfColours];
    String [] shapes={"square","triangle","circle"};
    Pair[] shapeList=new Pair[shapes.length];
    List clickedImageTags=new ArrayList();
    List notToClickImageIndex=new ArrayList();
    String Question;
    String critertia="",critertia2="";
    int criteriaIndex,criteriaIndex2;
    int choice,noOfQuestions=0,decision;
    int levelImageCount=9,condition=1,level=0;
    int correctScore=0, wrongScore=0, totalanswers=0,missedScore=0,levelScore=0;
    SessionManagement sessionManagement;
    private TextView timer ;
    private static final long COUNTDOWN_IN=30000;
    private CountDownTimer cd;
    private long timeleft;
    int back=0;

    images[] images;

    DatabaseHelper db= new DatabaseHelper(this);
    pop popup=new pop(FindTheMatch.this);

    public void checkColorShape(View view)
    {
        ImageView counter=(ImageView)view;
        int taggedCounter=Integer.parseInt(counter.getTag().toString());
        if(clickedImageTags.contains(taggedCounter))
        {
            counter.setBackgroundColor(Color.WHITE);
            clickedImageTags.remove((Object)taggedCounter);
        }
        else
        {
            counter.setBackgroundResource(R.drawable.squareselect);
            clickedImageTags.add(taggedCounter);
        }


    }

    public void checkAnswers(View view) throws InterruptedException {
        try {
            if (timeleft > 1000) {
                Collections.sort(clickedImageTags);
                ImageView[] imageViews;
                if (levelImageCount == 9) {
                    imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3),
                            (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6),
                            (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9)};
                } else {
                    imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4),
                            (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8),
                            (ImageView) findViewById(R.id.imageView9), (ImageView) findViewById(R.id.imageView10), (ImageView) findViewById(R.id.imageView11), (ImageView) findViewById(R.id.imageView12),
                            (ImageView) findViewById(R.id.imageView13), (ImageView) findViewById(R.id.imageView14), (ImageView) findViewById(R.id.imageView15), (ImageView) findViewById(R.id.imageView16)};
                }
//            Log.i("Image Count ", String.valueOf(levelImageCount));
                for (int i = 0; i < levelImageCount; i++) {
                    if (choice == 1) {
                        if (images[i].shape.equals(critertia) && images[i].colour.equals(critertia2)) {
                            notToClickImageIndex.add(i);
                        }
                    } else {
                        if (images[i].colour.equals(critertia) && images[i].shape.equals(critertia2)) {
                            notToClickImageIndex.add(i);
                        }
                    }

                }
                //Log.i("Not to click Index", String.valueOf(notToClickImageIndex));
                if (choice == 0) {
                    if (condition == 2) {
                        colourList[criteriaIndex].index.removeAll(notToClickImageIndex);
                    }
                    totalanswers += colourList[criteriaIndex].index.size();
                    if (colourList[criteriaIndex].index.size() == clickedImageTags.size() && colourList[criteriaIndex].index.equals(clickedImageTags)) {
                        Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < clickedImageTags.size(); i++) {
                            imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                            correctScore++;
                            levelScore++;
                        }
                    } else if (clickedImageTags.containsAll(colourList[criteriaIndex].index)) {
                        for (int i = 0; i < clickedImageTags.size(); i++) {

                            if (colourList[criteriaIndex].index.contains((Object) clickedImageTags.get(i))) {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                                correctScore++;
                                levelScore++;
                            } else {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                                wrongScore++;
                            }
                        }
                        Toast.makeText(this, "Extra images selected", Toast.LENGTH_SHORT).show();


                    } else {

                        for (int i = 0; i < clickedImageTags.size(); i++) {

                            if (colourList[criteriaIndex].index.contains((Object) clickedImageTags.get(i))) {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                                correctScore++;
                                levelScore++;
                            } else {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                                wrongScore++;
                            }
                        }
                        Toast.makeText(this, "All images not selected", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (condition == 2) {
                        shapeList[criteriaIndex].index.removeAll(notToClickImageIndex);
                    }
                    totalanswers += shapeList[criteriaIndex].index.size();
                    if (shapeList[criteriaIndex].index.size() == clickedImageTags.size()) {
                        if (shapeList[criteriaIndex].index.equals(clickedImageTags)) {
                            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < clickedImageTags.size(); i++) {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                                correctScore++;
                                levelScore++;
                            }
                        }
                    } else if (clickedImageTags.containsAll(shapeList[criteriaIndex].index)) {
                        for (int i = 0; i < clickedImageTags.size(); i++) {
                            if (shapeList[criteriaIndex].index.contains((Object) clickedImageTags.get(i))) {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                                correctScore++;
                                levelScore++;
                            } else {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                                wrongScore++;
                            }
                        }
                        Toast.makeText(this, "Extra images selected", Toast.LENGTH_SHORT).show();

                    } else {
                        for (int i = 0; i < clickedImageTags.size(); i++) {
                            if (shapeList[criteriaIndex].index.contains((Object) clickedImageTags.get(i))) {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                                correctScore++;
                                levelScore++;
                            } else {
                                imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                                wrongScore++;
                            }
                        }
                        Toast.makeText(this, "All images not selected", Toast.LENGTH_SHORT).show();

                    }
                }


                //Log.i("Score ", correctScore + " " + wrongScore + " " + totalanswers);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(timeleft>500) {
                                reset();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 500);
            }
        }
        catch(Exception e)
        {
            levelCheck();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            interpreter=new Interpreter(loadModelfile(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        level++;
        for(int i=0;i<noOfColours;i++)
        {
            colourList[i]=new Pair(colours[i]);
        }
        for(int i=0;i<shapes.length;i++)
        {
            shapeList[i]=new Pair(shapes[i]);
        }
        levelCheck();

        sessionManagement=new SessionManagement(FindTheMatch.this);
    }
    public void levelCheck(){

        if(levelImageCount==9) {
            clickedImageTags.clear();
            setContentView(R.layout.activity_findthematch8);
            timer = (TextView) findViewById(R.id.time);
            timeleft = COUNTDOWN_IN;
            TextView levelTag=(TextView)findViewById(R.id.levelNo);
            levelTag.setText(Integer.toString(level));
            startCountDown();
            generateImages();
        }
        if(levelImageCount==16) {
            clickedImageTags.clear();
            setContentView(R.layout.activity_findthematch16);
            TextView levelTag=(TextView)findViewById(R.id.levelNo);
            levelTag.setText(Integer.toString(level));
            timer = (TextView) findViewById(R.id.time);
            timeleft = COUNTDOWN_IN+15000;
            startCountDown();
            generateImages();
        }

    }
    public void generateImages(){
        noOfQuestions++;
        clickedImageTags.clear();
        Random rand = new Random();
        int[][] drawables=new int[][]{{R.drawable.squareblack, R.drawable.squareblue, R.drawable.squarebrown, R.drawable.squaregrey, R.drawable.squareorange, R.drawable.squarepurple, R.drawable.squarered, R.drawable.squareyellow, R.drawable.squaregreen},{R.drawable.triangleblack, R.drawable.triangleblue, R.drawable.trianglebrown, R.drawable.trianglegrey, R.drawable.triangleorange, R.drawable.trianglepurple, R.drawable.trianglered, R.drawable.triangleyellow, R.drawable.trianglegreen},{R.drawable.circleblack, R.drawable.circleblue, R.drawable.circlebrown, R.drawable.circlegrey, R.drawable.circleorange, R.drawable.circlepurple, R.drawable.circlered, R.drawable.circleyellow, R.drawable.circlegreen}};
        ImageView[] imageViews;
        if(levelImageCount==9) {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3),
                    (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6),
                    (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9)};
        }
        else {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4),
                    (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8),
                    (ImageView) findViewById(R.id.imageView9), (ImageView) findViewById(R.id.imageView10), (ImageView) findViewById(R.id.imageView11), (ImageView) findViewById(R.id.imageView12),
                    (ImageView) findViewById(R.id.imageView13), (ImageView) findViewById(R.id.imageView14), (ImageView) findViewById(R.id.imageView15), (ImageView) findViewById(R.id.imageView16)};
        }

        images=new images[levelImageCount];
        for (int i = 0; i < imageViews.length; i++) {
            int shape=rand.nextInt(shapes.length);
            int color = rand.nextInt(noOfColours);
            imageViews[i].setImageResource(drawables[shape][color]);
            images[i]=new images(colours[color],shapes[shape]);
            colourList[color].count++;
            colourList[color].index.add(i);
            shapeList[shape].count++;
            shapeList[shape].index.add(i);
        }
        //Log.i("Image Info ", Arrays.toString(images));
        generateQuestion();
    }
    public void generateQuestion()
    {
        Random rand = new Random();
        boolean flag=true,flag2=true;

        if(condition==1) {
            int pick=0;
            choice = rand.nextInt(2);
            if (choice == 0) {
                while (flag == true) {
                    pick = rand.nextInt(noOfColours);
                    if (colourList[pick].count > 1) {
                        flag = false;
                        critertia = colourList[pick].type;
                    }
                }
            } else if (choice == 1) {
                while (flag == true) {
                    pick = rand.nextInt(shapes.length);
                    if (shapeList[pick].count > 1) {
                        flag = false;
                        critertia = shapeList[pick].type;
                    }
                }
            }
            TextView questionTextView = (TextView) findViewById(R.id.questionTextView);
            questionTextView.setVisibility(View.VISIBLE);
            String code;
            if(choice==0) {
                code=colourCode[pick];
            }
            else
            {
                code="#000000";
            }
            Question="Find all <font color="+code+">"+critertia+"</font> images";
            questionTextView.setText(Html.fromHtml(Question));
            if (choice == 0) {
                for (int i = 0; i < noOfColours; i++) {
                    if (critertia.equals(colours[i])) {
                        criteriaIndex = i;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < shapes.length; i++) {
                    if (critertia.equals(shapes[i])) {
                        criteriaIndex = i;
                        break;
                    }
                }
            }
        }
        else if(condition==2)              //images { (Square,yellow),(Triangle,Blue),(Circle,Black),.........}
        {
            choice = rand.nextInt(2);
            int c=0,pick=0,pick1=0;
            if (choice == 0) {
                while (flag == true && c<50) {
                    pick = rand.nextInt(noOfColours);
                    c++;
                    if (colourList[pick].count > 1) {
                        flag = false;
                        critertia = colourList[pick].type;
                    }
                    //Log.i("Loop","1");
                }

                if(c==50)
                    generateImages();

                flag = true;
                c=0;
                while (flag == true && c<50) {
                    pick1 = rand.nextInt(levelImageCount);
                    c++;
                    if (images[pick1].colour.equals(critertia)) {
                        flag = false;
                        critertia2 = images[pick1].shape;
                        criteriaIndex2 = pick1;
                    }
                    //Log.i("Loop","2");
                }
                if(c==50)
                    generateImages();

            } else if (choice == 1) {
                c=0;
                while (flag == true && c<50) {
                    pick = rand.nextInt(shapes.length);
                    c++;
                    if (shapeList[pick].count > 2) {
                        flag = false;
                        critertia = shapeList[pick].type;
                    }
                    //Log.i("Loop","3");

                }
                if(c==50)
                    generateImages();
                flag = true;
                c=0;
                while (flag == true && c<50) {
                    pick1 = rand.nextInt(levelImageCount);
                    c++;
                    if (images[pick1].shape.equals(critertia)) {
                        flag = false;
                        critertia2 = images[pick1].colour;
                        criteriaIndex2 = pick1;
                    }
                    //Log.i("Loop","4");
                }
                if(c==50)
                    generateImages();
            }
            //Log.i("Loop","out");
            TextView questionTextView = (TextView) findViewById(R.id.questionTextView);
            questionTextView.setVisibility(View.VISIBLE);
            questionTextView.setTextSize(25f);
            String code1,code2="";
            if(choice==0) {
                code1=colourCode[pick];
                code2="#000000";
            }
            else {
                int i=0;
                for(i=0;i<colourList.length;i++)
                {
                    if(critertia2.equals(colours[i]))
                    {
                        break;
                    }
                }
                code1="#000000";
                if(i<9) {
                    code2 = colourCode[i];
                }
            }
            Question="Find all <font color="+code1+">"+critertia+"</font> without <br>selecting any <font color="+code2+">"+critertia2+"</font>";
            //Log.i("Question : ","Find all " + critertia + " without selecting any " + critertia2);
            questionTextView.setText(Html.fromHtml(Question));
            for (int i = 0; i < noOfColours; i++) {
                if (critertia.equals(colours[i])) {
                    criteriaIndex = i;
                    break;
                }
                else if (critertia2.equals(colours[i])) {
                    criteriaIndex2 = i;
                    break;
                }

            }
            for (int i = 0; i < shapes.length; i++) {
                if (critertia.equals(shapes[i])) {
                    criteriaIndex = i;
                    break;
                }
                else if (critertia2.equals(shapes[i])) {
                    criteriaIndex2 = i;
                    break;
                }
            }
        }
    }
    public void reset() throws InterruptedException {

        for(int i=0;i<noOfColours;i++){
            colourList[i].count=0;
            colourList[i].index.clear();

        }
        for(int i=0;i<shapes.length;i++)
        {
            shapeList[i].count=0;
            shapeList[i].index.clear();
        }
        clickedImageTags.clear();
        notToClickImageIndex.clear();
        ImageView[] imageViews;
        if(levelImageCount==9) {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3),
                    (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6),
                    (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9)};
        }
        else {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4),
                    (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8),
                    (ImageView) findViewById(R.id.imageView9), (ImageView) findViewById(R.id.imageView10), (ImageView) findViewById(R.id.imageView11), (ImageView) findViewById(R.id.imageView12),
                    (ImageView) findViewById(R.id.imageView13), (ImageView) findViewById(R.id.imageView14), (ImageView) findViewById(R.id.imageView15), (ImageView) findViewById(R.id.imageView16)};
        }
        for(int i=0;i<levelImageCount;i++)
        {
            imageViews[i].setBackgroundColor(Color.WHITE);
        }
        generateImages();
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
                timeleft = 0;

                //Log.i("Level",":"+level+" "+levelImageCount+" "+condition);


                if (back != 1) {
                    if (level == 1 && levelScore >= 10) {
                        popup.startlevelpop();
                        levelImageCount = 16;
                        level++;
                        levelScore = 0;
                        clickedImageTags.clear();
                        notToClickImageIndex.clear();
                        for (int i = 0; i < noOfColours; i++) {
                            colourList[i].count = 0;
                            colourList[i].index.clear();

                        }
                        for (int i = 0; i < shapes.length; i++) {
                            shapeList[i].count = 0;
                            shapeList[i].index.clear();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popup.dismisslevelpop();
                                levelCheck();

                            }
                        }, 2000);

                    } else if (level == 2 && levelScore >= 20) {
                        popup.startlevelpop();
                        levelImageCount = 9;
                        condition = 2;
                        level++;
                        levelScore = 0;
                        clickedImageTags.clear();
                        notToClickImageIndex.clear();
                        for (int i = 0; i < noOfColours; i++) {
                            colourList[i].count = 0;
                            colourList[i].index.clear();

                        }
                        for (int i = 0; i < shapes.length; i++) {
                            shapeList[i].count = 0;
                            shapeList[i].index.clear();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                popup.dismisslevelpop();
                                levelCheck();
                            }
                        }, 3000);
                    } else if (level == 3 && levelScore >= 7) {
                        popup.startlevelpop();
                        levelImageCount = 16;
                        condition = 2;
                        level++;
                        levelScore = 0;
                        clickedImageTags.clear();
                        notToClickImageIndex.clear();
                        for (int i = 0; i < noOfColours; i++) {
                            colourList[i].count = 0;
                            colourList[i].index.clear();

                        }
                        for (int i = 0; i < shapes.length; i++) {
                            shapeList[i].count = 0;
                            shapeList[i].index.clear();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                popup.dismisslevelpop();
                                levelCheck();
                            }
                        }, 3000);
                    } else {
                        gameOver();

                        missedScore = totalanswers - correctScore;
                        Log.i("Correct Score ", correctScore + "\nWrong Score :" + wrongScore + "\nMissed Score :" + missedScore + "\nTotal Score :" + totalanswers + "\nNo of questions :" + noOfQuestions);
                        String email = db.getEmailForChild(sessionManagement.getTableID());
                        if (correctScore == 0) {
                            analysis = 0;
                        } else {
                            analysis = (int) doInference(correctScore, wrongScore, missedScore, totalanswers, noOfQuestions);
                        }
                        db.insertScore(email, sessionManagement.getnaaam(), correctScore, wrongScore, totalanswers);
                        db.insert_findmatch_analysis(analysis, email);
                        Log.i("anal:", " " + analysis);
                        noOfQuestions = 0;
                        correctScore = 0;
                        wrongScore = 0;
                        levelScore = 0;
                        totalanswers = 0;
                        missedScore = 0;
                        Intent homepage = new Intent(FindTheMatch.this, FindMatchInstruct.class);
                        ;
                        homepage.putExtra("Game", "Over");
                        startActivity(homepage);
                        finish();

                    }

                }
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
    public void gameOver(){
        //Log.i("Game ","Over");
        Toast.makeText(this,"Game Over",Toast.LENGTH_SHORT);
    }

    private MappedByteBuffer loadModelfile() throws IOException {
        AssetFileDescriptor assetFileDescriptor=this.getAssets().openFd("findmatch.tflite");
        FileInputStream fileInputStream=new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel=fileInputStream.getChannel();
        long startOffset=assetFileDescriptor.getStartOffset();
        long length=assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,length);

    }

    public  float doInference(int correctScore, int wrongScore,int missedScore, int totalanswers,int noOfQuestions){
        float[][] input= new float[1][5];
        input[0][0]=(float)(correctScore);
        input[0][1]=(float)(wrongScore);
        input[0][2]=(float)(missedScore);
        input[0][3]=(float)(totalanswers);
        input[0][4]=(float)(noOfQuestions);
        float[][] output=new float[1][1];
        interpreter.run(input,output);
        return output[0][0];
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            back=1;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}