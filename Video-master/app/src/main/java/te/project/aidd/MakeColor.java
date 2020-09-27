package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MakeColor extends AppCompatActivity {
    Button redb,yellowb,whiteb,blackb,blueb,greenb;
    ImageView yourcolor, givencolor;
    String colorvalue="";
    private int count=-1;
    String checktag="";
    pop popup=new pop(MakeColor.this);
    Random r=new Random();
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_color);
        redb=(Button) findViewById(R.id.redb);
        yellowb=(Button) findViewById(R.id.yellowb);
        greenb=(Button) findViewById(R.id.greenb);
        blackb=(Button) findViewById(R.id.blackb);
        blueb=(Button) findViewById(R.id.blueb);
        whiteb=(Button) findViewById(R.id.whiteb);
        yourcolor=findViewById((R.id.yourcolor));
        givencolor=findViewById((R.id.givencolor));
        clear=findViewById(R.id.clear);
        count=r.nextInt(5);
        newQuestion();
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
                yourcolor.setImageResource(R.drawable.plain);
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
            default:
                colorvalue="";
                popup.startincorrect();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popup.dismissincorrect();
                        yourcolor.setImageResource(R.drawable.plain);
                        newQuestion();

                    }
                },2000);

                break;
        }
    }

    public void newQuestion(){
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
//        else{
//            popup.startincorrect();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    popup.dismissincorrect();
//                    yourcolor.setImageResource(R.drawable.plain);
//                    newQuestion();
//
//                }
//            },2000);
//
//        }
    }



}