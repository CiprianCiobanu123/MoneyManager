package com.second.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import java.time.LocalDate;

public class ShowExpense extends AppCompatActivity {

    TextView tvShowExpenseProduct, tvShowExpensePrice, tvShowExpenseCantity, tvShowExpenseDate, tvShowExpenseTotalSpent, tvLabelSpent;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        tvShowExpenseProduct = findViewById(R.id.tvShowExpenseProduct);
        tvShowExpensePrice = findViewById(R.id.tvShowExpensePrice);
        tvShowExpenseCantity = findViewById(R.id.tvShowExpenseCantity);
        tvShowExpenseDate = findViewById(R.id.tvShowExpenseDate);
        tvShowExpenseTotalSpent = findViewById(R.id.tvShowExpenseTotalSpent);


        final String product = getIntent().getStringExtra("product");
        final double price = getIntent().getDoubleExtra("price",0);
        final int cantity = getIntent().getIntExtra("cantity",0);
        final double amountSpent = getIntent().getDoubleExtra("amountSpent",0);
        final int day = getIntent().getIntExtra("day",0);
        final String month = getIntent().getStringExtra("month");
        final int year = getIntent().getIntExtra("year",0);

        String date = day + "-" + month + "-" + year;

        tvShowExpenseTotalSpent.setTextColor(Color.parseColor("#b91400"));

        tvShowExpenseProduct.setText(String.format(""+product));
        tvShowExpensePrice.setText(String.format(""+ price));
        tvShowExpenseCantity.setText(String.format( ""+cantity));
        tvShowExpenseDate.setText(String.format(""+ date));
        tvShowExpenseTotalSpent.setText(""+amountSpent);
    }
}
