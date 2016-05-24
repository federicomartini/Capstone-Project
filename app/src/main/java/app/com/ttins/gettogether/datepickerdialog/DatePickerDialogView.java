package app.com.ttins.gettogether.datepickerdialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import app.com.ttins.gettogether.common.EventActivity;

public class DatePickerDialogView extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = DatePickerDialogView.class.getSimpleName();
    Callback callback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EventActivity) {
            callback = (Callback) activity;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(LOG_TAG, "onDateSet");
        String date = String.valueOf(year + "/" + monthOfYear + "/" + dayOfMonth);
        if (callback != null) {
            Log.d(LOG_TAG, "Callback onDateSet");
            callback.onDateSet(getTag(), date);
        } else {
            Log.d(LOG_TAG, "callback is null!");
        }

    }

    public interface Callback {
        void onDateSet(String tag, String time);
    }
}
