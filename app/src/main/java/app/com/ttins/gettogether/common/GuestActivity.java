package app.com.ttins.gettogether.common;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import app.com.ttins.gettogether.R;

public class GuestActivity extends AppCompatActivity{

    private static final String LOG_TAG = GuestActivity.class.getSimpleName();

    Toolbar toolbar;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        toolbar = (Toolbar) findViewById(R.id.toolbar_guest_activity);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
