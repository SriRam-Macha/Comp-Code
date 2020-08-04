package com.mrwhitehat.compcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddContest extends AppCompatActivity {

    TextInputEditText title, platform, url;
    MaterialTextView start_time, start_date, end_time, end_date;
    MaterialButton start_time_btn, start_date_btn, end_time_btn, end_date_btn, add_contest;

    DatePickerDialog start_date_dialog, end_date_dialog;

    int start_hour, start_min, start_day, start_month, start_year, end_hour, end_min, end_day, end_month, end_year;
    Calendar start_calender, end_calender;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contest);

        title = (TextInputEditText) findViewById(R.id.title);
        platform = (TextInputEditText) findViewById(R.id.platform_d);
        url = (TextInputEditText) findViewById(R.id.url_d);
        start_time = (MaterialTextView) findViewById(R.id.start_time);
        start_time_btn = (MaterialButton) findViewById(R.id.start_time_btn);
        start_date = (MaterialTextView) findViewById(R.id.start_date);
        start_date_btn = (MaterialButton) findViewById(R.id.start_date_btn);
        end_time = (MaterialTextView) findViewById(R.id.end_time);
        end_time_btn = (MaterialButton) findViewById(R.id.end_time_btn);
        end_date = (MaterialTextView) findViewById(R.id.end_date);
        end_date_btn = (MaterialButton) findViewById(R.id.end_date_btn);
        add_contest = (MaterialButton) findViewById(R.id.add_contest);

        reference = FirebaseDatabase.getInstance().getReference("Contests");

        start_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_calender = Calendar.getInstance();
                start_hour = start_calender.get(Calendar.HOUR_OF_DAY);
                start_min = start_calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddContest.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int mins) {

                        if (hours < 10 & mins < 10) {
                            start_time.setText("0" + hours + ":0" + mins);
                        }
                        else if (hours < 10) {
                            start_time.setText("0" + hours + ":" + mins);
                        }
                        else if (mins < 10 ) {
                            start_time.setText(hours + ":0" + mins);
                        }
                        else {
                            start_time.setText(hours + ":" + mins);
                        }
                    }
                }, start_hour, start_min, android.text.format.DateFormat.is24HourFormat(AddContest.this));
                timePickerDialog.show();
            }
        });

        start_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_calender = Calendar.getInstance();
                start_year = start_calender.get(Calendar.YEAR);
                start_month = start_calender.get(Calendar.MONTH);
                start_day = start_calender.get(Calendar.DAY_OF_MONTH);

                start_date_dialog = new DatePickerDialog(AddContest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (day < 10 & (month + 1) < 10) {
                                    start_date.setText("0" + day + "-0" + (month + 1) + "-" + (year - 2000));
                                }
                                else if (day < 10) {
                                    start_date.setText("0" + day + "-" + (month + 1) + "-" + (year - 2000));
                                }
                                else if ((month + 1) < 10) {
                                    start_date.setText(day + "-0" + (month + 1) + "-" + (year - 2000));
                                }
                                else {
                                    start_date.setText(day + "-" + (month + 1) + "-" + (year - 2000));
                                }
                            }
                        }, start_year, start_month, start_day);
                start_date_dialog.show();
            }
        });

        end_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_calender = Calendar.getInstance();
                end_hour = end_calender.get(Calendar.HOUR_OF_DAY);
                end_min = end_calender.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddContest.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                        if (hours < 10 & mins < 10) {
                            end_time.setText("0" + hours + ":0" + mins);
                        }
                        else if (hours < 10) {
                            end_time.setText("0" + hours + ":" + mins);
                        }
                        else if (mins < 10 ) {
                            end_time.setText(hours + ":0" + mins);
                        }
                        else {
                            end_time.setText(hours + ":" + mins);
                        }
                    }
                }, end_hour, end_min, android.text.format.DateFormat.is24HourFormat(AddContest.this));
                timePickerDialog.show();
            }
        });

        end_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_calender = Calendar.getInstance();
                end_year = end_calender.get(Calendar.YEAR);
                end_month = end_calender.get(Calendar.MONTH);
                end_day = end_calender.get(Calendar.DAY_OF_MONTH);

                end_date_dialog = new DatePickerDialog(AddContest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (day < 10 & (month + 1) < 10) {
                                    end_date.setText("0" + day + "-0" + (month + 1) + "-" + (year - 2000));
                                }
                                else if (day < 10) {
                                    end_date.setText("0" + day + "-" + (month + 1) + "-" + (year - 2000));
                                }
                                else if ((month + 1) < 10) {
                                    end_date.setText(day + "-0" + (month + 1) + "-" + (year - 2000));
                                }
                                else {
                                    end_date.setText(day + "-" + (month + 1) + "-" + (year - 2000));
                                }

                            }
                        }, end_year, end_month, end_day);
                end_date_dialog.show();
            }
        });

        add_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contest_title = String.valueOf(title.getText());
                String contest_platform = String.valueOf(platform.getText());
                String contest_url = String.valueOf(url.getText());
                String contest_start_time = String.valueOf(start_time.getText());
                String contest_start_date = String.valueOf(start_date.getText());
                String contest_end_time = String.valueOf(end_time.getText());
                String contest_end_date = String.valueOf(end_date.getText());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id = user.getUid();

                if (contest_title.isEmpty() | contest_url.isEmpty() | contest_platform.isEmpty() | contest_start_time.isEmpty() | contest_start_date.isEmpty() | contest_end_time.isEmpty() | contest_end_date.isEmpty()) {
                    Toast.makeText(AddContest.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    Api_custom api_custom = new Api_custom(contest_title, contest_url, contest_platform, contest_start_time, contest_start_date, contest_end_time, contest_end_date, id);
                    reference.child(id + "   " + contest_title + contest_platform + contest_start_date + contest_start_time).setValue(api_custom);
                    Toast.makeText(AddContest.this, "Contest added successfully!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddContest.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        platform.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}