package com.example.wearef1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class CalenderFragment extends Fragment {
    private String selectedYear = "2024";
    private ViewPager2 viewPager;

    public CalenderFragment() {
    }

    public static CalenderFragment newInstance() {
        return new CalenderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        // Initialize the Spinner
        Spinner yearSpinner = view.findViewById(R.id.yearSpinner);
        String[] years = {"2022", "2023", "2024", "2025", "2026"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_calender, years);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_calender);
        yearSpinner.setAdapter(adapter);

        // Handle year selection
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Selected year: " + selectedYear, Toast.LENGTH_SHORT).show();
                setupViewPager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Initialize TabLayout and ViewPager
        TabLayout tabLayout = view.findViewById(R.id.calenderTabLayout);
        viewPager = view.findViewById(R.id.calenderViewPager);

        setupViewPager();

        // Connect TabLayout and ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Upcoming Races");
            } else {
                tab.setText("Past Races");
            }
        }).attach();

        return view;
    }

    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(UpcomingRacesFragment.newInstance(selectedYear));
        fragments.add(PastRacesFragment.newInstance(selectedYear));

        CalenderViewPagerAdapter adapter = new CalenderViewPagerAdapter(this, fragments);
        viewPager.setAdapter(adapter);
    }
}
