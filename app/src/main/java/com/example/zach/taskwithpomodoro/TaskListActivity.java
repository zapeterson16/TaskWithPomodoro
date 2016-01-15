package com.example.zach.taskwithpomodoro;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TaskListActivity extends AppCompatActivity {
    TaskDB db;
    ListView listView;
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
                Intent i = new Intent(getApplicationContext(),TimerActivity.class);
                i.putExtra("TASK_ID", id);
                startActivity(i);
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





}
