package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_Screening_2.PRIOR_SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_Treatment.TREATMENT_1;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_screening_3.RESULT_1;

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

import java.util.ArrayList;
import java.util.List;

public class Prior_Screening_1 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout, linearLayout_2;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    EditText editText;
    String reason, other_reason;
    public static  final String REASON_VISIT ="reason_for_visit";
    public static  final String OTHER_REASON ="other_reason_for_visit";

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
        editText = view.findViewById(R.id.reason);
        linearLayout = view.findViewById(R.id.nin_layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                reason = selectedRadioButton.getText().toString();

                if (reason.equals("Other")){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    editText.setText("");
                }
            }
        });

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
                other_reason = editText.getText().toString().trim();

                if (reason.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (reason.equals("Other") && other_reason.isEmpty()){
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
        editor.putString(REASON_VISIT, reason);
        editor.putString(OTHER_REASON, other_reason);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (reason.equals("Other") || reason.equals("Initial Screening")){
            editor.putString(PRIOR_SCREEN_METHOD, "");
            editor.putString(RESULT_1, "");
            editor.putString(TREATMENT_1, "");
            editor.apply();
            fr.replace(R.id.fragment_container, new Screening_1());
        }
        else {
            fr.replace(R.id.fragment_container, new Prior_Screening_2());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        reason = sharedPreferences.getString(REASON_VISIT, "");
        other_reason = sharedPreferences.getString(OTHER_REASON, "");
    }

    private void update_views(){
        if (!reason.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, reason);

            if (reason.equals("Other")){
                linearLayout.setVisibility(View.VISIBLE);
                editText.setText(other_reason);
            }
        }
    }
}