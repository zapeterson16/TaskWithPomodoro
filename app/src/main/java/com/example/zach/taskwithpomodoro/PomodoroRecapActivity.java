package com.example.zach.taskwithpomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PomodoroRecapActivity extends Fragment {
    TaskDB db;
    Task currentTask;
    View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences savedValues = super.getActivity().getSharedPreferences("SavedValues", super.getActivity().MODE_PRIVATE);
        long id = savedValues.getLong("itemID", 0);

        db = ShareData.get(super.getActivity()).getTaskDB();
        currentTask = db.getTask((int)id);
        if(currentTask.getNumPomodoros()!=0) {
            currentTask.setNumPomodoros(currentTask.getNumPomodoros() - 1);
        }
        db.updateTask(currentTask);
        drawScreen(currentTask);
        view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
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
        startActivity(new Intent(super.getActivity(), TaskListActivity.class));
    }

    public void drawScreen(Task task){
        TextView taskNumPomodorosTextView = (TextView) view.findViewById(R.id.numPomodorosTextView);
        taskNumPomodorosTextView.setText("Pomodoros left: "+task.getNumPomodoros());
        TextView taskTitle = (TextView) view.findViewById(R.id.taskTitleTextView);
        taskTitle.setText("Did you finish " + task.getTitle() + "?");
    }

    public static PomodoroRecapActivity newInstance() {
        PomodoroRecapActivity fragment = new PomodoroRecapActivity();

        return fragment;
    }

    public PomodoroRecapActivity(){}


}
