package com.ug.air.uci_cacx.Fragments.Forms;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ug.air.uci_cacx.Activities.Screening;
import com.ug.air.uci_cacx.Adapters.FormAdapter;
import com.ug.air.uci_cacx.Models.Form;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.util.ArrayList;
import java.util.List;

public class Incomplete extends Fragment {

    View view;
    List<Form> formList = new ArrayList<>();
    FormAdapter formAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_incomplete, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        List<String> sharedPreferenceFilenames = FunctionalUtils.getSharedPreferencesFileNames(requireActivity(), false);
        formList = FunctionalUtils.getDataFromSharedPreferences(requireActivity(), sharedPreferenceFilenames);

        formAdapter = new FormAdapter(requireActivity(), formList);
        recyclerView.setAdapter(formAdapter);

        formAdapter.setOnItemClickListener(new FormAdapter.OnItemClickListener() {
            @Override
            public void onShowClick(int position) {
                Form form = formList.get(position);
                String filename = form.getFilename();

                FunctionalUtils.moveSharedPreferences(requireActivity(), filename, "shared_pref");
                Intent intent = new Intent(requireActivity(), Screening.class);
                startActivity(intent);


            }

            @Override
            public void onUploadClick(int position) {

            }
        });

        return view;
    }
}