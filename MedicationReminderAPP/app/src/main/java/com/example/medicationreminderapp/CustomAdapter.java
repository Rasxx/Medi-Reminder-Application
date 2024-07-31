package com.example.medicationreminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    Controllerdb controllerdb;
    private ArrayList<String> NameMed = new ArrayList<>();
    private ArrayList<String> AOD = new ArrayList<>();
    private ArrayList<String> Type = new ArrayList<>();
    private ArrayList<String> Day = new ArrayList<>();
    private ArrayList<String> Time = new ArrayList<>();
    private ArrayList<String> TypeOfRemindar = new ArrayList<>();
    // private RadioGroupSelectedListener radioGroupSelectedListener;
    private boolean useLayout1;

    public CustomAdapter(Context mContext, ArrayList<String> NameMed, ArrayList<String> AOD,
                         ArrayList<String> Type, ArrayList<String> Day, ArrayList<String> Time,
                         ArrayList<String> TypeOfReminder) {
        this.mContext = mContext;
        this.NameMed = NameMed;
        this.AOD = AOD;
        this.Type = Type;
        this.Day = Day;
        this.Time = Time;
        this.TypeOfRemindar = TypeOfReminder;
        this.useLayout1 = false;
    }


    public CustomAdapter(Context mContext, ArrayList<String> NameMed1, ArrayList<String> AOD,
                         ArrayList<String> Day, ArrayList<String> Time ){
        this.mContext = mContext;
        NameMed = NameMed1;
        this.AOD = AOD;
        this.Day = Day;
        this.Time= Time;
        this.useLayout1 = true;
    }

    @Override
    public int getCount() {
        return NameMed.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        controllerdb = new Controllerdb(mContext);
        LayoutInflater layoutInflater;

        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (useLayout1) {
                convertView = layoutInflater.inflate(R.layout.layout, null);
            } else {
                convertView = layoutInflater.inflate(R.layout.layout2, null);
            }
            holder = new ViewHolder();
            holder.NameMedTextView = convertView.findViewById(R.id.MedName);
            holder.AODTextView = convertView.findViewById(R.id.AOD);
            holder.DayTextView = convertView.findViewById(R.id.Day);
            holder.TimeTextView = convertView.findViewById(R.id.Time);
            if (!useLayout1) {
                holder.TypeTextView = convertView.findViewById(R.id.radioGroup3);
                holder.TypeOfRemindarTextView = convertView.findViewById(R.id.radioGroup1);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.NameMedTextView.setText(NameMed.get(position));
        holder.AODTextView.setText(AOD.get(position));
        holder.DayTextView.setText(Day.get(position));
        holder.TimeTextView.setText(Time.get(position));
        // ط¹ط´ط§ظ† ظ…ط§ ظ‚ط¯ط±ظ†ط§ ظ†ط³ظˆظٹ ظ„ظ‡ ظ…ط«ظ„ظ‡ظ…  74-91 ط§ظ„ظ„ظٹ ظ‡ظٹ ط§ظ„ ط±ظٹط¯ظٹظˆ ظ‚ط±ظˆط¨ ظپ ط³ظˆظٹظ†ط§ ظ„ظ„ظƒظ„ط§ط³ظٹظ† ظ…ظ† ط³ط·ط± 93-120
        // Set selected radio button for TypeTextView FOR ADDMEDICATION CLASS(RADIO GROUP3)
        if(!useLayout1){
            holder.TypeTextView.setOnCheckedChangeListener(null); // Clear previous listener to avoid triggering unnecessary events
            holder.TypeTextView.check(getSelectedRadioButtonId(Type.get(position)));
            holder.TypeTextView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton selectedRadioButton = radioGroup.findViewById(checkedId);
                    if (selectedRadioButton != null) {
                        String selectedType = selectedRadioButton.getText().toString();
                        //   radioGroupSelectedListener.onTypeSelected(position, selectedType);
                    }
                }
            });

            // Set selected radio button for TypeOfRemindarTextView // FOR REMAINDER CLASS (RADIO GROUP1)
            holder.TypeOfRemindarTextView.setOnCheckedChangeListener(null); // Clear previous listener to avoid triggering unnecessary events
            holder.TypeOfRemindarTextView.check(getSelectedRadioButtonId1(TypeOfRemindar.get(position)));
            holder.TypeOfRemindarTextView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton selectedRadioButton = radioGroup.findViewById(checkedId);
                    if (selectedRadioButton != null) {
                        String selectedRemindarType = selectedRadioButton.getText().toString();
                        //  radioGroupSelectedListener.onRemindarTypeSelected(position, selectedRemindarType);
                    }
                }
            });
        }

        return convertView;
    }

    private int getSelectedRadioButtonId(String selectedValue) {
        switch (selectedValue) {
            case "Capsule":
                return R.id.RBcapsule;
            case "Drops":
                return R.id.RBdrops;
            case "Injection":
                return R.id.RBinjection;
            case "Tablets":
                return R.id.RBtablets;
        }

        return -1;
    }
    private int getSelectedRadioButtonId1(String selectedValue) {
        switch (selectedValue) {
            case "Weekly":
                return R.id.Weekly;
            case "Monthly":
                return R.id.Monthly;
            case "Daily":
                return R.id.Daily;
        }
        return -1;
    }

    // public interface RadioGroupSelectedListener {
    //   void onTypeSelected(int position, String selectedType);

    //    void onRemindarTypeSelected(int position, String selectedRemindarType);
    // }

    private static class ViewHolder {
        TextView NameMedTextView;
        TextView AODTextView;
        RadioGroup TypeTextView;
        TextView DayTextView;
        TextView TimeTextView;
        RadioGroup TypeOfRemindarTextView;
    }
}