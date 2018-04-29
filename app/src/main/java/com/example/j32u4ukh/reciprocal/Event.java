package com.example.j32u4ukh.reciprocal;

class Event {
    private String title, content;
    private int id, year, month, day;
    Event(){
        this.id = 0;
        this.title = "title";
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.content = "content";
    }
    Event(int id, String title, int year, int month, int day,String content){
        this.id = id;
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getMonth(){
        return month;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return day;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }
}

