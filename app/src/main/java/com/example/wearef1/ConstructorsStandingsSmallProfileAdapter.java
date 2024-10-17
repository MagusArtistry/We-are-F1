package com.example.wearef1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConstructorsStandingsSmallProfileAdapter extends RecyclerView.Adapter<ConstructorsStandingsSmallProfileAdapter.ViewHolder> {

    private List<ConstructorStanding> constructorStandings;
    private FragmentManager fragmentManager;

    public ConstructorsStandingsSmallProfileAdapter(List<ConstructorStanding> constructorStandings, FragmentManager fragmentManager) {
        this.constructorStandings = constructorStandings;
        this.fragmentManager = fragmentManager; // Initialize FragmentManager
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructor_standing_page_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConstructorStanding standing = constructorStandings.get(position);
        holder.positionTextView.setText(standing.getPosition());
        holder.teamTextView.setText(standing.getTeam());
        holder.pointsTextView.setText(standing.getPoints());

        // Set colors based on position
        switch (position) {
            case 0:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.gold));
                holder.teamTextView.setTextColor(holder.itemView.getContext().getColor(R.color.gold));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.gold));
                break;
            case 1:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.bronze));
                holder.teamTextView.setTextColor(holder.itemView.getContext().getColor(R.color.bronze));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.bronze));
                break;
            case 2:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.silver));
                holder.teamTextView.setTextColor(holder.itemView.getContext().getColor(R.color.silver));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.silver));
                break;
            default:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
                holder.teamTextView.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
                break;
        }

        // Assign a unique ID to the FrameLayout if it doesn't already have one
        if (holder.fragmentContainer.getId() == View.NO_ID) {
            int uniqueId = View.generateViewId();
            holder.fragmentContainer.setId(uniqueId);
        }

        // Set up the toggle behavior
        holder.itemView.setOnClickListener(v -> toggleFragment(holder, position));
    }




    private boolean isFragmentVisible = false; // Flag to check if the fragment is currently visible

    private void toggleFragment(ViewHolder holder, int position) {
        FrameLayout fragmentContainer = holder.fragmentContainer;
        Fragment fragment = fragmentManager.findFragmentById(fragmentContainer.getId());

        if (fragment != null) {
            // Remove the fragment if it's already displayed
            fragmentManager.beginTransaction().remove(fragment).commit();
        } else {
            // Add a new fragment
            ConstructorStandingPageSmallProfileFragment fragmentToDisplay = new ConstructorStandingPageSmallProfileFragment();
            fragmentManager.beginTransaction()
                    .replace(fragmentContainer.getId(), fragmentToDisplay)
                    .commit();
        }
    }




    @Override
    public int getItemCount() {
        return constructorStandings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView positionTextView;
        public TextView teamTextView;
        public TextView pointsTextView;
        public FrameLayout fragmentContainer; // Add this

        public ViewHolder(View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.constructor_standing_page_row_pos);
            teamTextView = itemView.findViewById(R.id.constructor_standing_page_row_team);
            pointsTextView = itemView.findViewById(R.id.constructor_standing_page_row_pts);
            fragmentContainer = itemView.findViewById(R.id.constructor_standing_small_profile_fragment_container);
        }
    }

}
