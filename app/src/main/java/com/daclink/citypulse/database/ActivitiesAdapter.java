package com.daclink.citypulse.database;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.R;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>{
    private final List<Activities> activities;

    private String city;
    private String category;

    public ActivitiesAdapter(List<Activities> activities, String city, String category) {
        this.activities = activities;
        this.city = city;
        this.category = category;
    }
    public ActivitiesAdapter(List<Activities> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Activities activity = activities.get(position);
        holder.titleTextView.setText(activity.getTitle());
        holder.venueTextView.setText(activity.getVenue());
        holder.dateTextView.setText(activity.getDates());

        holder.btnWishlist.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() { //
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, venueTextView, dateTextView;

        ImageButton btnWishlist;

        ActivityViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.eventTitle);
            venueTextView = itemView.findViewById(R.id.eventLocation);
            dateTextView = itemView.findViewById(R.id.eventDate);
            btnWishlist = itemView.findViewById(R.id.btnWishlist);
        }
    }
}
