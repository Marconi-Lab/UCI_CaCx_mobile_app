package com.ug.air.uci_cacx.Fragments;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Contraceptives extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout, linearLayout_2, linearLayout_3;
    Button next_btn, back_btn;
    RadioGroup radioGroup, radioGroup2;
    EditText editText;
    String contra, method, other, months;
    public static  final String CONTRA ="contraceptives_used";
    public static  final String MONTHS ="duration_on_contraceptives";
    public static  final String CONTRA_METHOD ="contraceptives_method";
    public static  final String OTHER_CONTRA ="other_contraceptives_method";
    List<String> checkBoxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contraceptives, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        linearLayout_2 = view.findViewById(R.id.nin_layout);
        radioGroup2 = view.findViewById(R.id.radioGroup_1);
        linearLayout_3 = view.findViewById(R.id.con_layout);
        linearLayout = view.findViewById(R.id.check_layout);
        editText = view.findViewById(R.id.other);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                contra = selectedRadioButton.getText().toString();

                if (contra.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout_3.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    linearLayout_3.setVisibility(View.GONE);
                    method = "";
                    months = "";
                    editText.setText("");
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                months = selectedRadioButton.getText().toString();
            }
        });

        load_data();
        update_views();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                String value = checkBox.getText().toString();
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            checkBoxList.add(value);
                            if (value.equals("Other")){
                                linearLayout_2.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            if (value.equals("Other")){
                                linearLayout_2.setVisibility(View.GONE);
                                editText.setText("");
                            }
                            checkBoxList.remove(value);
                        }
                    }
                });
            }
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Parity());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = editText.getText().toString().trim();
                method = FunctionalUtils.convertListToString(checkBoxList);

                if (contra.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (contra.equals("Yes") && (method.isEmpty() || months.isEmpty())){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (method.contains("Other") && other.isEmpty()){
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
        editor.putString(CONTRA, contra);
        editor.putString(CONTRA_METHOD, method);
        editor.putString(OTHER_CONTRA, other);
        editor.putString(MONTHS, months);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Symptoms());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        contra = sharedPreferences.getString(CONTRA, "");
        method = sharedPreferences.getString(CONTRA_METHOD, "");
        other = sharedPreferences.getString(OTHER_CONTRA, "");
        months = sharedPreferences.getString(MONTHS, "");
    }

    private void update_views(){

        if (!contra.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, contra);

            if (contra.equals("Yes")){
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout_3.setVisibility(View.VISIBLE);
                FunctionalUtils.setRadioButton(radioGroup2, months);

                checkBoxList.clear();
                checkBoxList = FunctionalUtils.convertStringToList(method);
                FunctionalUtils.checkBoxes(linearLayout, checkBoxList);

                if (method.contains("Other")){
                    linearLayout_2.setVisibility(View.VISIBLE);
                    editText.setText(other);
                }
            }
        }
    }

}