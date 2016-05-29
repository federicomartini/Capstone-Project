package app.com.ttins.gettogether.eventguesthandler.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventguesthandler.EventGuestHandlerView;
import app.com.ttins.gettogether.eventguesthandler.loader.EventGuestHandlerLoader;

public class EventGuestHandlerRecyclerViewAdapter extends RecyclerView.Adapter<EventGuestHandlerView.ViewHolder> {

    private static final String LOG_TAG = EventGuestHandlerRecyclerViewAdapter.class.getSimpleName();

    Cursor cursor;
    EventGuestHandlerRecyclerViewAdapter.OnItemClickListener listener;
    Context context;

    public EventGuestHandlerRecyclerViewAdapter(Context context, Cursor cursor,
                                                EventGuestHandlerRecyclerViewAdapter.OnItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        this.cursor.moveToPosition(position);
        return this.cursor.getLong(EventGuestHandlerLoader.Query._ID);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public EventGuestHandlerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_guest_handler, parent, false);
        final EventGuestHandlerView.ViewHolder viewHolder = new EventGuestHandlerView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EventGuestHandlerView.ViewHolder holder, int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            holder.guestName.setText(cursor.getString(EventGuestHandlerLoader.Query.NAME));
            Glide.with(context).load(cursor.getString(EventGuestHandlerLoader.Query.PHOTO_PATH))
                    .into(holder.guestPhoto);

            holder.id = getItemId(position);
            holder.photoPath = cursor.getString(EventGuestHandlerLoader.Query.PHOTO_PATH);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onClick item received");
                    if (listener == null) {
                        Log.d(LOG_TAG, "Listener is null");
                    } else {
                        Log.d(LOG_TAG, "Item id = " + holder.id);
                        listener.onItemClick(holder.id, holder.guestName.getText().toString(),
                                holder.photoPath);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id, String guestName, String photoPath);
    }
}
