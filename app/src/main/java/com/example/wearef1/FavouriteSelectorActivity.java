package com.example.wearef1;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class FavouriteSelectorActivity extends AppCompatActivity {
    private String selectedDriver = "";
    private String selectedTeam = "";

    private Button nextButton;
    private Button backButton;
    private Boolean inDriverSelection = true;
    private GridLayout gridLayout;
    private TextView dynamicHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite_selector);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridLayout = findViewById(R.id.grid_pics_favourite_page);
        nextButton = findViewById(R.id.button_2_favourite_page);
        backButton = findViewById(R.id.button_1_favourite_page);
        dynamicHeading = findViewById(R.id.select_text_dynamic_favourite_page);

        createDriverList();

        nextButton.setOnClickListener(v -> {
            if(inDriverSelection){
                if(selectedDriver.isEmpty()){
                    Toast.makeText(FavouriteSelectorActivity.this, "Please select a driver first.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FavouriteSelectorActivity.this, "Driver Selected.", Toast.LENGTH_SHORT).show();
                    inDriverSelection = false;
                    nextButton.setText("Done");
                    dynamicHeading.setText("Favourite Team");
                    createTeamList();
                }
            }
            else{
                if(selectedTeam.isEmpty()){
                    Toast.makeText(FavouriteSelectorActivity.this, "Please select a team first.", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveInfo();
                }
            }
        });

        backButton.setOnClickListener(v -> {
            if(!inDriverSelection){
                inDriverSelection = true;
                nextButton.setText("Next");
                dynamicHeading.setText("Favourite Driver");
                createDriverList();
            }
        });
    }

    private void createDriverList(){
        // Reference to Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("driver_pic");
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(3);

        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference fileRef : listResult.getItems()) {
                // Create an ImageView for each image
                ImageView imageView = new ImageView(FavouriteSelectorActivity.this);

                // Set layout parameters
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = 0; // Match column width
                layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal column weight
                layoutParams.setMargins(8, 8, 8, 8); // Spacing
                imageView.setLayoutParams(layoutParams);

                // Set default background
                imageView.setBackgroundColor(Color.BLACK);

                // Add rotating animation to the placeholder
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
                animator.setDuration(1000); // Rotate in 1 second
                animator.setRepeatCount(ObjectAnimator.INFINITE); // Keep rotating
                animator.start();

                // Load the image using Glide
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(FavouriteSelectorActivity.this)
                            .load(uri)
                            .placeholder(R.drawable.f1_tire) // Use the rotating placeholder
                            .into(imageView);

                    // Stop the rotation animation once the image is loaded
                    animator.end();
                }).addOnFailureListener(e -> {
                    // Handle failure and stop animation
                    animator.end();
                });

                // Set onClickListener for the ImageView
                imageView.setOnClickListener(v -> {
                    // Reset the background of all other ImageViews
                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        View child = gridLayout.getChildAt(i);
                        child.setBackgroundColor(Color.TRANSPARENT); // Default background
                    }

                    // Highlight the clicked ImageView
                    imageView.setBackgroundColor(Color.parseColor("#FF0000")); // Red background

                    selectedDriver = fileRef.getName().split("_")[0];

                    // Show the image file name in a Toast
                    Toast.makeText(FavouriteSelectorActivity.this, "Selected: " + selectedDriver, Toast.LENGTH_SHORT).show();
                });

                // Add the ImageView to the GridLayout
                gridLayout.addView(imageView);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(FavouriteSelectorActivity.this, "Failed to load images: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void createTeamList(){
        // Reference to Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("car_pic");
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(1);

        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference fileRef : listResult.getItems()) {
                // Create an ImageView for each image
                ImageView imageView = new ImageView(FavouriteSelectorActivity.this);

                // Set layout parameters
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = 0; // Match column width
                layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal column weight
                layoutParams.setMargins(8, 8, 8, 8); // Spacing
                imageView.setLayoutParams(layoutParams);

                // Set default background
                imageView.setBackgroundColor(Color.BLACK);

                // Add rotating animation to the placeholder
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
                animator.setDuration(1000); // Rotate in 1 second
                animator.setRepeatCount(ObjectAnimator.INFINITE); // Keep rotating
                animator.start();

                // Load the image using Glide
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(FavouriteSelectorActivity.this)
                            .load(uri)
                            .placeholder(R.drawable.f1_tire) // Use the rotating placeholder
                            .into(imageView);

                    // Stop the rotation animation once the image is loaded
                    animator.end();
                }).addOnFailureListener(e -> {
                    // Handle failure and stop animation
                    animator.end();
                });

                // Set onClickListener for the ImageView
                imageView.setOnClickListener(v -> {
                    // Reset the background of all other ImageViews
                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        View child = gridLayout.getChildAt(i);
                        child.setBackgroundColor(Color.TRANSPARENT); // Default background
                    }

                    // Highlight the clicked ImageView
                    imageView.setBackgroundColor(Color.parseColor("#FF0000")); // Red background

                    selectedTeam = fileRef.getName().split("_")[0];

                    // Show the image file name in a Toast
                    Toast.makeText(FavouriteSelectorActivity.this, "Selected: " + selectedTeam, Toast.LENGTH_SHORT).show();
                });

                // Add the ImageView to the GridLayout
                gridLayout.addView(imageView);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(FavouriteSelectorActivity.this, "Failed to load images: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void saveInfo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userID", null); // Default to null if not found

        if (userId == null) {
            Log.d("SharedPreferences", "User ID not found");
            return;
        }

        // Retrieve driver ID asynchronously
        getDriverIdByRefName(selectedDriver, driverID -> {
            if (driverID == null) {
                System.out.println("Driver not found for selectedDriver: " + selectedDriver);
                return;
            }

            // Retrieve team ID asynchronously
            getTeamIdByRefName(selectedTeam, teamID -> {
                if (teamID == null) {
                    System.out.println("Team not found for selectedTeam: " + selectedTeam);
                    return;
                }

                // Once both IDs are retrieved, update the user's data
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("favoriteDriverId", driverID);
                updates.put("favoriteTeamId", teamID);

                User u = UserPreferenceManager.getUser(FavouriteSelectorActivity.this);
                if(u != null) {
                    u.setFavoriteTeamId(teamID);
                    u.setFavoriteDriverId(driverID);
                    UserPreferenceManager.updateUser(FavouriteSelectorActivity.this, u);
                }

                usersRef.child(userId).updateChildren(updates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("User updated successfully!");

                        Intent intent = new Intent(FavouriteSelectorActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        System.out.println("Failed to update user: " + task.getException().getMessage());
                    }
                });
            });
        });
    }


    private void getTeamIdByRefName(String refName, OnIdRetrievedCallback callback) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference("teams");

        teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String teamId = null;
                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    String teamRefName = teamSnapshot.child("ref_name").getValue(String.class);
                    if (teamRefName != null && teamRefName.equals(refName)) {
                        teamId = teamSnapshot.getKey();
                        break; // Exit the loop once a match is found
                    }
                }

                if (teamId != null) {
                    System.out.println("Team ID: " + teamId);
                } else {
                    System.out.println("No team found with ref_name: " + refName);
                }
                callback.onIdRetrieved(teamId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError.getMessage());
                callback.onIdRetrieved(null);
            }
        });
    }

    private void getDriverIdByRefName(String refName, OnIdRetrievedCallback callback) {
        DatabaseReference driversRef = FirebaseDatabase.getInstance().getReference("drivers");

        driversRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String driverId = null;
                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    String driverRefName = driverSnapshot.child("ref_name").getValue(String.class);
                    if (driverRefName != null && driverRefName.equals(refName)) {
                        driverId = driverSnapshot.getKey();
                        break; // Exit the loop once a match is found
                    }
                }

                if (driverId != null) {
                    System.out.println("Driver ID: " + driverId);
                } else {
                    System.out.println("No driver found with ref_name: " + refName);
                }
                callback.onIdRetrieved(driverId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError.getMessage());
                callback.onIdRetrieved(null);
            }
        });
    }

    public interface OnIdRetrievedCallback {
        void onIdRetrieved(String id);
    }
}
