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
import android.widget.ScrollView;
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


public class DriverDetailFragment extends Fragment {

    private static final String ARG_DRIVER_NAME = "leclerc";
    private static final String ARG_INFO_YEAR = "2024";
    private String driver_name;
    private String team_ref;
    private String info_year;

    private TextView country_view;
    private TextView dob_view;
    private TextView podiums_view;
    private TextView total_points_view;
    private TextView races_entered_view;
    private TextView wc_count_view;
    private TextView nationality_view;
    private TextView driver_name_view;
    private TextView team_name_view;
    private ImageView driver_image_view;
    private ImageView car_image_view;
    private Driver selectedDriver;
    private FirebaseStorage storage;
    private Button driver_page_team_click;

    public DriverDetailFragment() {
    }

    public static DriverDetailFragment newInstance(String param1, String param2) {
        DriverDetailFragment fragment = new DriverDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_driver_detail, container, false);

        ScrollView scrollView = getActivity().findViewById(R.id.main_activity_scroll_view);
        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });
        }

        country_view = view.findViewById(R.id.driver_detail_page_country_value);
        dob_view = view.findViewById(R.id.driver_detail_page_dob_value);
        podiums_view = view.findViewById(R.id.driver_detail_page_podiums_value);
        total_points_view = view.findViewById(R.id.driver_detail_page_total_points_value);
        races_entered_view = view.findViewById(R.id.driver_detail_page_races_entered_value);
        wc_count_view = view.findViewById(R.id.driver_detail_page_driver_wc_value);
        nationality_view = view.findViewById(R.id.driver_detail_page_pole_nationality_value);
        driver_name_view = view.findViewById(R.id.driver_detail_page_heading_name);
        team_name_view = view.findViewById(R.id.driver_detail_page_team_heading);
        driver_image_view = view.findViewById(R.id.driver_detail_page_driver_pic);
        car_image_view = view.findViewById(R.id.driver_detail_page_car_pic);
        driver_page_team_click = view.findViewById(R.id.driver_page_team_click);

        storage = FirebaseStorage.getInstance();
        getDriverData();

        return view;
    }

    private void getDriverData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("drivers");
        Log.e("getDriverData: ", driver_name);
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
                Log.w("FirebaseData", "loadTeam:onCancelled", databaseError.toException());
            }
        });
    }

    private void getTeamRef(){

        String teamName = selectedDriver.getTeam_name();
        switch (teamName){
            case "Mercedes-AMG PETRONAS F1 Team":
                team_ref = "mercedes";
                break;
            case "McLaren Formula 1 Team":
                team_ref = "mclaren";
                break;
            case "Oracle Red Bull Racing":
                team_ref = "red bull";
                break;
            case "Scuderia Ferrari":
                team_ref = "ferrari";
                break;
            case "BWT Alpine F1 Team":
                team_ref = "alpine f1 team";
                break;
            case "Aston Martin Aramco F1 Team":
                team_ref = "aston martin";
                break;
            case "Stake F1 Team Kick Sauber":
                team_ref = "kick sauber";
                break;
            case "Visa Cash App RB Formula One Team":
                team_ref = "rb f1 team";
                break;
            case "MoneyGram Haas F1 Team":
                team_ref = "haas f1 team";
                break;
            case "Williams Racing":
                team_ref = "williams";
                break;
            default:
                team_ref = "mercedes";
        }
    }

    private void updateUI() {
        if (selectedDriver != null) {
            // Set UI components
            country_view.setText(selectedDriver.getCountry());
            dob_view.setText(selectedDriver.getDob());
            podiums_view.setText(Integer.toString(selectedDriver.getPodiums()));
            total_points_view.setText(Float.toString(selectedDriver.getTotal_points()));
            races_entered_view.setText(Integer.toString(selectedDriver.getRaces_entered()));
            wc_count_view.setText(Integer.toString(selectedDriver.getWorld_championships_count()));
            driver_name_view.setText(selectedDriver.getName());
            nationality_view.setText(selectedDriver.getNationality());
            team_name_view.setText(selectedDriver.getTeam_name());

            getTeamRef();
            loadDriverImageFromFirebase();

            // Load image from Firebase
            loadCarImageFromFirebase();
            loadDriverImageFromFirebase();

            driver_page_team_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamDetailFragment fragment = TeamDetailFragment.newInstance(team_ref, info_year);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }


    private void loadCarImageFromFirebase() {
        // Create a reference to the file location in Firebase Storage
        String image_file_name = "car_pic/" + team_ref.replace(' ', '_') + "_" + info_year + ".png";
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

    private void loadDriverImageFromFirebase() {
        // Create a reference to the file location in Firebase Storage
        String image_file_name = "driver_pic/" + selectedDriver.getRef_name().toLowerCase(Locale.ROOT) + "_" + info_year + ".png";
        StorageReference storageRef = storage.getReference().child(image_file_name);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .into(driver_image_view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }
}