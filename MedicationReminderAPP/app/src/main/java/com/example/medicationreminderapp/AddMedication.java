package com.example.medicationreminderapp;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddMedication extends AppCompatActivity {
    Button NextToReminder;
    EditText MedName;
    EditText AOD;
    RadioButton radioButton;
    RadioGroup radioGroup;
    Controllerdb controllerdb;
    Intent intent;
    SQLiteDatabase db;
    String username = "";
    String typeofmed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        controllerdb = new Controllerdb(this);

        NextToReminder = (Button) findViewById(R.id.NextToReminder);
        MedName = (EditText) findViewById(R.id.MedName);
        AOD = (EditText) findViewById(R.id.AOD);

        NextToReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup = (RadioGroup) findViewById(R.id.radioGroup3);
                String selected = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

                switch (selected) {
                    case "Capsule":
                        typeofmed= "Capsule";
                        break;
                    case "Drops":
                        typeofmed = "Drops";
                        break;
                    case "Injection":
                        typeofmed = "Injection";
                        break;
                    case "Tablets":
                        typeofmed = "Tablets";
                        break;
                    default:
                        typeofmed = "";
                        break;
                }

                db = controllerdb.getWritableDatabase();
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    username = extras.getString("UserName");

                }
                String stname = MedName.getText().toString();
                String stAOD = AOD.getText().toString();



                db.execSQL("INSERT INTO Medication(Name, AmountOfDose, Type, UserName)" +
                        " VALUES('" + stname + "','" + stAOD + "','" + typeofmed + "','" + username + "')");

                Intent intent = new Intent(AddMedication.this, Reminder.class);
                intent.putExtra("Name", stname);
                intent.putExtra("Username", username);
                startActivity(intent);

            }
        });
    }
}