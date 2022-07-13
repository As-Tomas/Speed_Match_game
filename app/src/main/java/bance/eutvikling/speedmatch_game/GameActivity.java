package bance.eutvikling.speedmatch_game;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Random;
//TODO result fragment show up for few seconds.
//TODO addToBackStack stack true to allow quit game to main activity
//TODO user allow chose level time
//TODO function for points calculation with involving sets of right / wrong answers
//TODO sounds on change fragment or push button
public class GameActivity extends AppCompatActivity {
    private int time = 60000; //60 seconds
    private int points;
    private int incrementStep = 1;
    private int fiveInTheRow = 0;
    private int tenInTheRow = 0;
    private int totalWrong = 0;
    private int totalShownCards = 0;

    private Context mContext;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private TriangleFragment triangleFragment ;
    private SquareFragment squareFragment ;
    private CircleFragment circleFragment ;

    private ArrayList <Fragment> ShapesFragments = new ArrayList();

    private Random random = new Random();
    private int min = 0;
    private int max = 2;

    private int shapeNumber;
    private int previousShape;

    CountDownTimer cTimer = null;
    TextView mTimeField;
    Handler handler = new Handler();
    Runnable runnable;
    Runnable runnableFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //play game  / return points

        shapeNumber = random.nextInt(max - min + 1) + min;
        previousShape = shapeNumber;

        setCurrentFragment(shapeNumber);

        // first move auto after 1,2s
        runnable = new Runnable() {
            @Override
            public void run() {
                shapeNumber = random.nextInt(max - min + 1) + min;
                //previousShape = shapeNumber;
                setCurrentFragment(shapeNumber);
            }
        };
        handler.postDelayed(runnable, 1200);

        Button btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //right
                if(previousShape != shapeNumber){
                    points += 1 * incrementStep;
                    fiveInTheRow++;
                    tenInTheRow++;

                    if(fiveInTheRow == 4){
                        incrementStep++;
                        fiveInTheRow = 0;
                    }
                    if(tenInTheRow == 10){
                        points += incrementStep * incrementStep;
                        tenInTheRow = 0;
                    }

                    //wrong
                } else {
                    totalWrong++;
                    fiveInTheRow = 0;
                    tenInTheRow = 0;
                }
                previousShape = shapeNumber;
                shapeNumber = random.nextInt(max - min + 1) + min;
                setCurrentFragment(shapeNumber);
            }
        });

        Button btnYes = findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //right
                if(previousShape == shapeNumber){
                    points += 1 * incrementStep;
                    fiveInTheRow++;
                    tenInTheRow++;

                    if(fiveInTheRow == 4){
                        incrementStep++;
                        fiveInTheRow = 0;
                    }
                    if(tenInTheRow == 10){
                        points += incrementStep * incrementStep;
                        tenInTheRow = 0;
                    }

                    //wrong
                }else {
                    totalWrong++;
                    fiveInTheRow = 0;
                    tenInTheRow = 0;
                }
                previousShape = shapeNumber;
                shapeNumber = random.nextInt(max - min + 1) + min;
                setCurrentFragment(shapeNumber);
            }
        });

        mTimeField = findViewById(R.id.time);
        startTimer();

    }

    public void setCurrentFragment(int shapeNumber){

        Fragment fragment;
        if(shapeNumber == 0){
            fragment = new TriangleFragment();
        } else if (shapeNumber == 1){
            fragment = new SquareFragment();
        } else {
            fragment = new CircleFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();

        totalShownCards++;

    }

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeField.setText("seconds remaining: " + millisUntilFinished / 1000);

            }
            public void onFinish() {
                mTimeField.setText("done!");
                cancelTimer();

                // summarize points
                if(points > 0){
                    int wrongAnswersPercents = (int) totalWrong * 100 / totalShownCards;
                    Log.d(TAG, "points: "+ String.valueOf(points));
                    Log.d(TAG, "totalWrong: "+ String.valueOf(totalWrong));
                    Log.d(TAG, "totalShownCards: "+ String.valueOf(totalShownCards));
                    Log.d(TAG, "wrongAnswersPercents: "+ String.valueOf(wrongAnswersPercents));

                    double startScore = points;
                    points = (int) Math.round(startScore - ((((double) wrongAnswersPercents) / 100) * startScore));
                    Log.d(TAG, "points: "+ String.valueOf(points));
                } else {
                    points = 0;
                }

                //TODO add finish fragment, her should display finish fragment

                //Display popup
                mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
                PopUpWindow popUpClass = new PopUpWindow(String.valueOf(points));
                popUpClass.showPopupWindow(mRelativeLayout);


                runnableFinish = new Runnable() {
                    @Override
                    public void run() {
                        //mPopupWindow.dismiss();

                        Intent retIntent=new Intent();
                        retIntent.putExtra("Points", points);
                        setResult(Activity.RESULT_OK,retIntent);
                        finish();
                    }
                };
                handler.postDelayed(runnableFinish, 5000);




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
        handler.removeCallbacks(runnable);
        cTimer.cancel();
    }
}