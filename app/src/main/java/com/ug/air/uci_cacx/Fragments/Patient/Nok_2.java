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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.List;


public class Nok_2 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    Spinner spinner_region;
    EditText editText_contact, editText_email;
    AutoCompleteTextView autoCompleteTextView;
    String contact, email, region, district;
    public static  final String NOK_EMAIL ="nok_email";
    public static  final String NOK_REGION ="nok_region";
    public static  final String NOK_DISTRICT ="nok_district";
    public static  final String NOK_CONTACT ="nok_contact_number";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayList<String> districtList;
    ArrayAdapter<String> singleAdapter;
    ArrayAdapter<CharSequence> adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nok_2, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_contact = view.findViewById(R.id.contact);
        editText_email = view.findViewById(R.id.email);
        autoCompleteTextView = view.findViewById(R.id.district);

        initializeSpinners();
        setupSpinnerListeners();

        setupAutoComplete();

        load_data();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Nok_1());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editText_email.getText().toString().trim();
                contact = editText_contact.getText().toString().trim();
                district = autoCompleteTextView.getText().toString().trim();

                if (contact.isEmpty() || district.isEmpty() || region.equals("Select one")) {
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
        spinner_region = view.findViewById(R.id.spinner_region);

        spinnerList.add(spinner_region);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
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
                requireActivity(), R.array.region, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);
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

    private void save_data(){
        editor.putString(NOK_CONTACT, contact);
        editor.putString(NOK_EMAIL, email);
        editor.putString(NOK_REGION, region);
        editor.putString(NOK_DISTRICT, district);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Contact_3());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        editText_email.setText(sharedPreferences.getString(NOK_EMAIL, ""));
        editText_contact.setText(sharedPreferences.getString(NOK_CONTACT, ""));
        autoCompleteTextView.setText(sharedPreferences.getString(NOK_DISTRICT, ""));
        setSpinner(0, adapter1, sharedPreferences.getString(NOK_REGION, ""));
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