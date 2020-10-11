package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class JigsawInstructActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw_instruct);
    }
    public void goTo(View view){
        Intent homepage=new Intent(JigsawInstructActivity.this, Jigsaw.class);
        startActivity(homepage);
    }
}