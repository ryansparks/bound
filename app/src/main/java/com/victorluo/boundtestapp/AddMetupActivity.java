package com.victorluo.boundtestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddMetupActivity extends AppCompatActivity {

    EditText selectLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_metup);

        selectLocation = (EditText) findViewById(R.id.location);

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMetupActivity.this, SelectLocationActivity.class);
                startActivity(i);
            }
        });
    }
}
