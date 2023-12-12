package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_Screening_1.REASON_VISIT;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_screening_3.RESULT_1;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.List;

public class Screening_01 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    Spinner spinner_screen;
    String screened, why_not;
    public static  final String SCREENED ="screening_done";
    public static  final String WHY_NOT ="why_not_screened";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_screening_01, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        linearLayout = view.findViewById(R.id.nin_layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                screened = selectedRadioButton.getText().toString();

                if (screened.equals("No")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
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
//                using = editText_using.getText().toString().trim();

                if (screened.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (screened.equals("No") && (why_not.isEmpty())){
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
        spinner_screen = view.findViewById(R.id.spinner_screen);

        spinnerList.add(spinner_screen);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    why_not = selectedItem;
                    if (why_not.equals("Select one")){
                        why_not = "";
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
                requireActivity(), R.array.screened, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);
    }

    private void save_data() {
        editor.putString(SCREENED, screened);
        editor.putString(WHY_NOT, why_not);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (screened.equals("Yes")){
            fr.replace(R.id.fragment_container, new Screening_1());
        }
        else {
            fr.replace(R.id.fragment_container, new Visit());
        }
        fr.addToBackStack(null);
        fr.commit();

    }

    private void load_data(){
        screened = sharedPreferences.getString(SCREENED, "");
        why_not = sharedPreferences.getString(WHY_NOT, "");
    }

    private void update_views(){

        if (!screened.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, screened);

            if (screened.equals("No")){
                linearLayout.setVisibility(View.VISIBLE);
                setSpinner(0, adapter1, why_not);
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