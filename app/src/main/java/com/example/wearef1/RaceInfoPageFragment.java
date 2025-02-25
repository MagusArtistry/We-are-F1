package com.example.wearef1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class RaceInfoPageFragment extends Fragment {
    private static final String ARG_RACE_NUMBER = "1";
    private static final String ARG_RACE_YEAR = "2024";
    private static final String ARG_RACE_TYPE = "Race";
    private String race_year;
    private String race_number;
    private String race_type;
    private String race_name;
    private String race_date;
    private TextView race_name_tv;
    private TextView race_type_tv;
    private TextView race_date_tv;
    private LinearLayout containerLayout;
    private FragmentManager fragmentManager;

    public RaceInfoPageFragment() {
    }
    public static RaceInfoPageFragment newInstance(String param1, String param2, String param3) {
        RaceInfoPageFragment fragment = new RaceInfoPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RACE_NUMBER, param1);
        args.putString(ARG_RACE_YEAR, param2);
        args.putString(ARG_RACE_TYPE, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            race_number = getArguments().getString(ARG_RACE_NUMBER);
            race_year = getArguments().getString(ARG_RACE_YEAR);
            race_type = getArguments().getString(ARG_RACE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_race_info_page, container, false);
        race_name_tv = view.findViewById(R.id.race_page_race_name);
        race_date_tv = view.findViewById(R.id.race_page_race_date);
        race_type_tv = view.findViewById(R.id.race_page_race_type);

        containerLayout = view.findViewById(R.id.race_result_list_container);
        fragmentManager = getChildFragmentManager();

        Toast.makeText(getContext(), race_type, Toast.LENGTH_SHORT).show();

        if(race_type.equals("Race")){
            Log.d("Race: ", race_type);
            fetchRaceDataAsync(race_year, race_number);
        }
        else{
            Log.d("Qualifying: ", race_type);
            fetchQualifyingDataAsync(race_year, race_number);
        }

        return view;
    }

    private void fetchQualifyingDataAsync(String raceYear, String raceNumber) {
        new FetchQualifyingDataTask().execute(raceYear, raceNumber);
    }

    private class FetchQualifyingDataTask extends AsyncTask<String, Void, List<QualifyingDetails>> {
        @Override
        protected List<QualifyingDetails> doInBackground(String... params) {
            String raceYear = params[0];
            String raceNumber = params[1];
            OkHttpClient client = new OkHttpClient();
            String url = "https://ergast.com/api/f1/" + raceYear + "/" + raceNumber + "/qualifying.json";
            List<QualifyingDetails> qualifyingData = new ArrayList<>();

            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                    JsonObject raceTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("RaceTable");
                    JsonArray races = raceTable.getAsJsonArray("Races");

                    for (JsonElement raceElement : races) {
                        JsonObject race = raceElement.getAsJsonObject();
                        String raceName = race.get("raceName").getAsString();
                        String date = race.get("date").getAsString();
                        String time = race.has("time") ? race.get("time").getAsString() : "-";

                        race_date = DateTimeParser.convertToLocalTime(date + "T" + time);
                        race_date = race_date.replace(' ', '\n');
                        race_name = raceName;
                        int position = 1;

                        JsonArray qualifyingResults = race.getAsJsonArray("QualifyingResults");
                        for (JsonElement resultElement : qualifyingResults) {
                            JsonObject result = resultElement.getAsJsonObject();
                            String driverName = result.getAsJsonObject("Driver").get("givenName").getAsString() +
                                    " " + result.getAsJsonObject("Driver").get("familyName").getAsString();
                            String q1 = result.has("Q1") ? result.get("Q1").getAsString() : "-";
                            String q2 = result.has("Q2") ? result.get("Q2").getAsString() : "-";
                            String q3 = result.has("Q3") ? result.get("Q3").getAsString() : "-";

                            qualifyingData.add(new QualifyingDetails(driverName, q1, q2, q3, position));
                            position++;
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("QualifyingData", "Network request failed", e);
            }
            return qualifyingData;
        }

        @Override
        protected void onPostExecute(List<QualifyingDetails> qualifyingData) {
            createListQualifying(qualifyingData);
        }
    }

    private void createListQualifying(List<QualifyingDetails> qualifyingData){

        containerLayout.removeAllViews();
        for(QualifyingDetails qualifier: qualifyingData){
            View row = LayoutInflater.from(getContext()).inflate(R.layout.qualifying_list_row, containerLayout, false);
            TextView race_page_driver_name = row.findViewById(R.id.race_page_driver_name);
            race_page_driver_name.setText(qualifier.driverName);

            TextView race_page_q1_time = row.findViewById(R.id.race_page_q1_time);
            race_page_q1_time.setText(qualifier.q1_time);

            TextView race_page_q2_time = row.findViewById(R.id.race_page_q2_time);
            race_page_q2_time.setText(qualifier.q2_time);

            TextView race_page_q3_time = row.findViewById(R.id.race_page_q3_time);
            race_page_q3_time.setText(qualifier.q3_time);

            containerLayout.addView(row);
        }

        race_name_tv.setText(race_name);
        race_type_tv.setText("Qualifying");
        race_date_tv.setText(race_date);
    }

    private class QualifyingDetails{
        public String driverName;
        public String q1_time;
        public String q2_time;
        public String q3_time;
        public int position;

        public QualifyingDetails(){

        }

        public QualifyingDetails(String driverName, String q1_time, String q2_time, String q3_time, int position){
            this.q1_time = q1_time;
            this.q2_time = q2_time;
            this.q3_time = q3_time;
            this.position = position;
            this.driverName = driverName.replace(' ', '\n');
        }
    }

    private void fetchRaceDataAsync(String raceYear, String raceNumber) {
        new FetchRaceDataTask().execute(raceYear, raceNumber);
    }

    private class FetchRaceDataTask extends AsyncTask<String, Void, List<RaceDetails>> {
        @Override
        protected List<RaceDetails> doInBackground(String... params) {
            String raceYear = params[0];
            String raceNumber = params[1];
            OkHttpClient client = new OkHttpClient();
            String url = "https://ergast.com/api/f1/" + raceYear + "/" + raceNumber + "/results.json";
            List<RaceDetails> qualifyingData = new ArrayList<>();

            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                    JsonObject raceTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("RaceTable");
                    JsonArray races = raceTable.getAsJsonArray("Races");

                    for (JsonElement raceElement : races) {
                        JsonObject race = raceElement.getAsJsonObject();
                        String raceName = race.get("raceName").getAsString();
                        String date = race.get("date").getAsString();
                        String time = race.has("time") ? race.get("time").getAsString() : "-";

                        race_date = DateTimeParser.convertToLocalTime(date + "T" + time);
                        race_date = race_date.replace(' ', '\n');
                        race_name = raceName;
                        int position = 1;

                        JsonArray qualifyingResults = race.getAsJsonArray("Results");
                        for (JsonElement resultElement : qualifyingResults) {
                            JsonObject result = resultElement.getAsJsonObject();
                            String driverName = result.getAsJsonObject("Driver").get("givenName").getAsString() +
                                    " " + result.getAsJsonObject("Driver").get("familyName").getAsString();

                            // Check if the "Time" object exists before accessing its value
                            String raceTime = "-";  // Default value if Time object is missing
                            if (result.has("Time") && result.getAsJsonObject("Time").has("time")) {
                                raceTime = result.getAsJsonObject("Time").get("time").getAsString();
                            }

                            String points = result.has("points") ? result.get("points").getAsString() : "-";
                            String laps = result.has("laps") ? result.get("laps").getAsString() : "-";

                            qualifyingData.add(new RaceDetails(driverName, raceTime, points, laps, position));
                            position++;
                        }

                    }
                }
            } catch (IOException e) {
                Log.e("QualifyingData", "Network request failed", e);
            }
            return qualifyingData;
        }

        @Override
        protected void onPostExecute(List<RaceDetails> qualifyingData) {
            createListRace(qualifyingData);
        }
    }

    private void createListRace(List<RaceDetails> raceData){

        containerLayout.removeAllViews();
        for(RaceDetails qualifier: raceData){
            View row = LayoutInflater.from(getContext()).inflate(R.layout.race_list_row, containerLayout, false);
            TextView race_page_driver_name = row.findViewById(R.id.race_page_driver_name);
            race_page_driver_name.setText(qualifier.driverName);

            TextView race_page_q1_time = row.findViewById(R.id.race_page_race_time);
            race_page_q1_time.setText(qualifier.raceTime);

            TextView race_page_q2_time = row.findViewById(R.id.race_page_points_count);
            race_page_q2_time.setText(qualifier.points);

            TextView race_page_q3_time = row.findViewById(R.id.race_page_lap_count);
            race_page_q3_time.setText(qualifier.laps);

            containerLayout.addView(row);
        }

        race_name_tv.setText(race_name);
        race_type_tv.setText("Race");
        race_date_tv.setText(race_date);
    }

    private class RaceDetails{
        public String driverName;
        public String raceTime;
        public String points;
        public String laps;
        public int position;

        public RaceDetails(){

        }

        public RaceDetails(String driverName, String raceTime, String points, String laps, int position){
            this.raceTime = raceTime;
            this.points = points;
            this.laps = laps;
            this.position = position;
            this.driverName = driverName.replace(' ', '\n');
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Override the back press behavior in the fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle back press here and navigate to ConstructorStandingsFragment
                CalenderFragment calenderFragment = new CalenderFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment_container, calenderFragment);
                transaction.commit();
            }
        });
    }
}