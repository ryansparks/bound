package com.victorluo.boundtestapp;

/**
 * Created by Ryan on 8/2/2016.
 */
public class Meetup {

    public String date;
    public String time;
    public String location;
    public String price;
    public boolean accepted = false;

    public Meetup() {
        // default constructor
    }

    public Meetup(String date, String time, String location, String price) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.price = price;
    }
}
