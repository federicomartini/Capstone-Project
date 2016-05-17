package app.com.ttins.gettogether.eventlist.adapter;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventlist.EventListView;
import app.com.ttins.gettogether.eventlist.loader.EventLoader;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventListView.ViewHolder>{

    private static final String LOG_TAG = EventRecyclerViewAdapter.class.getSimpleName();
    private Cursor cursor;
    private OnClickItemListener listener;


    public EventRecyclerViewAdapter(Cursor cursor, OnClickItemListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        this.cursor.moveToPosition(position);
        return this.cursor.getLong(EventLoader.Query._ID);
    }

    @Override
    public EventListView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        final EventListView.ViewHolder viewHolder = new EventListView.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EventListView.ViewHolder holder, final int position) {

        if(cursor != null) {
            cursor.moveToPosition(position);

            holder.titleView.setText(cursor.getString(EventLoader.Query.TITLE));
            holder.id = getItemId(position);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Add click event handler here
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(holder.id);
                    return true;
                }
            });

        } else {
            Log.d(LOG_TAG, "cursor is null");
        }

        //TODO: insert here the upload of the photo from local storage through Picasso, Glide, etc

    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public interface OnClickItemListener {
        void onLongClick(long id);
    }

}
