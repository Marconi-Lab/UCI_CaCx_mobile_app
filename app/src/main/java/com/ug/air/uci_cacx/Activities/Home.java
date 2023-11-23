package com.ug.air.uci_cacx.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Home extends AppCompatActivity {

    CardView screening, incomplete, upload, expert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        findViewById(R.id.screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Screening.class));
            }
        });

        findViewById(R.id.incomplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                Intent intent = new Intent(Home.this, Forms.class);
//                bundle.putString("Fragment", "incomplete");
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                Intent intent = new Intent(Home.this, Forms.class);
//                bundle.putString("Fragment", "complete");
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Home.this, Screening.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}