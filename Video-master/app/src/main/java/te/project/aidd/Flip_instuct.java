package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Flip_instuct extends AppCompatActivity {
 Button flipinstrut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_instuct);
        flipinstrut=(Button) findViewById(R.id.flipin);
        flipinstrut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fin=new Intent(Flip_instuct.this,FlipCards.class);
                startActivity(fin);
            }
        });
    }
}