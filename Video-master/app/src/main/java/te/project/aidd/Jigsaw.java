package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Jigsaw extends AppCompatActivity {

    int noOfRows=3,noOfColumn=3;
    int rotationBy[];
    int rotatedBy[];
    int level=1;
    int wrongMoves=0;
    private TextView timer ;
    private static final long COUNTDOWN_IN1=30000;
    private static final long COUNTDOWN_IN2=30000;
    private CountDownTimer cd;
    private long timeleft,timetaken;
    boolean correctAnswer=false;
    ArrayList<Integer> shuffle;
    ArrayList<Integer> clickedImageTags;
    ImageView[] imageViews;
    ArrayList<Bitmap> images;
    int[] arr;

    int swapsRequired,swapsDone;
    Date currentTime;
    Date generateTime;

    ArrayList<Date> time=new ArrayList<>();
    TextView answer,levelNo;
    boolean generatedImage=false;

    pop popup=new pop(Jigsaw.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);
        currentTime = Calendar.getInstance().getTime();
        time.add(currentTime);
        Log.i("From ","Oncreate");
        generateImage();

    }
    public void generateImage(){

        generateTime=Calendar.getInstance().getTime();
        answer=(TextView)findViewById(R.id.correctAns);
        levelNo=(TextView)findViewById(R.id.level);
        answer.setText("");
        ArrayList<Bitmap> bs = new ArrayList<Bitmap>();
        timer = (TextView) findViewById(R.id.time);
        if(correctAnswer){
            cd.cancel();
        }
        correctAnswer=false;
        if(level==1 || level==2) {
            timeleft = COUNTDOWN_IN1;
        }
        else
        {
            timeleft=COUNTDOWN_IN2;
        }

        startCountDown();
        shuffle=new ArrayList();
        for(int i=0;i<noOfColumn*noOfRows; i++)
        {
            shuffle.add(i);
        }
        Collections.shuffle(shuffle);
        Random rand = new Random();
        int[] drawables=new int[]{R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6,R.drawable.image8,R.drawable.image9,R.drawable.image10};
        int imageSelection=rand.nextInt(drawables.length);
        Bitmap b = BitmapFactory.decodeResource(getResources(), drawables[imageSelection]);
        images = divideImages(b);
        TextView question=(TextView)findViewById(R.id.Question);
        if(noOfRows==3) {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9)};
        }
        else if(noOfRows==4)
        {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9),(ImageView) findViewById(R.id.imageView10),(ImageView) findViewById(R.id.imageView11),(ImageView) findViewById(R.id.imageView12),(ImageView) findViewById(R.id.imageView13),(ImageView) findViewById(R.id.imageView14),(ImageView) findViewById(R.id.imageView15), (ImageView) findViewById(R.id.imageView16)};
        }
        levelNo.setText("Level : "+level);
        if(level==1 || level==3) {

            rotationBy=new int[noOfColumn*noOfRows];
            rotatedBy=new int[noOfColumn*noOfRows];
            question.setText("Rotate to find the Final Image");
            for (int i = 0; i < imageViews.length; i++) {
                Log.i("Counter", String.valueOf((i)));
                int x = rand.nextInt(3);
                rotationBy[i] = 3 - x;
                imageViews[i].animate().rotationBy(90 + x * 90);
                imageViews[i].setImageBitmap(images.get(i));
                imageViews[i].setPadding(4,4,4,4);
                imageViews[i].setBackgroundColor(Color.WHITE);

            }
        }
        else if(level==2 || level==4)
        {
            arr=new int[shuffle.size()];
            for(int x=0;x<shuffle.size();x++)
            {
                arr[x]=shuffle.get(x)+1;
            }
            Log.i("Shuffle", String.valueOf(shuffle));
            Log.i("Array", Arrays.toString(arr));
            swapsRequired =Sort.minimumSwaps(arr);
            question.setText("Swap tiles to find the Final Image");
            for (int i = 0; i < noOfRows*noOfColumn; i++) {
                imageViews[i].setImageBitmap(images.get(shuffle.get(i)));
            }
            clickedImageTags=new ArrayList<>();

        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 200);
        generatedImage=true;
        Log.i("Original ", Arrays.toString(rotationBy));
    }
    private ArrayList<Bitmap> divideImages(Bitmap b) {


        final int width = b.getWidth();
        final int height = b.getHeight();
        ArrayList<Bitmap> bs = new ArrayList<Bitmap>();
        final int pixelByCol = width / noOfColumn;
        final int pixelByRow = height / noOfRows;

        for (int i = 0; i < noOfColumn; i++) {
            for (int j = 0; j < noOfRows; j++) {
                int startx = pixelByCol * j;
                int starty = pixelByRow * i;
                Bitmap b1 = Bitmap.createBitmap(b, startx, starty, pixelByCol, pixelByRow);
                bs.add(b1);

                b1 = null;
            }
        }
        b = null;
        return bs;
    }
    public void onClickImageView(View view)
    {
        if(!correctAnswer) {
            Date now = Calendar.getInstance().getTime();
            long diff = now.getTime() - generateTime.getTime();
            if (generatedImage && diff > 500) {
                final ImageView image = (ImageView) view;
                if (level == 1 || level == 3) {
                    currentTime = Calendar.getInstance().getTime();
                    time.add(currentTime);
                    Long time1 = time.get(time.size() - 1).getTime();
                    Long time2 = time.get(time.size() - 2).getTime();

                    long difference = time1 - time2;
                    if (difference > 200) {
                        Log.i("Rotated by 90", "Successfull");
                        //image.animate().rotationBy(90).start();
                        image.setRotation(image.getRotation() + 90F);
                        int taggedCounter = Integer.parseInt(image.getTag().toString());
                        rotatedBy[taggedCounter]++;
                        if (rotatedBy[taggedCounter] - rotationBy[taggedCounter] == 1)
                            wrongMoves++;
                        if (rotatedBy[taggedCounter] == 4) {
                            rotatedBy[taggedCounter] = 0;
                        }

                        checkAnswers();
                    }

                } else if (level == 2 || level == 4) {

                    int taggedCounter = Integer.parseInt(image.getTag().toString());
                    if (clickedImageTags.contains((taggedCounter))) {
                    } else {
                        clickedImageTags.add(taggedCounter);
                    }

                    if (clickedImageTags.size() == 2) {
                        swapsDone++;
                        int firstTagImage = clickedImageTags.get(0);
                        int secondTagImage = taggedCounter;
                        imageViews[firstTagImage].setImageBitmap(images.get(shuffle.get(secondTagImage)));
                        imageViews[secondTagImage].setImageBitmap(images.get(shuffle.get(firstTagImage)));
                        Collections.swap(shuffle, firstTagImage, secondTagImage);
                        clickedImageTags.clear();
                        Log.i("Shuffle ", String.valueOf(shuffle));

                        checkAnswers2();
                    }
                }
            }
        }
    }

    private void checkAnswers2() {
        int i=0;
        for(i=0;i<noOfRows*noOfColumn;i++)
        {
            if(shuffle.get(i) !=i)
            {
                break;
            }
        }
        if(i==(noOfColumn*noOfRows))
        {
            answer.setText("Correct Answer!");
            Log.i("Hi","Here");
            correctAnswer=true;

        }
        if(correctAnswer && level==2)
        {
            popup.startlevelpop();
            level++;
            timetaken=30000-timeleft;
            timeleft=0;
            noOfColumn=4;
            noOfRows=4;
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generatedImage=false;
                    popup.dismisslevelpop();
                }
            }, 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_jigsaw16);
                    generateImage();
                }
            }, 1000);

        }
        if(correctAnswer && level==4)
        {
            timetaken=30000-timeleft;
            popup.startpop();
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.dismisspop();
                    }
                }, 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homepage=new Intent(Jigsaw.this, JigsawInstructActivity.class);
                    startActivity(homepage);
                }
            }, 1000);

        }

    }

    public void checkAnswers() {
        int i;
        for(i=0;i<noOfRows*noOfColumn;i++)
        {
            if(rotatedBy[i]!=rotationBy[i]) {
                break; }
        }
        if(i==(noOfColumn*noOfRows))
        {
            correctAnswer=true;
            answer.setText("Correct Answer!!");
        }
        if(correctAnswer && (level==1 || level==3 ))
        {
            level++;
            timetaken=30000-timeleft;
            popup.startlevelpop();
            timeleft=0;
            wrongMoves=0;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.dismisslevelpop();
                    generatedImage=false;
                    generateImage();
                }
            }, 3000);
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
                timetaken=30000;
                wrongMoves=0;
                level=1;
                noOfColumn=3;
                noOfRows=3;
                popup.startpop();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popup.dismisspop();
                    }
                }, 3000);


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent homepage=new Intent(Jigsaw.this, JigsawInstructActivity.class);
                        startActivity(homepage);
                    }
                }, 1000);

                Log.i("From","onFinish");
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