package com.example.medicationreminderapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup extends AppCompatActivity implements View.OnClickListener {
    EditText stdName, stEmail, stdPassword;
    Button Signupbutton, prevbutton;
    Controllerdb controllerdb;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        controllerdb = new Controllerdb(this);


        stdName = findViewById(R.id.editUserName);
        stEmail = findViewById(R.id.editEmail);
        stdPassword = findViewById(R.id.editPassword);
        Signupbutton = findViewById(R.id.btsignup);
        Signupbutton.setOnClickListener(this);

        prevbutton = (Button) findViewById(R.id.btnprev2);

    }
    @Override
    public void onClick (View view){
        if (view.getId() == R.id.btsignup) {
            database = controllerdb.getWritableDatabase();

            String stdname = stdName.getText().toString();
            String stemail = stEmail.getText().toString();
            String stpass = stdPassword.getText().toString();

            database.execSQL("INSERT INTO users(UserName,Email,Password) VALUES('" + stdname + "','" +stemail + "','" + stpass + "')");
            Toast.makeText(signup.this, "Data Inserted Into Sql Database", Toast.LENGTH_LONG).show();


            Intent intent = new Intent(signup.this, login.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnprev2) {
            Intent intent = new Intent(signup.this, MainActivity.class);
            startActivity(intent);
        }
    }


}


