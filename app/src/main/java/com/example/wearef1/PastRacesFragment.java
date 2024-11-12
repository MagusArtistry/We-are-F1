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

import java.util.ArrayList;
import java.util.List;

public class PastRacesFragment extends Fragment {
    private static final String ARG_YEAR = "year";
    private String year;
    private RecyclerView recyclerView;
    private RaceAdapter adapter;

    public static PastRacesFragment newInstance(String year) {
        PastRacesFragment fragment = new PastRacesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            year = getArguments().getString(ARG_YEAR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_races, container, false);

        // Use the `year` argument to filter or display data as needed
        // For example, fetch races for the selected year and display them

        recyclerView = view.findViewById(R.id.recyclerViewPast);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch data based on the selected year
        List<String> pastRacesList = getRacesForYear(year);
        Log.d("PastRacesFragment", "Number of races: " + pastRacesList.size());


        // Set up adapter and RecyclerView
        adapter = new RaceAdapter(pastRacesList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    private List<String> getRacesForYear(String selectedYear) {
        List<String> pastRaces = new ArrayList<>();

        // Sample data for demonstration
//        pastRaces.add(" HEINEKEN SILVE LAS VEGAS\nGRAND PRIX");
//        pastRaces.add("QATAR AIRWAYS QATAR\nGRAND PRIX");
//        pastRaces.add("ETIHAD AIRWAYS ABU DHABI\nGRAND PRIX");

        pastRaces.add("Past Race 1");
        pastRaces.add("Past Race 2");
        pastRaces.add("Past Race 3");

        return pastRaces;
    }
}