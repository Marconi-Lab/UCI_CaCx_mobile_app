package com.ug.air.uci_cacx.Activities;

import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.FACILITIES;
import static com.ug.air.uci_cacx.Activities.Login.PERSON;
import static com.ug.air.uci_cacx.Activities.Login.PROVIDERS;
import static com.ug.air.uci_cacx.Activities.Login.SESSION;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ug.air.uci_cacx.APIs.ApiClient;
import com.ug.air.uci_cacx.APIs.JsonPlaceHolder;
import com.ug.air.uci_cacx.Adapters.PatientAdapter;
import com.ug.air.uci_cacx.Models.Error;
import com.ug.air.uci_cacx.Models.Patient;
import com.ug.air.uci_cacx.Models.Result;
import com.ug.air.uci_cacx.Models.Search;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormMenu extends AppCompatActivity {

    RelativeLayout relativeLayout;
    Button btn_register, btn_screen;
    EditText editText_search;
    CardView cardView_search;
    RecyclerView recyclerView;
    TextView textView_empty;
    String search_name, first_name, last_name, patient_id, patient_name;
    SharedPreferences.Editor editor, editor_2;
    SharedPreferences sharedPreferences, sharedPreferences_2;
    JsonPlaceHolder jsonPlaceHolder;
    ProgressDialog progressDialog;
    public static  final String TAG ="UCI_CaCx";
    List<Patient> patientsList = new ArrayList<>();
    PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);

        sharedPreferences_2 = getSharedPreferences(CREDENTIALS_PREFS, Context.MODE_PRIVATE);
        editor_2 = sharedPreferences_2.edit();
        jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);

        btn_screen= findViewById(R.id.screen);
        btn_register = findViewById(R.id.register);

        recyclerView = findViewById(R.id.recyclerView);
        editText_search = findViewById(R.id.email);
        textView_empty = findViewById(R.id.patient);
        cardView_search = findViewById(R.id.card);
        relativeLayout = findViewById(R.id.search_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FormMenu.this, Register.class));
            }
        });

        btn_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

        cardView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_name = editText_search.getText().toString().trim();

                if (search_name.isEmpty()){
                    Toast.makeText(FormMenu.this, "Please provide the patient name", Toast.LENGTH_SHORT).show();
                }
                else {
                    search_server();
                }
            }
        });

    }

    private void find_user() {
        if (doesSharedPreferencesFileExist(this, patient_id)) {
            System.out.println("SharedPreferences file exists!");
            FunctionalUtils.moveSharedPreferences(this, patient_id, "shared_pref");
            Intent intent = new Intent(this, Screening.class);
            startActivity(intent);

        } else {
            System.out.println("SharedPreferences file does not exist.");
            Bundle bundle = new Bundle();
            Intent intent = new Intent(FormMenu.this, Screening.class);
            bundle.putString("Patient", patient_id);
            bundle.putString("Name", patient_name);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public static boolean doesSharedPreferencesFileExist(Context context, String fileName) {
        File sharedPrefsFile = new File(context.getFilesDir().getParent() + "/shared_prefs/" + fileName + ".xml");
        return sharedPrefsFile.exists();
    }

    private void search_server() {
        progressDialog = ProgressDialog.show(this, "Searching for Patient", "Please wait...", true);

        String session_id = sharedPreferences_2.getString(SESSION, "");

        String[] parts = search_name.split(" ");

        if (parts.length > 1) {
            first_name = parts[0];
            last_name = parts[1];
        }
        else {
            first_name = parts[0];
            last_name = "";
        }

        Search search = new Search(first_name, last_name, session_id);
        Call<Result> call = jsonPlaceHolder.patients(search);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    patientsList.clear();
                    patientsList = response.body().getPatient();
                    if (patientsList == null){
                        textView_empty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        System.out.println("empty");
                    }
                    else {
                        System.out.println("not empty");
                        System.out.println(patientsList);
                        textView_empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        patientAdapter = new PatientAdapter(FormMenu.this, patientsList);
                        recyclerView.setAdapter(patientAdapter);

                        patientAdapter.setOnItemClickListener(new PatientAdapter.OnItemClickListener() {
                            @Override
                            public void onScreenClick(int position) {
                                Patient patient = patientsList.get(position);
                                patient_id = patient.getPatient_id();
                                patient_name = patient.getPatient_name();
                                find_user();

                            }
                        });
                    }
                }
                else {
                    try {
                        int statusCode = response.code();
                        if (statusCode == 400 || statusCode == 409 || statusCode == 403 || statusCode == 404){
                            String error = response.errorBody().string();
                            progressDialog.dismiss();
                            Toast.makeText(FormMenu.this, error, Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == 401){
                            String error = response.errorBody().string();
                            progressDialog.dismiss();
                            Gson gson = new Gson();
                            Error error1 = gson.fromJson(error, Error.class);
                            String message = error1.getError();
                            Toast.makeText(FormMenu.this, message, Toast.LENGTH_SHORT).show();
                            if(message.equals("Not authorized")){
                                Toast.makeText(FormMenu.this, "Please first login again", Toast.LENGTH_SHORT).show();
                                editor_2.putString(TOKEN, "");
                                editor_2.putString(PERSON, "");
                                editor_2.putString(PROVIDERS, null);
                                editor_2.putString(FACILITIES, null);
                                editor_2.apply();
                                startActivity(new Intent(FormMenu.this, Login.class));
                            }
                        }
                        else if(statusCode == 500){
                            progressDialog.dismiss();
                            Toast.makeText(FormMenu.this, "There was an internal server error", Toast.LENGTH_SHORT).show();

                        }
                        else if(statusCode == 422) {
                            progressDialog.dismiss();
                            Toast.makeText(FormMenu.this, "Header variables missing", Toast.LENGTH_SHORT).show();
                        }
                        else if(statusCode == 413) {
                            progressDialog.dismiss();
                            Toast.makeText(FormMenu.this, "Request Entity Too Large", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e) {
                        Log.e("UCI_CaCx", "onResponse: exception");
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(FormMenu.this, "Something went wrong: " + t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FormMenu.this, Home.class));
        finish();
    }

}