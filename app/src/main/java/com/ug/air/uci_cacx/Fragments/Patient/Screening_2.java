package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_1.SCREEN_METHOD;

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
    LinearLayout linearLayout_1, linearLayout_2;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    String result, pap, hpv, method;
    public static  final String RESULT ="screening_result";
    public static  final String PAP ="pap_smear_screening_result";
    public static  final String HPV ="hpv_screening_result";
    List<String> checkBoxList_1 = new ArrayList<>();
    List<String> checkBoxList_2 = new ArrayList<>();

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
        linearLayout_1 = view.findViewById(R.id.pap);
        linearLayout_2 = view.findViewById(R.id.hpv);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                result = selectedRadioButton.getText().toString();

                if (result.equals("Positive")) {
                    if (method.equals("Pap Smear")){
                        linearLayout_1.setVisibility(View.VISIBLE);
                        linearLayout_2.setVisibility(View.GONE);
                    }
                    else if (method.equals("HPV")){
                        linearLayout_2.setVisibility(View.VISIBLE);
                        linearLayout_1.setVisibility(View.GONE);
                    }
                }
                else {
                    linearLayout_1.setVisibility(View.GONE);
                    linearLayout_2.setVisibility(View.GONE);
                }

            }
        });

        load_data();
        update_views();

        for (int i = 0; i < linearLayout_1.getChildCount(); i++) {
            if (linearLayout_1.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout_1.getChildAt(i);
                String value = checkBox.getText().toString();
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            checkBoxList_1.add(value);
                        }
                        else {
                            checkBoxList_1.remove(value);
                        }
                    }
                });
            }
        }

        for (int i = 0; i < linearLayout_2.getChildCount(); i++) {
            if (linearLayout_2.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout_2.getChildAt(i);
                String value = checkBox.getText().toString();
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            checkBoxList_2.add(value);
                        }
                        else {
                            checkBoxList_2.remove(value);
                        }
                    }
                });
            }
        }

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

                if (method.equals("Pap Smear")){
                    pap = FunctionalUtils.convertListToString(checkBoxList_1);
                }
                else {
                    hpv = FunctionalUtils.convertListToString(checkBoxList_2);
                }

                if (result.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (method.equals("Pap Smear") && pap.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (method.equals("HPV") && hpv.isEmpty()){
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
        editor.putString(RESULT, result);
        editor.putString(PAP, pap);
        editor.putString(HPV, hpv);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Treatment());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        result = sharedPreferences.getString(RESULT, "");
        pap = sharedPreferences.getString(PAP, "");
        hpv = sharedPreferences.getString(HPV, "");
    }

    private void update_views(){
        if (!result.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, result);

            if (result.equals("Positive")){
                if (method.equals("Pap Smear")){
                    linearLayout_1.setVisibility(View.VISIBLE);
                    checkBoxList_1.clear();
                    checkBoxList_1 = FunctionalUtils.convertStringToList(pap);
                    FunctionalUtils.checkBoxes(linearLayout_1, checkBoxList_1);
                }
                else if (method.equals("HPV")){
                    linearLayout_2.setVisibility(View.VISIBLE);
                    checkBoxList_2.clear();
                    checkBoxList_2 = FunctionalUtils.convertStringToList(hpv);
                    FunctionalUtils.checkBoxes(linearLayout_2, checkBoxList_2);
                }
            }
        }
    }
}