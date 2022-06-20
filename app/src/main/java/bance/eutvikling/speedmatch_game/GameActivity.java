package bance.eutvikling.speedmatch_game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private float points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //play game  / return points

        points = 12;



        Intent retIntent=new Intent();
        retIntent.putExtra("Points", points);
        setResult(Activity.RESULT_OK,retIntent);
        finish();
    }
}