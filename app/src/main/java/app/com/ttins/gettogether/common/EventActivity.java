package app.com.ttins.gettogether.common;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventedit.EventEditView;
import app.com.ttins.gettogether.eventlist.EventListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        EventListView fragmentEventListView = new EventListView();

        /* Starts with the List */
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentEventListView).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_event_event_activity);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventEditView fragmentEventEditView = new EventEditView();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentEventEditView).addToBackStack(null).commit();
            }
        });


    }
}
