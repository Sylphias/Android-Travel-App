package com.example.hermes.travelapp;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ValueAnimator posAnim, opAnim, posAnim1, opAnim1, posAnim2, opAnim2, posAnim3, opAnim3, posAnimScreenOutLeft, opAnimScreenOutLeft, posAnimScreenOutRight, opAnimScreenOutRight, posAnimScreenInLeft, opAnimScreenInLeft, posAnimScreenInRight, opAnimScreenInRight;
    static int currScreen = 1;
    static int animScreen = 1;
    static int initial = 0;
    static ArrayList<RelativeLayout> screens = new ArrayList<RelativeLayout>();
    RelativeLayout screen1, screen2, screen3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen1 = (RelativeLayout) findViewById(R.id.content_main);
        screen2 = (RelativeLayout) findViewById(R.id.budget);
        screen3 = (RelativeLayout) findViewById(R.id.destinations);
        screen2.setX(2000);
        screen2.setVisibility(View.VISIBLE);
        screen3.setX(2000);
        screen3.setVisibility(View.VISIBLE);
        screens.add(null);
        screens.add(screen1);
        screens.add(screen2);
        screens.add(screen3);
        initial = 0;
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        posAnimScreenOutLeft = ValueAnimator.ofFloat(0, -1 * screen1.getWidth());
        posAnimScreenOutLeft.setDuration(1000);
        posAnimScreenOutLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setX((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });
        opAnimScreenOutLeft = ValueAnimator.ofFloat(1, 0);
        opAnimScreenOutLeft.setDuration(1000);
        opAnimScreenOutLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });

        posAnimScreenOutRight = ValueAnimator.ofFloat(0, screen1.getWidth());
        posAnimScreenOutRight.setDuration(1000);
        posAnimScreenOutRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setX((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });
        opAnimScreenOutRight = ValueAnimator.ofFloat(1, 0);
        opAnimScreenOutRight.setDuration(1000);
        opAnimScreenOutRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });

        posAnimScreenInLeft = ValueAnimator.ofFloat(-1 * screen1.getWidth(), 0);
        posAnimScreenInLeft.setDuration(1000);
        posAnimScreenInLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen-1).setX((float) animation.getAnimatedValue());
                screens.get(animScreen-1).requestLayout();
            }
        });
        opAnimScreenInLeft = ValueAnimator.ofFloat(0, 1);
        opAnimScreenInLeft.setDuration(1000);
        opAnimScreenInLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen-1).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen-1).requestLayout();
            }
        });

        posAnimScreenInRight = ValueAnimator.ofFloat(screen1.getWidth(),0);
        posAnimScreenInRight.setDuration(1000);
        posAnimScreenInRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen+1).setX((float) animation.getAnimatedValue());
                screens.get(animScreen+1).requestLayout();
            }
        });
        opAnimScreenInRight = ValueAnimator.ofFloat(0, 1);
        opAnimScreenInRight.setDuration(1000);
        opAnimScreenInRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen+1).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen+1).requestLayout();
            }
        });

        if (initial == 0) {
            final TextView what = (TextView) findViewById(R.id.textView2);
            posAnim = ValueAnimator.ofFloat(screen1.getHeight() / 2 - 150, screen1.getHeight() / 2 - 350);//1000, 800);
            posAnim.setDuration(1000).setStartDelay(250);
            posAnim.start();
            posAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    what.setY((float) animation.getAnimatedValue());
                    what.requestLayout();
                }
            });
            opAnim = ValueAnimator.ofFloat(0, 1);
            opAnim.setDuration(1000).setStartDelay(250);
            opAnim.start();
            opAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    what.setAlpha((float) animation.getAnimatedValue());
                    what.requestLayout();
                }
            });

            final Button newTrip = (Button) findViewById(R.id.newTrip);
            posAnim1 = ValueAnimator.ofFloat(screen1.getHeight() / 2 + 200, screen1.getHeight() / 2);//1000, 800);
            posAnim1.setDuration(800).setStartDelay(750);
            posAnim1.start();
            posAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    newTrip.setY((float) animation.getAnimatedValue());
                    newTrip.requestLayout();
                }
            });
            opAnim1 = ValueAnimator.ofFloat(0, 1);
            opAnim1.setDuration(800).setStartDelay(750);
            opAnim1.start();
            opAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    newTrip.setAlpha((float) animation.getAnimatedValue());
                    newTrip.requestLayout();
                }
            });

            final Button checkItinerary = (Button) findViewById(R.id.checkItinerary);
            posAnim2 = ValueAnimator.ofFloat(screen1.getHeight() / 2 + 350, screen1.getHeight() / 2 + 150);//1150, 950);
            posAnim2.setDuration(800).setStartDelay(900);
            posAnim2.start();
            posAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    checkItinerary.setY((float) animation.getAnimatedValue());
                    checkItinerary.requestLayout();
                }
            });
            opAnim2 = ValueAnimator.ofFloat(0, 1);
            opAnim2.setDuration(800).setStartDelay(900);
            opAnim2.start();
            opAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    checkItinerary.setAlpha((float) animation.getAnimatedValue());
                    checkItinerary.requestLayout();
                }
            });

            final Button settings = (Button) findViewById(R.id.settings);
            posAnim3 = ValueAnimator.ofFloat(screen1.getHeight() / 2 + 500, screen1.getHeight() / 2 + 300);//1300, 1100);
            posAnim3.setDuration(800).setStartDelay(1050);
            posAnim3.start();
            posAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    settings.setY((float) animation.getAnimatedValue());
                    settings.requestLayout();
                }
            });
            opAnim3 = ValueAnimator.ofFloat(0, 1);
            opAnim3.setDuration(800).setStartDelay(1050);
            opAnim3.start();
            opAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    settings.setAlpha((float) animation.getAnimatedValue());
                    settings.requestLayout();
                }
            });
            initial = 1;
        }
    }

    public void budget(View v){
        animScreen = currScreen;
        posAnimScreenOutLeft.start();
        opAnimScreenOutLeft.start();
        posAnimScreenInRight.start();
        opAnimScreenInRight.start();
        currScreen = 2;
    }

    public void submitBudget(View v){
        animScreen = currScreen;
        posAnimScreenOutLeft.start();
        opAnimScreenOutLeft.start();
        posAnimScreenInRight.start();
        opAnimScreenInRight.start();
        currScreen = 3;
    }

    public void onBackPressed() {
        if (currScreen == 1) super.onBackPressed();
        else{
            animScreen = currScreen;
            posAnimScreenOutRight.start();
            opAnimScreenOutRight.start();
            posAnimScreenInLeft.start();
            opAnimScreenInLeft.start();
            currScreen -= 1;
        }
    }

}
