package com.example.medicationreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    Button Login1, Prevbutton;
    EditText editName1;
    EditText editPassword1;
    Controllerdb controllerdb;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controllerdb = new Controllerdb(this);
        Login1 = findViewById(R.id.Login1);
        editName1 = findViewById(R.id.editName1);
        editPassword1 = findViewById(R.id.editPassword1);

        Prevbutton = findViewById(R.id.btnprev);

        Prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editName1.getText().toString();
                String password = editPassword1.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "You did not enter a valid username or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isExist = checkUserExist(username, password);

                if (isExist) {
                    Intent intent = new Intent(login.this, Home.class);
                    intent.putExtra("username", username);
                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    ///////////////////////////bounse?
                    editPassword1.setText(null);
                    Toast.makeText(login.this, "Login Failed..Invalid UserName or Password!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkUserExist(String username, String password) {
        String[] columns = {"UserName"};

        db = controllerdb.getReadableDatabase();

        String selection = "UserName=? and Password=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}