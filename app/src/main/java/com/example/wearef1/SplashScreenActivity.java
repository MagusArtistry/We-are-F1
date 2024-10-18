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
//
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("teams");
//
//        String[][] teams = {
//                {"Mercedes-AMG PETRONAS F1 Team", "Brackley, United Kingdom", "Toto Wolff", "James Allison", "W15", "Mercedes", "8", "131", "100", "Lewis Hamilton", "George Russell", "Mercedes", "2024"},
//                {"Oracle Red Bull Racing", "Milton Keynes, United Kingdom", "Christian Horner", "Pierre Waché", "RB20", "Honda RBPT", "6", "105", "98", "Max Verstappen", "Sergio Pérez", "Red Bull", "2024"},
//                {"Scuderia Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Enrico Cardile", "SF-24", "Ferrari", "16", "252", "262", "Charles Leclerc", "Carlos Sainz", "Ferrari", "2024"},
//                {"McLaren Formula 1 Team", "Woking, United Kingdom", "Andrea Stella", "Peter Prodromou", "MCL38", "Mercedes", "8", "162", "170", "Lando Norris", "Oscar Piastri", "McLaren", "2024"},
//                {"BWT Alpine F1 Team", "Enstone, United Kingdom", "Oliver Oakes", "David Sanchez", "A524", "Renault", "2", "20", "15", "Pierre Gasly", "Esteban Ocon", "Alpine F1 Team", "2024"},
//                {"Aston Martin Aramco F1 Team", "Silverstone, United Kingdom", "Mike Krack", "Dan Fallows", "AMR24", "Mercedes", "0", "1", "3","Fernando Alonso", "Lance Stroll", "Aston Martin", "2024"},
//                {"Stake F1 Team Kick Sauber", "Hinwil, Switzerland", "Alessandro Alunni Bravi", "James Key", "C44", "Ferrari", "0", "1", "7", "Valtteri Bottas", "Zhou Guanyu", "Sauber", "2024"},
//                {"Visa Cash App RB Formula One Team", "Faenza, Italy", "Laurent Mekies", "Jody Egginton", "VCARB 01", "Honda RBPT", "0", "1", "4", "Yuki Tsunoda", "Liam Lawson", "RB F1 Team", "2024"},
//                {"MoneyGram Haas F1 Team", "Kannapolis, USA", "Ayao Komatsu", "Andrea De Zordo", "VF-24", "Ferrari", "0", "1", "2", "Nico Hulkenberg", "Kevin Magnussen", "Haas F1 Team", "2024"},
//                {"Williams Racing", "Grove, United Kingdom", "James Vowles", "Pat Fry", "FW46", "Mercedes", "9", "128", "133", "Alexander Albon", "Franco Colapinto", "Williams", "2024"}
//        };
//
//        for (int i = 0; i < teams.length; i++) {
//            Team team = new Team();
//            team.setFull_team_name(teams[i][0]);
//            team.setBase(teams[i][1]);
//            team.setTeam_chief(teams[i][2]);
//            team.setTechnical_chief(teams[i][3]);
//            team.setChassis(teams[i][4]);
//            team.setPower_unit(teams[i][5]);
//            team.setWorld_championships(teams[i][6]);
//            team.setPole_position_count(Integer.parseInt(teams[i][7]));
//            team.setFastest_lap_count(Integer.parseInt(teams[i][8]));
//            team.setFirst_driver(teams[i][9]);
//            team.setSecond_driver(teams[i][10]);
//            team.setRef_name(teams[i][11]);
//            team.setYear_info(Integer.parseInt(teams[i][12]));
//
//            String teamId = database.push().getKey(); // Generates a unique key
//            database.child(teamId).setValue(team)
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
            Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIMEOUT);
    }
}