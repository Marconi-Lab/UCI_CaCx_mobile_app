package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Height extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    TextView textView;
    EditText editText_height, editText_weight, editText_systolic, editText_diastolic;
    String we, he, sys, dis;
    int height, systolic, diastolic;
    float weight, bmi;
    public static  final String WEIGHT ="weight";
    public static  final String HEIGHT ="height";
    public static  final String SYSTOLIC ="systolic";
    public static  final String DIASTOLIC ="diastolic";
    public static  final String BLOOD_PRESSURE ="blood_pressure";
    public static  final String BMI ="body_mass_index";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_height, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_height = view.findViewById(R.id.height);
        editText_weight = view.findViewById(R.id.weight);
        editText_systolic = view.findViewById(R.id.systolic);
        editText_diastolic = view.findViewById(R.id.diastolic);
        textView = view.findViewById(R.id.bmi);

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Contact_3());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                we = editText_weight.getText().toString().trim();
                he = editText_height.getText().toString().trim();
                sys = editText_systolic.getText().toString().trim();
                dis = editText_diastolic.getText().toString().trim();

                if (we.isEmpty() || he.isEmpty() || sys.isEmpty() || dis.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        editText_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                we = editText_weight.getText().toString().trim();
                if (charSequence.length() > 0 && !we.isEmpty()){
                    int hi = Integer.parseInt(charSequence.toString());
                    float bmi = FunctionalUtils.bmi(hi, Float.parseFloat(we));
                    textView.setText(String.valueOf(bmi));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                he = editText_height.getText().toString().trim();
                if (charSequence.length() > 0 && !he.isEmpty()){
                    float we = Float.parseFloat(charSequence.toString());
                    float bmi = FunctionalUtils.bmi(Integer.parseInt(he), we);
                    textView.setText(String.valueOf(bmi));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void save_data() {
        editor.putFloat(WEIGHT, Float.parseFloat(we));
        editor.putInt(HEIGHT, Integer.parseInt(he));
        editor.putInt(SYSTOLIC, Integer.parseInt(sys));
        editor.putInt(DIASTOLIC, Integer.parseInt(dis));
        String bp = sys + "/" + dis;
        editor.putString(BLOOD_PRESSURE, bp);

        float bmi = FunctionalUtils.bmi(Integer.parseInt(he), Float.parseFloat(we));
        editor.putFloat(BMI, bmi);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Tobacco());
        fr.addToBackStack(null);
        fr.commit();

    }

    private void load_data() {
        weight = sharedPreferences.getFloat(WEIGHT, 0);
        height = sharedPreferences.getInt(HEIGHT, 0);
        systolic = sharedPreferences.getInt(SYSTOLIC, 0);
        diastolic = sharedPreferences.getInt(DIASTOLIC, 0);
    }

    private void update_views() {
        FunctionalUtils.checkZeroValueFloat(editText_weight, weight);
        FunctionalUtils.checkZeroValue(editText_height, height);
        FunctionalUtils.checkZeroValue(editText_systolic, systolic);
        FunctionalUtils.checkZeroValue(editText_diastolic, diastolic);

    }

}