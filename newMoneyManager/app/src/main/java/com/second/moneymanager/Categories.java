package com.second.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

public class Categories extends AppCompatActivity {
    ListView lvCategories;
    SharedPreferences prefs = null;
    ArrayList<String> incomeCategories = new ArrayList<>();
    ArrayList<String> expenseCategories = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        lvCategories = findViewById(R.id.lvCategories);

        prefs = getSharedPreferences("com.mycompany.MoneyManager", MainActivity.MODE_PRIVATE);

        try{
            ExpensesDB db = new ExpensesDB(Categories.this);
            db.open();
            incomeCategories = db.getAllIncomeCategories();
            expenseCategories = db.getAllExpenseCategories();
            db.close();
        }catch(SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        if (prefs.getBoolean("isExpense", true)) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, expenseCategories);
            lvCategories.setAdapter(adapter);

        } else if (prefs.getBoolean("isIncome", true)) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, incomeCategories);
            lvCategories.setAdapter(adapter);
        }

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) lvCategories.getItemAtPosition(position);

                Intent intent = new Intent();
                if (prefs.getBoolean("isIncome", true)) {

                    intent.putExtra("categoryIncome", itemValue);
                } else if (prefs.getBoolean("isExpense", true)) {
                    intent.putExtra("categoryExpense", itemValue);

                }

                setResult(RESULT_OK, intent);
                Categories.this.finish();

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

    }
}
