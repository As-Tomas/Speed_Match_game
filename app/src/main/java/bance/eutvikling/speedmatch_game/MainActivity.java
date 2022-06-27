package bance.eutvikling.speedmatch_game;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    // Game: different shapes slides from right to left, stops in the middle, player must chose buttons
    // yes or no to answer  Does this symbol match the previous symbol.
    // score calculates by amount of right answers in a given time.

    ActivityResultLauncher<Intent> game_launcher;
    public ArrayList<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_launcher =registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            setContentView(R.layout.activity_main);
                            Intent intent=result.getData();

                            //Save result and display it
                            float points=intent.getFloatExtra("Points",0);
                            saveDB(points);
//TODO display as list to top 10 scores
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
                game_launcher.launch(intent);
            }
        });
    }

    public void saveDB(float points){
        SharedPreferences sharedPrefs = this.getSharedPreferences("speedMatchPoints",this.MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPrefs.edit();

        if(results == null){
            results = new ArrayList();
        }

        results.add(String.valueOf(points));

        Set<String> set  = new HashSet<String>();
        set.addAll(results);

        edit.putStringSet("Points", set);
    }

    public void readDB(){
        SharedPreferences sharedPrefs = this.getSharedPreferences("speedMatchPoints",this.MODE_PRIVATE);
        Set<String> set = sharedPrefs.getStringSet("Points", null);
        if(results == null){
            results=new ArrayList<String>(set);
        } else {
            results.clear();
            results=new ArrayList<String>(set);
        }

    }


}