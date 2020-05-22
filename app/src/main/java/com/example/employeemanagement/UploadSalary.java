package com.example.employeemanagement;

public class UploadSalary {
    String basic_salary,house_rent,medical,food,transport,mobile,kpi,incentive,sales_commission,
            other_addition,tax,other_deduction,gross,gmail,name,month,year;

    public UploadSalary(){

    }
    public UploadSalary(String basic_salary, String house_rent, String medical, String food, String transport, String mobile, String kpi, String incentive, String sales_commission, String other_addition, String tax,
                        String other_deduction, String gross, String gmail, String name, String month, String year) {
        this.basic_salary = basic_salary;
        this.house_rent = house_rent;
        this.medical = medical;
        this.food = food;
        this.transport = transport;
        this.mobile = mobile;
        this.kpi = kpi;
        this.incentive = incentive;
        this.sales_commission = sales_commission;
        this.other_addition = other_addition;
        this.tax = tax;
        this.other_deduction = other_deduction;
        this.gross = gross;
        this.gmail = gmail;
        this.name = name;
        this.month = month;
        this.year = year;
    }

    public String getBasic_salary() {
        return basic_salary;
    }

    public void setBasic_salary(String basic_salary) {
        this.basic_salary = basic_salary;
    }

    public String getHouse_rent() {
        return house_rent;
    }

    public void setHouse_rent(String house_rent) {
        this.house_rent = house_rent;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKpi() {
        return kpi;
    }

    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    public String getSales_commission() {
        return sales_commission;
    }

    public void setSales_commission(String sales_commission) {
        this.sales_commission = sales_commission;
    }

    public String getOther_addition() {
        return other_addition;
    }

    public void setOther_addition(String other_addition) {
        this.other_addition = other_addition;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getOther_deduction() {
        return other_deduction;
    }

    public void setOther_deduction(String other_deduction) {
        this.other_deduction = other_deduction;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
