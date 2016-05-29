package app.com.ttins.gettogether.eventdetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.eventdetail.EventDetailView;

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailView.ViewHolder> {

    private static final String LOG_TAG = EventDetailAdapter.class.getSimpleName();

    private Guests guests;
    private OnItemClickListener listener;
    Context context;

    public EventDetailAdapter(Context context, Guests guests, OnItemClickListener listener) {
        this.guests = guests;
        this.listener = listener;
        this.context = context;
    }

    public void swapGuests(Guests guests){
        this.guests = guests;
        notifyDataSetChanged();
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
    public void onBindViewHolder(final EventDetailView.ViewHolder holder, final int position) {
        if(guests.getGuests().get(holder.getAdapterPosition()) != null) {
            holder.guestName.setText(String.valueOf(guests.getGuests().get(holder.getAdapterPosition()).getName()));
            holder.note.setText(guests.getGuests().get(holder.getAdapterPosition()).getNote());

            Glide.with(context).load(guests.getGuests().get(holder.getAdapterPosition()).getPhotoPath())
                    .into(holder.guestPhoto);

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(guests.getGuests().get(holder.getAdapterPosition()).getId());
                }
            });

            holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongItemClick(guests.getGuests().get(position).getId());
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
        void onLongItemClick(long id);
    }
}
