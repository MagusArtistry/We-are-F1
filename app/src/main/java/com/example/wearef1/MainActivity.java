package com.example.wearef1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    ImageView upcomingButton;
    ImageView homeButton;
    ImageView communityButton;
    ImageView profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        upcomingButton = findViewById(R.id.bottom_nav_upcoming);
        upcomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CalenderFragment fragment = new CalenderFragment();
                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        homeButton = findViewById(R.id.bottom_nav_home);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                HomePageFragment fragment = new HomePageFragment();
                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        communityButton = findViewById(R.id.bottom_nav_community);

        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                HomePageFragment fragment = new HomePageFragment();
//                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
//                fragmentTransaction.commit();
            }
        });

        profileButton = findViewById(R.id.bottom_nav_profile);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                HomePageFragment fragment = new HomePageFragment();
//                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
//                fragmentTransaction.commit();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomePageFragment fragment = new HomePageFragment();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.commit();



    }
}