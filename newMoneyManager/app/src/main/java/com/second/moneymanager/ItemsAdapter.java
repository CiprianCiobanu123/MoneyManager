package com.second.moneymanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList items;
    SharedPreferences prefs = null;


    public ItemsAdapter(Context context, ArrayList items) {
        super(context, R.layout.row_layout, items);
        this.context = context;
        this.items = items;
        prefs = this.getContext().getSharedPreferences("com.mycompany.MoneyManager", 0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView tvIncomeExpense = rowView.findViewById(R.id.tvIncomeExpense);
        TextView tvDescription = rowView.findViewById(R.id.tvDescription);
        TextView tvCurrencyIncomeExpense = rowView.findViewById(R.id.tvCurrencyIncomeExpense);
        ImageView ivIncome = rowView.findViewById(R.id.ivIncome);
        tvCurrencyIncomeExpense.setText(prefs.getString("currency",""));


        if(items.get(position) instanceof Income){
            ivIncome.setImageResource(R.mipmap.moneybagincome);
           Income income = (Income) items.get(position);
            tvIncomeExpense.setTextColor(Color.parseColor("#388e3c"));
            tvIncomeExpense.setText("+" + String.valueOf(income.getSum()));
            tvDescription.setText(income.getType());



        }else{
            tvIncomeExpense.setTextColor(Color.parseColor("#b91400"));
            ivIncome.setImageResource(R.mipmap.expense);
           Expense expense = (Expense)items.get(position);
            tvIncomeExpense.setText("-" + String.valueOf(expense.getSpent()));
            tvDescription.setText(expense.getProduct());
        }
        return rowView;
    }
}
