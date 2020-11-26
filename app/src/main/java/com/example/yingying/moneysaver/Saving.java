package com.example.yingying.moneysaver;

public class Saving {
    private String id;
    private String category;
    private String title;
    private double amount;
    private String date;
    private String remarks;

    public Saving(){

    }

    public Saving(String id, String category, String title, double amount, String date, String remarks) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.remarks = remarks;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getRemarks(){
        return remarks;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }
}
