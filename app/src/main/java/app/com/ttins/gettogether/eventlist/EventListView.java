package app.com.ttins.gettogether.eventlist;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventlist.adapter.EventRecyclerViewAdapter;
import app.com.ttins.gettogether.eventlist.loader.EventLoader;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventListView extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventListView.class.getSimpleName();

    @BindView(R.id.empty_view) TextView emptyView;
    @BindView(R.id.recycler_view_event_list_view) RecyclerView recyclerView;
    @BindInt(R.integer.event_list_column_count) int columnCount;
    Callback callback;
    Unbinder unbinder;
    EventRecyclerViewAdapter eventRecyclerViewAdapter;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) public ImageView thumbnailView;
        @BindView(R.id.event_title) public TextView titleView;
        @BindView(R.id.card_view_list_item_event) public CardView cardView;
        public long id;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            titleView = ButterKnife.findById(view, R.id.event_title);
            thumbnailView = ButterKnife.findById(view, R.id.thumbnail);
            cardView = ButterKnife.findById(view, R.id.card_view_list_item_event);
        }

    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            callback = (EventListView.Callback) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        if(this.isVisible()) {
            callback.onEventListViewResume();
        }

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.event_list_view, container, false);
        unbinder = ButterKnife.bind(this, root);
        recyclerView = ButterKnife.findById(root, R.id.recycler_view_event_list_view);
        emptyView = ButterKnife.findById(root, R.id.empty_view);
        columnCount = root.getResources().getInteger(R.integer.event_list_column_count);

        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return EventLoader.allEvents(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");

        if (!cursor.moveToFirst()) {
            Log.d(LOG_TAG, "RecyclerView is empty. Showing empty view");
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            Log.d(LOG_TAG, "Total Events number: " + cursor.getCount());
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        if (eventRecyclerViewAdapter == null) {
            eventRecyclerViewAdapter = new EventRecyclerViewAdapter(cursor, new EventRecyclerViewAdapter.OnClickItemListener() {

                @Override
                public void onClick(long id, String eventTitle) {
                    callback.onEventItemClick(id, eventTitle);
                }

                @Override
                public void onLongClick(final long id, String title) {
                    Log.d(LOG_TAG, "onLongClick received");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Event")
                            .setMessage("Want to delete event \"" + title + "\" ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().getContentResolver().delete(GetTogetherContract.Events.CONTENT_URI,
                                            GetTogetherContract.Events._ID + " = ?",
                                            new String[]{String.valueOf(id)});
                                    eventRecyclerViewAdapter = null;
                                }
                            });
                    builder.create().show();
                }
            });
        }

        recyclerView.setAdapter(eventRecyclerViewAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                columnCount,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        if(recyclerView != null)
            recyclerView.setAdapter(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    public interface Callback {
        void onEventItemClick(long id, String titleText);
        void onEventListViewResume();
    }
}
