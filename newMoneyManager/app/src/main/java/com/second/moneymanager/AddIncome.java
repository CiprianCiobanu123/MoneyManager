package com.second.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AddIncome extends AppCompatActivity {

    EditText etType, etSum;
    Button btnAdd, btnCancel, btnDate;
    private Calendar myCalendar = Calendar.getInstance();
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        btnDate = findViewById(R.id.btnDate);
        btnAdd = findViewById(R.id.btnAdd);
        etType = findViewById(R.id.etType);
        btnCancel = findViewById(R.id.btnCancel);
        etSum = findViewById(R.id.etSum);

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                AddIncome.this.finish();
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

                DatePickerDialog dpDialog = new DatePickerDialog(AddIncome.this, listener, year, month, day);
                dpDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etType.getText().toString().isEmpty()) {
                    Toast.makeText(AddIncome.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else if (btnDate.getText().toString().isEmpty()) {
                    Toast.makeText(AddIncome.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else if (etSum.getText().toString().isEmpty()) {
                    Toast.makeText(AddIncome.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (etSum.getText().toString().trim().charAt(0) == '.') {
                        etSum.setText("0" + etSum.getText().toString().trim());
                    }
                    double sum = Double.parseDouble(etSum.getText().toString().trim());
                    String type = etType.getText().toString().trim();
                    String dateFromInput = btnDate.getText().toString().trim();

                    StringTokenizer tokens = new StringTokenizer(dateFromInput, "-");
                    int yearFromButton = Integer.parseInt(tokens.nextToken());
                    String monthFromButton = tokens.nextToken();
                    int dayFromButton = Integer.parseInt(tokens.nextToken());

                    try {
                        ExpensesDB db = new ExpensesDB(AddIncome.this);
                        db.open();
                        db.createEntryIncome(type, sum, dayFromButton, myCalendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), yearFromButton);
                        MyApplication app = (MyApplication) AddIncome.this.getApplication();
                        app.addIncomeToItems(new Income(sum, type, dayFromButton, myCalendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), yearFromButton, null));
                        db.close();
                        Toast.makeText(AddIncome.this, "Succesfully saved", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Toast.makeText(AddIncome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    AddIncome.this.finish();
                }
            }
        });
    }
}
