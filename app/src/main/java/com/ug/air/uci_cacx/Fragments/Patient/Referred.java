package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Prior_Screening_1.PRIOR;

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

import com.ug.air.uci_cacx.R;

public class Referred extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    EditText editText;
    String referral;
    public static  final String REFERRAL ="referral_from";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_referred, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText = view.findViewById(R.id.referral);

        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prior = sharedPreferences.getString(PRIOR, "");
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (prior.equals("Yes")) {
                    fr.replace(R.id.fragment_container, new Prior_Treatment());
                }
                else {
                    fr.replace(R.id.fragment_container, new Prior_Screening_1());
                }
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referral = editText.getText().toString().trim();

                save_data();

//                if (referral.isEmpty()){
//                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    save_data();
//                }
            }
        });

        return view;
    }

    private void save_data() {
        editor.putString(REFERRAL, referral);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Screening_1());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void update_views() {
        editText.setText(sharedPreferences.getString(REFERRAL, ""));
    }


}