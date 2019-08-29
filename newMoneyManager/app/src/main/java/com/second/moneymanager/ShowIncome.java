package com.second.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Calendar;

public class ShowIncome extends AppCompatActivity {

    TextView tvShowIncome, tvShowType, tvShowDate;
    Calendar myCalendar = Calendar.getInstance();
    private int day, month, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_income);

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        month = myCalendar.get(Calendar.MONTH);
        year = myCalendar.get(Calendar.YEAR);


        tvShowIncome = findViewById(R.id.tvShowIncome);
        tvShowType = findViewById(R.id.tvShowType);
        tvShowDate = findViewById(R.id.tvShowDate);

        String type = getIntent().getStringExtra("type");
        double sum = getIntent().getDoubleExtra("sum", 0);
        int day = getIntent().getIntExtra("day", 0);
        String month = getIntent().getStringExtra("month");
        int year = getIntent().getIntExtra("year", 0);


        tvShowIncome.setTextColor(Color.parseColor("#388e3c"));

        String date = day + "-" + month + "-" + year;

        tvShowIncome.setText(String.format("" + sum));
        tvShowType.setText(String.format("" + type));
        tvShowDate.setText(String.format("" + date));
    }
}
