package com.example.eddie.songs3;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.eddie.songs3.R.layout.activity_splash);


            Intent intent = new Intent(SplashActivity.this, SearchActivityReg.class);
            intent.putExtra("parent_activity", "splash");
            startActivity(intent);

    }
}
