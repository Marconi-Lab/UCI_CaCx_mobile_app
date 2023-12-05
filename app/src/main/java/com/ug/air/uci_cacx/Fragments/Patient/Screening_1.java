package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_1.IMAGE_NAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_1.IMAGE_PATH;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_2.IMAGE_NAME_2;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_2.IMAGE_PATH_2;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_3.IMAGE_NAME_3;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_3.IMAGE_PATH_3;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_4.IMAGE_NAME_4;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_4.IMAGE_PATH_4;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_Screening_1.REASON_VISIT;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_screening_3.RESULT_1;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.HPV;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.PAP;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_COL;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_VIA;

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

public class Screening_1 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    String method, other, old_method;
    public static  final String SCREEN_METHOD ="screening_method";
    public static  final String OTHER_SCREENING_METHOD ="other_screening_method";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_screening_1, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                method = selectedRadioButton.getText().toString();

            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reason = sharedPreferences.getString(REASON_VISIT, "");
                String result = sharedPreferences.getString(RESULT_1, "");

                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (reason.equals("Other") || reason.equals("Initial Screening")){
                    fr.replace(R.id.fragment_container, new Prior_Screening_1());
                }
                else if (result.equals("Positive")){
                    fr.replace(R.id.fragment_container, new Prior_Treatment());
                }
                else {
                    fr.replace(R.id.fragment_container, new Prior_screening_3());
                }
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (method.isEmpty()) {
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
        editor.putString(SCREEN_METHOD, method);
        editor.putString(OTHER_SCREENING_METHOD, other);
        if (!method.equals(old_method)) {
            editor.putString(RESULT_VIA, "");
            editor.putString(RESULT_COL, "");
            editor.putString(PAP, "");
            editor.putString(HPV, "");
        }
            editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (method.equals("VIA")){
            fr.replace(R.id.fragment_container, new Photo_1());
        }
        else {
            editor.putString(IMAGE_NAME, "");
            editor.putString(IMAGE_PATH, "");
            editor.putString(IMAGE_NAME_2, "");
            editor.putString(IMAGE_PATH_2, "");
            editor.putString(IMAGE_NAME_3, "");
            editor.putString(IMAGE_PATH_3, "");
            editor.putString(IMAGE_NAME_4, "");
            editor.putString(IMAGE_PATH_4, "");
            editor.apply();
            fr.replace(R.id.fragment_container, new Screening_2());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        old_method = sharedPreferences.getString(SCREEN_METHOD, "");
    }

    private void update_views() {
        if (!old_method.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, old_method);
        }
    }

}