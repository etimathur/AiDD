package te.project.aidd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Jigsaw extends AppCompatActivity {

    int noOfRows = 3, noOfColumn = 3;
    int rotationBy[];
    int rotatedBy[];
    int level = 1;
    int analysis_rotate = 0;
    int analysis_swap = 0;
    int final_result = 0;
    int wrongMoves = 0;
    private long timetaken;
    boolean correctAnswer = false;
    ArrayList<Integer> shuffle;
    ArrayList<Integer> clickedImageTags;
    ImageView[] imageViews;
    ArrayList<Bitmap> images;
    int decision = 0;
    int[] arr;

    int swapsRequired, swapsDone;
    Date currentTime;
    Date generateTime;
    Date currentTime1;
    Date generateTime1;
    Interpreter interpreter;
    DatabaseHelper db;
    ArrayList<Date> time = new ArrayList<>();
    TextView answer, levelNo;
    boolean generatedImage = false;


    pop popup = new pop(Jigsaw.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);
        db = new DatabaseHelper(this);
        try {
            interpreter = new Interpreter(loadModelfile(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            interpreter = new Interpreter(loadModelfile1(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentTime = Calendar.getInstance().getTime();
        time.add(currentTime);
        //Log.i("From ","Oncreate");
        generateImage();

    }

    public void generateImage() {
        correctAnswer=false;

        generateTime = Calendar.getInstance().getTime();
        generateTime1 = Calendar.getInstance().getTime();
        answer = (TextView) findViewById(R.id.correctAns);
        levelNo = (TextView) findViewById(R.id.level);
        answer.setText("");
        ArrayList<Bitmap> bs = new ArrayList<Bitmap>();

        shuffle = new ArrayList();
        for (int i = 0; i < noOfColumn * noOfRows; i++) {
            shuffle.add(i);
        }
        Collections.shuffle(shuffle);
        Random rand = new Random();
        int[] drawables = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10, R.drawable.image12, R.drawable.image11, R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image16};
        int imageSelection = rand.nextInt(drawables.length);
        Bitmap b = BitmapFactory.decodeResource(getResources(), drawables[imageSelection]);
        images = divideImages(b);
        TextView question = (TextView) findViewById(R.id.Question);
        if (noOfRows == 3) {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9)};
        } else if (noOfRows == 4) {
            imageViews = new ImageView[]{(ImageView) findViewById(R.id.imageView1), (ImageView) findViewById(R.id.imageView2), (ImageView) findViewById(R.id.imageView3), (ImageView) findViewById(R.id.imageView4), (ImageView) findViewById(R.id.imageView5), (ImageView) findViewById(R.id.imageView6), (ImageView) findViewById(R.id.imageView7), (ImageView) findViewById(R.id.imageView8), (ImageView) findViewById(R.id.imageView9), (ImageView) findViewById(R.id.imageView10), (ImageView) findViewById(R.id.imageView11), (ImageView) findViewById(R.id.imageView12), (ImageView) findViewById(R.id.imageView13), (ImageView) findViewById(R.id.imageView14), (ImageView) findViewById(R.id.imageView15), (ImageView) findViewById(R.id.imageView16)};
        }
        levelNo.setText("Level : " + level);
        if (level == 1 || level == 2) {

            rotationBy = new int[noOfColumn * noOfRows];
            rotatedBy = new int[noOfColumn * noOfRows];
            question.setText("Rotate to find the Final Image");
            for (int i = 0; i < imageViews.length; i++) {
                //Log.i("Counter", String.valueOf((i)));
                int x = rand.nextInt(3);
                rotationBy[i] = 3 - x;
                imageViews[i].animate().rotationBy(90 + x * 90);
                imageViews[i].setImageBitmap(images.get(i));
                imageViews[i].setPadding(4, 4, 4, 4);
                imageViews[i].setBackgroundColor(Color.WHITE);

            }
        } else if (level == 3 || level == 4) {
            arr = new int[shuffle.size()];
            for (int x = 0; x < shuffle.size(); x++) {
                arr[x] = shuffle.get(x) + 1;
            }
            //Log.i("Shuffle", String.valueOf(shuffle));
            //Log.i("Array", Arrays.toString(arr));
            swapsRequired = Sort.minSwaps(arr, arr.length);
            question.setText("Swap tiles to find the Final Image");
            for (int i = 0; i < noOfRows * noOfColumn; i++) {
                imageViews[i].setImageBitmap(images.get(shuffle.get(i)));
            }
            clickedImageTags = new ArrayList<>();

        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 200);
        generatedImage = true;
        //Log.i("Original ", Arrays.toString(rotationBy));
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

    public void onClickImageView(View view) {
        if (!correctAnswer) {
            Date now = Calendar.getInstance().getTime();
            long diff = now.getTime() - generateTime.getTime();
            // Log.i("Level", String.valueOf(level));

            if (generatedImage && diff > 500) {
                final ImageView image = (ImageView) view;
                if (level == 1 || level == 2) {
//                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
//                    image.startAnimation(animation);


                    currentTime = Calendar.getInstance().getTime();
                    time.add(currentTime);
                    Long time1 = time.get(time.size() - 1).getTime();
                    Long time2 = time.get(time.size() - 2).getTime();

                    long difference = time1 - time2;
                    if (difference > 200) {
                        //Log.i("Rotated by 90", "Successfull");
                        //image.setColorFilter(Color.CYAN, PorterDuff.Mode.LIGHTEN);
                        //image.animate().rotationBy(90).start();
                        image.setOnTouchListener(new View.OnTouchListener() {
                            private Rect rect;

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    image.setColorFilter(Color.argb(50, 0, 0, 0));
                                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                                }
                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                    image.setColorFilter(Color.argb(0, 0, 0, 0));
                                }
                                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                    if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                                        image.setColorFilter(Color.argb(0, 0, 0, 0));
                                    }
                                }
                                return false;
                            }
                        });
                        image.setRotation(image.getRotation() + 90F);
                        int taggedCounter = Integer.parseInt(image.getTag().toString());
                        rotatedBy[taggedCounter]++;
                        if (rotatedBy[taggedCounter] - rotationBy[taggedCounter] == 1)
                            wrongMoves++;
                        if (rotatedBy[taggedCounter] == 4) {
                            rotatedBy[taggedCounter] = 0;
                        }
//                        Handler handler = new Handler();
//
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                image.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
//                            }
//                        }, 100);
                        checkAnswers();
                    }

                } else if (level == 3 || level == 4) {
                    int taggedCounter = Integer.parseInt(image.getTag().toString());

                    if (clickedImageTags.contains((taggedCounter))) {
                        //image.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                        image.setBackgroundResource(R.drawable.squarewhite);
                        clickedImageTags.remove(clickedImageTags.indexOf(taggedCounter));
                    } else {
                        clickedImageTags.add(taggedCounter);
                        //image.setColorFilter(0xD3D3D3, PorterDuff.Mode.LIGHTEN);

                        image.setBackgroundResource(R.drawable.squareblack);
                    }

                    if (clickedImageTags.size() == 2) {
                        swapsDone++;
                        int firstTagImage = clickedImageTags.get(0);
                        int secondTagImage = taggedCounter;
                        imageViews[firstTagImage].setImageBitmap(images.get(shuffle.get(secondTagImage)));
                        imageViews[secondTagImage].setImageBitmap(images.get(shuffle.get(firstTagImage)));
                        Collections.swap(shuffle, firstTagImage, secondTagImage);
                        imageViews[firstTagImage].setBackgroundResource(R.drawable.squarewhite);
                        imageViews[secondTagImage].setBackgroundResource(R.drawable.squarewhite);
                        clickedImageTags.clear();
                        //Log.i("Shuffle ", String.valueOf(shuffle));
                        image.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                        checkAnswers2();
                    }
                }
            }
        }
    }

    private void checkAnswers2() {
        int i = 0;
        for (i = 0; i < noOfRows * noOfColumn; i++) {
            if (shuffle.get(i) != i) {
                break;
            }
        }
        if (i == (noOfColumn * noOfRows)) {
            answer.setText("Correct Answer!");
            //Log.i("Hi", "Here");
            correctAnswer = true;


        }
        if (correctAnswer && level == 3) {
            popup.startlevelpop();
            level++;
            Date now1 = Calendar.getInstance().getTime();
            long diff1 = now1.getTime() - generateTime1.getTime();
            timetaken = diff1;
            Log.i("Terms :", swapsDone + " " + swapsRequired + " " + timetaken);
            int timetakens = (int) timetaken / 1000;
            analysis_swap = (int) doInference1(swapsRequired, swapsDone, timetakens);
            Log.i("swaps_model", analysis_swap + " ");
            noOfColumn = 4;
            noOfRows = 4;
            correctAnswer=false;
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generatedImage = false;
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
        if (correctAnswer && level == 4) {
            //Log.i("Level","4");
            Date now1 = Calendar.getInstance().getTime();
            long diff1 = now1.getTime() - generateTime1.getTime();
            timetaken = diff1;
            Log.i("Terms :", swapsDone + " " + swapsRequired + " " + timetaken);
            popup.startpop();
            Handler handler = new Handler();
            int timetakens = (int) timetaken / 1000;
            int analysis_swap1 = (int) doInference1(swapsRequired, swapsDone, timetakens);

            analysis_swap=(analysis_swap+analysis_swap1)/2;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.dismisspop();
                }
            }, 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homepage = new Intent(Jigsaw.this, JigsawInstructActivity.class);
                    startActivity(homepage);
                }
            }, 1000);

//            timeleft = 0;
//            timetaken = 30000;
//            wrongMoves = 0;
//            level = 1;
//            noOfColumn = 3;
//            noOfRows = 3;
            //Log.i("Terms :",swapsDone+" "+swapsRequired+" "+timetaken);
            //Log.i("Terms :",wrongMoves+" "+timetaken);
            if (decision != 1) {
                SessionManagement ses = new SessionManagement(Jigsaw.this);
                String email = db.getEmailForChild(ses.getTableID());
                final_result = (int) ((analysis_rotate/2 + analysis_swap) / 2);
                if(final_result>100)
                    final_result=100;
                db.insert_puzzle_analysis(final_result, email);
                Log.i("final",final_result+" ");
                Log.i("data", "saved");
                popup.startpop();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popup.dismisspop();
                    }
                }, 3000);


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent homepage = new Intent(Jigsaw.this, JigsawInstructActivity.class);
                        startActivity(homepage);
                    }
                }, 1000);

                //Log.i("From","onFinish");
            }

        }

    }


    public void checkAnswers() {
        int i;
        for (i = 0; i < noOfRows * noOfColumn; i++) {
            if (rotatedBy[i] != rotationBy[i]) {
                break;
            }
        }
        if (i == (noOfColumn * noOfRows)) {
            correctAnswer = true;
            answer.setText("Correct Answer!!");
        }
        if (correctAnswer && (level == 1 || level == 2)) {
            if(level==1)
            {
                noOfColumn=4;
                noOfRows=4;

            }
            else if(level==2)
            {
                noOfColumn=3;
                noOfRows=3;
            }
            level++;
            Date now1 = Calendar.getInstance().getTime();
            long diff1 = now1.getTime() - generateTime1.getTime();
            timetaken = diff1;
            int timetakens = (int) timetaken / 1000;
            Log.i("Terms :", wrongMoves + " " + timetaken);
            analysis_rotate += (int) doInference(wrongMoves, timetakens);
            Log.i("modell : ", analysis_rotate + " ");
            popup.startlevelpop();
            wrongMoves = 0;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.dismisslevelpop();

                }
            }, 3000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generatedImage = false;
                    if(level==2)
                    {
                        setContentView(R.layout.activity_jigsaw16);
                    }
                    else if(level==3)
                    {
                        setContentView(R.layout.activity_jigsaw);
                    }
                    generateImage();
                }
            }, 3000);
        }
    }



    public void onBackPressed() {


    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            decision = 1;
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit the game?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Jigsaw.super.onBackPressed();
                    if(level>1) {
                        SessionManagement ses = new SessionManagement(Jigsaw.this);
                        String email = db.getEmailForChild(ses.getTableID());
                        final_result = (int) ((analysis_rotate / 2 + analysis_swap) / 2);
                        db.insert_puzzle_analysis(final_result, email);
                        Log.i("final", final_result + " ");
                        Log.i("data", "saved");
                    }
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();



        }
        return super.onKeyDown(keyCode, event);
    }

    private MappedByteBuffer loadModelfile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("rotate_puzzle.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);

    }

    public float doInference(int wrongMoves, int timetaken) {
        float[][] input = new float[1][2];
        input[0][0] = (float) (wrongMoves);
        input[0][1] = (float) (timetaken);
        float[][] output = new float[1][1];
        interpreter.run(input, output);
        return output[0][0];
    }

    public float doInference1(int swapsRequired, int swapsDone, int timetaken) {
        float[][] input = new float[1][3];
        input[0][0] = (float) (swapsRequired);
        input[0][1] = (float) (swapsDone);
        input[0][2] = (float) (timetaken);
        float[][] output = new float[1][1];
        interpreter.run(input, output);
        return output[0][0];
    }

    private MappedByteBuffer loadModelfile1() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("swap_puzzle.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }
}

