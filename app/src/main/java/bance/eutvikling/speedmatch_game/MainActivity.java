package bance.eutvikling.speedmatch_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // Game: different shapes slides from right to left, stops in the middle, player must chose buttons
    // yes or no to answer  Does this symbol match the previous symbol.
    // score calculates by amount of right answers in a given time.

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}