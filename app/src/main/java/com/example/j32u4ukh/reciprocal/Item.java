package com.example.j32u4ukh.reciprocal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class Item extends AppCompatActivity {
    Button buttonComplete;
    LinearLayout layoutDate;
    TextView textViewDateChoose, textViewSortKind;
    EditText editTextTitle, editTextNote;
    // 建立資料庫
    DateTimeSQLiteOpenHelper dateTimeSQLiteOpenHelper;
    // 建立微資料庫
    SharedPreferences sharedPreferences;
    // 取得現在時間
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getView();
        setView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dateTimeSQLiteOpenHelper.close();
    }

    public void getView(){
        // 日期選擇layout
        layoutDate =(LinearLayout)findViewById(R.id.layoutDate);
        // 顯示選擇的日期
        textViewDateChoose = (TextView)findViewById(R.id.textViewDateChoose);
        // 種類選擇
        textViewSortKind = (TextView)findViewById(R.id.textViewSortKind);
        // 標題
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        // 備註
        editTextNote = (EditText)findViewById(R.id.editTextNote);
        // 完成紐
        buttonComplete = (Button)findViewById(R.id.buttonComplete);
    }

    public void setView(){
        // 建立資料庫
        dateTimeSQLiteOpenHelper = new DateTimeSQLiteOpenHelper(this);
        // 建立微資料庫
        sharedPreferences = getSharedPreferences("date_time" , MODE_PRIVATE);
        // 取得 event 的 id
        Intent intent = this.getIntent();
        int event_id = intent.getIntExtra("event_id", 0);
        // 取得 event
        final Event event = dateTimeSQLiteOpenHelper.read(event_id);
        //  匯入標題
        editTextTitle.setText(event.getTitle());
        //  匯入備註
        editTextNote.setText(event.getContent());
        //  匯入年月日
        textViewDateChoose.setText(event.getYear() + "/" + event.getMonth() + "/" +  event.getDay());
        // 設定年月日
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Item.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 記憶選擇的日期
                        sharedPreferences.edit().putInt("item_year", year).apply();
                        sharedPreferences.edit().putInt("item_monthOfYear", monthOfYear + 1).apply();
                        sharedPreferences.edit().putInt("item_dayOfMonth", dayOfMonth).apply();
                        textViewDateChoose.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // 完成紐設定
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String content = editTextNote.getText().toString().trim();
                int year = sharedPreferences.getInt("item_year" , 0);
                int monthOfYear = sharedPreferences.getInt("item_monthOfYear" , 0);
                int dayOfMonth = sharedPreferences.getInt("item_dayOfMonth" , 0);
                dateTimeSQLiteOpenHelper.update(event.getId(), title, year, monthOfYear, dayOfMonth, content);
                // 返回原 layout
                Intent intent = new Intent(Item.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            // 返回紐設定
            case R.id.buttonBack:
                // 返回原 layout
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
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

