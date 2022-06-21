package bance.eutvikling.speedmatch_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private int time;
    private float points;
    private TriangleFragment triangleFragment = new TriangleFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //play game  / return points

        points = 12;


        setCurrentFragment(triangleFragment);

//        Intent retIntent=new Intent();
//        retIntent.putExtra("Points", points);
//        setResult(Activity.RESULT_OK,retIntent);
//        finish();
    }

    public void setCurrentFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}