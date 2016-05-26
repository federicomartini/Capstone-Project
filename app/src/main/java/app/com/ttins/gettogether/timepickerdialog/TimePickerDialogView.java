package app.com.ttins.gettogether.timepickerdialog;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import app.com.ttins.gettogether.common.EventActivity;
import app.com.ttins.gettogether.common.utils.DateTimeFormat;

public class TimePickerDialogView extends DialogFragment implements TimePickerDialogMVP.RequestedViewOps,
        TimePickerDialog.OnTimeSetListener {

    private static final String LOG_TAG = TimePickerDialogView.class.getSimpleName();

    Callback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EventActivity) {
            callback = (Callback) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String time = DateTimeFormat.convertTime(hourOfDay, minute);

        if (callback != null) {
            callback.onTimeSet(getTag(), time);
        }
    }

    public interface Callback {
        void onTimeSet(String tag, String time);
    }

}
