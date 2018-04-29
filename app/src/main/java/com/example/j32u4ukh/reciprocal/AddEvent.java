package com.example.j32u4ukh.reciprocal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {
    LinearLayout layoutDate;
    TextView textViewDateChoose, textViewSortKind;
    DateTimeSQLiteOpenHelper dateTimeSQLiteOpenHelper;
    EditText editTextTitle, editTextNote;
    SharedPreferences sharedPreferences;

    Calendar calendar = Calendar.getInstance();
    int y, m, d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getView();
        setView();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    }

    public void setView(){
        sharedPreferences = getSharedPreferences("date_time" , MODE_PRIVATE);
        // 建立資料庫
        dateTimeSQLiteOpenHelper = new DateTimeSQLiteOpenHelper(this);
        // 預設年月日
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH) + 1;
        d = calendar.get(Calendar.DAY_OF_MONTH);
        // 顯示年月日
        textViewDateChoose.setText(y + "/" + m + "/" + d);
        // 預設記憶選擇的日期
        sharedPreferences.edit().putInt("year", y).apply();
        sharedPreferences.edit().putInt("monthOfYear", m).apply();
        sharedPreferences.edit().putInt("dayOfMonth", d).apply();
        // 設定年月日
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 記憶選擇的日期
                        sharedPreferences.edit().putInt("year", year).apply();
                        sharedPreferences.edit().putInt("monthOfYear", monthOfYear + 1).apply();
                        sharedPreferences.edit().putInt("dayOfMonth", dayOfMonth).apply();
                        // 日期選擇紐顯示選擇的日期
                        textViewDateChoose.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // 種類選擇，暫時先不用
        textViewSortKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddEvent.this, "加入AlertDialog.Builder", Toast.LENGTH_SHORT).show();
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
            // 完成紐設定
            case R.id.buttonComplete:
                sharedPreferences = getSharedPreferences("date_time" , MODE_PRIVATE);
                String title = editTextTitle.getText().toString().trim();
                String content = editTextNote.getText().toString().trim();
                int year = sharedPreferences.getInt("year" , 0);
                int monthOfYear = sharedPreferences.getInt("monthOfYear" , 0);
                int dayOfMonth = sharedPreferences.getInt("dayOfMonth" , 0);
                dateTimeSQLiteOpenHelper.create(title, year, monthOfYear, dayOfMonth, content);
                // 返回原 layout
                intent = new Intent(this, MainActivity.class);
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