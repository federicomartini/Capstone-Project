package app.com.ttins.gettogether.eventlist.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventlist.EventListView;
import app.com.ttins.gettogether.eventlist.loader.EventLoader;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventListView.ViewHolder>{

    private Cursor cursor;

    public EventRecyclerViewAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public long getItemId(int position) {
        this.cursor.moveToPosition(position);
        return this.cursor.getLong(EventLoader.Query._ID);
    }

    @Override
    public EventListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        final EventListView.ViewHolder viewHolder = new EventListView.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add click event handler here
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(EventListView.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.titleView.setText(cursor.getString(EventLoader.Query.TITLE));
        //TODO: insert here the upload of the photo from local storage through Picasso, Glide, etc

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
