package com.example.medicationreminderapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class Details extends AppCompatActivity {

    Controllerdb controllerdb = new Controllerdb((this));
    SQLiteDatabase db;
    ListView lv;
    Button delete;
    Button gohome;

    private ArrayList<String> NameMed = new ArrayList<>();
    private ArrayList<String> AOD = new ArrayList<>();
    private ArrayList<String> Type = new ArrayList<>();
    private ArrayList<String> Day = new ArrayList<>();
    private ArrayList<String> Time = new ArrayList<>();
    private ArrayList<String> TypeOfRemindar = new ArrayList<>();
    String username = "";
    String MedName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        lv = findViewById(R.id.lstvw);


        // Retrieve the medication name from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MedName = extras.getString("Name");
            username = extras.getString("UserName");
        }

        DisplayData();
        delete = (Button) findViewById(R.id.Delete);
        gohome = (Button) findViewById(R.id.Home);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = controllerdb.getWritableDatabase();


                db.execSQL("DELETE FROM Medication WHERE Name IN (SELECT NameOfMed FROM Reminder WHERE NameOfMed = '" + MedName + "');");
                db.execSQL("DELETE FROM Reminder WHERE NameOfMed = '" + MedName + "';");


                Toast.makeText(Details.this, "Data Deleted From Sqlite Database", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(Details.this, Home.class);
                intent.putExtra("Username", username);
                startActivity(intent);


            }
        });

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Details.this, Home.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

    }


    @SuppressLint("Range")
    public void DisplayData() {


        db = controllerdb.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Medication INNER JOIN Reminder ON Medication.Name = Reminder.NameOfMed " +
                "WHERE Medication.Name = '" + MedName + "' AND Medication.Username = '" + username +  "';", null);
        NameMed.clear(); // array name
        AOD.clear();
        Type.clear();
        Day.clear();
        Time.clear();
        TypeOfRemindar.clear();

        if (cursor.moveToFirst()) {
            do {

                NameMed.add(cursor.getString(cursor.getColumnIndex("Name")));
                AOD.add(cursor.getString(cursor.getColumnIndex("AmountOfDose")));
                Day.add(cursor.getString(cursor.getColumnIndex("Day")));
                Time.add(cursor.getString(cursor.getColumnIndex("Time")));
                Type.add(cursor.getString(cursor.getColumnIndex("Medication.Type")));
                TypeOfRemindar.add(cursor.getString(cursor.getColumnIndex("Reminder.Type")));

            } while (cursor.moveToNext());
        }

        CustomAdapter Md = new CustomAdapter(Details.this, NameMed, AOD, Type, Day, Time, TypeOfRemindar);
        lv.setAdapter(Md);
        cursor.close();
    }
}