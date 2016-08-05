package com.victorluo.boundtestapp;

/**
 * Created by Ryan on 7/31/2016.
 */
public class User {

    public String firstname;
    public String lastname;
    public String email;
    public String uid;

    public User() {
        //Default constructor
    }

    public User(String first, String last, String email, String uid) {
        this.firstname = first;
        this.lastname = last;
        this.email = email;
        this.uid = uid;
    }
}
