package com.example.zach.taskwithpomodoro;

import android.content.Context;

import java.util.Timer;

/**
 * Created by Zach on 1/8/2016.
 */
public class ShareData {
    private static ShareData sData;  // NOTE: static reference sData

    private TaskDB db;

    private static TimerFragment timerFragment;
    private static TaskFragment taskFragment;

    private Context context;
    // declare other variables here, or change value to a reference to a Model object

    private ShareData(Context c) {
        db = new TaskDB(c);
        timerFragment = TimerFragment.newInstance("test","test");
        taskFragment = TaskFragment.newInstance(1);
    }

    public static ShareData get(Context c) {

        if (sData == null) {
            sData = new ShareData(c.getApplicationContext());
        }
        return sData;
    }

    public TimerFragment getTimerFragment(){
        if(timerFragment == null){
            timerFragment = TimerFragment.newInstance("test","test");
        }
        return timerFragment;
    }

    public TaskFragment getTaskFragment(){
        if(taskFragment == null){
            taskFragment = taskFragment.newInstance(1);
        }
        return taskFragment;
    }

    





    public TaskDB getTaskDB() {
        return db;
    }

    // Put additional set and get methods here
}
