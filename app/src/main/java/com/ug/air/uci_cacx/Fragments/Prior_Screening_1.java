package com.ug.air.uci_cacx.Fragments;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Prior_Screening_2.PRIOR_OTHER_SCREENING_METHOD;
import static com.ug.air.uci_cacx.Fragments.Prior_Screening_2.PRIOR_SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Prior_Treatment.TREATMENT_1;
import static com.ug.air.uci_cacx.Fragments.Prior_screening_3.HPV_1;
import static com.ug.air.uci_cacx.Fragments.Prior_screening_3.PAP_1;
import static com.ug.air.uci_cacx.Fragments.Prior_screening_3.RESULT_1;

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
    String prior;
    public static  final String PRIOR ="prior_cacx_screening";
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                prior = selectedRadioButton.getText().toString();
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

                if (prior.isEmpty()){
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
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (prior.equals("Yes")){
            fr.replace(R.id.fragment_container, new Prior_Screening_2());
        }
        else {
            editor.putString(PRIOR_SCREEN_METHOD, "");
            editor.putString(PRIOR_OTHER_SCREENING_METHOD, "");
            editor.putString(RESULT_1, "");
            editor.putString(PAP_1, "");
            editor.putString(HPV_1, "");
            editor.putString(TREATMENT_1, "");
            editor.apply();
            fr.replace(R.id.fragment_container, new Referred());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        prior = sharedPreferences.getString(PRIOR, "");
    }

    private void update_views(){
        if (!prior.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, prior);
        }
    }
}