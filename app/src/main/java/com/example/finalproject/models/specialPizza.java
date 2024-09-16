package com.example.finalproject.models;

public class specialPizza {
    private String name;
    private String description;
    private String category;
    private String size;
    private double oldPrice;
    private double newPrice;
    private String startDate;
    private String endDate;


    public specialPizza(String name, String size,String description, String category,  double oldPrice,double newPrice,String startDate,String endDate) {
        this.name = name;
        this.size = size;
        this.description = description;
        this.category = category;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
