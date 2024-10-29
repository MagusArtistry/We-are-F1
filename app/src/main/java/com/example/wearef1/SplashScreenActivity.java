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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("drivers");
//
//        String[][] drivers = {
//                {"Max Verstappen", "Oracle Red Bull Racing", "Netherlands", "110", "2948.5", "205", "3", "30/09/1997", "Dutch", "verstappen", "2024"},
//                {"Lando Norris", "McLaren Formula 1 Team", "United Kingdom", "25", "948", "124", "0", "13/11/1999", "British", "norris", "2024"},
//                {"Charles Leclerc", "Scuderia Ferrari", "Monaco", "41", "1365", "145", "0", "16/10/1997", "Monegasque", "leclerc", "2024"},
//                {"Oscar Piastri", "McLaren Formula 1 Team", "Australia", "9", "348", "42", "0", "06/04/2001", "Australian", "piastri", "2024"},
//                {"Carlos Sainz", "Scuderia Ferrari", "Spain", "25", "1222.5", "204", "0", "01/09/1994", "Spanish", "sainz", "2024"},
//                {"Lewis Hamilton", "Mercedes-AMG PETRONAS F1 Team", "United Kingdom", "201", "4828.5", "352", "7", "07/01/1985", "British", "hamilton", "2024"},
//                {"George Russell", "Mercedes-AMG PETRONAS F1 Team", "United Kingdom", "14", "646", "124", "0", "15/02/1998", "British", "russell", "2024"},
//                {"Sergio Perez", "Oracle Red Bull Racing", "Mexico", "39", "1636", "278", "0", "26/01/1990", "Mexican", "perez", "2024"},
//                {"Fernando Alonso", "Aston Martin Aramco F1 Team", "Spain", "106", "2329", "400", "2", "29/07/1981", "Spanish", "alonso", "2024"},
//                {"Nico Hulkenberg", "MoneyGram Haas F1 Team", "Germany", "0", "561", "226", "0", "19/08/1987", "German", "hulkenberg", "2024"},
//                {"Lance Stroll", "Aston Martin Aramco F1 Team", "Canada", "3", "292", "163", "0", "29/10/1998", "Canadian", "stroll", "2024"},
//                {"Yuki Tsunoda", "Visa Cash App RB Formula One Team", "Japan", "0", "83", "86", "0", "11/05/2000", "Japanese", "tsunoda", "2024"},
//                {"Kevin Magnussen", "MoneyGram Haas F1 Team", "Denmark", "1", "200", "183", "0", "05/10/1992", "Danish", "magnussen", "2024"},
//                {"Alexander Albon", "Williams Racing", "Thailand", "2", "240", "101", "0", "23/03/1996", "Thai", "albon", "2024"},
//                {"Daniel Ricciardo", "Visa Cash App RB Formula One Team", "Australia", "32", "1329", "258", "0", "01/07/1989", "Australia", "ricciardo", "2024"},
//                {"Pierre Gasly", "BWT Alpine F1 Team", "France", "4", "403", "150", "0", "07/02/1996", "French", "gasly", "2024"},
//                {"Oliver Bearman", "Scuderia Ferrari", "United Kingdom", "0", "7", "2", "0", "08/05/2005", "British", "bearman", "2024"},
//                {"Franco Colapinto", "Williams Racing", "Argentina", "0", "5", "5", "0", "27/05/2003", "Argentinian", "colapinto", "2024"},
//                {"Esteban Ocon", "BWT Alpine F1 Team", "France", "3", "427", "153", "0", "17/09/1996", "French", "ocon", "2024"},
//                {"Liam Lawson", "Visa Cash App RB Formula One Team", "New Zealand", "0", "4", "7", "0", "11/02/2002", "New Zealander", "lawson", "2024"},
//                {"Zhou Guanyu", "Stake F1 Team Kick Sauber", "China", "0", "12", "64", "0", "30/05/1999", "Chinese", "guanyu", "2024"},
//                {"Logan Sargeant", "Williams Racing", "United States", "0", "1", "37", "0", "31/12/2000", "American", "sargeant", "2024"},
//                {"Valtteri Bottas", "Stake F1 Team Kick Sauber", "Finland", "67", "1797", "242", "0", "28/08/1989", "Finnish", "bottas", "2024"}
//                };
//
//        for (int i = 0; i < drivers.length; i++) {
//            Driver driver = new Driver(drivers[i][0], drivers[i][1], drivers[i][2], Integer.parseInt(drivers[i][3]), Float.parseFloat(drivers[i][4]), Integer.parseInt(drivers[i][5]), Integer.parseInt(drivers[i][6]), drivers[i][7], drivers[i][8], drivers[i][9], Integer.parseInt(drivers[i][10]));
//
//            String driverId = database.push().getKey(); // Generates a unique key
//            database.child(driverId).setValue(driver)
//                    .addOnSuccessListener(aVoid -> {
//                        // Data saved successfully
//                    })
//                    .addOnFailureListener(e -> {
//                        // Failed to save data
//                    });
//        }


        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(logo, "scaleX", 0.6f, 1.6f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(logo, "scaleY", 0.6f, 1.6f);
        scaleXAnim.setDuration(SPLASH_SCREEN_TIMEOUT);
        scaleYAnim.setDuration(SPLASH_SCREEN_TIMEOUT);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnim, scaleYAnim);
        animatorSet.start();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIMEOUT);
    }
}