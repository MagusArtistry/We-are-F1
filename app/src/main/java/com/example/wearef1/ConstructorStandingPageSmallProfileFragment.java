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


public class ConstructorStandingPageSmallProfileFragment extends Fragment {

    private static final String ARG_TEAM_NAME = "Mercedes";
    private static final String ARG_INFO_YEAR = "2024";

    private String team_name;
    private String info_year;

    Team selectedTeam;

    private ImageView imageView;
    private TextView team_principal_name_view;
    private TextView team_wins_view;
    private Button viewAllButton;
    private TextView team_drivers_view;
    private FirebaseStorage storage;

    public ConstructorStandingPageSmallProfileFragment() { }

    public static ConstructorStandingPageSmallProfileFragment newInstance(String param1, String param2) {
        ConstructorStandingPageSmallProfileFragment fragment = new ConstructorStandingPageSmallProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_NAME, param1);
        args.putString(ARG_INFO_YEAR, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            team_name = getArguments().getString(ARG_TEAM_NAME);
            info_year = getArguments().getString(ARG_INFO_YEAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constructor_standing_page_small_profile, container, false);

        imageView = view.findViewById(R.id.constructor_standing_page_pic);
        team_principal_name_view = view.findViewById(R.id.constructor_standing_page_team_principal_value);
        team_wins_view = view.findViewById(R.id.constructor_standing_page_team_wins_value);
        team_drivers_view = view.findViewById(R.id.constructor_standing_page_team_drivers_value);
        viewAllButton = view.findViewById(R.id.constructor_standing_page_view_team_button);

        viewAllButton.setOnClickListener(v -> {
            TeamDetailFragment detailsFragment = TeamDetailFragment.newInstance(team_name, info_year);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, detailsFragment);
            fragmentTransaction.commit();
        });

        storage = FirebaseStorage.getInstance();
        getTeamData();

        return view;
    }

    private void getTeamData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teams");

        Query query = databaseReference.orderByChild("ref_name").equalTo(team_name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Team team = snapshot.getValue(Team.class);
                    // Further filter by info_year
                    if (team != null && info_year.equals(Integer.toString(team.getYear_info()))) {
                        selectedTeam = team;
                        updateUI(); // Update the UI after retrieving the team data
                        break; // Exit loop after finding the matching team
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseData", "loadTeam:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateUI() {
        if (selectedTeam != null) {
            // Set UI components
            team_principal_name_view.setText(selectedTeam.getTeam_chief());
            team_wins_view.setText(selectedTeam.getWorld_championships());
            String drivers = selectedTeam.getFirst_driver() + "\n" + selectedTeam.getSecond_driver();
            team_drivers_view.setText(drivers);

            // Load image from Firebase
            loadImageFromFirebase();
        }
    }

    private void loadImageFromFirebase() {
        // Create a reference to the file location in Firebase Storage
        String image_file_name = "car_pic/" + team_name.replace(' ', '_') + "_" + info_year + ".png";
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
