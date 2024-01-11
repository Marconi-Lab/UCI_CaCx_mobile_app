package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Facilities.CODE;
import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.FACILITIES;
import static com.ug.air.uci_cacx.Activities.Login.PERSON;
import static com.ug.air.uci_cacx.Activities.Login.PROVIDERS;
import static com.ug.air.uci_cacx.Activities.Login.SESSION;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;
import static com.ug.air.uci_cacx.Activities.Register.REGISTER_SHARED_PREFS;
import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ug.air.uci_cacx.APIs.ApiClient;
import com.ug.air.uci_cacx.APIs.JsonPlaceHolder;
import com.ug.air.uci_cacx.Activities.FormMenu;
import com.ug.air.uci_cacx.Activities.Login;
import com.ug.air.uci_cacx.Models.Error;
import com.ug.air.uci_cacx.Models.Message;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Nok_2 extends Fragment {

    private SharedPreferences.Editor editor, editor_2;
    private SharedPreferences sharedPreferences, sharedPreferences_2;
    View view;
    LinearLayout linearLayout;
    Button next_btn, back_btn;
    ProgressBar progressBar;
    Spinner spinner_region;
    EditText editText_contact, editText_email;
    AutoCompleteTextView autoCompleteTextView;
    String contact, email, region, district;
    public static  final String NOK_EMAIL ="nok_email";
    public static  final String NOK_REGION ="nok_region";
    public static  final String NOK_DISTRICT ="nok_district";
    public static  final String NOK_CONTACT ="nok_contact_number";
    public static  final String TAG ="UCI_CaCx";
    List<Spinner> spinnerList = new ArrayList<>();
    ArrayList<String> districtList;
    ArrayAdapter<String> singleAdapter;
    ArrayAdapter<CharSequence> adapter1;
    JsonPlaceHolder jsonPlaceHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nok_2, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(REGISTER_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sharedPreferences_2 = requireActivity().getSharedPreferences(CREDENTIALS_PREFS, Context.MODE_PRIVATE);
        editor_2 = sharedPreferences_2.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);
        progressBar = view.findViewById(R.id.login_progress_bar);

        editText_contact = view.findViewById(R.id.contact);
        editText_email = view.findViewById(R.id.email);
        autoCompleteTextView = view.findViewById(R.id.district);

        initializeSpinners();
        setupSpinnerListeners();

        setupAutoComplete();

        load_data();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Nok_1());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editText_email.getText().toString().trim();
                contact = editText_contact.getText().toString().trim();
                district = autoCompleteTextView.getText().toString().trim();

                if (contact.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        editText_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString().toLowerCase();
                if (charSequence.length() > 0 && charSequence.charAt(0) != '0'){
                    next_btn.setEnabled(false);
                    editText_contact.setError("Let the first number be 0");
                }
                else if (charSequence.length() < 10 || charSequence.length() > 10){
                    next_btn.setEnabled(false);
                    editText_contact.setError("The phone number should have 10 numbers");
                }
                else {
                    next_btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void initializeSpinners() {
        spinner_region = view.findViewById(R.id.spinner_region);

        spinnerList.add(spinner_region);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner selectedSpinner = (Spinner) parentView;
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedSpinner == spinnerList.get(0)) {
                    region = selectedItem;
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
                requireActivity(), R.array.region, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.get(0).setAdapter(adapter1);
    }

    private void setupAutoComplete() {
        districtList = new ArrayList<>();
        singleAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, districtList);
        autoCompleteTextView.setAdapter(singleAdapter);
        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString().toLowerCase();

                districtList = FunctionalUtils.get_districts(userInput);
                singleAdapter.clear();
                singleAdapter.addAll(districtList);
                singleAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void save_data(){
        editor.putString(NOK_CONTACT, contact);
        editor.putString(NOK_EMAIL, email);
        editor.putString(NOK_REGION, region);
        editor.putString(NOK_DISTRICT, district);
        editor.apply();

//        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
//        fr.replace(R.id.fragment_container, new Contact_3());
//        fr.addToBackStack(null);
//        fr.commit();
        send_data();
    }

    private void load_data(){
        editText_email.setText(sharedPreferences.getString(NOK_EMAIL, ""));
        editText_contact.setText(sharedPreferences.getString(NOK_CONTACT, ""));
        autoCompleteTextView.setText(sharedPreferences.getString(NOK_DISTRICT, ""));
        setSpinner(0, adapter1, sharedPreferences.getString(NOK_REGION, ""));
    }

    private void setSpinner(int index, ArrayAdapter<CharSequence> adapter, String value){
        if (value.isEmpty()){
            spinnerList.get(index).setSelection(adapter.getPosition("Select one"));
        }
        else {
            spinnerList.get(index).setSelection(adapter.getPosition(value));
        }
    }

    private void send_data(){
        progressBar.setVisibility(View.VISIBLE);
        next_btn.setEnabled(false);

        jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);
        String facility_code = sharedPreferences_2.getString(CODE, "");
        String session_id = sharedPreferences_2.getString(SESSION, "");

        File file = new File(requireActivity().getApplicationInfo().dataDir + "/shared_prefs/register_pref.xml");
        Map<String, RequestBody> map = new HashMap<>();
        map.put("facility_id", toRequestBody(facility_code));
        map.put("session_id", toRequestBody(session_id));

        RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(),filePart);

        Call<Message> call = jsonPlaceHolder.register_patient(fileUpload, map);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                progressBar.setVisibility(View.GONE);
                next_btn.setEnabled(true);
                if (response.isSuccessful()){
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(requireActivity(), FormMenu.class));
                }
                else {
                    try {
                        int statusCode = response.code();
                        if (statusCode == 400 || statusCode == 409 || statusCode == 403 || statusCode == 404){
                            String error = response.errorBody().string();
                            Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == 401){
                            String error = response.errorBody().string();
                            Gson gson = new Gson();
                            Error error1 = gson.fromJson(error, Error.class);
                            String message = error1.getError();
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                            if(message.equals("Not authorized")){
                                Toast.makeText(requireActivity(), "Please first login again", Toast.LENGTH_SHORT).show();
                                editor_2.putString(TOKEN, "");
                                editor_2.putString(PERSON, "");
                                editor_2.putString(PROVIDERS, null);
                                editor_2.putString(FACILITIES, null);
                                editor_2.apply();
                                startActivity(new Intent(requireActivity(), Login.class));
                            }
                        }
                        else if(statusCode == 500){
                            Toast.makeText(requireActivity(), "There was an internal server error", Toast.LENGTH_SHORT).show();

                        }
                        else if(statusCode == 422) {
                            Toast.makeText(requireActivity(), "Header variables missing", Toast.LENGTH_SHORT).show();
                        }
                        else if(statusCode == 413) {
                            Toast.makeText(requireActivity(), "Request Entity Too Large", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e) {
                        Log.e("UCI_CaCx", "onResponse: exception");
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                next_btn.setEnabled(true);
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(requireActivity(), "Something went wrong: " + t.getMessage() , Toast.LENGTH_SHORT).show();
            }

        });

    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }
}