package com.example.zach.taskwithpomodoro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;


/**
 * Created by Zach on 1/15/2016.
 */
public class TaskAlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
       Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        Intent i = new Intent(context, PomodoroRecapActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("TASK_ID",intent.getExtras().getLong("TASK_ID",0));
        context.startActivity(i);

    }
}
