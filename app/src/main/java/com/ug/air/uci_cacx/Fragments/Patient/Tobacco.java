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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.List;

public class Tobacco extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    Spinner spinner_units;
    EditText editText_duration;
    RadioGroup radioGroup1;
    LinearLayout linearLayout;
    String tobacco, duration, units;
    int time;
    public static  final String TOBACCO ="tobacco_use";
    public static  final String DURATION ="for_how_long_on_tobacco";
    public static  final String UNITS ="units";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tobacco, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup1 = view.findViewById(R.id.radioGroup_1);
        linearLayout = view.findViewById(R.id.nin_layout);
        editText_duration = view.findViewById(R.id.duration);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                tobacco = selectedRadioButton.getText().toString();

                if (tobacco.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    editText_duration.setText("");
                    units = "";
                    setSpinner(0, adapter1, "Select one");
                }
            }
        });

        initializeSpinners();
        setupSpinnerListeners();

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Height());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duration = editText_duration.getText().toString().trim();

                if (tobacco.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (tobacco.equals("Yes") && (duration.isEmpty() || units.isEmpty())){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        return view;
    }

    private void initializeSpinners() {
        spinner_units = view.findViewById(R.id.spinner_units);

        spinnerList.add(spinner_units);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    units = selectedItem;
                    if (units.equals("Select one")){
                        units = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        // Apply the listener to each spinner
        for (Spinner spinner : spinnerList) {
            spinner.setOnItemSelectedListener(spinnerListener);
        }

        adapter1 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.units, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);
    }


    private void save_data() {
        editor.putString(TOBACCO, tobacco);
        if (duration.isEmpty()){
            editor.putInt(DURATION, 0);
        }
        else {
            editor.putInt(DURATION, Integer.parseInt(duration));
        }
        editor.putString(UNITS, units);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Hiv_Status());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        tobacco = sharedPreferences.getString(TOBACCO, "");
        time = sharedPreferences.getInt(DURATION, 0);
        units = sharedPreferences.getString(UNITS, "");
    }

    private void update_views() {

        if (!tobacco.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup1, tobacco);

            if (tobacco.equals("Yes")) {
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.checkZeroValue(editText_duration, time);
                setSpinner(0, adapter1, units);
            }

        }
    }

    private void setSpinner(int index, ArrayAdapter<CharSequence> adapter, String value){
        if (value.isEmpty()){
            spinnerList.get(index).setSelection(adapter.getPosition("Select one"));
        }
        else {
            spinnerList.get(index).setSelection(adapter.getPosition(value));
        }
    }
}