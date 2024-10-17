package com.example.wearef1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConstructorsStandingAdapter extends RecyclerView.Adapter<ConstructorsStandingAdapter.ViewHolder> {

    private List<ConstructorStanding> constructorStandings;

    public ConstructorsStandingAdapter(List<ConstructorStanding> constructorStandings) {
        this.constructorStandings = constructorStandings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructor_standing_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConstructorStanding standing = constructorStandings.get(position);
        holder.positionTextView.setText(standing.getPosition());
        holder.teamTextView.setText(standing.getTeam());
        holder.pointsTextView.setText(standing.getPoints());

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

    }

    @Override
    public int getItemCount() {
        return constructorStandings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView positionTextView;
        public TextView teamTextView;
        public TextView pointsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.constructor_standing_row_pos);
            teamTextView = itemView.findViewById(R.id.constructor_standing_row_team);
            pointsTextView = itemView.findViewById(R.id.constructor_standing_row_pts);
        }
    }
}
