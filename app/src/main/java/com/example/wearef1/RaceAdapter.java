package com.example.wearef1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RaceAdapter extends RecyclerView.Adapter<RaceAdapter.RaceViewHolder> {

    private final List<String> raceList;

    public RaceAdapter(List<String> raceList) {
        this.raceList = raceList;
    }

    @NonNull
    @Override
    public RaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.race_item, parent, false);
        return new RaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RaceViewHolder holder, int position) {
        String race = raceList.get(position);
        holder.raceName.setText(race);
    }

    @Override
    public int getItemCount() {
        Log.d("RaceAdapter", "Item count: " + (raceList != null ? raceList.size() : 0));
        return raceList != null ? raceList.size() : 0;
    }


    public class RaceViewHolder extends RecyclerView.ViewHolder {
        TextView raceName;
        ImageView raceIcon;

        RaceViewHolder(@NonNull View itemView) {
            super(itemView);
            raceName = itemView.findViewById(R.id.raceName);
            raceIcon = itemView.findViewById(R.id.raceIcon);
        }
    }
}