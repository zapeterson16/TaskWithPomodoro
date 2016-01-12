package com.example.zach.taskwithpomodoro;

/**
 * Created by Zach on 1/6/2016.
 */
public class Task {
    String title;
    int numPomodoros = 1;
    int id = 0;

    public Task(String title){
        this.title = title;

    }

    public Task(String title, int id,int numPomodoros){
        this.title = title;
        this.id = id;
        this.numPomodoros = numPomodoros;
    }

    public int getNumPomodoros() {
        return numPomodoros;
    }

    public void setNumPomodoros(int numPomodoros) {
        this.numPomodoros = numPomodoros;
    }


    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
