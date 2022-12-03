package com.example.completeandroiduserkit;


public class ListItem {
    private String date;
    private String time;
    private String desc;
    private String amount;
    private String user;
    private String name;
    private String type;
    private String tIncome;
    private String tExpense;
    private String tBalance;

    public ListItem(String date, String time, String desc, String amount, String user, String name, String type, String tIncome, String tExpense, String tBalance) {
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.amount = amount;
        this.user = user;
        this.name = name;
        this.type = type;
        this.tIncome = tIncome;
        this.tExpense = tExpense;
        this.tBalance = tBalance;
    }

    public ListItem() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String gettIncome() {
        return tIncome;
    }

    public void settIncome(String tIncome) {
        this.tIncome = tIncome;
    }

    public String gettExpense() {
        return tExpense;
    }

    public void settExpense(String tExpense) {
        this.tExpense = tExpense;
    }

    public String gettBalance() {
        return tBalance;
    }

    public void settBalance(String tBalance) {
        this.tBalance = tBalance;
    }
}
