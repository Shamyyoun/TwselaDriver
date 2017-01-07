package com.twsela.driver.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 9/11/2015.
 */
public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener timePickerListener;
    private Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (calendar == null) {
            // Use the current date as the default date in the picker
            calendar = Calendar.getInstance();
        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                timePickerListener, hour, minute, false);

        return timePickerDialog;
    }

    public TimePickerDialog.OnTimeSetListener getTimePickerListener() {
        return timePickerListener;
    }

    public void setTimePickerListener(TimePickerDialog.OnTimeSetListener timePickerListener) {
        this.timePickerListener = timePickerListener;
    }

    public void setTime(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setTime(String time, String timeFormat) {
        if (time != null) {
            try {
                this.calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
                this.calendar.setTime(sdf.parse(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}