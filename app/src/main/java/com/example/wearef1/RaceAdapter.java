package com.example.wearef1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class RaceAdapter extends ArrayAdapter<String> {

    private final List<String> raceList;
    private final LayoutInflater inflater;

    public RaceAdapter(@NonNull Context context, List<String> raceList) {
        super(context, R.layout.race_item, raceList);
        this.raceList = raceList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.race_item, parent, false);
            holder = new ViewHolder();
            holder.raceName = convertView.findViewById(R.id.raceName);
            holder.raceIcon = convertView.findViewById(R.id.raceIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the race name
        String race = raceList.get(position);
        holder.raceName.setText(race);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Optionally set an image or other properties
        // holder.raceIcon.setImageResource(R.drawable.some_icon); // Set an icon if needed

        return convertView;
    }

    static class ViewHolder {
        TextView raceName;
        ImageView raceIcon;
    }
}
