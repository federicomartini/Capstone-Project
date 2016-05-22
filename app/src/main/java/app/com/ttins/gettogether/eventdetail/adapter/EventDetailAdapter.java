package app.com.ttins.gettogether.eventdetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.eventdetail.EventDetailView;

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailView.ViewHolder> {

    private static final String LOG_TAG = EventDetailAdapter.class.getSimpleName();

    private Guests guests;
    private OnItemClickListener listener;

    public EventDetailAdapter(Guests guests, OnItemClickListener listener) {
        this.guests = guests;
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return guests.getGuests().size();
    }

    @Override
    public long getItemId(int position) {
        return guests.getGuests().get(position).getId();
    }

    @Override
    public EventDetailView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_guest, null, false);
        final EventDetailView.ViewHolder viewHolder = new EventDetailView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventDetailView.ViewHolder holder, final int position) {
        if(guests.getGuests().get(position) != null) {
            holder.guestName.setText(String.valueOf(guests.getGuests().get(position).getId()));
            holder.note.setText(guests.getGuests().get(position).getNote());
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(guests.getGuests().get(position).getId());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }
}
