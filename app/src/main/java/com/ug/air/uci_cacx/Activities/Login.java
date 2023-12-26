package com.ug.air.uci_cacx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.uci_cacx.APIs.ApiClient;
import com.ug.air.uci_cacx.APIs.JsonPlaceHolder;
import com.ug.air.uci_cacx.Models.Check;
import com.ug.air.uci_cacx.Models.Facility;
import com.ug.air.uci_cacx.Models.Message;
import com.ug.air.uci_cacx.Models.User;
import com.ug.air.uci_cacx.R;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText edit_email;
    TextInputEditText text_password;
    Button btn_signin;
    ProgressBar progressBar;
    public static final String CREDENTIALS_PREFS ="credentials";
    public static  final String TOKEN ="access_token";
    public static  final String SESSION ="session_id";
    public static  final String PERSON ="display_name";
    public static  final String PROVIDERS ="providers";
    public static  final String FACILITIES ="facilities";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String token, password, email, person, session_id;
    JsonPlaceHolder jsonPlaceHolder;
    List<Facility> facilityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edit_email = findViewById(R.id.email);
        text_password = findViewById(R.id.password);
        btn_signin = findViewById(R.id.btnsign);
        progressBar = findViewById(R.id.login_progress_bar);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edit_email.getText().toString().trim();
                password = text_password.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Please provide fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    btn_signin.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    send_data_to_server();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void send_data_to_server() {
        jsonPlaceHolder = ApiClient.getClient_1(email, password).create(JsonPlaceHolder.class);
        Call<User> call = jsonPlaceHolder.loginUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()){

                    boolean auth = response.body().isAuthenticated();
                    if (auth){
                        token = response.body().getProvider().getUuid();
                        person = response.body().getAnother_user().getPerson().getDisplay();
                        session_id = response.body().getSession_id();
                        editor.putString(TOKEN, token);
                        editor.putString(PERSON, person);
                        editor.putString(SESSION, session_id);
                        editor.apply();
                        check_for_provider();
                    }
                    else {
                        btn_signin.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Please provide the right credentials", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    btn_signin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Please provide the right credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                btn_signin.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
//                dialog.dismiss();
//                Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void check_for_provider() {
        jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);
        Check check = new Check(token);
        Call<Message> call = jsonPlaceHolder.check_for_provider(check);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    String message = response.body().getMessage();
//                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    get_providers();
                }
                else {
                    btn_signin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    int code = response.code();
                    Toast.makeText(Login.this, code + ": Couldn't check if the provider exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                btn_signin.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Couldn't check if the provider exists", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void get_providers() {
        jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);
        Call<List<String>> call = jsonPlaceHolder.getProviders();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()){
                    List<String> stringList = response.body();
                    if(stringList != null){

                        if (stringList.contains(person) || stringList.contains("Super User")){
                            stringList.remove(person);
                        }

                        Gson gson = new Gson();
                        String json2 = gson.toJson(stringList);
                        editor.putString(PROVIDERS, json2);
                        editor.apply();
//                        String providers = sharedPreferences.getString(PROVIDERS, null);
                        getFacilities();
                    }
                    else {
                        btn_signin.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "There are no providers in the system", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    btn_signin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    int code = response.code();
                    Toast.makeText(Login.this, code + ": Couldn't access the list of providers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                btn_signin.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Couldn't access the list of providers", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getFacilities() {
        jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);
        Call<List<Facility>> call = jsonPlaceHolder.getFacilities();
        call.enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.isSuccessful()){
                    btn_signin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    facilityList = response.body();
                    Log.d("UCI_CaCx", "onResponse: "+ facilityList);
                    if(facilityList != null){
                        Gson gson = new Gson();
                        String json2 = gson.toJson(facilityList);
                        editor.putString(FACILITIES, json2);
                        editor.apply();
                        String facilities = sharedPreferences.getString(FACILITIES, null);
//                        Log.d("UCI_CaCx", "Facilities: " + facilities);
//                        Log.d("UCI_CaCx", "onResponse: saved list");
                    }
                    startActivity(new Intent(Login.this, Facilities.class));
                }
                else {
                    btn_signin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    int code = response.code();
                    Toast.makeText(Login.this, code + ": Couldn't access the facilities list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                btn_signin.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Couldn't access the facilities list", Toast.LENGTH_SHORT).show();

            }
        });
    }
}