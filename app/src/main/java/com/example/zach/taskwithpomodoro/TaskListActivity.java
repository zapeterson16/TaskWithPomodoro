package com.example.zach.taskwithpomodoro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TaskListActivity extends AppCompatActivity {
    TaskDB db;
    ListView listView;
    TextView timerTextView;
    public static Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(),CreateTaskActivity.class);
                startActivity(i);


            }
        });
        timer = new Timer();
        db = ShareData.get(this).getTaskDB();

        listView = (ListView) findViewById(R.id.taskListView);
        Cursor cursor = db.getTasksAsCurso();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {TaskDB.TITLE, TaskDB.NUMPOMODOROS},
                new int[] {android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startTimer(id);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteTask(id);
                reAssignDB();
                return true;
            }
        });

        timerTextView = (TextView) findViewById(R.id.timerTextView);
    }

    public void reAssignDB(){
        Cursor cursor = db.getTasksAsCurso();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {TaskDB.TITLE, TaskDB.NUMPOMODOROS},
                new int[] {android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reAssignDB();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        reAssignDB();
    }

    public void startTimer(Long itemID){
        final long startMillis = System.currentTimeMillis();
        final long totalLength = 10;

        final Timer timer = new Timer();

        SharedPreferences savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
        savedValues.edit().putLong("itemID", itemID).commit();


        Log.i("zach", "id from tasklistACtivity is" + itemID);
        Log.i("zach", "value back from saved values" + savedValues.getLong("itemID",0));



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
                    SharedPreferences savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
                    long id = savedValues.getLong("itemID", 0);
                    PomodoroEndNotification nm = new PomodoroEndNotification();
                    PomodoroEndNotification.notify(getApplicationContext(), "You are done with your pomodoro", (int)id);
                    timer.cancel();
                    timer.purge();

                }
                final String displayResult = result;
                timerTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        timerTextView.setText(displayResult);
                    }

                });
            }


        };


        timer.schedule(task, 0, 1000);

    }





}
