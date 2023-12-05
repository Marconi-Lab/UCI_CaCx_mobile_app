package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_1.SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_COL;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_VIA;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.Calendar;


public class Visit extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn, select_btn;
    EditText editText_reco, editText_visit;
    TextView textView_visit;
    RadioGroup radioGroup1, radioGroup2;
    String recco, visit, reminder, cancer;
    public static  final String NEXT_VISIT ="next_visit";
    public static  final String OBSERVE ="clinician_observations";
    public static  final String REMINDER ="next_visit_message_reminders";
    public static  final String INFORMATION ="get_information_about_cancer";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_visit, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_reco = view.findViewById(R.id.recco);
        textView_visit = view.findViewById(R.id.visit);
        select_btn = view.findViewById(R.id.select);
        radioGroup1 = view.findViewById(R.id.radioGroup_1);
        radioGroup2 = view.findViewById(R.id.radioGroup_2);

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog();
            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                reminder = selectedRadioButton.getText().toString();
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                cancer = selectedRadioButton.getText().toString();
            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String via = sharedPreferences.getString(RESULT_VIA, "");
                String col = sharedPreferences.getString(RESULT_COL, "");

                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (via.equals("Positive") || col.equals("Positive")){
                    fr.replace(R.id.fragment_container, new Treatment());
                }
                else {
                    fr.replace(R.id.fragment_container, new Screening_2());
                }
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recco = editText_reco.getText().toString().trim();
                visit = textView_visit.getText().toString().trim();

                if (recco.isEmpty() || visit.isEmpty() || cancer.isEmpty() || reminder.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }

            }
        });

        return view;
    }

    private void save_data() {
        editor.putString(REMINDER, reminder);
        editor.putString(OBSERVE, recco);
        editor.putString(NEXT_VISIT, visit);
        editor.putString(INFORMATION, cancer);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();


        String method = sharedPreferences.getString(SCREEN_METHOD, "");
        if (method.equals("VIA")){
            fr.replace(R.id.fragment_container, new Clinicians());
        }
        else{
            fr.replace(R.id.fragment_container, new Results());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        reminder = sharedPreferences.getString(REMINDER, "");
        recco = sharedPreferences.getString(OBSERVE, "");
        visit = sharedPreferences.getString(NEXT_VISIT, "");
        cancer = sharedPreferences.getString(INFORMATION, "");
    }

    private void update_views(){
        editText_reco.setText(recco);
        textView_visit.setText(visit);

        if (!reminder.isEmpty()) {
            FunctionalUtils.setRadioButton(radioGroup1, reminder);
        }

        if (!cancer.isEmpty()) {
            FunctionalUtils.setRadioButton(radioGroup2, cancer);
        }
    }

    private void open_dialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textView_visit.setText(day + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }
}