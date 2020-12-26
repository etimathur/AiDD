package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FindMatchInstruct extends AppCompatActivity {
Button ftm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match_instruct);
        ftm=(Button) findViewById(R.id.ftm);
        ftm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cmin=new Intent(FindMatchInstruct.this,FindTheMatch.class);
                cmin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cmin);
                finish();
            }
        });
    }
}