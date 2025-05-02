package com.daclink.citypulse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.database.Activities;
import com.daclink.citypulse.database.ActivitiesDAO;
import com.daclink.citypulse.model.CachedEvent;
import com.daclink.citypulse.model.EventItem;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.EventViewHolder> {

    private final List<EventItem> events;

    private String city;
    private String category;

    public EventItemAdapter(List<EventItem> events, String city, String category) {
        this.events = events;
        this.city = city;
        this.category = category;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EventItem event = events.get(position);
        holder.titleTextView.setText(event.getName());
        holder.venueTextView.setText(event.getVenueName());
        holder.dateTextView.setText(event.getLocalDate());

        if (event.isWishlist()){
            holder.btnWishlist.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else {
            holder.btnWishlist.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean currentState = event.isWishlist();
                if (!currentState){
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(v.getContext());
                        db.activitiesDAO().insert(fromEventItem(event, city, category));
                    });
                } else{
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(v.getContext());
                        db.activitiesDAO().deleteEvent(event.getId());
                    });
                }
                event.setWishlist(!currentState);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() { //
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, venueTextView, dateTextView;

        ImageButton btnWishlist;

        EventViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.eventTitle);
            venueTextView = itemView.findViewById(R.id.eventLocation);
            dateTextView = itemView.findViewById(R.id.eventDate);
            btnWishlist = itemView.findViewById(R.id.btnWishlist);
        }
    }
    private Activities fromEventItem(EventItem e, String city, String category) {
        return new Activities(
                e.getId() != null ? e.getId() : "",
                e.getName() != null ? e.getName() : "Untitled",
                e.getLocalDate(),
                e.getVenueName(),
                city,
                category,
                e.getImageUrl(),
                false
        );
    }
}
