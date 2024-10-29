package com.example.wearef1;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;


public class DriverStandingPageSmallProfileFragment extends Fragment {
    private static final String ARG_DRIVER_NAME = "hamilton";
    private static final String ARG_INFO_YEAR = "2024";

    private String driver_name;
    private String info_year;

    Driver selectedDriver;

    private ImageView imageView;
    private TextView driver_team_name_view;
    private TextView driver_wins_view;
    private Button viewAllButton;
    private TextView driver_nationality_view;
    private FirebaseStorage storage;

    public DriverStandingPageSmallProfileFragment() { }

    public static DriverStandingPageSmallProfileFragment newInstance(String param1, String param2) {
        DriverStandingPageSmallProfileFragment fragment = new DriverStandingPageSmallProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DRIVER_NAME, param1);
        args.putString(ARG_INFO_YEAR, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            driver_name = getArguments().getString(ARG_DRIVER_NAME);
            info_year = getArguments().getString(ARG_INFO_YEAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_standing_page_small_profile, container, false);

        imageView = view.findViewById(R.id.driver_standing_page_pic);
        driver_team_name_view = view.findViewById(R.id.driver_standing_page_driver_team_name_value);
        driver_wins_view = view.findViewById(R.id.driver_standing_page_driver_wins_value);
        driver_nationality_view = view.findViewById(R.id.driver_standing_page_driver_nationality_value);
        viewAllButton = view.findViewById(R.id.driver_standing_page_view_driver_button);

        viewAllButton.setOnClickListener(v -> {
            DriverDetailFragment detailsFragment = DriverDetailFragment.newInstance(driver_name, info_year);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, detailsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        storage = FirebaseStorage.getInstance();
        getTeamData();

        return view;
    }

    private void getTeamData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("drivers");

        Query query = databaseReference.orderByChild("ref_name").equalTo(driver_name.split(" ")[1].toLowerCase(Locale.ROOT));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Driver driver = snapshot.getValue(Driver.class);
                    // Further filter by info_year
                    if (driver != null && info_year.equals(Integer.toString(driver.getYear_info()))) {
                        selectedDriver = driver;
                        updateUI(); // Update the UI after retrieving the team data
                        break; // Exit loop after finding the matching team
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseData", "loadDriver:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateUI() {
        if (selectedDriver != null) {
            // Set UI components
            driver_team_name_view.setText(selectedDriver.getTeam_name());
            driver_wins_view.setText(String.valueOf(selectedDriver.getWorld_championships_count()));
            driver_nationality_view.setText(selectedDriver.getNationality());

            // Load image from Firebase
            loadImageFromFirebase();
        }
    }

    private void loadImageFromFirebase() {
        // Create a reference to the file location in Firebase Storage
        String image_file_name = "driver_pic/" + selectedDriver.getRef_name().toLowerCase(Locale.ROOT) + "_" + info_year + ".png";
        StorageReference storageRef = storage.getReference().child(image_file_name);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }
}