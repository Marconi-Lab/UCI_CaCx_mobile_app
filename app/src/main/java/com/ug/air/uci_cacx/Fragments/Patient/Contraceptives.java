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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


public class Contraceptives extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    Spinner spinner_contra;
    EditText editText_using;
    String contra, method, using;
    public static  final String CONTRA ="contraceptives_used";
    public static  final String MONTHS ="duration_on_contraceptives";
    public static  final String CONTRA_METHOD ="contraceptives_method";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contraceptives, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        linearLayout = view.findViewById(R.id.check_layout);
        editText_using = view.findViewById(R.id.using);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                contra = selectedRadioButton.getText().toString();

                if (contra.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    method = "";
                    setSpinner(0, adapter1, "Select one");
                    editText_using.setText("");
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
                fr.replace(R.id.fragment_container, new Parity());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                using = editText_using.getText().toString().trim();

                if (contra.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (contra.equals("Yes") && (method.isEmpty() || using.isEmpty())){
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
        spinner_contra = view.findViewById(R.id.spinner_contra);

        spinnerList.add(spinner_contra);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    method = selectedItem;
                    if (method.equals("Select one")){
                        method = "";
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
                requireActivity(), R.array.contraceptives, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);
    }

    private void save_data() {
        editor.putString(CONTRA, contra);
        editor.putString(CONTRA_METHOD, method);
        editor.putString(MONTHS, using);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Symptoms());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        contra = sharedPreferences.getString(CONTRA, "");
        method = sharedPreferences.getString(CONTRA_METHOD, "");
        using = sharedPreferences.getString(MONTHS, "");
    }

    private void update_views(){

        if (!contra.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, contra);

            if (contra.equals("Yes")){
                linearLayout.setVisibility(View.VISIBLE);
                editText_using.setText(using);
                setSpinner(0, adapter1, method);

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