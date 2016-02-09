package com.example.zach.taskwithpomodoro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateTaskActivity extends AppCompatActivity {
    TaskDB db;
    EditText taskTitle;
    EditText taskNumPomodoros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        db = ShareData.get(this).getTaskDB();
        taskTitle = (EditText) findViewById(R.id.TaskNameEditText);
        taskNumPomodoros = (EditText) findViewById(R.id.numPomodorosEditText);
    }

    public void submitTask(View v) {


        if (taskTitle.getText() != null && taskNumPomodoros.getText() != null) {
            String taskTitleString = taskTitle.getText().toString();
            db.insertTaskAutoId(new Task(taskTitle.getText().toString(), 0, Integer.parseInt(taskNumPomodoros.getText().toString())));


            startActivity(new Intent(getApplicationContext(), TabbedMainActivity.class));


        }
    }
}
