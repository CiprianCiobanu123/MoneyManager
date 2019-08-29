package com.second.moneymanager;
import android.os.Parcel;
import android.os.Parcelable;


public class Income implements Parcelable {
    private double sum;
    private String type;
    private int dayIncome;
    private String monthIncome;
    private int yearIncome;
    private String id;
    public Income (double sum, String type, int dayIncome, String monthIncome, int yearIncome, String id){
        this.sum = sum;
        this.type= type;
        this.dayIncome= dayIncome;
        this.monthIncome=monthIncome;
        this.yearIncome=yearIncome;
        this.id = id;
    }


    public Income(double sum, String type, String monthIncome, int yearIncome, String id){
        this.sum = sum;
        this.type= type;
        this.monthIncome=monthIncome;
        this.yearIncome=yearIncome;
        this.id = id;
    }


    public Income(double sum, String type, int yearIncome, String id){
        this.sum = sum;
        this.type= type;
        this.yearIncome=yearIncome;
        this.id = id;
    }

    protected Income(Parcel in) {
        sum = in.readDouble();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(sum);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Income> CREATOR = new Creator<Income>() {
        @Override
        public Income createFromParcel(Parcel in) {
            return new Income(in);
        }

        @Override
        public Income[] newArray(int size) {
            return new Income[size];
        }
    };

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Income{" +
                "sum=" + sum +
                ", type='" + type + '\'' +
                ", dayIncome=" + dayIncome +
                ", monthIncome=" + monthIncome +
                ", yearIncome=" + yearIncome +
                '}';
    }

    public int getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(int dayIncome) {
        this.dayIncome = dayIncome;
    }

    public String getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(String monthIncome) {
        this.monthIncome = monthIncome;
    }

    public int getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(int yearIncome) {
        this.yearIncome = yearIncome;
    }


}
