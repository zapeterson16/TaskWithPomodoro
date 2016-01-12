package com.example.zach.taskwithpomodoro;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {
    TextView textViewTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        textViewTimer = (TextView) findViewById(R.id.timerTextView);



    }

    public void startTimer(){
        final long startMillis = System.currentTimeMillis();
        final long totalLength = 1500;
        final Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                final long elapseMillis = System.currentTimeMillis() - startMillis;
                final long elapseSeconds = elapseMillis / 1000;
                final long timeLeft = totalLength - elapseSeconds;
                String result;
                if (timeLeft >= 0) {
                    if(timeLeft%60<10) {
                        result = String.valueOf(timeLeft / 60) + ":0" + String.valueOf(timeLeft % 60);
                    }
                    else{
                        result = String.valueOf(timeLeft / 60) + ":" + String.valueOf(timeLeft % 60);
                    }
                } else {
                    result = "done";
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    startActivity(new Intent(getApplicationContext(),TaskListActivity.class));
                    timer.cancel();
                    timer.purge();

                }
                final String displayResult = result;
                textViewTimer.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewTimer.setText(displayResult);
                    }

                });
            }


        };


        timer.schedule(task,0,1000);

    }

    public void startPomodoro(View v){
        startTimer();
    }
}
