package com.example.wearef1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalenderFragment extends Fragment {
    private String selectedYear = "2024";
    private boolean selectedUpcoming = true;
    private Button upcomingButton;
    private Button pastButton;
    private List<RaceInfo> raceList;
    private HashMap<String, Integer> raceHashMap;
    private List<String> raceNames;
    LinearLayout containerLayout;
    private FragmentManager fragmentManager;

    public CalenderFragment() {
    }

    public static CalenderFragment newInstance() {
        return new CalenderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        containerLayout = view.findViewById(R.id.calender_container);
        fragmentManager = getChildFragmentManager();

        // Initialize the Spinner
        Spinner yearSpinner = view.findViewById(R.id.yearSpinner);
        String[] years = {"2022", "2023", "2024"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_calender, years);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_calender);
        yearSpinner.setAdapter(adapter);

        // Handle year selection
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Selected year: " + selectedYear, Toast.LENGTH_SHORT).show();
                selectedUpcoming = true;
                createNewFragmentNewYear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        upcomingButton = view.findViewById(R.id.upcomingRacesButton);
        upcomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getParentFragmentManager();
//
//                // Check if the UpcomingRacesFragment already exists
//                Fragment existingFragment = fragmentManager.findFragmentByTag("UpcomingRacesFragment");
//
//                if (existingFragment == null || !existingFragment.isAdded()) {
//                    // Fragment doesn't exist or is not added, so create a new one
//                    UpcomingRacesFragment fragment = UpcomingRacesFragment.newInstance(selectedYear);
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.calender_container, fragment, "UpcomingRacesFragment");
//                    fragmentTransaction.commit();
//                }
                if(!selectedUpcoming){
                    selectedUpcoming = true;
                    createNewFragmentNewYear();
                }
            }
        });

        pastButton = view.findViewById(R.id.pastRacesButton);
        pastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getParentFragmentManager();
//
//                // Check if the PastRacesFragment already exists
//                Fragment existingFragment = fragmentManager.findFragmentByTag("PastRacesFragment");
//
//                if (existingFragment == null || !existingFragment.isAdded()) {
//                    // Fragment doesn't exist or is not added, so create a new one
//                    PastRacesFragment fragment = PastRacesFragment.newInstance(selectedYear);
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.calender_container, fragment, "PastRacesFragment");
//                    fragmentTransaction.commit();
//                }
                if(selectedUpcoming){
                    selectedUpcoming = false;
                    createNewFragment();
                }
            }
        });

        createNewFragmentNewYear();

        return view;
    }

    public void createNewFragmentNewYear() {
        raceList = new ArrayList<RaceInfo>();
        raceNames = new ArrayList<String>();
        raceHashMap = new HashMap<>();
        fetchRaceDataAsync();
    }

    public void createNewFragment(){
        raceNames = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        containerLayout.removeAllViews();

        if(selectedUpcoming){
            int counter = 0;
            for(RaceInfo races: raceList){
                String raceDateString = races.getRaceDate();
                try {
                    Date raceDate = sdf.parse(raceDateString);
                    if(raceDate.after(currentDate)){
                        String raceName = races.getRaceName();
                        raceNames.add(raceName);
                        View row = LayoutInflater.from(getContext()).inflate(R.layout.race_item, containerLayout, false);

                        TextView raceNameTextView = row.findViewById(R.id.raceName);
                        raceNameTextView.setText(raceName);
                        //get Image view and set the image

                        FrameLayout fragmentContainer = row.findViewById(R.id.race_small_info);
                        fragmentContainer.setId(View.generateViewId());

                        containerLayout.addView(row);

                        final int position = counter;
                        counter++;

                        // Add click listener to toggle fragment
                        row.setOnClickListener(v -> toggleFragment(fragmentContainer, position));
                    }
                }
                catch (Exception e){
                    Log.e("Date Error", "NULL", e);
                }
            }
        }
        else{
            int counter = 0;
            for(RaceInfo races: raceList){
                String raceDateString = races.getRaceDate();
                try {
                    Date raceDate = sdf.parse(raceDateString);
                    if(raceDate.before(currentDate) || raceDate.equals(currentDate)){
                        String raceName = races.getRaceName();
                        raceNames.add(raceName);
                        View row = LayoutInflater.from(getContext()).inflate(R.layout.race_item, containerLayout, false);

                        TextView raceNameTextView = row.findViewById(R.id.raceName);
                        raceNameTextView.setText(raceName);
                        //get Image view and set the image

                        FrameLayout fragmentContainer = row.findViewById(R.id.race_small_info);
                        fragmentContainer.setId(View.generateViewId());

                        containerLayout.addView(row);

                        final int position = counter;
                        counter++;

                        // Add click listener to toggle fragment
                        row.setOnClickListener(v -> toggleFragment(fragmentContainer, position));
                    }
                }
                catch (Exception e){
                    Log.e("Date Error", "NULL", e);
                }
            }
        }
    }

    private void toggleFragment(FrameLayout fragmentContainer, int position) {
        Fragment fragment = fragmentManager.findFragmentById(fragmentContainer.getId());

        if (fragment != null) {
            // Remove the fragment if it's already displayed
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragmentContainer.setVisibility(View.GONE);
        } else {
            // Add a new fragment
            String raceName = raceNames.get(position);
            int raceNumber = raceHashMap.get(raceName);
            Log.d("Race Number: ", String.valueOf(raceNumber));
            Log.d("Race Number: ", selectedYear);
            RaceInfoSmallFragment fragmentToDisplay = RaceInfoSmallFragment.newInstance(String.valueOf(raceNumber), selectedYear);
            fragmentManager.beginTransaction()
                    .replace(fragmentContainer.getId(), fragmentToDisplay)
                    .commit();
            fragmentContainer.setVisibility(View.VISIBLE);
        }
    }

        private void fetchRaceDataAsync() {
        new FetchRaceDataTask().execute(selectedYear);
    }

    private class FetchRaceDataTask extends AsyncTask<String, Void, List<RaceInfo>> {
        @Override
        protected List<RaceInfo> doInBackground(String... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            String year = params[0];
            List<RaceInfo> result = null;
            OkHttpClient client = new OkHttpClient();
            String url = "https://ergast.com/api/f1/" + selectedYear + ".json";

            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                    JsonObject raceTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("RaceTable");
                    Type raceListType = new TypeToken<List<JsonObject>>() {}.getType();
                    List<JsonObject> races = new Gson().fromJson(raceTable.get("Races"), raceListType);
                    int count = 1;
                    int counter = 0;
                    for (JsonObject race : races) {
                        String raceName = race.get("raceName").getAsString();
                        String date = race.getAsJsonObject("FirstPractice").get("date").getAsString();
                        raceList.add(new RaceInfo(count, raceName, date));
                        raceHashMap.put(raceName, count);
                        count++;
                    }
                }
            } catch (IOException e) {
                Log.e("CalenderFragment", "Network request failed", e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<RaceInfo> result) {
            createNewFragment();
        }
    }

//    private void fetchRaceData(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date currentDate = new Date();
//        String url = "https://ergast.com/api/f1/" + selectedYear + ".json";
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                String jsonResponse = response.body().string();
//                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
//                JsonObject raceTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("RaceTable");
//                Type raceListType = new TypeToken<List<JsonObject>>() {}.getType();
//                List<JsonObject> races = new Gson().fromJson(raceTable.get("Races"), raceListType);
//                int count = 1;
//                int counter = 0;
//                for (JsonObject race : races) {
//                    String raceName = race.get("raceName").getAsString();
//                    String date = race.getAsJsonObject("FirstPractice").get("date").getAsString();
//                    raceList.add(new RaceInfo(count, raceName, date));
//                    raceHashMap.put(raceName, count);
//
//                    try {
//                        Date raceDate = sdf.parse(date);
//
//                        // Check if the race date is before the current date
//                        if (raceDate.after(currentDate)) {
//                            raceNames.add(raceName);
//
//                            View row = LayoutInflater.from(getContext()).inflate(R.layout.race_item, containerLayout, false);
//
//                            TextView raceNameTextView = row.findViewById(R.id.raceName);
//                            raceNameTextView.setText(raceName);
//                            //get Image view and set the image
//
//                            FrameLayout fragmentContainer = row.findViewById(R.id.race_small_info);
//                            fragmentContainer.setId(View.generateViewId());
//
//                            containerLayout.addView(row);
//
//                            final int position = counter;
//                            counter++;
//
//                            // Add click listener to toggle fragment
//                            row.setOnClickListener(v -> toggleFragment(fragmentContainer, position));
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace(); // Handle parsing errors
//                    }
//                    count++;
//                }
//                Log.d("Data Loaded: ", String.valueOf(count));
//            }
//        } catch (IOException e) {
//            Log.e("UpcomingRacesFragment", "Error fetching race data", e);
//        }
//    }

}
