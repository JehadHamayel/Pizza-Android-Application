package com.example.finalproject.view_all_orders;

public class reportPizzaTypes {
    String pizzaType;
    String category;
    String size;
    String numberOfOrders;
    String totalIncome;
    boolean isSpecial;
    public reportPizzaTypes(String pizzaType, String category, String size, String numberOfOrders, String totalIncome, boolean isSpecial) {
        this.pizzaType = pizzaType;
        this.category = category;
        this.size = size;
        this.numberOfOrders = numberOfOrders;
        this.totalIncome = totalIncome;
        this.isSpecial = isSpecial;
    }

    public String getPizzaType() {
        return pizzaType;
    }

    public void setPizzaType(String pizzaType) {
        this.pizzaType = pizzaType;
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

    public String getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(String numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }
}
