package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Pair{
    String type;
    int count=0;
    List index=new ArrayList();
    Pair(String colour)
    {
        this.type=colour;
        count=0;
    }
}
public class FindTheMatch extends AppCompatActivity {


    String [] colours={"black","blue","brown","grey","orange","purple","red","yellow","green"};
    int noOfColours=colours.length;
    Pair[] colourList=new Pair[noOfColours];
    String [] shapes={"square","triangle","circle"};
    Pair[] shapeList=new Pair[shapes.length];
    List clickedImageTags=new ArrayList();
    String critertia="";
    int criteriaIndex;
    int choice;
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
        Collections.sort(clickedImageTags);
        ImageView[] imageViews=new ImageView[]{(ImageView)findViewById(R.id.imageView1),(ImageView)findViewById(R.id.imageView2),(ImageView)findViewById(R.id.imageView3),(ImageView)findViewById(R.id.imageView4),(ImageView)findViewById(R.id.imageView5),(ImageView)findViewById(R.id.imageView6),(ImageView)findViewById(R.id.imageView7),(ImageView)findViewById(R.id.imageView8),(ImageView)findViewById(R.id.imageView9)};
        if(choice==0) {
            if (colourList[criteriaIndex].index.size() == clickedImageTags.size() && colourList[criteriaIndex].index.equals(clickedImageTags)) {
                Toast.makeText(this, "All images selected", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < clickedImageTags.size(); i++) {
                    imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            reset();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);



            } else if (clickedImageTags.containsAll(colourList[criteriaIndex].index) ) {
                for(int i=0;i<clickedImageTags.size();i++)
                {
                    if(colourList[criteriaIndex].index.contains((Object)clickedImageTags.get(i))) {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                    }
                    else
                    {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                    }
                }
                Toast.makeText(this, "Extra images selected", Toast.LENGTH_SHORT).show();
                resetAnswers(view,imageViews);

            } else {
                for(int i=0;i<clickedImageTags.size();i++)
                {
                    if(colourList[criteriaIndex].index.contains((Object)clickedImageTags.get(i))) {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                    }
                    else
                    {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                    }
                }
                Toast.makeText(this, "All images not selected", Toast.LENGTH_SHORT).show();
                resetAnswers(view,imageViews);
            }
        }
        else
        {
            if(shapeList[criteriaIndex].index.size() == clickedImageTags.size())
            {
                if(shapeList[criteriaIndex].index.equals(clickedImageTags)) {
                    Toast.makeText(this, "All images selected", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < clickedImageTags.size(); i++) {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                reset();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                }
            }
            else if(clickedImageTags.containsAll(shapeList[criteriaIndex].index))
            {
                for(int i=0;i<clickedImageTags.size();i++)
                {
                    if(shapeList[criteriaIndex].index.contains((Object)clickedImageTags.get(i))) {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                    }
                    else
                    {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                    }
                }
                Toast.makeText(this, "Extra images selected", Toast.LENGTH_SHORT).show();
                resetAnswers(view,imageViews);
            }else {
                for(int i=0;i<clickedImageTags.size();i++)
                {
                    if(shapeList[criteriaIndex].index.contains((Object)clickedImageTags.get(i))) {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squaregreen);
                    }
                    else
                    {
                        imageViews[(int) clickedImageTags.get(i)].setBackgroundResource(R.drawable.squarered);
                    }
                }
                Toast.makeText(this, "All images not selected", Toast.LENGTH_SHORT).show();
                resetAnswers(view,imageViews);
            }
        }
    }
    public void resetAnswers(View view, final ImageView imageViews[])
    {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<clickedImageTags.size();i++)
                    imageViews[(int) clickedImageTags.get(i)].setBackgroundColor(Color.WHITE);
                clickedImageTags.clear();
            }
        }, 1000);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findthematch);
        generateImages();
    }
    public void generateImages(){
        Random rand = new Random();
        for(int i=0;i<noOfColours;i++)
        {
            colourList[i]=new Pair(colours[i]);
        }
        for(int i=0;i<shapes.length;i++)
        {
            shapeList[i]=new Pair(shapes[i]);
        }


        int[][] drawables=new int[][]{{R.drawable.squareblack, R.drawable.squareblue, R.drawable.squarebrown, R.drawable.squaregrey, R.drawable.squareorange, R.drawable.squarepurple, R.drawable.squarered, R.drawable.squareyellow, R.drawable.squaregreen},{R.drawable.triangleblack, R.drawable.triangleblue, R.drawable.trianglebrown, R.drawable.trianglegrey, R.drawable.triangleorange, R.drawable.trianglepurple, R.drawable.trianglered, R.drawable.triangleyellow, R.drawable.trianglegreen},{R.drawable.circleblack, R.drawable.circleblue, R.drawable.circlebrown, R.drawable.circlegrey, R.drawable.circleorange, R.drawable.circlepurple, R.drawable.circlered, R.drawable.circleyellow, R.drawable.circlegreen}};
        ImageView[] imageViews=new ImageView[]{(ImageView)findViewById(R.id.imageView1),(ImageView)findViewById(R.id.imageView2),(ImageView)findViewById(R.id.imageView3),(ImageView)findViewById(R.id.imageView4),(ImageView)findViewById(R.id.imageView5),(ImageView)findViewById(R.id.imageView6),(ImageView)findViewById(R.id.imageView7),(ImageView)findViewById(R.id.imageView8),(ImageView)findViewById(R.id.imageView9)};
        for (int i = 0; i < imageViews.length; i++) {
            int shape=rand.nextInt(shapes.length);
            int color = rand.nextInt(noOfColours);
            imageViews[i].setImageResource(drawables[shape][color]);
            colourList[color].count++;
            colourList[color].index.add(i);
            shapeList[shape].count++;
            shapeList[shape].index.add(i);
        }

        generateQuestion();
    }
    public void generateQuestion()
    {
        Random rand = new Random();
        boolean flag=true;

        choice=rand.nextInt(2);
        if(choice==0) {
            while(flag==true) {
                int pick = rand.nextInt(noOfColours);
                if (colourList[pick].count > 1) {
                    flag = false;
                    critertia = colourList[pick].type;
                }
            }
        }
        else if(choice==1)
        {
            while(flag==true) {
                int pick = rand.nextInt(shapes.length);
                if (shapeList[pick].count > 1) {
                    flag = false;
                    critertia = shapeList[pick].type;
                }
            }
        }
        TextView questionTextView=(TextView)findViewById(R.id.questionTextView);
        questionTextView.setVisibility(View.VISIBLE);

        questionTextView.setText("Find all "+critertia+" images");
        if(choice==0) {
            for (int i = 0; i < noOfColours; i++) {
                if (critertia.equals(colours[i])) {
                    criteriaIndex = i;
                    break;
                }
            }
        }
        else {
            for (int i = 0; i < shapes.length; i++) {
                if (critertia.equals(shapes[i])) {
                    criteriaIndex = i;
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
        ImageView[] imageViews=new ImageView[]{(ImageView)findViewById(R.id.imageView1),(ImageView)findViewById(R.id.imageView2),(ImageView)findViewById(R.id.imageView3),(ImageView)findViewById(R.id.imageView4),(ImageView)findViewById(R.id.imageView5),(ImageView)findViewById(R.id.imageView6),(ImageView)findViewById(R.id.imageView7),(ImageView)findViewById(R.id.imageView8),(ImageView)findViewById(R.id.imageView9)};
        for(int i=0;i<imageViews.length;i++)
        {
            imageViews[i].setBackgroundColor(Color.WHITE);
        }
        generateImages();
    }
}