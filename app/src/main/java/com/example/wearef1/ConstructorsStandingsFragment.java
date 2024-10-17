package com.example.wearef1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConstructorsStandingsFragment extends Fragment {

    private LinearLayout containerLayout;
    private List<ConstructorStanding> constructorStandings;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constructors_standings, container, false);

        containerLayout = view.findViewById(R.id.constructors_standing_page_list_view);
        fragmentManager = getChildFragmentManager();

        if (constructorStandings == null) {
            constructorStandings = new ArrayList<>();
        }

        // Fetch and display constructor standings
        fetchConstructorStandings();

        return view;
    }

    private void setRowColor(TextView positionTextView, TextView teamTextView, TextView pointsTextView, int position) {
        int color;
        switch (position) {
            case 0:
                color = getResources().getColor(R.color.gold);
                break;
            case 1:
                color = getResources().getColor(R.color.silver);
                break;
            case 2:
                color = getResources().getColor(R.color.bronze);
                break;
            default:
                color = getResources().getColor(R.color.grey);
                break;
        }
        positionTextView.setTextColor(color);
        teamTextView.setTextColor(color);
        pointsTextView.setTextColor(color);
    }

    private void toggleFragment(FrameLayout fragmentContainer) {
        Fragment fragment = fragmentManager.findFragmentById(fragmentContainer.getId());

        if (fragment != null) {
            // Remove the fragment if it's already displayed
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragmentContainer.setVisibility(View.GONE);
        } else {
            // Add a new fragment
            ConstructorStandingPageSmallProfileFragment fragmentToDisplay = new ConstructorStandingPageSmallProfileFragment();
            fragmentManager.beginTransaction()
                    .replace(fragmentContainer.getId(), fragmentToDisplay)
                    .commit();
            fragmentContainer.setVisibility(View.VISIBLE);
        }
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

                        constructorStandings.clear(); // Clear existing data

                        containerLayout.removeAllViews(); // Clear the container layout before adding new views

                        for (int i = 0; i < standings.length(); i++) {
                            JSONObject standing = standings.getJSONObject(i);
                            String name = standing.getJSONObject("Constructor").getString("name");
                            String points = standing.getString("points");
                            constructorStandings.add(new ConstructorStanding(Integer.toString(i + 1), name, points));

                            // Inflate custom row layout
                            View row = LayoutInflater.from(getContext()).inflate(R.layout.constructor_standing_page_row, containerLayout, false);

                            // Set text for position, team, points
                            TextView positionTextView = row.findViewById(R.id.constructor_standing_page_row_pos);
                            TextView teamTextView = row.findViewById(R.id.constructor_standing_page_row_team);
                            TextView pointsTextView = row.findViewById(R.id.constructor_standing_page_row_pts);

                            positionTextView.setText(constructorStandings.get(i).getPosition());
                            teamTextView.setText(constructorStandings.get(i).getTeam());
                            pointsTextView.setText(constructorStandings.get(i).getPoints());

                            // Assign a unique ID to the FrameLayout for each row
                            FrameLayout fragmentContainer = row.findViewById(R.id.constructor_standing_small_profile_fragment_container);
                            fragmentContainer.setId(View.generateViewId()); // Ensure a unique ID

                            // Set row colors based on position
                            setRowColor(positionTextView, teamTextView, pointsTextView, i);

                            // Add row to container layout
                            containerLayout.addView(row);

                            // Add click listener to toggle fragment
                            row.setOnClickListener(v -> toggleFragment(fragmentContainer));
                        }

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
}
