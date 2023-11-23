package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Contact_2 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4;
    EditText editText;
    String eduction, marital, income, religion, other_religion;
    public static  final String EDUCATION ="level_of_eduction";
    public static  final String MARITAL ="marital_status";
    public static  final String INCOME ="level_of_income";
    public static  final String RELIGION ="religion";
    public static  final String OTHER_RELIGION ="other_religion";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_2, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup1 = view.findViewById(R.id.radioGroup_1);
        radioGroup2 = view.findViewById(R.id.radioGroup_2);
        radioGroup3 = view.findViewById(R.id.radioGroup_3);
        radioGroup4 = view.findViewById(R.id.radioGroup_4);
        linearLayout = view.findViewById(R.id.other_layout);
        editText = view.findViewById(R.id.other);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                eduction = selectedRadioButton.getText().toString();
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                marital = selectedRadioButton.getText().toString();
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                religion = selectedRadioButton.getText().toString();

                if (religion.equals("Other")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                }
                editText.setText("");
            }
        });

        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                income = selectedRadioButton.getText().toString();
            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Contact_1());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_religion = editText.getText().toString().trim();

                if (eduction.isEmpty() || marital.isEmpty() || income.isEmpty() || religion.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(religion.equals("Other") && other_religion.isEmpty()){
                    Toast.makeText(requireActivity(), "Please provide your other religion", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        return view;
    }

    private void save_data() {
        editor.putString(RELIGION, religion);
        editor.putString(INCOME, income);
        editor.putString(EDUCATION, eduction);
        editor.putString(MARITAL, marital);
        editor.putString(OTHER_RELIGION, other_religion);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Residence());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        eduction = sharedPreferences.getString(EDUCATION, "");
        income = sharedPreferences.getString(INCOME, "");
        religion = sharedPreferences.getString(RELIGION, "");
        marital = sharedPreferences.getString(MARITAL, "");
        other_religion = sharedPreferences.getString(OTHER_RELIGION, "");
    }

    private void update_views() {
        if (!eduction.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup1, eduction);
        }

        if (!marital.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup2, marital);
        }

        if (!religion.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup3, religion);
            if (religion.equals("Other")){
                linearLayout.setVisibility(View.VISIBLE);
                editText.setText(other_religion);
            }
        }

        if (!income.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup4, income);
        }
    }
}