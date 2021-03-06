package com.example.zach.taskwithpomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PomodoroRecapActivity extends AppCompatActivity{
    TaskDB db;
    Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences savedValues = getSharedPreferences("SavedValues",MODE_PRIVATE);
        long id = savedValues.getLong("itemID", 0);
        setContentView(R.layout.activity_pomodoro_recap);
        db = ShareData.get(this).getTaskDB();
        currentTask = db.getTask((int)id);
        if(currentTask.getNumPomodoros()!=0) {
            currentTask.setNumPomodoros(currentTask.getNumPomodoros() - 1);
        }
        db.updateTask(currentTask);
        drawScreen(currentTask);




    }

    public void taskFinished(View v){
        db.deleteTask(currentTask.getId());
        goHome();
    }

    public void addPomodoro(View v){
        currentTask.setNumPomodoros(currentTask.getNumPomodoros() + 1);
        db.updateTask(currentTask);
        drawScreen(currentTask);
    }

    public void continueTask(View v){

        goHome();
    }

    public void goHome(){
        startActivity(new Intent(this, TabbedMainActivity.class));
    }

    public void drawScreen(Task task){
        TextView taskNumPomodorosTextView = (TextView) findViewById(R.id.numPomodorosTextView);
        taskNumPomodorosTextView.setText("Pomodoros left: "+task.getNumPomodoros());
        TextView taskTitle = (TextView) findViewById(R.id.taskTitleTextView);
        taskTitle.setText("Did you finish " + task.getTitle() + "?");
    }


}
