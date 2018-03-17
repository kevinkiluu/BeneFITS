package com.example.ddk.benefits;
import android.util.Log;
import android.text.TextUtils;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.R.*;
import java.util.ArrayList;
import android.content.Intent;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.*;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.ddk.benefits.User;
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.*;

public class Registration extends AppCompatActivity {
    private Button registerButton1;
    private FirebaseAuth mAuth;
    public TextView text_birthday;
    public TextView text_name;
    public TextView text_email;
    public TextView text_password;
    public Spinner text_gender;
    public TextView text_weight;
    private String userId;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFDB;
    private  com.example.ddk.benefits.User new_user  = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuthException e;
        text_birthday = (TextView) findViewById(R.id.birthday);
        text_name = (TextView) findViewById(R.id.name);
        text_email = (TextView) findViewById(R.id.email);
        text_password = (TextView) findViewById(R.id.password);
        text_gender = (Spinner) findViewById(R.id.gender);
        text_weight = (TextView) findViewById(R.id.weight);

        registerButton1 = (Button) findViewById(R.id.register_button1);

        text_gender = findViewById(R.id.gender);
        String[] items = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        text_gender.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();
        registerButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Reterives user inputs
                final String birthday = text_birthday.getText().toString();
                final String name = text_name.getText().toString();
                final String email = text_email.getText().toString();
                final String usr_password = text_password.getText().toString();
                final String gender = text_gender.getSelectedItem().toString();
                final String weight = text_weight.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(usr_password) ) {
                    //userId =  mDatabase.push().getKey();
                   new_user = new User(email,usr_password,name,birthday,gender,weight);
                    //mDatabase.child(userId).setValue(new_user);
                    mAuth.createUserWithEmailAndPassword(email, usr_password)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        mDatabase.child("users").child(user.getUid()).setValue((email));
                                        mDatabase.child("users").child(user.getUid()).setValue(new_user);
                                        Intent intent = new Intent(Registration.this, MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Registration.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                        Toast.makeText(Registration.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // ...
                                }
                            });

                }
                else {
                    Toast.makeText(Registration.this, "Account Creation Failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }


                        });
            }


       // });

    }

