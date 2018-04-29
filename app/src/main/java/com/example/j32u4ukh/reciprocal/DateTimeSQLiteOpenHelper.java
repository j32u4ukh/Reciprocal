package com.example.j32u4ukh.reciprocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

class DateTimeSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DATE_COUNT";
    private static final String DATABASE_TABLE = "DATE_TIME";
    private static final int DATABASE_VERSION = 2;
    private static final String COL_1 = "title";
    private static final String COL_2 = "year";
    private static final String COL_3 = "month";
    private static final String COL_4 = "day";
    private static final String COL_5 = "content";
    private SQLiteDatabase sqliteDatabase = getWritableDatabase();

    DateTimeSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_TABLE +
                " (_id integer primary key autoincrement, " +
                COL_1 + " text no null, " +
                COL_2 + " real no null, " +
                COL_3 + " real no null, " +
                COL_4 + " real no null, " +
                COL_5 + " text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    void create(String title, int year, int month, int day, String content){
        sqliteDatabase.execSQL("INSERT INTO " + DATABASE_TABLE +
                " (" + COL_1 + ", " + COL_2 + ", " + COL_3 + ", " + COL_4 + ", " + COL_5 +
                ") VALUES (?, ?, ?, ?, ?)", new Object[] {title, year, month, day, content});
    }

    List<Event> readAll() {
        Cursor cursor = sqliteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        // String[] columnNames = cursor.getColumnNames();
        // columnNames = [ _id, title, year, month, day, content]

        List<Event> eventListAll = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            int year = cursor.getInt(2);
            int month = cursor.getInt(3);
            int day = cursor.getInt(4);
            String content = cursor.getString(5);
            Event event = new Event(id, title, year, month, day, content);
            eventListAll.add(0, event);
        }
        cursor.close();
        return eventListAll;
    }

    Event read(int index) {
        Cursor cursor = sqliteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        Event event = new Event();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if(index == id){
                String title = cursor.getString(1);
                int year = cursor.getInt(2);
                int month = cursor.getInt(3);
                int day = cursor.getInt(4);
                String content = cursor.getString(5);
                Event eventItem = new Event(id, title, year, month, day, content);
                cursor.close();
                return eventItem;
            }
        }
        cursor.close();
        return event;
    }

    void update(int id, String title, int year, int month, int day, String content) {
        sqliteDatabase.execSQL("UPDATE " + DATABASE_TABLE +
                " SET " + COL_1 + " ='" + title + "', " +
                COL_2 + " = " + year + ", " +
                COL_3 + " = " + month + ", " +
                COL_4 + " = " + day + ", " +
                COL_5 + " = '" + content + "'" +
                " WHERE _id =" + id);

    }

    void delete(int id) {
        sqliteDatabase.execSQL("DELETE FROM " + DATABASE_TABLE + " WHERE _id='" + id + "'");

    }
}

