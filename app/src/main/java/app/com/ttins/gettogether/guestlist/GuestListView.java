package app.com.ttins.gettogether.guestlist;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.guestlist.adapter.GuestRecyclerViewAdapter;
import app.com.ttins.gettogether.guestlist.loader.GuestLoader;

public class GuestListView extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = GuestListView.class.getSimpleName();

    GuestRecyclerViewAdapter guestRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView emptyView;
    int columnCount;
    Callback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView guestName;
        public ImageView photo;
        public CardView cardView;
        public long id;

        public ViewHolder (View view) {
            super(view);
            guestName = (TextView) view.findViewById(R.id.guest_name_item_list);
            photo = (ImageView) view.findViewById(R.id.guest_photo_item_list);
            cardView = (CardView) view.findViewById(R.id.card_view_list_item_guest);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return GuestLoader.allGuests(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");

        if(!cursor.moveToFirst()) {
            Log.d(LOG_TAG, "RecyclerView is empty. Showing empty view");
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            Log.d(LOG_TAG, "Total Guests number: " + cursor.getCount());
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (guestRecyclerViewAdapter == null) {
            guestRecyclerViewAdapter = new GuestRecyclerViewAdapter(cursor, new GuestRecyclerViewAdapter.OnClickItemListener() {
                @Override
                public void onClick(long id, String guestName) {
                    callback.onGuestItemClick(id, guestName);
                }

                @Override
                public void onLongClick(long id, String guestName) {

                }
            });
        }

        recyclerView.setAdapter(guestRecyclerViewAdapter);

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

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        callback.onGuestListViewResume();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity) {
            callback = (Callback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.guest_list_view, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_guest_list_view);
        emptyView = (TextView) root.findViewById(R.id.empty_guest_list_view);
        columnCount = root.getResources().getInteger(R.integer.guest_list_column_count);


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface Callback {
        void onGuestItemClick(long id, String guestName);
        void onGuestListViewResume();
    }
}
