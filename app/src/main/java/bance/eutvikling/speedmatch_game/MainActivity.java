package bance.eutvikling.speedmatch_game;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
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
    //TODO more shapes to differ game levels.


    ActivityResultLauncher<Intent> game_launcher;
    public ArrayList<String> results;
    public ArrayList<Integer> resultsToDisplayInt;
    public ArrayList<String> resultsToDisplay;
    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsToDisplay = new ArrayList();

        try {
            readDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateResultsToDisplay( -1);

        listView =  (ListView) findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(
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
                            Intent intent=result.getData();

                            //Save result and display it
                            int points=intent.getIntExtra("Points",0);

                            updateResultsToDisplay(points);

                            adapter.notifyDataSetChanged();
                            saveDB(points);

                        }
                    }
                }
        );


        Button btn = findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), GameActivity.class);
                game_launcher.launch(intent);

            }
        });
    }



    public void updateResultsToDisplay(int newPoints){
        resultsToDisplay.clear();
        resultsToDisplayInt = new ArrayList();

        if (newPoints == -1 ){ // for view constructor calls
            //limit to 10 results
            for(int i=0; i < 10; i++){
                if(results.size() > i){
                    resultsToDisplayInt.add(Integer.parseInt(results.get(i)));
                } else {
                    break;
                }
            }
            if(resultsToDisplayInt.size() > 1){
                Collections.sort(resultsToDisplayInt);
                Collections.reverse(resultsToDisplayInt);
            }

            for(int i=0; i < 10; i++){
                if(results.size() > i){
                    resultsToDisplay.add("Nr " + (i+1) + ":   " + resultsToDisplayInt.get(i));
                } else {
                    break;
                }
            }

        } else if (results.size() > 0){

            //limit to 10 results
            for(int i=0; i < 10; i++){
                if(results.size() > i){
                    resultsToDisplayInt.add(Integer.parseInt(results.get(i)));
                } else {
                    break;
                }
            }
            if(resultsToDisplayInt.size() > 1){
                Collections.sort(resultsToDisplayInt);
                Collections.reverse(resultsToDisplayInt);
            }

            //search for position in list of results to set new result
            int idx = 0;
            for (int i = 0; i < resultsToDisplayInt.size(); i++) {
                if (resultsToDisplayInt.get(i)<newPoints) {
                    break;
                }
                idx++;
            }
            // set new result before old one
            if(idx > 0){ idx--; }
            //resultsToDisplayInt.add(idx, newPoints);
            for(int i=0; i < 10; i++){
                if(i == idx){
                    resultsToDisplay.add(idx,"New result Nr " + (i+1) + ":   " + newPoints);
                } else if(resultsToDisplayInt.size() > i){
                    resultsToDisplay.add("Nr " + (i+1) + ":   " + resultsToDisplayInt.get(i));

                } else {
                    break;
                }
            }
        }

    }

    public void saveDB(int points){
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
        if(results == null && set == null){
            results=new ArrayList<String>();
        } else {
            if(results != null ) {
                results.clear();
            }
            results=new ArrayList<String>(set);
        }
    }
}