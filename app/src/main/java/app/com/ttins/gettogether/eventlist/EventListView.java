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

public class EventListView extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recycler_view_event_list_view)
    @BindInt(R.integer.event_list_column_count) int columnCount;
    RecyclerView recyclerView;
    Callback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) public ImageView thumbnailView;
        @BindView(R.id.event_title) public TextView titleView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.event_list_view, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    public interface Callback {
        void onEventListViewResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return EventLoader.allEvents(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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
        recyclerView.setAdapter(null);
    }
}
