package com.second.moneymanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import static java.util.Calendar.*;

public class moneyExpanded extends AppCompatActivity {

    Button nextDay, btnPrevious;
    ListView lvItems;
    TextView tvToday;
    LinearLayout layoutToSwipe;

    Calendar calendar = getInstance();

    ArrayList items = new ArrayList();
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();

    SharedPreferences prefs = null;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_expanded);

        final int day = calendar.get(DAY_OF_MONTH);
        final int month = calendar.get(MONTH);
        final int year = calendar.get(YEAR);

        btnPrevious = findViewById(R.id.btnPrevious);
        layoutToSwipe = findViewById(R.id.layoutToSwipe);
        nextDay = findViewById(R.id.nextDay);
        lvItems = findViewById(R.id.lvItems);
        tvToday = findViewById(R.id.tvToday);

        prefs = getSharedPreferences("com.mycompany.MoneyManager", moneyExpanded.MODE_PRIVATE);


        nextDay.setBackgroundResource(R.drawable.nexttotomorrow);
        btnPrevious.setBackgroundResource(R.drawable.previoustoyesterday);
        int monthToShow = month + 1;
        final MyApplication app = (MyApplication) moneyExpanded.this.getApplication();


        if (prefs.getString("monthlyOrYearly", "").equals("Yearly")) {
            tvToday.setText(prefs.getString("year", ""));

            try {
                ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                db.open();
                incomes = db.getIncomesByyear(String.valueOf(year));
                expenses = db.getExpensesByYear(String.valueOf(year));

                db.close();
                for (int i = 0; i < incomes.size(); i++) {
                    items.add(incomes.get(i));
                }
                for (int i = 0; i < expenses.size(); i++) {
                    items.add(expenses.get(i));
                }
                app.setItems(items);
            } catch (SQLException e) {
                Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else if (prefs.getString("monthlyOrYearly", "").equals("Monthly")) {
            calendar.set(MONTH, Integer.valueOf(prefs.getString("month", "")));
            tvToday.setText(calendar.getDisplayName(MONTH, LONG, Locale.getDefault()) + " - " + calendar.get(YEAR));

//            Toast.makeText(this, prefs.getString("month",""), Toast.LENGTH_SHORT).show();
            try {
               ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                db.open();
                incomes = db.getIncomesByMonthAndYear(calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(year));
                expenses = db.getExpensesByMonthAndYear(calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(year));

                db.close();
                for (int i = 0; i < incomes.size(); i++) {
                    items.add(incomes.get(i));
                }
                for (int i = 0; i < expenses.size(); i++) {
                    items.add(expenses.get(i));
                }
                app.setItems(items);
            } catch (SQLException e) {
                Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (prefs.getString("monthlyOrYearly", "").equals("Daily")) {
            calendar.set(MONTH, Integer.valueOf(prefs.getString("month", "")));
            tvToday.setText(calendar.get(DAY_OF_MONTH) + " - " +
                    calendar.getDisplayName(MONTH, LONG, Locale.getDefault())
                    + " - " + calendar.get(YEAR));

            try {
                ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                db.open();

                incomes = db.getIncomesByDate(String.valueOf(calendar.get(DAY_OF_MONTH)), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(calendar.get(YEAR)));
                expenses = db.getExpensesByDate(String.valueOf(calendar.get(DAY_OF_MONTH)), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(calendar.get(YEAR)));

                db.close();
                for (int i = 0; i < incomes.size(); i++) {
                    items.add(incomes.get(i));
                }
                for (int i = 0; i < expenses.size(); i++) {
                    items.add(expenses.get(i));
                }
                app.setItems(items);
            } catch (SQLException e) {
                Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        final ItemsAdapter adapter = new ItemsAdapter(moneyExpanded.this, items);
        lvItems.setAdapter(adapter);

//        tvToday.setText(year + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + day);


        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringTokenizer tokens = new StringTokenizer(tvToday.getText().toString().trim(), "-");
                int yearToModify = Integer.parseInt(tokens.nextToken());
                String monthToModify = tokens.nextToken();
                int dayToModify = Integer.parseInt(tokens.nextToken());

                int maxDayFromCurrentMonth = calendar.getActualMaximum(DAY_OF_MONTH);

                dayToModify++;

                if (dayToModify <= maxDayFromCurrentMonth) {
                    tvToday.setText(yearToModify + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayToModify);
                } else {
                    dayToModify = 1;
                    int monthToVerifyForChangingYear = calendar.get(MONTH) + 1;
                    calendar.set(MONTH, calendar.get(MONTH) + 1);
                    if (monthToVerifyForChangingYear <= 11) {
                        tvToday.setText(yearToModify + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayToModify);
                    } else {
                        dayToModify = 1;
                        yearToModify = yearToModify + 1;
                        calendar.set(MONTH, calendar.get(MONTH));
                        tvToday.setText(yearToModify + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayToModify);
                    }
                }

                try {
                    ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                    db.open();
                    incomes = db.getIncomesByDate(String.valueOf(dayToModify), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(yearToModify));
                    expenses = db.getExpensesByDate(String.valueOf(dayToModify), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(yearToModify));
                    db.close();
                    adapter.clear();

                    for (int i = 0; i < incomes.size(); i++) {
                        items.add(incomes.get(i));
                    }
                    for (int i = 0; i < expenses.size(); i++) {
                        items.add(expenses.get(i));
                    }

                    adapter.notifyDataSetChanged();

                } catch (SQLException e) {
                    Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int yearToModify = 0;

                StringTokenizer tokens = new StringTokenizer(tvToday.getText().toString().trim(), "-");
                yearToModify = Integer.parseInt(tokens.nextToken());
                String monthToModify = tokens.nextToken();
                int dayToModify = Integer.parseInt(tokens.nextToken());
                int maxDayFromCurrentMonth = calendar.getActualMaximum(DAY_OF_MONTH);

                dayToModify--;

                if (dayToModify >= 1) {
                    tvToday.setText(yearToModify + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayToModify);
                } else {
                    int monthToVerifyForChangingYear = calendar.get(MONTH) - 1;
                    calendar.set(MONTH, calendar.get(MONTH) - 1);
                    dayToModify = calendar.getActualMaximum(DAY_OF_MONTH);
                    if (monthToVerifyForChangingYear >= 0) {
                        tvToday.setText(yearToModify + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayToModify);
                    } else {
                        dayToModify = maxDayFromCurrentMonth;
                        calendar.set(MONTH, calendar.get(MONTH));
                        yearToModify = yearToModify - 1;
                        tvToday.setText(yearToModify + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayToModify);
                    }
                }

                try {
                  ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                    db.open();
                    incomes = db.getIncomesByDate(String.valueOf(dayToModify), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(yearToModify));
                    expenses = db.getExpensesByDate(String.valueOf(dayToModify), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(yearToModify));
                    db.close();

                    adapter.clear();

                    for (int i = 0; i < incomes.size(); i++) {
                        items.add(incomes.get(i));
                    }
                    for (int i = 0; i < expenses.size(); i++) {
                        items.add(expenses.get(i));
                    }

                    adapter.notifyDataSetChanged();

                } catch (SQLException e) {
                    Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                if (adapter.getItemAtPosition(i) instanceof Income) {
                   Income income = (Income) adapter.getItemAtPosition(i);
                    String type = income.getType();
                    double sum = income.getSum();
                    Intent intent = new Intent(moneyExpanded.this,
                            ShowIncome.class);
                    intent.putExtra("type", type);
                    intent.putExtra("sum", sum);
                    intent.putExtra("day", income.getDayIncome());
                    intent.putExtra("month", income.getMonthIncome());
                    intent.putExtra("year", income.getYearIncome());
                    intent.putExtra("rowId", i);
                    startActivity(intent);
                } else if (adapter.getItemAtPosition(i) instanceof Expense) {
                 Expense expense = (Expense) adapter.getItemAtPosition(i);
                    String product = expense.getProduct();
                    int cantity = expense.getCantity();
                    double price = expense.getPrice();
                    double amountSpent = expense.getSpent();

                    Intent intent = new Intent(moneyExpanded.this,
                            ShowExpense.class);
                    intent.putExtra("product", product);
                    intent.putExtra("cantity", cantity);
                    intent.putExtra("price", price);
                    intent.putExtra("amountSpent", amountSpent);
                    intent.putExtra("day", expense.getDayExpense());
                    intent.putExtra("month", expense.getMonthExpense());
                    intent.putExtra("year", expense.getYearExpense());
                    startActivity(intent);
                }
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            MyApplication app = (MyApplication) moneyExpanded.this.getApplication();

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) instanceof Income) {
                    final Income income = (Income) adapterView.getItemAtPosition(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(moneyExpanded.this);
                    builder.setMessage("Do you want to remove the selected item?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                            db.open();
                            db.deleteEntryIncome(income.getId());
                            db.close();
                            adapter.remove(income);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(moneyExpanded.this, "The item has been removed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();

                } else if (adapterView.getItemAtPosition(i) instanceof Expense) {
                    final Expense expense = (Expense) adapterView.getItemAtPosition(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(moneyExpanded.this);
                    builder.setMessage("Do you want to remove the selected item?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                               ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                                db.open();
                                db.deleteEntryExpense(expense.getId());
                                db.close();
                            } catch (SQLException e) {
                                Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            adapter.remove(expense);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(moneyExpanded.this, "The item has been removed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }

            void DateDialog() {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(MONTH, monthOfYear);
                        tvToday.setText(year + "-" + calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()) + "-" + dayOfMonth);
                        try {
                            ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                            db.open();
                            incomes = db.getIncomesByDate(String.valueOf(dayOfMonth), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(year));
                            expenses = db.getExpensesByDate(String.valueOf(dayOfMonth), calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(year));
                            db.close();

                            adapter.clear();

                            for (int i = 0; i < incomes.size(); i++) {
                                items.add(incomes.get(i));
                            }
                            for (int i = 0; i < expenses.size(); i++) {
                                items.add(expenses.get(i));
                            }

                            adapter.notifyDataSetChanged();

                        } catch (SQLException e) {
                            Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                DatePickerDialog dpDialog = new DatePickerDialog(moneyExpanded.this, listener, year, month, day);
                dpDialog.show();
            }
        });
    }
}

