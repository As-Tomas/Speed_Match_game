package bance.eutvikling.speedmatch_game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private int time;
    private float points;
    private TriangleFragment triangleFragment = new TriangleFragment();
    private SquareFragment squareFragment = new SquareFragment();
    private CircleFragment circleFragment = new CircleFragment();

    private ArrayList <Fragment> ShapesFragments = new ArrayList();

    private Random random = new Random();
    private int min = 0;
    private int max = 2;

    private int shapeNumber;
    private int previousShape;

    CountDownTimer cTimer = null;
    TextView mTimeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //play game  / return points


        ShapesFragments.add(triangleFragment);
        ShapesFragments.add(squareFragment);
        ShapesFragments.add(circleFragment);

        shapeNumber = random.nextInt(max - min + 1) + min;
        previousShape = shapeNumber;

        setCurrentFragment(ShapesFragments.get(shapeNumber));

        Button btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(previousShape != shapeNumber){
                    points++;
                }
                previousShape = shapeNumber;
                shapeNumber = random.nextInt(max - min + 1) + min;
                setCurrentFragment(ShapesFragments.get(shapeNumber));
            }
        });

        Button btnYes = findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(previousShape == shapeNumber){
                    points++;
                }
                previousShape = shapeNumber;
                shapeNumber = random.nextInt(max - min + 1) + min;
                setCurrentFragment(ShapesFragments.get(shapeNumber));
            }
        });

        mTimeField = findViewById(R.id.time);
        startTimer();

    }

    public void setCurrentFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeField.setText("seconds remaining: " + millisUntilFinished / 1000);

            }
            public void onFinish() {
                mTimeField.setText("done!");
                cancelTimer();
                //TODO add finish fragment, her should display finish fragment
                Intent retIntent=new Intent();
                retIntent.putExtra("Points", points);
                setResult(Activity.RESULT_OK,retIntent);
                finish();
            }
        };
        cTimer.start();
    }

    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cTimer.cancel();
    }
}