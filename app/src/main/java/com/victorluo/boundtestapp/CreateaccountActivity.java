package com.victorluo.boundtestapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateaccountActivity extends AppCompatActivity implements View.OnClickListener {

    final static String DB_URL = "https://boundtest-d3258.firebaseio.com/";

    AutoCompleteTextView firstname, lastname, email, password;
    Button createAccount;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        Firebase.setAndroidContext(this);

        // CONFIGURE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // INTIALIZE BUTTONS AND AUTOCOMPLETETEXTVIEWS
        firstname = (AutoCompleteTextView) findViewById(R.id.firstname);
        lastname = (AutoCompleteTextView) findViewById(R.id.lastname);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (AutoCompleteTextView) findViewById(R.id.password);
        createAccount = (Button) findViewById(R.id.createaccountbutton);

        // INITIALIZE ONCLICKLISTENERS
        createAccount.setOnClickListener(this);

        // INITIALIZE FIREBASE AUTH LISTENER
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("FIREBASE", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("FIREBASE", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("Test", "button clicked");

        switch(view.getId())
        {
            case R.id.createaccountbutton:
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("FIREBASE", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                if(task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    writeNewUser(firstname.getText().toString(), lastname.getText().toString(), email.getText().toString(), user.getUid());
                                    Intent i = new Intent(CreateaccountActivity.this, MainActivity.class);
                                    startActivity(i);
                                }

                                //if sign in fails...
                                if(!task.isSuccessful()) {
                                    Toast.makeText(CreateaccountActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
        }
    }

    private void writeNewUser(String firstname, String lastname, String email, String uid) {
        User user = new User(firstname, lastname, email, uid);
        mDatabase.child("userdata").child(uid).setValue(user);
    }
}
