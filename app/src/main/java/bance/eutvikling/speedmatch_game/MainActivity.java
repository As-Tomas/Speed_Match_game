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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    // Game: different shapes slides from right to left, stops in the middle, player must chose buttons
    // yes or no to answer  Does this symbol match the previous symbol.
    // score calculates by amount of right answers in a given time.

    ActivityResultLauncher<Intent> game_launcher;
    public ArrayList<String> results;
    public ArrayList<String> resultsToDisplay;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            readDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateResultsToDisplay();

        listView =  (ListView) findViewById(R.id.list);
        ArrayAdapter adapter=new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resultsToDisplay
        );
        listView.setAdapter(adapter);


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

                            adapter.notifyDataSetChanged();

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

    public void updateResultsToDisplay(){

        resultsToDisplay = new ArrayList<String>();
        Collections.sort(results);

        for(int i=0; i < 10; i++){
            if(results.size() > i){
                resultsToDisplay.add("Nr " + (i+1) + ":   " + results.get(i));
            } else {
                break;
            }
        }
    }

    public void saveDB(float points){
        SharedPreferences sharedPrefs = getSharedPreferences("speedMatchPoints", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPrefs.edit();

        if(results == null){
            results = new ArrayList();
        }

        results.add(String.valueOf(points));

        Set<String> set  = new HashSet<String>();
        set.addAll(results);

        editor.putStringSet("Points", set);
        editor.commit();
    }

    public void readDB(){
        SharedPreferences sharedPrefs = getSharedPreferences("speedMatchPoints", MODE_PRIVATE);
        Set<String> set = sharedPrefs.getStringSet("Points", null);
        if(results == null && set.size() == 0){
            results=new ArrayList<String>();
        } else {
            if(results != null ) {
                results.clear();
            }
            results=new ArrayList<String>(set);
        }
    }
}