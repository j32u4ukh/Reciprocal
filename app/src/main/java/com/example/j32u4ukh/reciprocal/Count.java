package com.example.j32u4ukh.reciprocal;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Count extends AppCompatActivity {
    Button buttonGapStart, buttonGapEnd, buttonBeforeAfter, buttonBefore, buttonAfter;
    TextView textViewGap, textViewBefore, textViewAfter;
    SharedPreferences sharedPreferences;
    // 取得現在日期時間
    Calendar calendar = Calendar.getInstance();
    int default_year = calendar.get(Calendar.YEAR);
    int default_month = calendar.get(Calendar.MONTH) + 1;
    int default_day = calendar.get(Calendar.DAY_OF_MONTH);
    // 設定日期格式
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        getView();
        setView();
    }

    public void getView(){
        buttonGapStart = (Button)findViewById(R.id.buttonGapStart);
        buttonGapEnd = (Button)findViewById(R.id.buttonGapEnd);
        buttonBeforeAfter = (Button)findViewById(R.id.buttonBeforeAfter);
        textViewGap = (TextView)findViewById(R.id.textViewGap);
        buttonBefore = (Button)findViewById(R.id.buttonBefore);
        buttonAfter = (Button)findViewById(R.id.buttonAfter);
        textViewBefore = (TextView)findViewById(R.id.textViewBefore);
        textViewAfter = (TextView)findViewById(R.id.textViewAfter);
    }

    public void setView(){
        // 建立資料庫
        sharedPreferences = getSharedPreferences("date_time" , MODE_PRIVATE);
        // 差幾天：取當天的值
        String default_date = dateFormat.format(calendar.getTime());
        buttonGapStart.setText(default_date);
        buttonGapEnd.setText(default_date);
        buttonBeforeAfter.setText(default_date);
        // 幾天前；幾天後：取當天的值
        textViewGap.setText("0天");
        String string = "天前為    " + default_date;
        textViewBefore.setText(string);
        string = "天後為    " + default_date;
        textViewAfter.setText(string);
    }

    public void onClick(View view){
        // 建立資料庫
        sharedPreferences = getSharedPreferences("date_time" , MODE_PRIVATE);
        switch (view.getId()){
            // 選擇開始日期
            case R.id.buttonGapStart:
                new DatePickerDialog(Count.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 記憶日期
                        sharedPreferences.edit().putInt("gap_start_year", year).apply();
                        sharedPreferences.edit().putInt("gap_start_month", monthOfYear + 1).apply();
                        sharedPreferences.edit().putInt("gap_start_day", dayOfMonth).apply();
                        // 更改開始日期
                        calendar.set(year, monthOfYear, dayOfMonth);
                        buttonGapStart.setText(dateFormat.format(calendar.getTime()));
                        // 更新：差幾天
                        setGapText();
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            // 選擇結束日期
            case R.id.buttonGapEnd:
                new DatePickerDialog(Count.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 記憶日期
                        sharedPreferences.edit().putInt("gap_end_year", year).apply();
                        sharedPreferences.edit().putInt("gap_end_month", monthOfYear + 1).apply();
                        sharedPreferences.edit().putInt("gap_end_day", dayOfMonth).apply();
                        // 更改結束日期
                        calendar.set(year, monthOfYear, dayOfMonth);
                        buttonGapEnd.setText(dateFormat.format(calendar.getTime()));
                        // 更新：差幾天
                        setGapText();
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            // 幾天前；幾天後
            case R.id.buttonBeforeAfter:
                new DatePickerDialog(Count.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 記憶日期
                        sharedPreferences.edit().putInt("before_after_year", year).apply();
                        sharedPreferences.edit().putInt("before_after_month", monthOfYear + 1).apply();
                        sharedPreferences.edit().putInt("before_after_day", dayOfMonth).apply();
                        // 更改開始日期
                        calendar.set(year, monthOfYear, dayOfMonth);
                        buttonBeforeAfter.setText(dateFormat.format(calendar.getTime()));
                        // 更新：幾天前；幾天後
                        setBeforeAfterText();
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            // 幾天前
            case R.id.buttonBefore:
                final EditText editTextBefore = new EditText(Count.this);   // 未來新增：限定輸入數字
                new AlertDialog.Builder(Count.this)
                        .setTitle("設定")
                        .setMessage("輸入天數")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 取得天數
                                String string = editTextBefore.getText().toString();
                                // 更改按鈕文字
                                buttonBefore.setText(string);
                                // 記憶天數
                                sharedPreferences.edit().putLong("before", Long.parseLong(string)).apply();
                                // 更新：幾天前；幾天後
                                setBeforeAfterText();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setView(editTextBefore)
                        .create().show();
                break;
            case R.id.buttonAfter:
                final EditText editTextAfter = new EditText(Count.this);    // 未來新增：限定輸入數字
                new AlertDialog.Builder(Count.this)
                        .setTitle("設定")
                        .setMessage("輸入天數")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 取得天數
                                String string = editTextAfter.getText().toString();
                                // 更改按鈕文字
                                buttonAfter.setText(string);
                                // 記憶天數
                                sharedPreferences.edit().putLong("after", Long.parseLong(string)).apply();
                                // 更新：幾天前；幾天後
                                setBeforeAfterText();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setView(editTextAfter)
                        .create().show();
                break;
        }
    }

    public static Calendar getTime(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, --month, day);
        return calendar;
    }

    public static long gap(Calendar c1, Calendar c2){
        long time1 = c1.getTimeInMillis();
        long time2 = c2.getTimeInMillis();
        long time = (time2 - time1);
        return time/(1000*60*60*24);
    }

    // 更新：差幾天
    public void setGapText(){
        // 取得開始時間
        int gap_start_year = sharedPreferences.getInt("gap_start_year" , default_year);
        int gap_start_month = sharedPreferences.getInt("gap_start_month" , default_month);
        int gap_start_day = sharedPreferences.getInt("gap_start_day" , default_day);
        // 取得結束時間
        int gap_end_year = sharedPreferences.getInt("gap_end_year" , default_year);
        int gap_end_month = sharedPreferences.getInt("gap_end_month" , default_month);
        int gap_end_day = sharedPreferences.getInt("gap_end_day" , default_day);
        // 取得中間差幾天
        long gap_between = gap(getTime(gap_start_year, gap_start_month, gap_start_day), getTime(gap_end_year, gap_end_month, gap_end_day));
        String gapBetween = gap_between + " 天";
        textViewGap.setText(gapBetween);
    }

    // 更新：幾天前；幾天後
    public void setBeforeAfterText(){
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E", Locale.getDefault());
        // 取得開始時間
        int before_after_year = sharedPreferences.getInt("before_after_year" , default_year);
        int before_after_month = sharedPreferences.getInt("before_after_month" , default_month);
        int before_after_day = sharedPreferences.getInt("before_after_day" , default_day);
        long start = getTime(before_after_year, before_after_month, before_after_day).getTimeInMillis();
        // 取得天數的毫秒數
        long day_before = sharedPreferences.getLong("before" , 0) *24*60*60*1000L;
        long day_after = sharedPreferences.getLong("after" , 0) *24*60*60*1000L;
        // 計算差距天數
        // 幾天前
        start -= day_before;
        calendar.setTimeInMillis(start);
        String textBefore = "天前為    " + dateFormat.format(calendar.getTime());
        textViewBefore.setText(textBefore);
        // 幾天後
        start += day_before;
        start += day_after;
        calendar.setTimeInMillis(start);
        String textAfter = "天後為    " + dateFormat.format(calendar.getTime());
        textViewAfter.setText(textAfter);
    }

    // 返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}

