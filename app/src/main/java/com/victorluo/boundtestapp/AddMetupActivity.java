package com.victorluo.boundtestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddMetupActivity extends AppCompatActivity {

    EditText selectLocation, time, date, location, price;
    Button addMeetupButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_metup);

        // INITIALIZE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // INITIALIZE UI COMPONENTS
        selectLocation = (EditText) findViewById(R.id.location);
        time = (EditText) findViewById(R.id.time);
        date = (EditText) findViewById(R.id.date);
        price = (EditText) findViewById(R.id.price);

        addMeetupButton = (Button) findViewById(R.id.addmeetup);

        // LINK LOCATION TO GOOGLE MAPS ACTIVITY ****** NOT FUNCTIONAL YET *******
        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMetupActivity.this, SelectLocationActivity.class);
                startActivity(i);
            }
        });

        addMeetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMeetup();
            }
        });
    }

    public void addMeetup() {
        Meetup meetup = new Meetup(date.getText().toString(), time.getText().toString(), selectLocation.getText().toString(), price.getText().toString());
        mDatabase.child("userdata").child(mAuth.getCurrentUser().getUid()).child("active-meetups").child(UUID.randomUUID().toString()).setValue(meetup);
        this.finish();
    }
}
