package com.example.hiwApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FirstActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(i);
            }
        },2000);
    }

}