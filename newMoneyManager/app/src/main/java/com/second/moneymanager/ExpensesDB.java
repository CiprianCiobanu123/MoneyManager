package com.second.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ExpensesDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PRODUCT = "_product";
    public static final String KEY_PRICE = "_price";
    public static final String KEY_CANTITY = "_cantity";
    public static final String KEY_DAY_FOR_EXPENSES = "day_for_expenses";
    public static final String KEY_MONTH_FOR_EXPENSES = "month_for_expenses";
    public static final String KEY_YEAR_FOR_EXPENSES = "year_for_expenses";

    public static final String KEY_TYPE = "_type";
    public static final String KEY_SUM = "_sum";
    public static final String KEY_DAY_FOR_INCOMES = "day_for_incomes";
    public static final String KEY_MONTH_FOR_INCOMES = "month_for_incomes";
    public static final String KEY_YEAR_FOR_INCOMES = "year_for_incomes";

    public static final String KEY_CURRENCY = "year_for_incomes";


    private final String DATABASE_NAME = "SpendingDB";
    private final String DATABASE_TABLE_EXPENSE = "ExpenseTable";
    private final String DATABASE_TABLE_INCOME = "IncomeTable";
    private final String DATABASE_TABLE_CURRENCY = "CurrencyTable";
    private final int DATABASE_VERSION = 1;


    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public ExpensesDB(Context context) {
        ourContext = context;
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_TABLE_EXPENSE, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {


            String sqlCodeForExpense = "CREATE TABLE " + DATABASE_TABLE_EXPENSE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_PRODUCT + " TEXT NOT NULL, " +
                    KEY_PRICE + " REAL, " +
                    KEY_CANTITY + " REAL, " +
                    KEY_DAY_FOR_EXPENSES + " INTEGER, " +
                    KEY_MONTH_FOR_EXPENSES + " INTEGER, " +
                    KEY_YEAR_FOR_EXPENSES + " INTEGER)";

            String sqlCodeForIncome = "CREATE TABLE " + DATABASE_TABLE_INCOME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TYPE + " TEXT NOT NULL, " +
                    KEY_SUM + " REAL, " +
                    KEY_DAY_FOR_INCOMES + " INTEGER, " +
                    KEY_MONTH_FOR_INCOMES + " INTEGER, " +
                    KEY_YEAR_FOR_INCOMES + " INTEGER)";

            sqLiteDatabase.execSQL(sqlCodeForExpense);
            sqLiteDatabase.execSQL(sqlCodeForIncome);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_INCOME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EXPENSE);
            onCreate(sqLiteDatabase);
        }
    }

    public ExpensesDB open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntryExpense(String product, double price, int cantity, int dayExpense, String monthExpense, int yearExpense) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRODUCT, product);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_CANTITY, cantity);
        cv.put(KEY_DAY_FOR_EXPENSES, dayExpense);
        cv.put(KEY_MONTH_FOR_EXPENSES, monthExpense);
        cv.put(KEY_YEAR_FOR_EXPENSES, yearExpense);
        return ourDatabase.insert(DATABASE_TABLE_EXPENSE, null, cv);

    }

    public long createEntryIncome(String type, double sum, int dayIncome, String monthIncome, int yearIncome) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TYPE, type);
        cv.put(KEY_SUM, sum);
        cv.put(KEY_DAY_FOR_INCOMES, dayIncome);
        cv.put(KEY_MONTH_FOR_INCOMES, monthIncome);
        cv.put(KEY_YEAR_FOR_INCOMES, yearIncome);

        return ourDatabase.insert(DATABASE_TABLE_INCOME, null, cv);
    }


    public long deleteEntryExpense(String id) {
        return ourDatabase.delete(DATABASE_TABLE_EXPENSE, KEY_ROWID + "=?", new String[]{id});
    }

    public long deleteEntryIncome(String id) {
        return ourDatabase.delete(DATABASE_TABLE_INCOME, KEY_ROWID + "=?", new String[]{id});
    }

    public long updateEntryExpense(String rowId, String product, double price, int cantity, Date dateExpenses) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PRODUCT, product);
        contentValues.put(KEY_PRICE, price);
        contentValues.put(KEY_CANTITY, cantity);
        return ourDatabase.update(DATABASE_TABLE_EXPENSE, contentValues, KEY_ROWID + "=?", new String[]{rowId});
    }

    public long updateEntryIncome(String type, double sum, Date dateIncomes) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TYPE, type);
        contentValues.put(KEY_SUM, sum);
        return ourDatabase.update(DATABASE_TABLE_INCOME, contentValues, KEY_TYPE + "=?", new String[]{type});

    }


    public ArrayList<Income> getIncomesByDate(String day, String month, String year) {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Income> incomeArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_INCOME + " where " + KEY_DAY_FOR_INCOMES + " =? AND " + KEY_MONTH_FOR_INCOMES + " =? AND "
                + KEY_YEAR_FOR_INCOMES + " =? ", new String[]{day, month, year});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double sum = res.getDouble(res.getColumnIndex(KEY_SUM));
            String type = res.getString(res.getColumnIndex(KEY_TYPE));
            int dayIncome = res.getInt(res.getColumnIndex(KEY_DAY_FOR_INCOMES));
            String monthIncome = res.getString(res.getColumnIndex(KEY_MONTH_FOR_INCOMES));
            int yearIncome = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_INCOMES));

           Income income = new Income(sum, type, dayIncome, monthIncome, yearIncome, id);
            incomeArray.add(income);
            res.moveToNext();
        }
        return incomeArray;
    }

    public ArrayList<Income> getIncomesByMonthAndYear(String month, String year) {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Income> incomeArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_INCOME + " where " + KEY_MONTH_FOR_INCOMES + " =? AND "
                + KEY_YEAR_FOR_INCOMES + " =? ", new String[]{month, year});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double sum = res.getDouble(res.getColumnIndex(KEY_SUM));
            String type = res.getString(res.getColumnIndex(KEY_TYPE));
            int dayIncome = res.getInt(res.getColumnIndex(KEY_DAY_FOR_INCOMES));
            String monthIncome = res.getString(res.getColumnIndex(KEY_MONTH_FOR_INCOMES));
            int yearIncome = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_INCOMES));

            Income income = new Income(sum, type, dayIncome, monthIncome, yearIncome, id);
            incomeArray.add(income);
            res.moveToNext();
        }
        return incomeArray;
    }


    public ArrayList<Income> getIncomesByyear(String year) {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Income> incomeArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_INCOME + " where " + KEY_YEAR_FOR_INCOMES + " =? ", new String[]{year});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double sum = res.getDouble(res.getColumnIndex(KEY_SUM));
            String type = res.getString(res.getColumnIndex(KEY_TYPE));
            int dayIncome = res.getInt(res.getColumnIndex(KEY_DAY_FOR_INCOMES));
            String monthIncome = res.getString(res.getColumnIndex(KEY_MONTH_FOR_INCOMES));
            int yearIncome = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_INCOMES));

           Income income = new Income(sum, type, dayIncome, monthIncome, yearIncome, id);
            incomeArray.add(income);
            res.moveToNext();
        }
        return incomeArray;
    }

    public ArrayList<Expense> getExpensesByDate(String day, String month, String year) {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Expense> expenseArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_EXPENSE + " where " + KEY_DAY_FOR_EXPENSES + " =? AND " + KEY_MONTH_FOR_EXPENSES + " =? AND "
                + KEY_YEAR_FOR_EXPENSES + " =? ", new String[]{day, month, year});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double price = res.getDouble(res.getColumnIndex(KEY_PRICE));
            int cantity = res.getInt(res.getColumnIndex(KEY_CANTITY));
            String product = res.getString(res.getColumnIndex(KEY_PRODUCT));
            int dayExpense = res.getInt(res.getColumnIndex(KEY_DAY_FOR_EXPENSES));
            String monthExpense = res.getString(res.getColumnIndex(KEY_MONTH_FOR_EXPENSES));
            int yearExpense = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_EXPENSES));

           Expense expense = new Expense(product, price, cantity, dayExpense, monthExpense, yearExpense, id);
            expenseArray.add(expense);
            res.moveToNext();
        }
        return expenseArray;
    }

    public ArrayList<Expense> getExpensesByMonthAndYear(String month, String year) {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Expense> expenseArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_EXPENSE + " where " + KEY_MONTH_FOR_EXPENSES + " =? AND "
                + KEY_YEAR_FOR_EXPENSES + " =? ", new String[]{month, year});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double price = res.getDouble(res.getColumnIndex(KEY_PRICE));
            int cantity = res.getInt(res.getColumnIndex(KEY_CANTITY));
            String product = res.getString(res.getColumnIndex(KEY_PRODUCT));
            int dayExpense = res.getInt(res.getColumnIndex(KEY_DAY_FOR_EXPENSES));
            String monthExpense = res.getString(res.getColumnIndex(KEY_MONTH_FOR_EXPENSES));
            int yearExpense = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_EXPENSES));

            Expense expense = new Expense(product, price, cantity, dayExpense, monthExpense, yearExpense, id);
            expenseArray.add(expense);
            res.moveToNext();
        }
        return expenseArray;
    }

    public ArrayList<Expense> getExpensesByYear(String year) {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Expense> expenseArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_EXPENSE + " where " + KEY_YEAR_FOR_EXPENSES + " =? ", new String[]{year});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double price = res.getDouble(res.getColumnIndex(KEY_PRICE));
            int cantity = res.getInt(res.getColumnIndex(KEY_CANTITY));
            String product = res.getString(res.getColumnIndex(KEY_PRODUCT));
            int dayExpense = res.getInt(res.getColumnIndex(KEY_DAY_FOR_EXPENSES));
            String monthExpense = res.getString(res.getColumnIndex(KEY_MONTH_FOR_EXPENSES));
            int yearExpense = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_EXPENSES));

          Expense expense = new Expense(product, price, cantity, dayExpense, monthExpense, yearExpense, id);
            expenseArray.add(expense);
            res.moveToNext();
        }
        return expenseArray;
    }


    public ArrayList<Income> getAllIncomeValues() throws ParseException {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Income> incomeArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_INCOME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double sum = res.getDouble(res.getColumnIndex(KEY_SUM));
            String type = res.getString(res.getColumnIndex(KEY_TYPE));
            int dayIncome = res.getInt(res.getColumnIndex(KEY_DAY_FOR_INCOMES));
            String monthIncome = res.getString(res.getColumnIndex(KEY_MONTH_FOR_INCOMES));
            int yearIncome = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_INCOMES));

          Income income = new Income(sum, type, dayIncome, monthIncome, yearIncome, id);
            incomeArray.add(income);
            res.moveToNext();
        }
        return incomeArray;
    }

    public ArrayList<Expense> getAllExpenseValues() throws ParseException {
        SQLiteDatabase db = this.ourHelper.getReadableDatabase();
        ArrayList<Expense> expenseArray = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_EXPENSE, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex(KEY_ROWID));
            Double price = res.getDouble(res.getColumnIndex(KEY_PRICE));
            int cantity = res.getInt(res.getColumnIndex(KEY_CANTITY));
            String product = res.getString(res.getColumnIndex(KEY_PRODUCT));
            int dayExpense = res.getInt(res.getColumnIndex(KEY_DAY_FOR_EXPENSES));
            String monthExpense = res.getString(res.getColumnIndex(KEY_MONTH_FOR_EXPENSES));
            int yearExpense = res.getInt(res.getColumnIndex(KEY_YEAR_FOR_EXPENSES));


            Expense expense = new Expense(product, price, cantity, dayExpense, monthExpense, yearExpense, id);
            expenseArray.add(expense);
            res.moveToNext();
        }
        return expenseArray;
    }


}
