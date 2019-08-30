package com.second.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.SHORT;

public class AddExpense extends AppCompatActivity {

    EditText  etPrice,  etCategoryExpense, etNotesExpense;
    Button btnAdd, btnCancel, btnDate;
    private Calendar myCalendar = Calendar.getInstance();
    private int day, month, year;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        btnDate = findViewById(R.id.btnDate);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        etCategoryExpense = findViewById(R.id.etCategoryExpense);
        etNotesExpense = findViewById(R.id.etNotesExpense);

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);

        prefs = getSharedPreferences("com.mycompany.MoneyManager", moneyExpanded.MODE_PRIVATE);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                AddExpense.this.finish();
            }
        });
        btnDate.setText(year + "-" + myCalendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()) + "-" + day);


        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }

            void DateDialog() {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(MONTH, monthOfYear);
                        btnDate.setText(year + "-" + myCalendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()) + "-" + dayOfMonth);
                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(AddExpense.this, listener, year, month, day);
                dpDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etPrice.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }else if (btnDate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (etCategoryExpense.getText().toString().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {

                    if (etPrice.getText().toString().trim().charAt(0) == '.') {
                        etPrice.setText("0" + etPrice.getText().toString().trim());
                    }
                    String category = etCategoryExpense.getText().toString().trim();
                    String notes = etNotesExpense.getText().toString().trim();
                    double price = Double.parseDouble(etPrice.getText().toString().trim());
                    String dateFromInput = btnDate.getText().toString().trim();

                    StringTokenizer tokens = new StringTokenizer(dateFromInput, "-");
                    int yearFromButton = Integer.parseInt(tokens.nextToken());
                    String monthFromButton = tokens.nextToken();
                    int dayFromButton = Integer.parseInt(tokens.nextToken());

                    try {
                        ExpensesDB db = new ExpensesDB(AddExpense.this);
                        db.open();
                        db.createEntryExpense( price, dayFromButton, myCalendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), yearFromButton, category, notes);
                        MyApplication app = (MyApplication) AddExpense.this.getApplication();
                        app.addExpenseToItems(new Expense( price, dayFromButton, myCalendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), yearFromButton, null, category, notes));
                        db.close();
                        Toast.makeText(AddExpense.this, "Succesfully Saved", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Toast.makeText(AddExpense.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    AddExpense.this.finish();
                }
            }
        });
    }
}
