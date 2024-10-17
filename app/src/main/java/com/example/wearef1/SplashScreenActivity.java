package com.example.wearef1;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;

public class SplashScreenActivity extends AppCompatActivity {

    final private int SPLASH_SCREEN_TIMEOUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_splash_screen);

        FirebaseApp.initializeApp(this);

        ImageView logo = findViewById(R.id.f1_logo);

        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(logo, "scaleX", 0.6f, 1.6f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(logo, "scaleY", 0.6f, 1.6f);
        scaleXAnim.setDuration(SPLASH_SCREEN_TIMEOUT);
        scaleYAnim.setDuration(SPLASH_SCREEN_TIMEOUT);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnim, scaleYAnim);
        animatorSet.start();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIMEOUT);
    }
}