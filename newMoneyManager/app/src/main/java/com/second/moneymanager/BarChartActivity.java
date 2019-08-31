package com.second.moneymanager;

import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class BarChartActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        BarChart chart = findViewById(R.id.barchart);

        prefs = getSharedPreferences("com.mycompany.MoneyManager", MainActivity.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        final int day = Integer.parseInt(prefs.getString("day", ""));
        final int month = Integer.parseInt(prefs.getString("month", ""));
        final int year = Integer.parseInt(prefs.getString("year", ""));

        float valueExpenses = 0;
        float valueExpensesForFuel = 0;
        float valueExpensesForDrinks = 0;
        float valueExpensesForEatingOut = 0;
        float valueExpensesForClothes = 0;
        float valueExpensesForVideoGames = 0;
        float valueExpensesForGifts = 0;
        float valueExpensesForHoliday = 0;
        float valueExpensesForKids = 0;
        float valueExpensesForSport = 0;
        float valueExpensesForTravel = 0;

        ArrayList<Expense> expenses = new ArrayList<>();


        if (prefs.getString("monthlyOrYearly", "").equals("Yearly")) {
            try {
                ExpensesDB db = new ExpensesDB(BarChartActivity.this);
                db.open();

                expenses = db.getExpensesByYear(prefs.getString("year", ""));

                db.close();


                for (int i = 0; i < expenses.size(); i++) {
                    valueExpenses = (float) (valueExpenses + expenses.get(i).getPrice());

                    switch (expenses.get(i).getCategory()) {
                        case "Fuel":
                            valueExpensesForFuel = (float) (valueExpensesForFuel + expenses.get(i).getPrice());
                            break;
                        case "Coffe":
                            valueExpensesForDrinks = (float) (valueExpensesForDrinks + expenses.get(i).getPrice());
                            break;
                        case "Eating Out":
                            valueExpensesForEatingOut = (float) (valueExpensesForEatingOut + expenses.get(i).getPrice());
                            break;
                        case "Clothes":
                            valueExpensesForClothes = (float) (valueExpensesForClothes + expenses.get(i).getPrice());
                            break;
                        case "Video Games":
                            valueExpensesForVideoGames = (float) (valueExpensesForVideoGames + expenses.get(i).getPrice());
                            break;
                        case "Gifts":
                            valueExpensesForGifts = (float) (valueExpensesForGifts + expenses.get(i).getPrice());
                            break;
                        case "Holiday":
                            valueExpensesForHoliday = (float) (valueExpensesForHoliday + expenses.get(i).getPrice());
                            break;
                        case "Kids":
                            valueExpensesForKids = (float) (valueExpensesForKids + expenses.get(i).getPrice());
                            break;
                        case "Sport":
                            valueExpensesForSport = (float) (valueExpensesForSport + expenses.get(i).getPrice());
                            break;
                        case "Travel":
                            valueExpensesForTravel = (float) (valueExpensesForTravel + expenses.get(i).getPrice());
                            break;
                        default:
                            return;
                    }
                }
            } catch (SQLException e) {
                Toast.makeText(BarChartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ArrayList expensesForBarChart = new ArrayList();

        expensesForBarChart.add(new BarEntry(((valueExpensesForFuel * 100) / valueExpenses), 0));
        expensesForBarChart.add(new BarEntry((((valueExpensesForDrinks * 100) / valueExpenses)), 1));
        expensesForBarChart.add(new BarEntry((((valueExpensesForEatingOut * 100) / valueExpenses)), 2));
        expensesForBarChart.add(new BarEntry(((valueExpensesForClothes * 100) / valueExpenses), 3));
        expensesForBarChart.add(new BarEntry(((valueExpensesForVideoGames * 100) / valueExpenses), 4));
        expensesForBarChart.add(new BarEntry(((valueExpensesForHoliday * 100) / valueExpenses), 5));
        expensesForBarChart.add(new BarEntry(((valueExpensesForKids * 100) / valueExpenses), 6));
        expensesForBarChart.add(new BarEntry(((valueExpensesForSport * 100) / valueExpenses), 7));
        expensesForBarChart.add(new BarEntry(((valueExpensesForTravel * 100) / valueExpenses), 8));
        expensesForBarChart.add(new BarEntry(((valueExpensesForGifts * 100) / valueExpenses), 8));

        ArrayList categories = new ArrayList();

        categories.add("Fuel");
        categories.add("Coffe");
        categories.add("Eating Out");
        categories.add("Clothes");
        categories.add("Video Games");
        categories.add("Gifts");
        categories.add("Holiday");
        categories.add("Kids");
        categories.add("Sport");
        categories.add("Travel");

        BarDataSet bardataset = new BarDataSet(expensesForBarChart, "No Of Employee");
        chart.animateY(2000);
        BarData data = new BarData(categories, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
    }
}
