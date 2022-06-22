package bance.eutvikling.speedmatch_game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

//        if(points > 10){
//            Intent retIntent=new Intent();
//            retIntent.putExtra("Points", points);
//            setResult(Activity.RESULT_OK,retIntent);
//            finish();
//        }
    }

    public void setCurrentFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}