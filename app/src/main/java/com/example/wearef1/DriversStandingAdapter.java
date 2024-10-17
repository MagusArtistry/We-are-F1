package com.example.wearef1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriversStandingAdapter extends RecyclerView.Adapter<DriversStandingAdapter.ViewHolder>{

    private List<DriversStanding> DriverStandings;
    public DriversStandingAdapter(List<DriversStanding> driverStandings) {
        this.DriverStandings = driverStandings;
    }
    @NonNull
    @Override
    public DriversStandingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drivers_standing_row, parent, false);
        return new DriversStandingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversStandingAdapter.ViewHolder holder, int position) {
        DriversStanding standing = DriverStandings.get(position);
        holder.positionTextView.setText(standing.getPosition());
        holder.driverTextView.setText(standing.getDriver());
        holder.pointsTextView.setText(standing.getPoints());

        switch (position) {
            case 0:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.gold));
                holder.driverTextView.setTextColor(holder.itemView.getContext().getColor(R.color.gold));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.gold));
                break;
            case 1:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.bronze));
                holder.driverTextView.setTextColor(holder.itemView.getContext().getColor(R.color.bronze));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.bronze));
                break;
            case 2:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.silver));
                holder.driverTextView.setTextColor(holder.itemView.getContext().getColor(R.color.silver));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.silver));
                break;
            default:
                holder.positionTextView.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
                holder.driverTextView.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
                holder.pointsTextView.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return DriverStandings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView positionTextView;
        public TextView driverTextView;
        public TextView pointsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.drivers_standing_row_pos);
            driverTextView = itemView.findViewById(R.id.drivers_standing_row_team);
            pointsTextView = itemView.findViewById(R.id.drivers_standing_row_pts);
        }
    }
}
