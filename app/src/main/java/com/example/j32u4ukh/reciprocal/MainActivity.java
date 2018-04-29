package com.example.j32u4ukh.reciprocal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    ListView listView;
    private DateTimeSQLiteOpenHelper dateTimeSQLiteOpenHelper;
    private List<Event> eventListAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventListAll = dateTimeSQLiteOpenHelper.readAll();
        listView.setAdapter(new EventAdapter(MainActivity.this, eventListAll));
    }

    @Override
    protected void onStop() {
        super.onStop();
        dateTimeSQLiteOpenHelper.close();
    }

    public void getView(){
        listView = (ListView)findViewById(R.id.listView);
    }

    public void setView(){
        dateTimeSQLiteOpenHelper = new DateTimeSQLiteOpenHelper(this);
        eventListAll = dateTimeSQLiteOpenHelper.readAll();
        listView.setAdapter(new EventAdapter(MainActivity.this, eventListAll));
    }

    private class EventAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<Event> eventListAll;

        private EventAdapter(Context context, List<Event> eventListAll) {
            layoutInflater = LayoutInflater.from(context);
            this.eventListAll = eventListAll;
        }

        @Override
        public int getCount() {
            return eventListAll.size();
        }

        @Override
        public Object getItem(int position) {
            return eventListAll.get(position);
        }

        @Override
        public long getItemId(int position) {
            return eventListAll.get(position).getId();
        }

        // 將內容填入事先準備的框架中
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.activity_adapter, parent, false);
            }
            Event event = eventListAll.get(position);
            final int getID = event.getId();
            TextView textViewShowTitle = (TextView) convertView.findViewById(R.id.textViewShowTitle);
            String title = String.valueOf(event.getTitle());
            textViewShowTitle.setText(title);

            TextView textViewShowTime = (TextView) convertView.findViewById(R.id.textViewShowTime);
            String how_many = beforeAfter(getTime(event.getYear(), event.getMonth(), event.getDay()), textViewShowTime);
            textViewShowTime.setText(how_many);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 每個在清單裡的事件，按了之後可以前往新頁面：檢視完整內容 & 更新
                    Intent intent = new Intent(MainActivity.this, ItemShow.class);
                    intent.putExtra("event_id", getID);
                    startActivity(intent);
                }
            });
            return convertView;
        }
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

    public static String beforeAfter(Calendar calendar, TextView textView){
        Calendar c = Calendar.getInstance();
        long number = gap(c, calendar);
        String string = null;
        switch(c.compareTo(calendar)){
            case 1:
                string = "過了" + number + "天";
                textView.setBackgroundResource(R.color.blue_green);
                textView.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 0:
                string = "就是今天";
                textView.setBackgroundResource(R.color.red);
                textView.setTextColor(Color.parseColor("#00FF48"));
                break;
            case -1:
                string = "還有" + number + "天";
                textView.setBackgroundResource(R.color.red);
                textView.setTextColor(Color.parseColor("#00FF48"));
                break;
        }
        return string;
    }

    // 建立menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    // 設定menu被按之後的動作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 新增事件
            case R.id.add_event:
                Intent intent = new Intent(MainActivity.this, AddEvent.class);
                startActivity(intent);
                break;
            // 日期計算器
            case R.id.app_name:
                Intent intent_count = new Intent(MainActivity.this, Count.class);
                startActivity(intent_count);
                break;
        }
        return true;
    }
}
