package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Register.REGISTER_SHARED_PREFS;
import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.Calendar;

public class Identification extends Fragment {

    //    public static  final String TOKEN ="access_token";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn, select_btn;
    EditText editText_screening_number, editText_first_name, editText_last_name, editText_age, editText_middle_name;
    String middle, first, last, ax;
    int age;
    int months = 0;
    int weeks = 0;
//    public static  final String SCREENING_NUMBER ="screening_number";
    public static  final String FIRST_NAME ="first_name";
    public static  final String MIDDLE_NAME ="middle_name";
    public static  final String LAST_NAME ="last_name";
    public static  final String AGE ="age";
    public static  final String DOB ="date_of_birth";
    public static  final String MONTHS ="months";
    public static  final String WEEKS ="weeks";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_identification, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(REGISTER_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_middle_name = view.findViewById(R.id.middle_name);
        editText_first_name = view.findViewById(R.id.first_name);
        editText_last_name = view.findViewById(R.id.last_name);
        editText_age = view.findViewById(R.id.age);
        editText_age = view.findViewById(R.id.age);
        editText_age = view.findViewById(R.id.age);
        select_btn = view.findViewById(R.id.select);

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Citizen());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                middle = editText_middle_name.getText().toString().trim();
                first = editText_first_name.getText().toString().trim();
                last = editText_last_name.getText().toString().trim();
                ax = editText_age.getText().toString().trim();

                if (first.isEmpty() || last.isEmpty() || ax.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
//                    Toast.makeText(requireActivity(), "months " + months + " weeks " + weeks, Toast.LENGTH_SHORT).show();
                }
            }
        });

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog();
            }
        });

        return view;
    }

    private void save_data() {
        editor.putString(MIDDLE_NAME, middle);
        editor.putString(FIRST_NAME, first);
        editor.putString(LAST_NAME, last);
        editor.putInt(AGE, Integer.parseInt(ax));
        editor.putInt(MONTHS, months);
        editor.putInt(WEEKS, weeks);

        if (select_btn.getText().toString().trim() == "Select Date"){
            editor.putString(DOB, "");
        }
        else {
            editor.putString(DOB, select_btn.getText().toString().trim());
        }

        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Contact_1());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        middle = sharedPreferences.getString(MIDDLE_NAME, "");
        first = sharedPreferences.getString(FIRST_NAME, "");
        last = sharedPreferences.getString(LAST_NAME, "");
        age = sharedPreferences.getInt(AGE, 0);
    }

    private void update_views() {
        editText_middle_name.setText(middle);
        editText_first_name.setText(first);
        editText_last_name.setText(last);

        FunctionalUtils.checkZeroValue(editText_age, age);
//        editText_age.setText(String.valueOf(age));
    }

    private void open_dialog(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                select_btn.setText(day + "/" + (month+1) + "/" + year);

                // Calculate age
                Calendar dob = Calendar.getInstance();
                dob.set(year, month, day);

                age = currentYear - year;
                months = currentMonth - month;
                int ageDays = currentDay - day;

                // Adjust age values based on negative differences
                if (ageDays < 0) {
                    months--;
                    ageDays += dob.getActualMaximum(Calendar.DAY_OF_MONTH);
                }

                if (months < 0) {
                    age--;
                    months += 12;
                }

                // Calculate weeks (assuming 7 days per week)
                weeks = ageDays / 7;
                editText_age.setText(String.valueOf(age));

            }
        }, currentYear, currentMonth, currentDay);

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }
}