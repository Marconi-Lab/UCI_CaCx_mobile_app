package com.ug.air.uci_cacx.Activities;

import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.SESSION;

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

//        patientsList.clear();
//        Patient patient1 = new Patient("310aa40e-9018-4202-8042-a74031cb4a8d", "john johns", "256 771234212", 34);
//        patientsList.add(patient1);
//        Patient patient2 = new Patient("9602f177-e29a-45e6-90c1-6a777fa0bea7", "mathew johns", "None", 23);
//        patientsList.add(patient2);
//        relativeLayout.setVisibility(View.VISIBLE);

        patientAdapter = new PatientAdapter(this, patientsList);
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

        Map<String, RequestBody> map = new HashMap<>();
        map.put("session_id", toRequestBody(session_id));
        String[] parts = search_name.split(" ");

        if (parts.length > 1) {
            first_name = parts[0];
            last_name = parts[1];
        }
        else {
            first_name = parts[0];
            last_name = "";
        }

        Call<Result> call = jsonPlaceHolder.patients(session_id, first_name, last_name);
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
                    }
                    else {
                        textView_empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        patientAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    int code = response.code();
                    if (code == 401 ||  code == 402 || code == 403 || code == 404 || code == 400 || code == 409){
                        try {
                            String error = response.errorBody().string();
//                            Gson gson = new Gson();
//                            Error error1 = gson.fromJson(error, Error.class);
//                            String message = error1.getError();
                            Toast.makeText(FormMenu.this, error, Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException e) {
                            Log.e("UCI_CaCx", "onResponse: exception");
                        }
                    }
                    else if(code == 500) {
                        Toast.makeText(FormMenu.this, "There was an internal server error", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(FormMenu.this, "Error code: " + code, Toast.LENGTH_SHORT).show();
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