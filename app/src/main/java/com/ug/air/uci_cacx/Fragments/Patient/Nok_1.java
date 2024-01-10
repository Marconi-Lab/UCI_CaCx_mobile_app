package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Register.REGISTER_SHARED_PREFS;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;

import java.util.ArrayList;
import java.util.List;

public class Nok_1 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    Spinner spinner_contact, spinner_id, spinner_relationship;
    EditText editText_name, editText_nin;
    TextView textView;
    String contact, nin_id, nin, relationship, name;
    public static  final String NAME ="nok_full_name";
    public static  final String RELATIONSHIP ="nok_relationship";
    public static  final String CONTACT_TYPE ="nok_contact_type";
    public static  final String NOK_ID ="nok_identification_type";
    public static  final String NOK_ID_NUMBER ="nok_identification_number";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nok_1, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(REGISTER_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_name = view.findViewById(R.id.name);
        editText_nin = view.findViewById(R.id.nin);
        textView = view.findViewById(R.id.nin_type);
        linearLayout = view.findViewById(R.id.nin_layout);

        initializeSpinners();
        setupSpinnerListeners();

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Contact_2());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText_name.getText().toString().trim();
                nin = editText_nin.getText().toString().trim();

                if (contact.equals("Select one") || name.isEmpty() || relationship.equals("Select one")){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (!nin_id.isEmpty() && nin.isEmpty()){
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
        spinner_contact = view.findViewById(R.id.spinner_person);
        spinner_id = view.findViewById(R.id.spinner_type);
        spinner_relationship = view.findViewById(R.id.spinner_relationship);

        spinnerList.add(spinner_contact);
        spinnerList.add(spinner_id);
        spinnerList.add(spinner_relationship);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    contact = selectedItem;
                }
                else if (selectedSpinner == spinnerList.get(1)) {
                    nin_id = selectedItem;
                    if (nin_id.equals("NIN")){
                        textView.setText("Enter Identification Number");
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    else if (nin_id.equals("Passport")){
                        textView.setText("Enter Passport Number");
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        linearLayout.setVisibility(View.GONE);
                        editText_nin.setText("");
                        nin_id = "";
                    }
                }
                else if (selectedSpinner == spinnerList.get(2)) {
                    relationship = selectedItem;
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
                requireActivity(), R.array.nok, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);

        adapter2 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.id_type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(1).setAdapter(adapter2);

        adapter3 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.relationship, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(2).setAdapter(adapter3);

    }

    private void save_data(){
        editor.putString(CONTACT_TYPE, contact);
        editor.putString(NOK_ID, nin_id);
        editor.putString(NOK_ID_NUMBER, nin);
        editor.putString(NAME, name);
        editor.putString(RELATIONSHIP, relationship);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Nok_2());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        relationship = sharedPreferences.getString(RELATIONSHIP, "");
        contact = sharedPreferences.getString(CONTACT_TYPE, "");
        nin_id = sharedPreferences.getString(NOK_ID, "");
        nin = sharedPreferences.getString(NOK_ID_NUMBER, "");
        name = sharedPreferences.getString(NAME, "");
    }

    private void update_views() {
        editText_name.setText(name);
        editText_nin.setText(nin);

        spinnerList.get(0).setSelection(adapter1.getPosition(contact));
        if (nin_id.equals("NIN")){
            spinnerList.get(1).setSelection(adapter2.getPosition(nin_id));
            linearLayout.setVisibility(View.VISIBLE);
            textView.setText("Enter Identification Number");
        }
        else if (nin_id.equals("Passport")){
            spinnerList.get(1).setSelection(adapter2.getPosition(nin_id));
            linearLayout.setVisibility(View.VISIBLE);
            textView.setText("Enter Passport Number");
        }
        else {
            spinnerList.get(1).setSelection(adapter2.getPosition("Select one"));
            linearLayout.setVisibility(View.GONE);
        }

        spinnerList.get(2).setSelection(adapter3.getPosition(relationship));

    }

}