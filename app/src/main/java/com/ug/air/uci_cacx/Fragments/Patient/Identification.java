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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Identification extends Fragment {

    //    public static  final String TOKEN ="access_token";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    EditText editText_screening_number, editText_first_name, editText_last_name, editText_age;
    String screening, first, last, ax;
    int age;
    public static  final String SCREENING_NUMBER ="screening_number";
    public static  final String FIRST_NAME ="first_name";
    public static  final String LAST_NAME ="last_name";
    public static  final String AGE ="age";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_identification, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);

        editText_screening_number = view.findViewById(R.id.screening_number);
        editText_first_name = view.findViewById(R.id.first_name);
        editText_last_name = view.findViewById(R.id.last_name);
        editText_age = view.findViewById(R.id.age);
        editText_age = view.findViewById(R.id.age);
        editText_age = view.findViewById(R.id.age);

        load_data();
        update_views();

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screening = editText_screening_number.getText().toString().trim();
                first = editText_first_name.getText().toString().trim();
                last = editText_last_name.getText().toString().trim();
                ax = editText_age.getText().toString().trim();

                if (screening.isEmpty() || first.isEmpty() || last.isEmpty() || ax.isEmpty()){
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
        editor.putString(SCREENING_NUMBER, screening);
        editor.putString(FIRST_NAME, first);
        editor.putString(LAST_NAME, last);
        editor.putInt(AGE, Integer.parseInt(ax));
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Height());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        screening = sharedPreferences.getString(SCREENING_NUMBER, "");
        first = sharedPreferences.getString(FIRST_NAME, "");
        last = sharedPreferences.getString(LAST_NAME, "");
        age = sharedPreferences.getInt(AGE, 0);
    }

    private void update_views() {
        editText_screening_number.setText(screening);
        editText_first_name.setText(first);
        editText_last_name.setText(last);

        FunctionalUtils.checkZeroValue(editText_age, age);
//        editText_age.setText(String.valueOf(age));
    }
}