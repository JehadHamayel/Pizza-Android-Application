package com.example.finalproject.models;

public class Pizza {
    private String name;
    private String description;
    private String category;
    private String size;
    private double price;
    public Pizza(String name,String category) {
        this.name = name;
        this.category = category;
    }
    public Pizza(String name, String size,String description, String category,  double price) {
        this.name = name;
        this.size = size;
        this.description = description;
        this.category = category;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
