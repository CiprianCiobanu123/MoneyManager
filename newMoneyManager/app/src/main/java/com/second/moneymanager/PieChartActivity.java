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

        if (prefs.getString("monthlyOrYearly", "").equals("Monthly")) {
            try {
                ExpensesDB db = new ExpensesDB(PieChartActivity.this);
                db.open();
                expenses = db.getExpensesByYear(String.valueOf(calendar.get(Calendar.YEAR)));
                db.close();
                for (int i = 0; i < expenses.size(); i++) {
                    valueExpenses = (float) (valueExpenses + expenses.get(i).getPrice());






//                    switch (expenses.get(i).getCategory()) {
//                        case "Fuel":
//                            valueExpensesForFuel = (float) (valueExpensesForFuel + expenses.get(i).getPrice());
//                            break;
//                        case "Coffe":
//                            valueExpensesForDrinks = (float) (valueExpensesForDrinks + expenses.get(i).getPrice());
//                            break;
//                        case "Eating Out":
//                            valueExpensesForEatingOut = (float) (valueExpensesForEatingOut + expenses.get(i).getPrice());
//                            break;
//                        case "Clothes":
//                            valueExpensesForClothes = (float) (valueExpensesForClothes + expenses.get(i).getPrice());
//                            break;
//                        case "Video Games":
//                            valueExpensesForVideoGames = (float) (valueExpensesForVideoGames + expenses.get(i).getPrice());
//                            break;
//                        case "Gifts":
//                            valueExpensesForGifts = (float) (valueExpensesForGifts + expenses.get(i).getPrice());
//                            break;
//                        case "Holiday":
//                            valueExpensesForHoliday = (float) (valueExpensesForHoliday + expenses.get(i).getPrice());
//                            break;
//                        case "Kids":
//                            valueExpensesForKids = (float) (valueExpensesForKids + expenses.get(i).getPrice());
//                            break;
//                        case "Sport":
//                            valueExpensesForSport = (float) (valueExpensesForSport + expenses.get(i).getPrice());
//                            break;
//                        case "Travel":
//                            valueExpensesForTravel = (float) (valueExpensesForTravel + expenses.get(i).getPrice());
//                            break;
//                        default:
//                            return ;
//                    }
                }
            } catch (SQLException e) {
                Toast.makeText(PieChartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        expensesForChart.add(new Entry(((valueExpensesForFuel * 100) / valueExpenses), 0));
        expensesForChart.add(new Entry((((valueExpensesForDrinks * 100) / valueExpenses)), 1));
        expensesForChart.add(new Entry((((valueExpensesForEatingOut * 100) / valueExpenses)), 2));
        expensesForChart.add(new Entry(((valueExpensesForClothes * 100) / valueExpenses), 3));
        expensesForChart.add(new Entry(((valueExpensesForVideoGames * 100) / valueExpenses), 4));
        expensesForChart.add(new Entry(((valueExpensesForHoliday * 100) / valueExpenses), 5));
        expensesForChart.add(new Entry(((valueExpensesForKids * 100) / valueExpenses), 6));
        expensesForChart.add(new Entry(((valueExpensesForSport * 100) / valueExpenses), 7));
        expensesForChart.add(new Entry(((valueExpensesForTravel * 100) / valueExpenses), 8));
        expensesForChart.add(new Entry(((valueExpensesForGifts * 100) / valueExpenses), 8));

        PieDataSet dataSet = new PieDataSet(expensesForChart, "Balance");

        ArrayList<String> categories = new ArrayList<>();


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


        PieData data = new PieData(categories, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(2000, 2000);
    }
}