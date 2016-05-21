package app.com.ttins.gettogether.eventguesthandler.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventguesthandler.EventGuestHandlerView;
import app.com.ttins.gettogether.eventguesthandler.loader.EventGuestHandlerLoader;

public class EventGuestHandlerRecyclerViewAdapter extends RecyclerView.Adapter<EventGuestHandlerView.ViewHolder> {

    private static final String LOG_TAG = EventGuestHandlerRecyclerViewAdapter.class.getSimpleName();

    Cursor cursor;
    EventGuestHandlerRecyclerViewAdapter.OnItemClickListener listener;

    public EventGuestHandlerRecyclerViewAdapter(Cursor cursor, EventGuestHandlerRecyclerViewAdapter.OnItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "Cursor Items: " + cursor.getCount());
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
            //cursor.getString(EventGuestHandlerLoader.Query.PHOTO_PATH);
            holder.id = getItemId(position);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onClick item received");
                    if (listener == null) {
                        Log.d(LOG_TAG, "Listener is null");
                    } else {
                        Log.d(LOG_TAG, "Item id = " + holder.id);
                        listener.onItemClick(holder.id);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }
}
