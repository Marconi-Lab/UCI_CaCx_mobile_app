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

public class Prior_Screening_1 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout, linearLayout_2;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    EditText editText;
    String prior, method, other;
    public static  final String PRIOR ="prior_cacx_screening";
    public static  final String PRIOR_METHOD ="prior_screening_method";
    public static  final String OTHER_METHOD ="other_prior_screening_method";
    List<String> checkBoxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_prior__screening_1, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        linearLayout = view.findViewById(R.id.check_layout);
        linearLayout_2 = view.findViewById(R.id.nin_layout);
        editText = view.findViewById(R.id.other);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                prior = selectedRadioButton.getText().toString();

                if (prior.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    editText.setText("");
                }
            }
        });

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
                            else {
                                linearLayout_2.setVisibility(View.GONE);
                                editText.setText("");
                            }
                        }
                        else {
                            checkBoxList.remove(value);
                        }
                    }
                });
            }
        }

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Symptoms());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = editText.getText().toString().trim();
                method = FunctionalUtils.convertListToString(checkBoxList);

                if (prior.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (prior.equals("Yes") && method.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (method.contains("Other") && other.isEmpty()) {
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
        editor.putString(PRIOR, prior);
        editor.putString(PRIOR_METHOD, method);
        editor.putString(OTHER_METHOD, other);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (prior.equals("Yes")){
            fr.replace(R.id.fragment_container, new Prior_Screening_2());
        }
        else {
            fr.replace(R.id.fragment_container, new Referred());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        prior = sharedPreferences.getString(PRIOR, "");
        method = sharedPreferences.getString(PRIOR_METHOD, "");
        other = sharedPreferences.getString(OTHER_METHOD, "");
    }

    private void update_views(){
        if (!prior.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, prior);

            if (prior.equals("Yes")){
                linearLayout.setVisibility(View.VISIBLE);

                checkBoxList.clear();
                checkBoxList = Arrays.asList(method.split(", "));
                FunctionalUtils.checkBoxes(linearLayout, checkBoxList);

                if (method.contains("Other")){
                    linearLayout_2.setVisibility(View.VISIBLE);
                    editText.setText(other);
                }
            }
        }
    }
}