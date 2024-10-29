package com.example.wearef1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    private RecyclerView constructorRecyclerView, driverRecyclerView;
    private ConstructorsStandingAdapter constructorAdapter;
    private DriversStandingAdapter driverAdapter;

    private List<ConstructorStanding> top5ConstructorStandings = new ArrayList<>();
    private List<DriversStanding> top5DriversStandings = new ArrayList<>();
    private Button constructorStandingSeeAllButton;
    private Button driverStandingSeeAllButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

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

        return view;
    }

    private void fetchConstructorStandings() {
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
