package app.com.ttins.gettogether.eventlist;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ttins.gettogether.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListView extends Fragment {

    @BindView(R.id.recycler_view_event_list_view)
    RecyclerView recyclerView;
    Callback callback;

    @Override
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
        callback.onEventListViewResume();

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
}
