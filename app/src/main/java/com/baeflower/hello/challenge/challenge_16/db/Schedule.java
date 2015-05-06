package com.baeflower.hello.challenge.challenge_16.db;

/**
 * Created by sol on 2015-04-16.
 */
public class Schedule {
    private String date;
    private String todo;
    private String time;
    private String weather;

    public Schedule(String date, String todo, String time, String weather) {
        this.date = date;
        this.todo = todo;
        this.time = time;
        this.weather = weather;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
