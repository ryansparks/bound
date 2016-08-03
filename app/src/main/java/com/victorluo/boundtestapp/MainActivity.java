package com.victorluo.boundtestapp;


import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView boundlogo;
    Button createMeetup;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        boundlogo = (ImageView) findViewById(R.id.boundlogo);
        createMeetup = (Button) findViewById(R.id.schedule);

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

        findViewById(R.id.test_fetch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ConfirmLocationActivity.class));
            }
        });
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

