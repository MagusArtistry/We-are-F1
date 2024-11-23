package com.example.wearef1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RaceInfoSmallFragment extends Fragment {

    private static final String ARG_RACE_NUMBER = "1";
    private static final String ARG_RACE_YEAR = "2024";

    // TODO: Rename and change types of parameters
    private String raceNumber;
    private String raceYear;
    private TextView raceDate;
    private TextView raceMonth;
    private TextView qualifyingDate;
    private TextView qualifyingMonth;
    private TextView practice3Date;
    private TextView practice3Month;
    private TextView practice2Date;
    private TextView practice2Month;
    private TextView practice1Date;
    private TextView practice1Month;
    private TextView raceTitle;
    private TextView qualifyingTitle;
    private TextView practice3Title;
    private TextView practice2Title;
    private TextView practice1Title;

    public RaceInfoSmallFragment() {
    }

    public static RaceInfoSmallFragment newInstance(String raceNumber, String raceYear) {
        RaceInfoSmallFragment fragment = new RaceInfoSmallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RACE_NUMBER, raceNumber);
        args.putString(ARG_RACE_YEAR, raceYear);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.raceNumber = getArguments().getString(ARG_RACE_NUMBER);
            this.raceYear = getArguments().getString(ARG_RACE_YEAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_race_info_small, container, false);

        raceDate = view.findViewById(R.id.raceDate_small);
        raceMonth = view.findViewById(R.id.raceMonth_small);
        qualifyingDate = view.findViewById(R.id.qualifyingDate_small);
        qualifyingMonth = view.findViewById(R.id.qualifyingMonth_small);
        practice3Date = view.findViewById(R.id.practice3Date_small);
        practice3Month = view.findViewById(R.id.practice3Month_small);
        practice2Date = view.findViewById(R.id.practice2Date_small);
        practice2Month = view.findViewById(R.id.practice2Month_small);
        practice1Date = view.findViewById(R.id.practice1Date_small);
        practice1Month = view.findViewById(R.id.practice1Month_small);
        raceTitle = view.findViewById(R.id.raceTitle_small);
        qualifyingTitle = view.findViewById(R.id.qualifyingName_small);
        practice3Title = view.findViewById(R.id.practice3Name_small);
        practice2Title = view.findViewById(R.id.practice2Name_small);
        practice1Title = view.findViewById(R.id.practice1Name_small);

        fetchRaceData();

        LinearLayout raceLayout = view.findViewById(R.id.race_clickable);
        raceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout qualifyingLayout = view.findViewById(R.id.qualifying_clickable);
        qualifyingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); // Use parent activity's FragmentManager
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                RaceInfoPageFragment fragment = RaceInfoPageFragment.newInstance(raceNumber, raceYear, "Qualifying");
                fragmentTransaction.replace(R.id.main_fragment_container, fragment); // Ensure main_fragment_container exists in the activity
                fragmentTransaction.addToBackStack(null); // Optional: allows navigating back
                fragmentTransaction.commit();
            }
        });


        LinearLayout practice3Layout = view.findViewById(R.id.practice3_clickable);
        practice3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout practice2Layout = view.findViewById(R.id.practice2_clickable);
        practice2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout practice1Layout = view.findViewById(R.id.practice1_clickable);
        practice1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void fetchRaceData() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://ergast.com/api/f1/" + raceYear + "/" + raceNumber + ".json";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonObject raceData = json.getAsJsonObject("MRData").getAsJsonObject("RaceTable").getAsJsonArray("Races").get(0).getAsJsonObject();

                    String raceDateStr = raceData.get("date").getAsString();
                    String qualifyingDateStr = raceData.getAsJsonObject("Qualifying").get("date").getAsString();
                    String practice1DateStr = raceData.getAsJsonObject("FirstPractice").get("date").getAsString();
                    String practice2DateStr = raceData.getAsJsonObject("SecondPractice").get("date").getAsString();
                    String practice3DateStr = raceData.getAsJsonObject("ThirdPractice").get("date").getAsString();

                    String raceTimeStr = raceData.get("time").getAsString();
                    String qualifyingTimeStr = raceData.getAsJsonObject("Qualifying").get("time").getAsString();
                    String practice3TimeStr = raceData.getAsJsonObject("ThirdPractice").get("time").getAsString();
                    String practice2TimeStr = raceData.getAsJsonObject("SecondPractice").get("time").getAsString();
                    String practice1TimeStr = raceData.getAsJsonObject("FirstPractice").get("time").getAsString();

                    String raceCorrectDateTime = DateTimeParser.convertToLocalTime(raceDateStr + "T" + raceTimeStr);
                    String qualifyingCorrectDateTime = DateTimeParser.convertToLocalTime(qualifyingDateStr + "T" + qualifyingTimeStr);
                    String practice3CorrectDateTime = DateTimeParser.convertToLocalTime(practice3DateStr + "T" + practice3TimeStr);
                    String practice2CorrectDateTime = DateTimeParser.convertToLocalTime(practice2DateStr + "T" + practice2TimeStr);
                    String practice1CorrectDateTime = DateTimeParser.convertToLocalTime(practice1DateStr + "T" + practice1TimeStr);

                    // Update UI on the main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        raceTitle.setText("Race | " + raceCorrectDateTime.split(" ")[1]);
                        raceDate.setText(raceCorrectDateTime.split(" ")[0].split("-")[2]);
                        raceMonth.setText(getMonth(Integer.parseInt(raceCorrectDateTime.split(" ")[0].split("-")[1])));

                        qualifyingTitle.setText("Race | " + qualifyingCorrectDateTime.split(" ")[1]);
                        qualifyingDate.setText(qualifyingCorrectDateTime.split(" ")[0].split("-")[2]);
                        qualifyingMonth.setText(getMonth(Integer.parseInt(qualifyingCorrectDateTime.split(" ")[0].split("-")[1])));

                        practice3Title.setText("Race | " + practice3CorrectDateTime.split(" ")[1]);
                        practice3Date.setText(practice3CorrectDateTime.split(" ")[0].split("-")[2]);
                        practice3Month.setText(getMonth(Integer.parseInt(practice3CorrectDateTime.split(" ")[0].split("-")[1])));

                        practice2Title.setText("Race | " + practice2CorrectDateTime.split(" ")[1]);
                        practice2Date.setText(practice2CorrectDateTime.split(" ")[0].split("-")[2]);
                        practice2Month.setText(getMonth(Integer.parseInt(practice2CorrectDateTime.split(" ")[0].split("-")[1])));

                        practice1Title.setText("Race | " + practice1CorrectDateTime.split(" ")[1]);
                        practice1Date.setText(practice1CorrectDateTime.split(" ")[0].split("-")[2]);
                        practice1Month.setText(getMonth(Integer.parseInt(practice1CorrectDateTime.split(" ")[0].split("-")[1])));
                    });
                }
            }
        });
    }

    private String getMonth(int m){
        switch (m) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }
}