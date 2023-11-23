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

public class Contact_1 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    EditText editText_tribe, editText_language, editText_contact, editText_alternative;
    String tribe, language, contact, alternative;
    public static  final String TRIBE ="tribe";
    public static  final String LANGUAGE ="preferred_language";
    public static  final String CONTACT ="contact";
    public static  final String ALTERNATIVE ="alternative_contact";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_1, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_tribe = view.findViewById(R.id.tribe);
        editText_language = view.findViewById(R.id.language);
        editText_contact = view.findViewById(R.id.contact);
        editText_alternative = view.findViewById(R.id.alternative);

        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Category());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tribe = editText_tribe.getText().toString().trim();
                language = editText_language.getText().toString().trim();
                contact = editText_contact.getText().toString().trim();
                alternative = editText_alternative.getText().toString().trim();

                if (tribe.isEmpty() || language.isEmpty() || contact.isEmpty()){
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
        editor.putString(TRIBE, tribe);
        editor.putString(LANGUAGE, language);
        editor.putString(CONTACT, contact);
        editor.putString(ALTERNATIVE, alternative);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Contact_2());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void update_views() {
        editText_tribe.setText(sharedPreferences.getString(TRIBE, ""));
        editText_language.setText(sharedPreferences.getString(LANGUAGE, ""));
        editText_contact.setText(sharedPreferences.getString(CONTACT, ""));
        editText_alternative.setText(sharedPreferences.getString(ALTERNATIVE, ""));
    }
}