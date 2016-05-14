package app.com.ttins.gettogether.eventlist;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.ttins.gettogether.R;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) public ImageView thumbnailView;
        @BindView(R.id.event_title) public TextView titleView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            titleView = ButterKnife.findById(view, R.id.event_title);
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
        if(this.isVisible()) {
            callback.onEventListViewResume();
        }

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.event_list_view, container, false);
        unbinder = ButterKnife.bind(this, root);
        recyclerView = ButterKnife.findById(root, R.id.recycler_view_event_list_view);
        emptyView = ButterKnife.findById(root, R.id.empty_view);
        columnCount = root.getResources().getInteger(R.integer.event_list_column_count);

        getLoaderManager().initLoader(0, null, this);

        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return EventLoader.allEvents(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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

        EventRecyclerViewAdapter eventRecyclerViewAdapter = new EventRecyclerViewAdapter(cursor);
        eventRecyclerViewAdapter.setHasStableIds(true);
        recyclerView.setAdapter(eventRecyclerViewAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                columnCount,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(recyclerView != null)
            recyclerView.setAdapter(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface Callback {
        void onEventListViewResume();
    }
}
