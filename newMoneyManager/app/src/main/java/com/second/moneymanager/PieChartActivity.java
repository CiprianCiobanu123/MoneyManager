package com.second.moneymanager;

import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class PieChartActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<Entry> expensesForChart = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        prefs = getSharedPreferences("com.mycompany.MoneyManager", MainActivity.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        final int day = Integer.parseInt(prefs.getString("day", ""));
        final int month = Integer.parseInt(prefs.getString("month", ""));
        final int year = Integer.parseInt(prefs.getString("year", ""));

        float valueExpenses = 0;

        ArrayList<Expense> expenses = new ArrayList<>();
        ArrayList<Double> expensesValues = new ArrayList<>();
        double totalValuesFromExpenseValues;

        if (prefs.getString("monthlyOrYearly", "").equals("Monthly")) {

            try {
                ExpensesDB db = new ExpensesDB(PieChartActivity.this);
                db.open();
                expenses = db.getExpensesByYear(String.valueOf(calendar.get(Calendar.YEAR)));

                for (int i = 0; i < expenses.size(); i++) {
                    valueExpenses = (float) (valueExpenses + expenses.get(i).getPrice());
                }

                for (int i = 0; i < expenses.size(); i++) {

                    totalValuesFromExpenseValues = 0;

                    expensesValues = db.getExpensesValuesByCategory(expenses.get(i).getCategory());

                    for (int j = 0; j < expensesValues.size(); j++) {
                        totalValuesFromExpenseValues = totalValuesFromExpenseValues + expensesValues.get(j);
                    }

                    if (totalValuesFromExpenseValues > 0) {
                        expensesForChart.add(new Entry(((float) (totalValuesFromExpenseValues * 100) / valueExpenses), i));
                    }

                    categories.add(expenses.get(i).getCategory());
                }
                db.close();
            } catch (SQLException e) {
                Toast.makeText(PieChartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        PieDataSet dataSet = new PieDataSet(expensesForChart, "");
        PieData data = new PieData(categories, dataSet);

        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(2000, 2000);
    }
}