package app.com.ttins.gettogether.eventguesthandler;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.ui.ThreeTwoImageView;
import app.com.ttins.gettogether.eventguesthandler.adapter.EventGuestHandlerRecyclerViewAdapter;

public class EventGuestHandlerView extends Fragment implements EventGuestHandlerMVP.RequestedViewOps {

    private static final String LOG_TAG = EventGuestHandlerView.class.getSimpleName();

    public static final String FRAG_GUEST_REMOVE_ARG = "FRAG_GUEST_REMOVE_ARG";

    RecyclerView recyclerView;
    TextView emptyRecyclerView;
    EventGuestHandlerMVP.PresenterOps presenter;
    EventGuestHandlerRecyclerViewAdapter eventGuestHandlerRecyclerViewAdapter;
    Callback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout itemLayout;
        public CircularImageView guestPhoto;
        public TextView guestName;
        public long id;
        public String photoPath;


        public ViewHolder(View view) {
            super(view);

            itemLayout = (RelativeLayout) view.findViewById(R.id.guest_item_event_guest_handler);
            guestPhoto = (CircularImageView) view.findViewById(R.id.photo_image_view_event_guest_handler);
            guestName = (TextView) view.findViewById(R.id.guest_name_text_view_event_guest_handler);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            callback = (EventGuestHandlerView.Callback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (presenter == null) {
            presenter = new EventGuestHandlerPresenter(this);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onInitLoader();
        callback.onEventGuestHandlerResumed();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_guest_handler_view, null, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_event_guest_handler_view);
        emptyRecyclerView = (TextView) root.findViewById(R.id.empty_guest_handler_view);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Context onContextViewRequired() {
        return getContext();
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        getLoaderManager().initLoader(5, null, loaderClass);
    }

    @Override
    public void onLoadResults(Cursor cursor) {
        if (eventGuestHandlerRecyclerViewAdapter == null) {
            eventGuestHandlerRecyclerViewAdapter = new EventGuestHandlerRecyclerViewAdapter(
                    getActivity(),
                    cursor,
                    new EventGuestHandlerRecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(long id, String name, String photoPath) {
                                    Log.d(LOG_TAG, "onItemClick Received by View");
                                    callback.onEventGuestHandlerAddRequest(id, name, photoPath);
                                }
                            });
        }
    }

    @Override
    public void onShowRecyclerView() {
        Log.d(LOG_TAG, "onShowRecyclerView");
        recyclerView.setVisibility(View.VISIBLE);
        emptyRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onShowEmptyRecyclerView() {
        Log.d(LOG_TAG, "onShowEmptyRecyclerView");
        recyclerView.setVisibility(View.GONE);
        emptyRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSetRecyclerViewAdapter() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(eventGuestHandlerRecyclerViewAdapter);

        eventGuestHandlerRecyclerViewAdapter.notifyDataSetChanged();
    }

    public interface Callback {
        void onEventGuestHandlerResumed();
        void onEventGuestHandlerAddRequest(long id, String name, String photoPath);
    }
}
