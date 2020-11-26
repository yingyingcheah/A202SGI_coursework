package com.example.yingying.moneysaver;

public class Category {
    private String name;
    private int iconResourceId;

    public Category(String name, int iconResourceId){
        this.name = name;
        this.iconResourceId = iconResourceId;
    }

    public String getname(){
        return name;
    }

    public int getIconResourceId(){
        return iconResourceId;
    }
}
