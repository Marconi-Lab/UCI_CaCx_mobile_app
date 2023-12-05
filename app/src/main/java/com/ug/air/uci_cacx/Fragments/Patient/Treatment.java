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
import java.util.List;


public class Treatment extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    RadioGroup radioGroup_1, radioGroup_2;
    EditText editText_reason;
    Button next_btn, back_btn;
    String treatment, reason, given;
    public static  final String TREATMENT ="treatment";
    public static  final String GIVEN_TREATMENT ="when_treatment_was_given";
    public static  final String POSTPONE ="reason_for_postponing";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_treatment, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        linearLayout = view.findViewById(R.id.nin_layout);
        radioGroup_1 = view.findViewById(R.id.radioGroup);
        radioGroup_2 = view.findViewById(R.id.radioGroup_2);
        editText_reason = view.findViewById(R.id.reason);

        radioGroup_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                treatment = selectedRadioButton.getText().toString();
            }
        });

        radioGroup_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                given = selectedRadioButton.getText().toString();

                if (given.equals("Postponed")){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    reason = "";
                }
            }
        });

        load_data();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Screening_2());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = editText_reason.getText().toString().trim();

                if (treatment.isEmpty() || given.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (given.equals("Postponed") && reason.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    save_data();
                }
            }
        });


        return view;
    }

    private void save_data() {
        editor.putString(TREATMENT, treatment);
        editor.putString(GIVEN_TREATMENT, given);
        editor.putString(POSTPONE, reason);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Visit());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        treatment = sharedPreferences.getString(TREATMENT, "");
        reason = sharedPreferences.getString(POSTPONE, "");
        given = sharedPreferences.getString(GIVEN_TREATMENT, "");

        if (!treatment.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup_1, treatment);
        }

        if (!given.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup_2, given);
            if (given.equals("Postponed")){
                editText_reason.setText(reason);
            }
        }
    }
}