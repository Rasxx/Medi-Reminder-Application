package com.example.medicationreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.medicationreminderapp.databinding.ActivityReminderBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;


public class Reminder extends AppCompatActivity {
    Controllerdb db = new Controllerdb(this);
    SQLiteDatabase database;
    private TextView reminderMessageTextView;
    private EditText dayEditText;
    private Button typebtn;
    private Button submitButton;
    private Calendar calendar;
    private String medicationName="";
    private String type = "";
    private String username="";
    private String selectedType="";
    private String selectedTime = "";

    private ActivityReminderBinding binding;
    private List<String> validDays = Arrays.asList(
            "Saturday",
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "saturday",
            "sunday",
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday"
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        typebtn=(Button)findViewById(R.id.Type);
        typebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(Reminder.this,typebtn);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Store the selected type in the variable
                        selectedType = item.getTitle().toString();
                        return true;
                    }
                });
                popup.show();
            }
        });

        reminderMessageTextView = findViewById(R.id.textView);
        dayEditText = findViewById(R.id.Day);

        submitButton = findViewById(R.id.Submit);

        // Retrieve the medication name from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            medicationName = extras.getString("Name");
            username=extras.getString("UserName");
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = dayEditText.getText().toString().trim();

                boolean isValidDay = isValidDay(day);

                if (!isValidDay) {
                    Toast.makeText(Reminder.this, "Invalid day", Toast.LENGTH_SHORT).show();
                } else {
                    switch (selectedType) {
                        case "Weekly":
                            type = "Weekly";
                            break;
                        case "Monthly":
                            type = "Monthly";
                            break;
                        case "Daily":
                            type = "Daily";
                            break;
                    }

                    if (calendar != null) {
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        database = db.getWritableDatabase();
                        database.execSQL("INSERT INTO Reminder(NameOfMed,Day,Time,Type,UserName)" +
                                " VALUES('" + medicationName + "','" + day + "','" + getTimeString(calendar) + "','"+type+"','"+username+"')");

                        Toast.makeText(Reminder.this, "Data Inserted To Sqlite Database", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Reminder.this, Home.class );
                        intent.putExtra("Username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Reminder.this, "Please select a time", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.imageView6Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Select Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                    }
                });

                timePicker.show(getSupportFragmentManager(), "tag");
            }
        });
    }

    private String getTimeString(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(minute);

        if (hourString.length() == 1) {
            hourString = "0" + hourString;
        }

        if (minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }

        return hourString + ":" + minuteString;
    }

    private boolean isValidDay(String day) {
        return validDays.contains(day);
    }
}