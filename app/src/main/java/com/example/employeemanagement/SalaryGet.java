package com.example.employeemanagement;

public class SalaryGet {

    String month,year,gmail;

    public SalaryGet() {
    }

    public SalaryGet(String month, String year, String gmail) {
        this.month = month;
        this.year = year;
        this.gmail = gmail;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
