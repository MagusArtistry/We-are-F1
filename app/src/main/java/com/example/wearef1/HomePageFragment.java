package com.example.wearef1;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomePageFragment extends Fragment {

    private RecyclerView constructorRecyclerView, driverRecyclerView;
    private ConstructorsStandingAdapter constructorAdapter;
    private DriversStandingAdapter driverAdapter;

    private List<ConstructorStanding> top5ConstructorStandings = new ArrayList<>();
    private List<DriversStanding> top5DriversStandings = new ArrayList<>();
    private Button constructorStandingSeeAllButton;
    private Button driverStandingSeeAllButton;
    Driver favoriteDriver;
    private FirebaseStorage storage;
    private ImageView driver_iv;
    private TextView driver_profile_main_name_tv;
    private TextView driver_profile_main_country_tv;
    private TextView driver_profile_main_team_name_tv;
    private TextView driver_profile_main_podiums_tv;
    private TextView driver_profile_main_wc_tv;
    private Button driver_profile_main_seeMore_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        driver_iv = view.findViewById(R.id.driver_profile_main_pic);
        driver_profile_main_name_tv = view.findViewById(R.id.driver_profile_main_name);
        driver_profile_main_country_tv = view.findViewById(R.id.driver_profile_main_country);
        driver_profile_main_team_name_tv = view.findViewById(R.id.driver_profile_main_team_name);
        driver_profile_main_podiums_tv = view.findViewById(R.id.driver_profile_main_podiums);
        driver_profile_main_wc_tv = view.findViewById(R.id.driver_profile_main_wc);
        driver_profile_main_seeMore_btn = view.findViewById(R.id.driver_profile_main_seeMore_button);

        driver_profile_main_seeMore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverDetailFragment detailsFragment = DriverDetailFragment.newInstance(favoriteDriver.getName(), Integer.toString(favoriteDriver.getYear_info()));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment_container, detailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Constructor RecyclerView setup
        constructorRecyclerView = view.findViewById(R.id.constructors_standing_recycler_view);
        constructorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize constructor adapter with an empty list
        constructorAdapter = new ConstructorsStandingAdapter(top5ConstructorStandings);
        constructorRecyclerView.setAdapter(constructorAdapter);

        // Driver RecyclerView setup
        driverRecyclerView = view.findViewById(R.id.drivers_standing_recycler_view);
        driverRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize driver adapter with an empty list
        driverAdapter = new DriversStandingAdapter(top5DriversStandings);
        driverRecyclerView.setAdapter(driverAdapter);

        // Fetch constructor and driver standings
        fetchConstructorStandings();
        fetchDriverStandings();

        constructorStandingSeeAllButton = view.findViewById(R.id.see_all_constructors_standing_button);
        driverStandingSeeAllButton = view.findViewById(R.id.see_all_drivers_standing_button);

        constructorStandingSeeAllButton.setOnClickListener(v -> {
            replaceFragment(new ConstructorsStandingsFragment());
        });

        driverStandingSeeAllButton.setOnClickListener(v -> {
            replaceFragment(new DriversStandingsFragment());
        });

        storage = FirebaseStorage.getInstance();
        User user = UserPreferenceManager.getUser(getContext());

        String driverId = "-OAMypPHCPxp60MeD4QO";
        if(user != null) {
            driverId = user.getFavoriteDriverId();
        }
        getDriverById(driverId);

        return view;
    }

    private void getDriverById(String driverId) {
        // Reference to the "drivers" table
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("drivers").child(driverId);

        // Use get() method to fetch the data once
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Convert the DataSnapshot to a Driver object
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    if (driver != null) {
                        // Driver object retrieved successfully
                        favoriteDriver = driver;
                        updateUI();
                        loadImageFromFirebase(); // Update the UI after retrieving the driver data
                    } else {
                        Log.e("FirebaseData", "Failed to convert data to Driver object");
                    }
                } else {
                    Log.e("FirebaseData", "Driver not found with ID: " + driverId);
                }
            } else {
                // Handle task failure (e.g., network issues, permission issues)
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("FirebaseData", "Error: " + exception.getMessage());
                }
            }
        });
    }

    private void updateUI(){
        driver_profile_main_name_tv.setText(favoriteDriver.getName());
        driver_profile_main_country_tv.setText(favoriteDriver.getCountry());
        driver_profile_main_team_name_tv.setText(favoriteDriver.getTeam_name().split(" ")[0]);
        driver_profile_main_podiums_tv.setText(Integer.toString(favoriteDriver.getPodiums()) + " Podiums");
        driver_profile_main_wc_tv.setText(Integer.toString(favoriteDriver.getWorld_championships_count()) + " WCs");
    }
    private void loadImageFromFirebase() {
        // Create a reference to the file location in Firebase Storage
        String image_file_name = "driver_pic/" + favoriteDriver.getRef_name().toLowerCase(Locale.ROOT) + "_" + "2024" + ".png";
        StorageReference storageRef = storage.getReference().child(image_file_name);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .into(driver_iv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }

    private void fetchConstructorStandings() {
//        top5ConstructorStandings.clear();
        if (!top5ConstructorStandings.isEmpty()) {
            constructorAdapter.notifyDataSetChanged();
            return;
        }
        String url = "https://ergast.com/api/f1/2024/constructorstandings.json";
        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray standings = response.getJSONObject("MRData")
                                .getJSONObject("StandingsTable")
                                .getJSONArray("StandingsLists")
                                .getJSONObject(0)
                                .getJSONArray("ConstructorStandings");

                        for (int i = 0; i < Math.min(standings.length(), 5); i++) {
                            JSONObject standing = standings.getJSONObject(i);
                            String name = standing.getJSONObject("Constructor").getString("name");
                            String points = standing.getString("points");
                            top5ConstructorStandings.add(new ConstructorStanding(Integer.toString(i + 1), name, points));
                        }

                        constructorAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("HomePageFragment", "Error parsing constructor standings", e);
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Failed to fetch constructor standings", Toast.LENGTH_SHORT).show();
                    Log.e("HomePageFragment", "Error fetching constructor standings", error);
                }
        );

        volleyQueue.add(jsonObjectRequest);
    }

    private void fetchDriverStandings() {
//        top5ConstructorStandings.clear();
        if (!top5DriversStandings.isEmpty()) {
            driverAdapter.notifyDataSetChanged();
            return;
        }
        String url = "https://ergast.com/api/f1/2024/driverstandings.json";
        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray standings = response.getJSONObject("MRData")
                                .getJSONObject("StandingsTable")
                                .getJSONArray("StandingsLists")
                                .getJSONObject(0)
                                .getJSONArray("DriverStandings");

                        for (int i = 0; i < Math.min(standings.length(), 5); i++) {
                            JSONObject standing = standings.getJSONObject(i);
                            String name = standing.getJSONObject("Driver").getString("givenName") + " " +
                                    standing.getJSONObject("Driver").getString("familyName");
                            String points = standing.getString("points");
                            top5DriversStandings.add(new DriversStanding(standing.getString("position"), name, points));
                        }

                        driverAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("HomePageFragment", "Error parsing driver standings", e);
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Failed to fetch driver standings", Toast.LENGTH_SHORT).show();
                    Log.e("HomePageFragment", "Error fetching driver standings", error);
                }
        );

        volleyQueue.add(jsonObjectRequest);
    }

    private void replaceFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
