package com.ug.air.uci_cacx.Activities;

import static com.ug.air.uci_cacx.Activities.Facilities.CODE;
import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.ug.air.uci_cacx.R;

public class Splash extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String token, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString(TOKEN, "");
        code = sharedPreferences.getString(CODE, "");
    }

    protected void onResume() {
        super.onResume();
        startSplashTimeout(4000);
    }

    private void startSplashTimeout(int timeout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(Splash.this, Login.class));
                if (token.isEmpty()){
                    startActivity(new Intent(Splash.this, Login.class));
                }
                else if (code.isEmpty()){
                    startActivity(new Intent(Splash.this, Facilities.class));
                }
                else {
                    startActivity(new Intent(Splash.this, Permissions.class));
                }

            }
        }, timeout);
    }
}