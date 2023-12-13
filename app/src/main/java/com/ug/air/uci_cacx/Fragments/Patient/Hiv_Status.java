package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Art.ART;
import static com.ug.air.uci_cacx.Fragments.Patient.Art.LONG;
import static com.ug.air.uci_cacx.Fragments.Patient.Art.REGIMEN;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.Calendar;

public class Hiv_Status extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn, select_btn;
    EditText editText_viral;
    TextView textView_diagnosis;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    String hiv, diagnosed, cid;
    int viral;
    public static  final String HIV ="hiv_status";
    public static  final String DIAGNOSED ="diagnosed_for_hiv";
    public static  final String CID ="viral_load";
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hiv_status, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup_1);
        linearLayout = view.findViewById(R.id.nin_layout);
        editText_viral = view.findViewById(R.id.cid);
        textView_diagnosis = view.findViewById(R.id.diagnosed);
        select_btn = view.findViewById(R.id.select);
        linearLayout = view.findViewById(R.id.nin_layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                hiv = selectedRadioButton.getText().toString();

                if (hiv.equals("Positive")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    textView_diagnosis.setText("");
                    editText_viral.setText("");
                }
            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Tobacco());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diagnosed = textView_diagnosis.getText().toString().trim();
                cid = editText_viral.getText().toString().trim();

                if (hiv.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (hiv.equals("Positive") && (cid.isEmpty() || diagnosed.isEmpty())) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
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
        editor.putString(HIV, hiv);
        editor.putString(DIAGNOSED, diagnosed);
        if (cid.isEmpty()){
            editor.putInt(CID, 0);
        }
        else {
            editor.putInt(CID, Integer.parseInt(cid));
        }

        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (hiv.equals("Positive")){
            fr.replace(R.id.fragment_container, new Art());
        }
        else {
            editor.putString(ART, "");
            editor.putString(REGIMEN, "");
            editor.putString(LONG, "");
            editor.apply();
            fr.replace(R.id.fragment_container, new Parity());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        hiv = sharedPreferences.getString(HIV, "");
        diagnosed = sharedPreferences.getString(DIAGNOSED, "");
        viral = sharedPreferences.getInt(CID, 0);
    }

    private void update_views(){

        if (!hiv.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, hiv);
            if (hiv.equals("Positive")){
                textView_diagnosis.setText(diagnosed);
                FunctionalUtils.checkZeroValue(editText_viral, viral);
            }
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
                textView_diagnosis.setText(day + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }
}