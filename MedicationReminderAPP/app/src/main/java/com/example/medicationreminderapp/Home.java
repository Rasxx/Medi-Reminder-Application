package com.example.medicationreminderapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import com.example.medicationreminderapp.databinding.ActivityReminderBinding;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.widget.Toast;
import android.content.Context;
import java.util.Calendar;
public class Home extends AppCompatActivity {
 Controllerdb controllerdb;
 SQLiteDatabase db;
 Button btn_Add;

 private ArrayList<String> Name = new ArrayList<String>();
 private ArrayList<String> AmountOfDose = new ArrayList<String>();
 private ArrayList<String> Day = new ArrayList<String>();
 private ArrayList<String> Time = new ArrayList<String>();
 String username="";
 ListView reminderView;
 private ActivityReminderBinding binding;
 private AlarmManager alarmManager;
 private PendingIntent pendingIntent;

 private Calendar calendar;


 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_home);
  binding = ActivityReminderBinding.inflate(getLayoutInflater());
  createNotificationChannel();
  controllerdb = new Controllerdb(this);
  reminderView = findViewById(R.id.ReminderListView);
  Bundle extras = getIntent().getExtras();
  if (extras != null) {
   username = extras.getString("UserName");
  }
  // Move to Add reminder activity
  btn_Add = findViewById(R.id.addmedbtn);
  registerForContextMenu(btn_Add);


  // Move to Description med Activity
  reminderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    String selected_Reminder = Name.get(position);
    Intent editReminderIntent = new Intent(Home.this, Details.class);
    editReminderIntent.putExtra("Username", username);
    editReminderIntent.putExtra("Name", selected_Reminder);
    startActivity(editReminderIntent);
   }
  });





 }



 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
  super.onCreateContextMenu(menu, v, menuInfo);
  MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.context_menu,menu);

  menu.setHeaderTitle("Menu");

 }

 @Override
 public boolean onContextItemSelected(MenuItem item) {
  if (item.getTitle().equals("Add Medication")) {
   Intent intent = new Intent(Home.this, AddMedication.class);
   intent.putExtra("Username", username);
   startActivity(intent);}

  return true;

 }

 @Override
 protected void onResume() {
  super.onResume();
  displayData();
 }


 @SuppressLint("Range")
 private void displayData() {
  db = controllerdb.getReadableDatabase();

  String query = "SELECT Medication.Name,  Medication.UserName, Medication.AmountOfDose, Reminder.Day, Reminder.Time " +
          "FROM Medication INNER JOIN Reminder ON Medication.Name = Reminder.NameOfMed " +
          "WHERE Medication.UserName = '" + username + "'";
  Cursor cursor = db.rawQuery(query, null);

  Name.clear();
  AmountOfDose.clear();
  Day.clear();
  Time.clear();

  if (cursor.moveToFirst()) {
   do {
    Name.add(cursor.getString(cursor.getColumnIndex("Name")));
    AmountOfDose.add(cursor.getString(cursor.getColumnIndex("AmountOfDose")));
    Day.add(cursor.getString(cursor.getColumnIndex("Day")));
    Time.add(cursor.getString(cursor.getColumnIndex("Time")));
   } while (cursor.moveToNext());
  }

  CustomAdapter ca = new CustomAdapter(Home.this, Name, AmountOfDose, Day, Time);
  // Schedule alarms for each medication reminder
  for (int i = 0; i < Name.size(); i++) {
   String reminderName = Name.get(i);
   String time = Time.get(i);

   // Parse the time string to extract hours and minutes
   String[] timeParts = time.split(":");
   int hour = Integer.parseInt(timeParts[0]);
   int minute = Integer.parseInt(timeParts[1]);

   // Create a calendar instance and set the alarm time
   Calendar calendar = Calendar.getInstance();
   calendar.set(Calendar.HOUR_OF_DAY, hour);
   calendar.set(Calendar.MINUTE, minute);
   calendar.set(Calendar.SECOND, 0);

   // Check if the alarm time is in the past, if so, add a day to it
   Calendar now = Calendar.getInstance();
   if (calendar.before(now)) {
    calendar.add(Calendar.DAY_OF_MONTH, 1);
   }

   // Create an intent for the ReminderReceiver class
   Intent alarmIntent = new Intent(Home.this, ReminderReceiver.class);
   alarmIntent.putExtra("ReminderName", reminderName);

   // Create a PendingIntent for the alarm
   PendingIntent pendingIntent = PendingIntent.getBroadcast(Home.this, i, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

   // Get the AlarmManager service and schedule the alarm
   AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
   alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
  }
  reminderView.setAdapter(ca);

  cursor.close();
 }

 private void createNotificationChannel(){
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
   CharSequence name = "akchannel";
   String desc = "Channel for Alarm Manager";
   int imp = NotificationManager.IMPORTANCE_HIGH;
   NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
   channel.setDescription(desc);
   NotificationManager notificationManager = getSystemService(NotificationManager.class);
   notificationManager.createNotificationChannel(channel);
  }
 }}