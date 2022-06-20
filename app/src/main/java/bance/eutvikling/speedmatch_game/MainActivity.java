package bance.eutvikling.speedmatch_game;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Game: different shapes slides from right to left, stops in the middle, player must chose buttons
    // yes or no to answer  Does this symbol match the previous symbol.
    // score calculates by amount of right answers in a given time.

    ActivityResultLauncher<Intent> game_louncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_louncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent intent=result.getData();
                            float points=intent.getFloatExtra("Points",0);
                            TextView resultPoints = findViewById(R.id.result);
                            resultPoints.setText("Points :  "+points);
                        }
                    }
                }
        );


        Button btn = findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent i=new Intent( , GameActivity.class);
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                game_louncher.launch(intent);
            }
        });
    }

}