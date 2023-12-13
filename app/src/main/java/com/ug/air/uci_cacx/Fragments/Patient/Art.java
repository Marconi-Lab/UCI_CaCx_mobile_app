package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Art extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    Spinner spinner_regime;
    Button select_btn;
    TextView textView_years;
    String art, years, regimen;
    int year;
    public static  final String ART ="on_ART";
    public static  final String LONG ="ART_initial_date";
    public static  final String REGIMEN ="art_regimen";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_art, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup_1);
        textView_years = view.findViewById(R.id.when);
        linearLayout = view.findViewById(R.id.nin_layout);
        select_btn = view.findViewById(R.id.select);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                art = selectedRadioButton.getText().toString();

                linearLayout.setVisibility(View.VISIBLE);
                if (art.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    regimen = "";
                    setSpinner(0, adapter1, "Select one");
                    textView_years.setText("");
                }
            }
        });

        initializeSpinners();
        setupSpinnerListeners();

        load_data();
        update_views();

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Hiv_Status());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                years = textView_years.getText().toString().trim();

                if (art.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(art.equals("Yes") && (regimen.isEmpty() || years.isEmpty())){
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
        spinner_regime = view.findViewById(R.id.spinner_regime);

        spinnerList.add(spinner_regime);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    regimen = selectedItem;

                    if (regimen.equals("Select one")){
                        regimen = "";
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
                requireActivity(), R.array.regimen, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);
    }

    private void save_data() {
        editor.putString(ART, art);
        editor.putString(REGIMEN, regimen);
        editor.putString(LONG, years);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Parity());
        fr.addToBackStack(null);
        fr.commit();
    }

    public void load_data(){
        art = sharedPreferences.getString(ART, "");
        regimen = sharedPreferences.getString(REGIMEN, "");
        years = sharedPreferences.getString(LONG, "");
//        Toast.makeText(requireActivity(), "Initital: " + years, Toast.LENGTH_SHORT).show();
    }

    private void update_views(){
        if (!art.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, art);

            if (art.equals("Yes")){
                linearLayout.setVisibility(View.VISIBLE);
                textView_years.setText(years);
                setSpinner(0, adapter1, regimen);
            }
        }
    }

    private void open_dialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textView_years.setText(day + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dialog.show();
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