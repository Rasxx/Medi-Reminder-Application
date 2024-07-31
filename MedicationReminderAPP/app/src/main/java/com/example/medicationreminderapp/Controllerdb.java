package com.example.medicationreminderapp;


import android.content. Context;
import android.database.sqlite.SQLiteDatabase;
import android. database.sqlite.SQLiteOpenHelper;

public class Controllerdb  extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "projectdb";

    public Controllerdb(Context applicationcontext) {
        super(applicationcontext, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //method
        String query;

        // Create users table to insert data
        query = "CREATE TABLE IF NOT EXISTS users (UserName VARCHAR PRIMARY KEY, Email VARCHAR, Password VARCHAR);";
        db.execSQL(query);

        // Create Medication table to insert data
        query = "CREATE TABLE IF NOT EXISTS Medication (Name VARCHAR PRIMARY KEY, AmountOfDose INTEGER, Type VARCHAR, UserName VARCHAR, FOREIGN KEY (UserName) REFERENCES users(UserName));";
        db.execSQL(query);

// Create Reminder table to insert data
        query = "CREATE TABLE IF NOT EXISTS Reminder (NameOfMed VARCHAR, Day VARCHAR, Time VARCHAR, Type VARCHAR, UserName VARCHAR, FOREIGN KEY (NameOfMed) REFERENCES Medication(Name), FOREIGN KEY (UserName) REFERENCES users(UserName));";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS Medication";
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS Reminder";
        db.execSQL(query);

        onCreate(db);
    }}
