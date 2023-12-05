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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;

public class Residence extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Spinner spinner;
    Button next_btn, back_btn;
    EditText editText_district, editText_sub_county, editText_parish, editText_village;
    String district, sub_county, village, region;
    public static  final String DISTRICT ="district_residence";
    public static  final String REGION ="region_residence";
    public static  final String SUB_COUNTY ="sub-county_residence";
    public static  final String VILLAGE ="village_residence";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_residence, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_district = view.findViewById(R.id.district);
        editText_village = view.findViewById(R.id.village);
        editText_sub_county = view.findViewById(R.id.sub_county);
        spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireActivity(), R.array.region, android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

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
                district = editText_district.getText().toString().trim();
                sub_county = editText_sub_county.getText().toString().trim();
//                parish = editText_parish.getText().toString().trim();
                village = editText_village.getText().toString().trim();
                region = spinner.getSelectedItem().toString();

                if (district.isEmpty() || sub_county.isEmpty() || region.isEmpty()  || village.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (region.equals("Select one")){
                    Toast.makeText(requireActivity(), "Please select the region", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        return view;
    }

    private Spinner.OnItemSelectedListener spinnerListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, android.view.View view, int position, long l) {
            region = spinner.getSelectedItem().toString();
            if (region.equals("Select on")){
                region = "";
            }
            Toast.makeText(requireActivity(), region, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {
            // Nothing to do here
        }
    };

    private void save_data() {
        editor.putString(DISTRICT, district);
        editor.putString(SUB_COUNTY, sub_county);
        editor.putString(REGION, region);
        editor.putString(VILLAGE, village);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Origin());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void update_views() {
        editText_district.setText(sharedPreferences.getString(DISTRICT, ""));
        editText_sub_county.setText(sharedPreferences.getString(SUB_COUNTY, ""));
        editText_village.setText(sharedPreferences.getString(VILLAGE, ""));


    }
}