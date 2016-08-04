package com.victorluo.boundtestapp;


import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView boundlogo;
    Button createMeetup;
    LinearLayout meetupList;
    TextView noMeetups;
    ProgressBar loading;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    ArrayList<Meetup> meetups = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        refreshMeetups();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INITIALIZE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // INITIALIZE UI COMPONENTS
        boundlogo = (ImageView) findViewById(R.id.boundlogo);
        createMeetup = (Button) findViewById(R.id.schedule);
        meetupList = (LinearLayout) findViewById(R.id.meetuplist);
        noMeetups = (TextView) findViewById(R.id.noMeetups);
        loading = (ProgressBar) findViewById(R.id.loading);

        createMeetup.setOnClickListener(this);

        boundlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, boundlogo);
                popup.getMenuInflater().inflate(R.menu.settings_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.settings:

                                break;
                            case R.id.logout:
                                mAuth.signOut();
                                MainActivity.this.finish();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

<<<<<<< HEAD
        findViewById(R.id.test_fetch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ConfirmLocationActivity.class));
            }
        });
=======
        // SHOW ALL MEETUPS THE USER HAS
        noMeetups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshMeetups();
            }
        });
    }

    public void refreshMeetups() {
        noMeetups.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        Log.d("FIREBASE", "yo a button was pressed dawg");
        Query query = mDatabase.child("userdata").child(mAuth.getCurrentUser().getUid().toString()).child("active-meetups").orderByChild("location");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE", "children count " + dataSnapshot.getChildrenCount());
                meetups.clear();
                
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Meetup meetup = ds.getValue(Meetup.class);
                    meetups.add(meetup);
                    Log.d("FIREBASE", "location " + meetup.location);
                }

                updateMeetupList();
            }

            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void updateMeetupList() {
        if(meetups.isEmpty()) {
            loading.setVisibility(View.GONE);
            noMeetups.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
            meetupList.removeAllViews();
            for(int i = 0; i < meetups.size(); i++) {
                Button button = new Button(MainActivity.this);
                button.setText(meetups.get(i).location);
                button.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                meetupList.addView(button);
            }
        }
>>>>>>> master
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schedule:
                Intent i = new Intent(MainActivity.this, AddMetupActivity.class);
                startActivity(i);
                break;
        }
    }

    //test comment
}

