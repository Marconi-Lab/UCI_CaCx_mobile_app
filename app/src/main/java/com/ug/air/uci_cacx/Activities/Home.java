package com.ug.air.uci_cacx.Activities;

import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.FACILITIES;
import static com.ug.air.uci_cacx.Activities.Login.PERSON;
import static com.ug.air.uci_cacx.Activities.Login.PROVIDERS;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Home extends AppCompatActivity {

    CardView screening, incomplete, upload, expert;
    TextView textView;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String inputString, username;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView = findViewById(R.id.username);
        imageView = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        inputString = sharedPreferences.getString(PERSON, "");
        String[] parts = inputString.split(" ");
        if (parts.length > 0) {
            username = parts[0];
            textView.setText("Hello " + username + ",");
        } else {
            textView.setText("Hello " + inputString + ",");
        }

        findViewById(R.id.screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Screening.class));
            }
        });

        findViewById(R.id.incomplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(Home.this, Forms.class);
                bundle.putString("Fragment", "incomplete");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(Home.this, Forms.class);
                bundle.putString("Fragment", "complete");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Home.this, Screening.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(TOKEN, "");
                editor.putString(PERSON, "");
                editor.putString(PROVIDERS, null);
                editor.putString(FACILITIES, null);
                editor.apply();
                startActivity(new Intent(Home.this, Login.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}