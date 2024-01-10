package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Register.REGISTER_SHARED_PREFS;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class Contact_2 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    Spinner spinner_marital, spinner_education, spinner_sector, spinner_region;
    EditText editText_occupation;
    AutoCompleteTextView autoCompleteTextView;
    String marital, eduction, region, occupation, sector, district;
    public static  final String EDUCATION ="Highest_eduction_level";
    public static  final String MARITAL ="marital_status";
    public static  final String OCCUPATION ="occupation";
    public static  final String REGION ="region";
    public static  final String SECTOR ="employment_sector";
//    public static  final String EMPLOYEE ="employee";
    public static  final String DISTRICT ="district";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayList<String> districtList;
    ArrayAdapter<String> singleAdapter;
    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3, adapter4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_2, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(REGISTER_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

//        editText_employer = view.findViewById(R.id.employer);
        editText_occupation = view.findViewById(R.id.occupation);
        autoCompleteTextView = view.findViewById(R.id.district);

        initializeSpinners();
        setupSpinnerListeners();

        setupAutoComplete();

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Contact_1());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                employer = editText_employer.getText().toString().trim();
                occupation = editText_occupation.getText().toString().trim();
                district = autoCompleteTextView.getText().toString().trim();

                if (district.isEmpty() || region.equals("Select one")){
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
        spinner_education = view.findViewById(R.id.spinner_education);
        spinner_marital = view.findViewById(R.id.spinner_marital);
        spinner_sector = view.findViewById(R.id.spinner_employment);
        spinner_region = view.findViewById(R.id.spinner_region);

        spinnerList.add(spinner_marital);
        spinnerList.add(spinner_education);
        spinnerList.add(spinner_sector);
        spinnerList.add(spinner_region);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    marital = selectedItem;
                }
                else if (selectedSpinner == spinnerList.get(1)) {
                    eduction = selectedItem;
                }
                else if (selectedSpinner == spinnerList.get(2)) {
                    sector = selectedItem;
                }
                else if (selectedSpinner == spinnerList.get(3)) {
                    region = selectedItem;
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
                requireActivity(), R.array.marital, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);

        adapter2 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.education, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(1).setAdapter(adapter2);

        adapter3 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.employment, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(2).setAdapter(adapter3);

        adapter4 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.region, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(3).setAdapter(adapter4);
    }

    private void setupAutoComplete() {
        districtList = new ArrayList<>();
        singleAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, districtList);
        autoCompleteTextView.setAdapter(singleAdapter);
        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString().toLowerCase();

                districtList = FunctionalUtils.get_districts(userInput);
                singleAdapter.clear();
                singleAdapter.addAll(districtList);
                singleAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void save_data() {
        editor.putString(EDUCATION, eduction);
        editor.putString(MARITAL, marital);
        editor.putString(REGION, region);
        editor.putString(DISTRICT, district);
//        editor.putString(EMPLOYEE, employer);
        editor.putString(OCCUPATION, occupation);
        editor.putString(SECTOR, sector);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Nok_1());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        eduction = sharedPreferences.getString(EDUCATION, "");
        marital = sharedPreferences.getString(MARITAL, "");
//        employer = sharedPreferences.getString(EMPLOYEE, "");
        occupation = sharedPreferences.getString(OCCUPATION, "");
        sector = sharedPreferences.getString(SECTOR, "");
        region = sharedPreferences.getString(REGION, "");
        district = sharedPreferences.getString(DISTRICT, "");
    }

    private void update_views() {
        autoCompleteTextView.setText(district);
        editText_occupation.setText(occupation);
//        editText_employer.setText(employer);

        setSpinner(0, adapter1, marital);
        setSpinner(1, adapter2, eduction);
        setSpinner(2, adapter3, sector);
        setSpinner(3, adapter4, region);

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