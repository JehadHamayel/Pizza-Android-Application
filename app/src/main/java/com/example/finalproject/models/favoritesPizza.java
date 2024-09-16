package com.example.finalproject.models;

public class favoritesPizza {
    private String userEmail;
    private String pizzaName;
    private String size;
    private int quantity;
    private double price;
    private String category;
    private boolean isSpecial;
    public favoritesPizza(String userEmail, String pizzaName, String size, int quantity, double price, String category,boolean isSpecial) {
        this.userEmail = userEmail;
        this.pizzaName = pizzaName;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.isSpecial = isSpecial;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userName) {
        this.userEmail = userName;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }
}
