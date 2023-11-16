package com.ug.air.uci_cacx.Fragments;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ug.air.uci_cacx.R;

import java.util.ArrayList;
import java.util.List;


public class Contraceptives extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    LinearLayout linearLayout, linearLayout_2;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    EditText editText;
    String contra, other;
    public static  final String CONTRA ="contraceptives_used";
    public static  final String OTHER_CONTRA ="other_contraceptives_used";
    List<String> checkBoxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contraceptives, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        linearLayout = view.findViewById(R.id.nin_layout);
        linearLayout_2 = view.findViewById(R.id.check_layout);
        editText = view.findViewById(R.id.other);

        for (int i = 0; i < linearLayout_2.getChildCount(); i++) {
            if (linearLayout_2.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout_2.getChildAt(i);
                String value = checkBox.getText().toString();
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked) {

                        }
                    }
                });
            }
        }

        return view;
    }
}