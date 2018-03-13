package com.example.ddk.benefits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.R.*;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.*;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;


public class Registration extends AppCompatActivity {
    protected Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        registerButton = (Button) findViewById(R.id.register);
        setContentView(R.layout.activity_registration);
        Spinner dropdown = findViewById(R.id.gender);
        String[] items = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();


 /*        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                // Reterives user inputs
               mAuth.createUserWithEmailAndPassword(,)
                        .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("TAG", "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "createUserWithEmailAndPassword", task.getException());
                                    Toast.makeText(LoginActivity.this, "User Account Creation failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            */}


       // });

    }

