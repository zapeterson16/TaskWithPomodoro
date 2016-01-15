package com.example.zach.taskwithpomodoro;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
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
        final long totalLength = 10;

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


        timer.schedule(task, 0, 1000);

    }
    public void startAlarmChecker(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long milisLeft = alarmManager.getNextAlarmClock().getTriggerTime()-System.currentTimeMillis();
        long minutesLeft = milisLeft/60000;
        Toast.makeText(this, String.valueOf(minutesLeft), Toast.LENGTH_SHORT).show();
                /*final long startMillis = System.currentTimeMillis();
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


        timer.schedule(task, 0, 60000);*/
    }

    public void setAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, TaskAlarmReciever.class);
        i.putExtra("TASK_ID", this.getIntent().getLongExtra("TASK_ID", 0));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,i,0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, 1500);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        TextView textViewTimer = (TextView) findViewById(R.id.timerTextView);
        textViewTimer.setText("End time: " + time.getTime().getHours() + ":" + time.getTime().getMinutes());
        Button startPomodoroButton = (Button) findViewById(R.id.startPomodoroButton);
        startPomodoroButton.setActivated(false);

    }



    public void startPomodoro(View v){

            setAlarm();


    }

}
