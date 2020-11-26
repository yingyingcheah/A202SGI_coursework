package com.example.yingying.moneysaver;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar calendar = Calendar.getInstance();
    TimePickerDialog mTimePickerDialog;
    DatePickerDialog mDatePickerDialog;
    private TextInputEditText mTitle, mDescription;
    private Button mScheduleNotification, mCancelNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mTitle = (TextInputEditText) findViewById(R.id.reminder_title);
        mDescription = (TextInputEditText) findViewById(R.id.reminder_description);
        mScheduleNotification = (Button) findViewById(R.id.reminder_scheduleNotification);
        mCancelNotification = (Button) findViewById(R.id.reminder_cancelNotification);

        //Get current date and set to DatePickerDialog
        mDatePickerDialog = DatePickerDialog.newInstance(ReminderActivity.this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //Initialize timePickerDialog with current time
        mTimePickerDialog = TimePickerDialog.newInstance(ReminderActivity.this,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), false);

        //Show notification
        mScheduleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        //Cancel notification
        mCancelNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyMe.cancel(getApplicationContext(), "reminder");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mTimePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        //Initialize notification
        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title(mTitle.getText().toString())
                .content(mDescription.getText().toString())
                .color(255, 0, 0, 255)
                .time(calendar)
                .addAction(new Intent(), "Dismiss", true, false)
                .key("reminder")
                .addAction(new Intent(), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }
}
