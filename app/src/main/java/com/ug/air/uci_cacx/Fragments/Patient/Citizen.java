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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;

public class Citizen extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout, linearLayout2;
    Button next_btn, back_btn;
    String citizen, passport, country, nin_id, nin;
    AutoCompleteTextView autoCompleteTextView;
    EditText editText, editText2;
    TextView nin_type;
    RadioGroup radioGroup, radioGroup2;
    ArrayList<String> countryList;
    ArrayAdapter<String> singleAdapter;
    public static  final String CITIZEN = "citizen_type";
    public static  final String COUNTRY = "country";
    public static  final String PASSPORT = "passport";
    public static  final String NIN_ID = "identification_type";
    public static  final String NIN_NUMBER = "identification_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_citizen, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(REGISTER_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);

        linearLayout = view.findViewById(R.id.nin_layout);
        linearLayout2 = view.findViewById(R.id.real_layout);
        radioGroup = view.findViewById(R.id.radioGroup_1);
        radioGroup2 = view.findViewById(R.id.radioGroup_2);
        autoCompleteTextView = view.findViewById(R.id.country);
        editText = view.findViewById(R.id.passport);
        editText2 = view.findViewById(R.id.nin);
        nin_type = view.findViewById(R.id.nin_type);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                citizen = selectedRadioButton.getText().toString();

                if (citizen.equals("Non Citizen")) {
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.GONE);
                    nin_id = "";
                    editText2.setText("");
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    editText.setText("");
                    autoCompleteTextView.setText("");
                    linearLayout2.setVisibility(View.VISIBLE);
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                nin_id = selectedRadioButton.getText().toString();
                if (nin_id.equals("NIN")){
                    nin_type.setText("Enter NIN");
                }
                else {
                    nin_type.setText("Enter Birth Certificate Number");
                }
            }
        });

        countryList = new ArrayList<>();
        singleAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, countryList);
        autoCompleteTextView.setAdapter(singleAdapter);
        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString().toLowerCase();

                countryList = FunctionalUtils.get_countries(userInput);
                singleAdapter.clear();
                singleAdapter.addAll(countryList);
                singleAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country = autoCompleteTextView.getText().toString().trim();
                passport = editText.getText().toString().trim();
                nin = editText2.getText().toString().trim();

                if (citizen.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (citizen.equals("Non Citizen") && (country.isEmpty())){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (citizen.equals("Citizen") && (nin_id.isEmpty())){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        load_data();
        update_views();

        return view;
    }

    private void save_data() {
        editor.putString(CITIZEN, citizen);
        editor.putString(COUNTRY, country);
        editor.putString(PASSPORT, passport);
        editor.putString(NIN_ID, nin_id);
        editor.putString(NIN_NUMBER, nin);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Identification());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        citizen = sharedPreferences.getString(CITIZEN, "");
        country = sharedPreferences.getString(COUNTRY, "");
        passport = sharedPreferences.getString(PASSPORT, "");
        nin = sharedPreferences.getString(NIN_NUMBER, "");
        nin_id = sharedPreferences.getString(NIN_ID, "");
    }

    private void update_views() {

        if (!citizen.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, citizen);
            if (citizen.equals("Non Citizen")){
                linearLayout.setVisibility(View.VISIBLE);
                autoCompleteTextView.setText(country);
                editText.setText(passport);
            }
            else {
                linearLayout2.setVisibility(View.VISIBLE);
                if (!nin_id.isEmpty()){
                    FunctionalUtils.setRadioButton(radioGroup2, nin_id);
                    editText2.setText(nin);
                    if (nin_id.equals("NIN")){
                        nin_type.setText("Enter NIN");
                    }
                    else {
                        nin_type.setText("Enter Birth Certificate Number");
                    }
                }
            }
        }

    }
}