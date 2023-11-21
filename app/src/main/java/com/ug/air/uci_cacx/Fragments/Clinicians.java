package com.ug.air.uci_cacx.Fragments;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.Activities.Home;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Clinicians extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    String staff, available;
    public static  final String AVAILABLE ="screening staff";
    public static final String DATE = "created_on";
    public static final String COMPLETE = "complete_form";
    public static final String FILENAME = "filename";
    List<String> checkBoxList = new ArrayList<>();
    List<String> staffList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clinicians, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        linearLayout = view.findViewById(R.id.staff_layout);

        staff = "John Mathews, Musisi Norbels, Senoga Mark";
        staffList.clear();
        staffList = Arrays.asList(staff.split(", "));

        for (String staff : staffList){
            CheckBox checkBox = new CheckBox(requireActivity());
            checkBox.setText(staff);
            checkBox.setChecked(false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        checkBoxList.add(staff);
                    } else {
                        checkBoxList.remove(staff);
                    }
                }
            });

            linearLayout.addView(checkBox);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Visit());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                available = FunctionalUtils.convertListToString(checkBoxList);

                if (available.isEmpty()){
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
        editor.putString(AVAILABLE, available);
        editor.apply();
        FunctionalUtils.save_file(requireActivity(), true);
        startActivity(new Intent(requireActivity(), Home.class));
    }
}