package com.example.wearef1;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


public class TeamDetailFragment extends Fragment {

    private static final String ARG_TEAM_NAME = "Mercedes";
    private static final String ARG_INFO_YEAR = "2024";

    private String team_name;
    private String info_year;
    private TextView teamNameView;
    private TextView baseView;
    private TextView teamPrincipalView;
    private TextView technicalChiefView;
    private TextView chassisView;
    private TextView powerUnitView;
    private TextView worldChampionView;
    private TextView polePositionCountView;
    private TextView fastestLapCountView;
    private TextView driver1_name_view;
    private TextView driver2_name_view;
    private ImageView driver1_image_view;
    private ImageView driver2_image_view;
    private ImageView car_image_view;
    private Team selectedTeam;
    private FirebaseStorage storage;
    private Button driver1_button;
    private Button driver2_button;
    public TeamDetailFragment() {
    }
    public static TeamDetailFragment newInstance(String param1, String param2) {
        TeamDetailFragment fragment = new TeamDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_team_detail, container, false);

        teamNameView = view.findViewById(R.id.team_detail_page_heading_name);
        baseView = view.findViewById(R.id.team_detail_page_team_base_value);
        teamPrincipalView = view.findViewById(R.id.team_detail_page_team_principal_value);
        technicalChiefView = view.findViewById(R.id.team_detail_page_team_technical_chief_value);
        chassisView = view.findViewById(R.id.team_detail_page_team_chassis_value);
        powerUnitView = view.findViewById(R.id.team_detail_page_team_power_unit_value);
        worldChampionView = view.findViewById(R.id.team_detail_page_team_wc_value);
        polePositionCountView = view.findViewById(R.id.team_detail_page_pole_positions_count_value);
        fastestLapCountView = view.findViewById(R.id.team_detail_page_fastest_laps_count_value);
        driver1_name_view = view.findViewById(R.id.team_detail_page_driver1_name);
        driver2_name_view = view.findViewById(R.id.team_detail_page_driver2_name);
        driver1_image_view = view.findViewById(R.id.team_detail_page_driver1_pic);
        driver2_image_view = view.findViewById(R.id.team_detail_page_driver2_pic);
        car_image_view = view.findViewById(R.id.team_detail_page_car_pic);
        driver1_button = view.findViewById(R.id.team_detail_page_driver1_click);
        driver2_button = view.findViewById(R.id.team_detail_page_driver2_click);

        driver1_button.setOnClickListener(v -> {
            DriverDetailFragment fragment = DriverDetailFragment.newInstance(selectedTeam.getFirst_driver().split(" ")[1], info_year);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        driver1_button.setOnClickListener(v -> {
            DriverDetailFragment fragment = DriverDetailFragment.newInstance(selectedTeam.getSecond_driver().split(" ")[1], info_year);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
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
            teamNameView.setText(selectedTeam.getFull_team_name());
            baseView.setText(selectedTeam.getBase());
            teamPrincipalView.setText(selectedTeam.getTeam_chief());
            technicalChiefView.setText(selectedTeam.getTechnical_chief());
            chassisView.setText(selectedTeam.getChassis());
            powerUnitView.setText(selectedTeam.getPower_unit());
            worldChampionView.setText(selectedTeam.getWorld_championships());
            polePositionCountView.setText(Integer.toString(selectedTeam.getPole_position_count()));
            fastestLapCountView.setText(Integer.toString(selectedTeam.getFastest_lap_count()));
            driver1_name_view.setText(selectedTeam.getFirst_driver());
            driver2_name_view.setText(selectedTeam.getSecond_driver());


            // Load image from Firebase
            loadCarImageFromFirebase();
            loadDriverImageFromFirebase(1);
            loadDriverImageFromFirebase(2);
        }
    }

    private void loadCarImageFromFirebase() {
        // Create a reference to the file location in Firebase Storage
        String image_file_name = "car_pic/" + team_name.replace(' ', '_') + "_" + info_year + ".png";
        StorageReference storageRef = storage.getReference().child(image_file_name);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .into(car_image_view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }

    private void loadDriverImageFromFirebase(int driver) {
        // Create a reference to the file location in Firebase Storage
        String driverName = "hamilton";
        if(driver == 1){
            driverName = selectedTeam.getFirst_driver().split(" ")[1].toLowerCase(Locale.ROOT);
        }
        else{
            driverName = selectedTeam.getSecond_driver().split(" ")[1].toLowerCase(Locale.ROOT);
        }
        String image_file_name = "driver_pic/" + driverName + "_" + info_year + ".png";
        StorageReference storageRef = storage.getReference().child(image_file_name);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(driver == 1){
                    Glide.with(getContext())
                            .load(uri)
                            .into(driver1_image_view);
                }
                else{
                    Glide.with(getContext())
                            .load(uri)
                            .into(driver2_image_view);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }
}