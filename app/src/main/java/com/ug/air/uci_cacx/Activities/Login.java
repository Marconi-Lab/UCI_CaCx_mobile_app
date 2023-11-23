package com.ug.air.uci_cacx.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.ug.air.uci_cacx.R;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText edit_email;
    TextInputEditText text_password;
    Button btn_signin;
    ProgressBar progressBar;
    public static final String CREDENTIALS_PREFS ="credentials";
    public static  final String TOKEN ="access_token";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String token, password, email;

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
        startActivity(new Intent(Login.this, Permissions.class));
    }
}