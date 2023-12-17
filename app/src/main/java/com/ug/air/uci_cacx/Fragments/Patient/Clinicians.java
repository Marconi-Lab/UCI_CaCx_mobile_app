package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.PROVIDERS;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.uci_cacx.Models.Facility;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Clinicians extends Fragment {

    SharedPreferences.Editor editor, editor_2;
    SharedPreferences sharedPreferences, sharedPreferences_2;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    String staff, available;
    public static  final String AVAILABLE ="screening staff";
    public static final String DATE = "created_on";
    public static final String COMPLETE = "complete_form";
    public static final String FILENAME = "filename";
    List<String> checkBoxList = new ArrayList<>();
    List<String> staffList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clinicians, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sharedPreferences_2 = requireActivity().getSharedPreferences(CREDENTIALS_PREFS, Context.MODE_PRIVATE);
        editor_2 = sharedPreferences_2.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        linearLayout = view.findViewById(R.id.staff_layout);

        staff = sharedPreferences_2.getString(PROVIDERS, null);
        if (staff != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            stringList = gson.fromJson(staff, type);
            if (stringList == null) {
                stringList = new ArrayList<>();
            }
        }

        for (String staff : stringList){
            CheckBox checkBox = new CheckBox(requireActivity());
            checkBox.setText(staff);
            checkBox.setChecked(false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        checkBoxList.add(staff);
                    } else {
                        checkBoxList.remove(staff);
                    }
                }
            });

            linearLayout.addView(checkBox);
        }

        load_data();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Visit());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                available = FunctionalUtils.convertListToString(checkBoxList);
//                Toast.makeText(requireActivity(), available, Toast.LENGTH_SHORT).show();

                if (available.isEmpty()){
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
        editor.putString(AVAILABLE, available);
        editor.apply();

//        FunctionalUtils.save_file(requireActivity(), true);
//        startActivity(new Intent(requireActivity(), Home.class));

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Results());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        available = sharedPreferences.getString(AVAILABLE, "");
        checkBoxList = null;
        checkBoxList = FunctionalUtils.convertStringToList(available);
        FunctionalUtils.checkBoxes(linearLayout, checkBoxList);
    }

}