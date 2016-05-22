package app.com.ttins.gettogether.eventguestremove;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.ttins.gettogether.R;

public class EventGuestRemoveView extends Fragment implements EventGuestRemoveMVP.RequestedViewOps {

    private static final String LOG_TAG = EventGuestRemoveView.class.getSimpleName();

    EventGuestRemoveMVP.PresenterOps presenter;
    RecyclerView recyclerView;
    TextView emptyRecyclerView;
    Callback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemLayout;
        public ImageView guestPhoto;
        public TextView guestName;
        public long id;


        public ViewHolder(View view) {
            super(view);

            itemLayout = (LinearLayout) view.findViewById(R.id.guest_item_event_guest_handler);
            guestPhoto = (ImageView) view.findViewById(R.id.photo_image_view_event_guest_handler);
            guestName = (TextView) view.findViewById(R.id.guest_name_text_view_event_guest_handler);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            callback = (EventGuestRemoveView.Callback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new EventGuestRemovePresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onInitLoader();
        callback.onEventGuestRemoveResumed();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_guest_remove_view, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_event_guest_remove_view);
        emptyRecyclerView = (TextView) root.findViewById(R.id.empty_guest_remove_view);

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
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId) {
        getLoaderManager().initLoader(loaderId, null, loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return getContext();
    }

    @Override
    public void onSetRecyclerViewAdapter() {

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
    public void onLoadResults(Cursor cursor) {

    }

    public interface Callback {
        void onEventGuestRemoveResumed();
        void onEventGuestRemoveAddRequest(long id);
    }

}
