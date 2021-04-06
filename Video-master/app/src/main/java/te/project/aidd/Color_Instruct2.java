package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Color_Instruct2 extends AppCompatActivity {
    Button cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color__instruct2);
        cm=(Button) findViewById(R.id.cm);
        cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cmin=new Intent(Color_Instruct2.this,MakeColor.class);
                startActivity(cmin);


                finish();
            }
        });
    }
}