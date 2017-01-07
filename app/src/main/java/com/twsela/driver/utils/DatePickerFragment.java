package com.twsela.driver.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 9/11/2015.
 */
public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private Calendar date;
    private Calendar minDate;
    private Calendar maxDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (date == null) {
            // Use the current date as the default date in the picker
            date = Calendar.getInstance();
        }

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        // create DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
        datePickerDialog.getDatePicker().setSpinnersShown(true);

        // set min & max if possible
        if (minDate != null) {
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }
        if (maxDate != null) {
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }

        return datePickerDialog;
    }

    public DatePickerDialog.OnDateSetListener getDatePickerListener() {
        return datePickerListener;
    }

    public void setDatePickerListener(DatePickerDialog.OnDateSetListener datePickerListener) {
        this.datePickerListener = datePickerListener;
    }

    public void setDate(Calendar calendar) {
        this.date = calendar;
    }

    public void setDate(String date, String dateFormat) {
        if (date != null) {
            try {
                this.date = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
                this.date.setTime(sdf.parse(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMinDate(Calendar minDate) {
        this.minDate = minDate;
    }

    public void setMinDate(String minDateStr, String dateFormat) {
        minDate = DateUtils.convertToCalendar(minDateStr, dateFormat);
    }

    public void setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
    }

    public void setMaxDate(String maxDateStr, String dateFormat) {
        maxDate = DateUtils.convertToCalendar(maxDateStr, dateFormat);
    }
}