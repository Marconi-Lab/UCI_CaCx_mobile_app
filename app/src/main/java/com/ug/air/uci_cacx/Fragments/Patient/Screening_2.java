package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_1.SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Patient.Treatment.GIVEN_TREATMENT;
import static com.ug.air.uci_cacx.Fragments.Patient.Treatment.POSTPONE;
import static com.ug.air.uci_cacx.Fragments.Patient.Treatment.TREATMENT;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.List;

public class Screening_2 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    RadioGroup radioGroup, radioGroup2, radioGroup3, radioGroup4;
    String result, pap, hpv, method, via, col, susp;
    public static  final String RESULT_VIA ="via_screening_result";
    public static  final String SUSPICIOUS ="suspicious_of_cancer";
    public static  final String RESULT_COL ="Colposcopy_screening_result";
    public static  final String PAP ="pap_smear_screening_result";
    public static  final String HPV ="hpv_screening_result";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_screening_2, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        method = sharedPreferences.getString(SCREEN_METHOD, "");

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup2 = view.findViewById(R.id.radioGroup_2);
        radioGroup3 = view.findViewById(R.id.radioGroup_3);
        radioGroup4 = view.findViewById(R.id.radioGroup_4);
        linearLayout = view.findViewById(R.id.nin_layout);

        if (method.equals("VIA") || method.equals("Colposcopy")){
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup2.setVisibility(View.GONE);
            radioGroup3.setVisibility(View.GONE);
        }
        else if (method.equals("HPV")) {
            radioGroup.setVisibility(View.GONE);
            radioGroup2.setVisibility(View.VISIBLE);
            radioGroup3.setVisibility(View.GONE);
        }
        else {
            radioGroup.setVisibility(View.GONE);
            radioGroup2.setVisibility(View.GONE);
            radioGroup3.setVisibility(View.VISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                result = selectedRadioButton.getText().toString();

                if (result.equals("Suspicious of cancer")){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    susp = "";
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                result = selectedRadioButton.getText().toString();
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                result = selectedRadioButton.getText().toString();

                if (result.equals("Suspicious of cancer")){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    susp = "";
                }
            }
        });

        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                susp = selectedRadioButton.getText().toString();
            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (method.equals("VIA")){
                    fr.replace(R.id.fragment_container, new Photo_4());
                }
                else {
                    fr.replace(R.id.fragment_container, new Screening_1());
                }
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (result.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (result.equals("Suspicious of cancer") && susp.isEmpty()){
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
        switch (method) {
            case "VIA":
                editor.putString(RESULT_VIA, result);
                editor.putString(RESULT_COL, "");
                editor.putString(HPV, "");
                editor.putString(PAP, "");
                break;
            case "Colposcopy":
                editor.putString(RESULT_COL, result);
                editor.putString(RESULT_VIA, "");
                editor.putString(HPV, "");
                editor.putString(PAP, "");
                break;
            case "HPV":
                editor.putString(HPV, result);
                editor.putString(RESULT_COL, "");
                editor.putString(RESULT_VIA, "");
                editor.putString(PAP, "");
                break;
            default:
                editor.putString(PAP, result);
                editor.putString(RESULT_COL, "");
                editor.putString(RESULT_VIA, "");
                editor.putString(HPV, "");

                break;
        }
        editor.putString(SUSPICIOUS, susp);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (result.equals("Positive") || (method.equals("HPV") && !result.equals("Negative")) || (method.equals("Pap Smear") && !result.equals("Negative"))){
            fr.replace(R.id.fragment_container, new Treatment());
        }
        else {
            editor.putString(TREATMENT, "");
            editor.putString(GIVEN_TREATMENT, "");
            editor.putString(POSTPONE, "");
            editor.apply();
            fr.replace(R.id.fragment_container, new Visit());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        via = sharedPreferences.getString(RESULT_VIA, "");
        col = sharedPreferences.getString(RESULT_COL, "");
        pap = sharedPreferences.getString(PAP, "");
        hpv = sharedPreferences.getString(HPV, "");
        if (!via.isEmpty()){
            result = via;
        }
        else if (!col.isEmpty()){
            result = col;
        }
        else if (!pap.isEmpty()){
            result = pap;
        }
        else if (!hpv.isEmpty()){
            result = hpv;
        }
        else {
            result = "";
        }
        susp = sharedPreferences.getString(SUSPICIOUS, "");
    }

    private void update_views(){
        if (!via.isEmpty() || !col.isEmpty()){
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup2.setVisibility(View.GONE);
            radioGroup3.setVisibility(View.GONE);
            FunctionalUtils.setRadioButton(radioGroup, via);
            if (!susp.isEmpty()){
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.setRadioButton(radioGroup4, susp);
            }
        }
        if (!col.isEmpty()){
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup2.setVisibility(View.GONE);
            radioGroup3.setVisibility(View.GONE);
            FunctionalUtils.setRadioButton(radioGroup, col);
            if (!susp.isEmpty()){
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.setRadioButton(radioGroup4, susp);
            }
        }
        if (!hpv.isEmpty()){
            radioGroup.setVisibility(View.GONE);
            radioGroup2.setVisibility(View.VISIBLE);
            radioGroup3.setVisibility(View.GONE);
            FunctionalUtils.setRadioButton(radioGroup2, hpv);
            susp = "";
        }
        if (!pap.isEmpty()){
            radioGroup.setVisibility(View.GONE);
            radioGroup2.setVisibility(View.GONE);
            radioGroup3.setVisibility(View.VISIBLE);
            FunctionalUtils.setRadioButton(radioGroup3, pap);
            if (!susp.isEmpty()){
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.setRadioButton(radioGroup4, susp);
            }
        }

    }
}