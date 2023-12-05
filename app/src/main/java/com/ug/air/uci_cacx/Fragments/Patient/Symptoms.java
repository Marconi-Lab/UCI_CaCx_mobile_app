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


public class Symptoms extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout, linearLayout_2;
    Button next_btn, back_btn;
    RadioGroup radioGroup, radioGroup2;
    EditText editText;
    String sym, symptoms, other;
    public static  final String SYM ="patient_with_symptoms";
    public static  final String SYMPTOMS ="symptom";
    public static  final String OTHER_SYM ="other_symptoms";
    List<String> checkBoxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_symptoms, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup2 = view.findViewById(R.id.radioGroup_2);
        linearLayout = view.findViewById(R.id.check_layout);
        linearLayout_2 = view.findViewById(R.id.nin_layout);
        editText = view.findViewById(R.id.other);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                sym = selectedRadioButton.getText().toString();

                if (sym.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    editText.setText("");
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                symptoms = selectedRadioButton.getText().toString();

                if (symptoms.equals("Other")) {
                    linearLayout_2.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout_2.setVisibility(View.GONE);
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
                fr.replace(R.id.fragment_container, new Contraceptives());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = editText.getText().toString().trim();

                if (sym.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (sym.equals("Yes") && symptoms.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (symptoms.equals("Other") && other.isEmpty()) {
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
        editor.putString(SYM, sym);
        editor.putString(SYMPTOMS, symptoms);
        editor.putString(OTHER_SYM, other);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Prior_Screening_1());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        sym = sharedPreferences.getString(SYM, "");
        symptoms = sharedPreferences.getString(SYMPTOMS, "");
        other = sharedPreferences.getString(OTHER_SYM, "");
    }

    private void update_views(){
        if (!sym.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, sym);

            if (sym.equals("Yes")){
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.setRadioButton(radioGroup2, symptoms);

                if (symptoms.equals("Other")){
                    linearLayout_2.setVisibility(View.VISIBLE);
                    editText.setText(other);
                }
            }
        }
    }
}