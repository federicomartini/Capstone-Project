package app.com.ttins.gettogether.guestlist.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.guestlist.GuestListView;
import app.com.ttins.gettogether.guestlist.loader.GuestLoader;


public class GuestRecyclerViewAdapter extends RecyclerView.Adapter<GuestListView.ViewHolder> {

    private static final String LOG_TAG = GuestRecyclerViewAdapter.class.getSimpleName();

    Cursor cursor;
    OnClickItemListener listener;

    public GuestRecyclerViewAdapter (Cursor cursor, OnClickItemListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        this.cursor.moveToPosition(position);
        return this.cursor.getLong(GuestLoader.Query._ID);
    }

    @Override
    public GuestListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_guest, parent, false);
        final GuestListView.ViewHolder viewHolder = new GuestListView.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GuestListView.ViewHolder holder, int position) {
        if(cursor != null) {
            cursor.moveToPosition(position);

            holder.guestName.setText((cursor.getString(GuestLoader.Query.NAME)));
            holder.id = getItemId(position);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(holder.id, holder.guestName.getText().toString());
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(holder.id, holder.guestName.getText().toString());
                    return true;
                }
            });

        } else {
            Log.d(LOG_TAG, "cursor is null");
        }

        //TODO: insert here the upload of the photo from local storage through Picasso, Glide, etc
    }

    public interface OnClickItemListener {
        void onClick(long id, String guestName);
        void onLongClick(long id, String guestName);
    }
}
