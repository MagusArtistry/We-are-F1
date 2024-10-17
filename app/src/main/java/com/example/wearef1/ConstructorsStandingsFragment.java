    package com.example.wearef1;

    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

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

        private RecyclerView constructorRecyclerView;
        private ConstructorsStandingsSmallProfileAdapter constructorAdapter;

        private List<ConstructorStanding> constructorStandings = new ArrayList<>();

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_constructors_standings, container, false);

            constructorRecyclerView = view.findViewById(R.id.constructors_standing_page_recycler_view);
            constructorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            constructorAdapter = new ConstructorsStandingsSmallProfileAdapter(constructorStandings, getChildFragmentManager());
            constructorRecyclerView.setAdapter(constructorAdapter);

            fetchConstructorStandings();

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

                            for (int i = 0; i < standings.length(); i++) {
                                JSONObject standing = standings.getJSONObject(i);
                                String name = standing.getJSONObject("Constructor").getString("name");
                                String points = standing.getString("points");
                                constructorStandings.add(new ConstructorStanding(Integer.toString(i + 1), name, points));
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

    }