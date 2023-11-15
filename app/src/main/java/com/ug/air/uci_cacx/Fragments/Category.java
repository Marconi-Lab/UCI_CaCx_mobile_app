package com.ug.air.uci_cacx.Fragments;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

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
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Category extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    TextView textView;
    EditText editText;
    String radio_button, nin_number, text;
    public static  final String CATEGORY ="category";
    public static  final String NIN ="category_nin";
    public static  final String NIN_TEXT ="category_nin_title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup);
        linearLayout = view.findViewById(R.id.nin_layout);
        textView = view.findViewById(R.id.nin_name);
        editText = view.findViewById(R.id.nin_number);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                radio_button = selectedRadioButton.getText().toString();

                linearLayout.setVisibility(View.VISIBLE);
                if (radio_button.equals("National")) {
                    textView.setText("National Identification Number");
                }
                else if (radio_button.equals("Refugee")) {
                    textView.setText("Refugee Number");
                }
                else {
                    textView.setText("Passport Number");
                }
            }
        });

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
                nin_number = editText.getText().toString().trim();

                if (nin_number.isEmpty() || radio_button.isEmpty()) {
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
        editor.putString(CATEGORY, radio_button);
        editor.putString(NIN, nin_number);
        editor.putString(NIN_TEXT, textView.getText().toString());
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Contact_1());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        text = sharedPreferences.getString(NIN_TEXT, "");
        radio_button = sharedPreferences.getString(CATEGORY, "");
        nin_number = sharedPreferences.getString(NIN, "");
    }

    private void update_views() {

        if (!radio_button.isEmpty()){
            linearLayout.setVisibility(View.VISIBLE);
            textView.setText(text);
            editText.setText(nin_number);
            FunctionalUtils.setRadioButton(radioGroup, radio_button);
        }
    }


}