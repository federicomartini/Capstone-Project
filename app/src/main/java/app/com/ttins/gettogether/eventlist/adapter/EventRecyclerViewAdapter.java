package app.com.ttins.gettogether.eventlist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventlist.EventListView;
import app.com.ttins.gettogether.eventlist.loader.EventLoader;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventListView.ViewHolder>{

    private static final String LOG_TAG = EventRecyclerViewAdapter.class.getSimpleName();
    private Cursor cursor;
    private OnClickItemListener listener;
    private Context context;
    private boolean loadPhotoPermission = false;


    public EventRecyclerViewAdapter(Context context, Cursor cursor, boolean loadPhotoPermission, OnClickItemListener listener) {
        this.cursor = cursor;
        this.listener = listener;
        this.context = context;
        this.loadPhotoPermission = loadPhotoPermission;
    }

    public void swapEvents(Cursor cursor){
        Log.d(LOG_TAG, "SwapEvents...");
        this.cursor = cursor;
        notifyDataSetChanged();
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
                    listener.onClick(holder.id, holder.titleView.getText().toString());
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(holder.id, holder.titleView.getText().toString());
                    return true;
                }
            });

            if (loadPhotoPermission) {
                Log.d(LOG_TAG, "ID: " + position + " Photo Path: " + cursor.getString(EventLoader.Query.PHOTO_PATH));
                Glide.with(context).load(cursor.getString(EventLoader.Query.PHOTO_PATH)).skipMemoryCache(true)
                        .into(holder.thumbnailView);
            }
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
        void onClick(long id, String eventTitle);
        void onLongClick(long id, String eventTitle);
    }


}
