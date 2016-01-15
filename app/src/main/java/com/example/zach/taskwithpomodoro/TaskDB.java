package com.example.zach.taskwithpomodoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TaskDB {
    public static final String DB_NAME = "task.db";
    public static final int    DB_VERSION = 1;

    public static final String TASK_TABLE = "task";

    public static final String TASK_ID = "_id";
    public static final int    TASK_ID_COL = 0;

    public static final String TITLE = "title";
    public static final int    TITLE_COL = 1;

    public static final String  NUMPOMODOROS = "numpomodoros";
    public static final int NUMPOMODOROS_COL = 2;

    public static final String CREATE_TASK_TABLE =
            "CREATE TABLE " + TASK_TABLE + " (" +
                    TASK_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE       + " TEXT    NOT NULL, " +
                    NUMPOMODOROS    + " INTEGER NOT NULL);";

    public static String DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS " + TASK_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TASK_TABLE);

            // insert sample movies
            // db.execSQL("INSERT INTO movie VALUES (1, 'Harry Potter', 120)");
            // db.execSQL("INSERT INTO movie VALUES (2, 'Lord of the Rings', 600)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("*** Movie ***", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(TaskDB.DROP_MOVIE_TABLE);
            onCreate(db);
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public TaskDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public long insertTaskAutoId(Task task){
        ContentValues cv = new ContentValues();

        cv.put(TITLE, task.getTitle());
        cv.put(NUMPOMODOROS, task.getNumPomodoros());

        this.openWriteableDB();
        long rowID = db.insert(TASK_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateTask(Task task){
        ContentValues cv = new ContentValues();
        cv.put(TASK_ID, task.getId());
        cv.put(TITLE, task.getTitle());
        cv.put(NUMPOMODOROS, task.getNumPomodoros());

        String where = TASK_ID + "= ?";
        String[] whereArgs = { String.valueOf(task.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(TASK_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteTask(long id){
        String where = TASK_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(TASK_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }

    public Cursor getTasksAsCurso(){
        this.openReadableDB();

        return db.query(TASK_TABLE,null,
                null, null,
                null, null, null);
    }
    public Task getTask(int id){
        String where = TASK_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(TASK_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Task task = getTaskFromCursor(cursor);
        if(cursor != null){
            cursor.close();
        }
        this.closeDB();
        return task;
    }

    private static Task getTaskFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try{

                return new Task(
                        cursor.getString(TITLE_COL),
                        cursor.getInt(TASK_ID_COL),
                        cursor.getInt(NUMPOMODOROS_COL));
            }
            catch(Exception e){
                return null;
            }
        }
    }

}
