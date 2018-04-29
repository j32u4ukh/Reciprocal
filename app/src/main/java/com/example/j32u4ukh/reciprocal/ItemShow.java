package com.example.j32u4ukh.reciprocal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ItemShow extends AppCompatActivity {
    Calendar calendar = Calendar.getInstance();
    DateTimeSQLiteOpenHelper dateTimeSQLiteOpenHelper;
    TextView textViewItemTitle, textViewItemTime, textViewItemDay, textViewItemContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getView();
        setView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dateTimeSQLiteOpenHelper.close();
    }

    public void getView(){
        textViewItemTitle = (TextView)findViewById(R.id.textViewItemTitle);
        textViewItemTime = (TextView)findViewById(R.id.textViewItemTime);
        textViewItemDay = (TextView)findViewById(R.id.textViewItemDay);
        textViewItemContent = (TextView)findViewById(R.id.textViewItemContent);
    }

    public void setView(){
        // 建立資料庫
        dateTimeSQLiteOpenHelper = new DateTimeSQLiteOpenHelper(this);
        // 取得 event 的 id
        Intent intent = this.getIntent();
        int event_id = intent.getIntExtra("event_id", 0);
        // 取得 event
        Event event = dateTimeSQLiteOpenHelper.read(event_id);
        int year = event.getYear();
        int month = event.getMonth();
        int day = event.getDay();
        // 取得 event 的 日期
        Calendar eventCalendar = getTime(year, month, day);
        // 計算差距幾天
        long between = gap(calendar, eventCalendar);
        // 顯示Title
        String itemTitle = "";
        switch (eventCalendar.compareTo(calendar)){
            case -1:
                itemTitle = event.getTitle() + "已過了";
                break;
            case 0:
                itemTitle = event.getTitle() + "就是今天";
                break;
            case 1:
                itemTitle = "距離" + event.getTitle() + "還有";
                break;
        }
        textViewItemTitle.setText(itemTitle);
        // 顯示差距幾天
        textViewItemDay.setText(String.valueOf(between));
        // 顯示當初設定日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E", Locale.getDefault());
        textViewItemTime.setText(dateFormat.format(eventCalendar.getTime()));
        // 顯示備註
        textViewItemContent.setText(event.getContent());
    }

    public static Calendar getTime(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, --month, day);
        return calendar;
    }

    public static long gap(Calendar c1, Calendar c2){
        long time1 = c1.getTimeInMillis() / 1000;
        long time2 = c2.getTimeInMillis() / 1000;
        long time = Math.abs(time1 - time2);
        return time/(60*60*24);
    }

    // 設置Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    // Menu選項按了後
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 取得 event 的 id
        Intent event_ID = this.getIntent();
        int event_id = event_ID.getIntExtra("event_id", 0);
        switch (item.getItemId()){
            case R.id.update:
                Intent intent = new Intent(this, Item.class);
                intent.putExtra("event_id", event_id);
                startActivity(intent);
                break;
            case R.id.delete:
                dateTimeSQLiteOpenHelper.delete(event_id);
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}

