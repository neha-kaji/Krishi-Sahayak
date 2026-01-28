package com.example.krishi_sahayak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Optional: check if user already logged in
        FirebaseAuth auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            if (auth.getCurrentUser() != null) {
                // User already signed in → go directly to dashboard
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Not signed in → open login screen
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
