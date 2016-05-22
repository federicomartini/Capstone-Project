package app.com.ttins.gettogether.eventguestremove.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.eventguestremove.EventGuestRemoveView;

public class EventGuestRemoveRecyclerViewAdapter extends RecyclerView.Adapter<EventGuestRemoveView.ViewHolder> {

    private static final String LOG_TAG = EventGuestRemoveRecyclerViewAdapter.class.getSimpleName();

    OnItemClickListener listener;
    Guests guests;

    public EventGuestRemoveRecyclerViewAdapter(Guests guests, OnItemClickListener listener) {
        this.guests = guests;
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount = " + guests.getGuests().size());
        return guests.getGuests().size();
    }

    @Override
    public EventGuestRemoveView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_guest_remove_view, null, false);
        EventGuestRemoveView.ViewHolder viewHolder = new EventGuestRemoveView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventGuestRemoveView.ViewHolder holder, int position) {

    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }
}
