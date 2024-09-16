package com.example.finalproject.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class orderdPizza {
    private String orderId;
    private String userEmail;
    private String userName;
    private String pizzaName;
    private String pizzaSize;
    private int quantity;
    private double price;
    private String orderDate;
    private String orderTime;
    private String category;
    private String pizzaDescription;

    boolean specialOrder = false;



    public orderdPizza(String orderId, String userEmail, String pizzaName, String pizzaSize, int quantity, double price, String orderDate, String category, String pizzaDescription, boolean specialOrder) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.pizzaName = pizzaName;
        this.pizzaSize = pizzaSize;
        this.quantity = quantity;
        this.price = price;
        try {
            // Storing the date and time in dataOfOrder
            String dataOfOrder = orderDate;

            // Create a SimpleDateFormat for parsing the stored date and time
            DateFormat parser = DateFormat.getDateTimeInstance();

            // Parse the dataOfOrder back to a Date object
            Date date = parser.parse(dataOfOrder);

            // Creating date format for date and time separately
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            // Extracting date and time
            this.orderDate= dateFormat.format(date);
            this.orderTime = timeFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.category = category;
        this.pizzaDescription = pizzaDescription;
        this.specialOrder = specialOrder;
    }

    public orderdPizza(String orderId, String userEmail,String userName, String pizzaName, String pizzaSize, int quantity, double price, String orderDate, String category, boolean specialOrder) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.pizzaName = pizzaName;
        this.pizzaSize = pizzaSize;
        this.quantity   = quantity;
        this.price = price;
        try {
            // Storing the date and time in dataOfOrder
            String dataOfOrder = orderDate;

            // Create a SimpleDateFormat for parsing the stored date and time
            DateFormat parser = DateFormat.getDateTimeInstance();

            // Parse the dataOfOrder back to a Date object
            Date date = parser.parse(dataOfOrder);

            // Creating date format for date and time separately
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            // Extracting date and time
            this.orderDate= dateFormat.format(date);
            this.orderTime = timeFormat.format(date);


        } catch (Exception e) {
            e.printStackTrace();
        }
        this.specialOrder = specialOrder;
        this.category = category;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
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

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public String getPizzaDescription() {
        return pizzaDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPizzaDescription(String pizzaDescription) {
        this.pizzaDescription = pizzaDescription;
    }

    public boolean isSpecialOrder() {
        return specialOrder;
    }

    public void setSpecialOrder(boolean specialOrder) {
        this.specialOrder = specialOrder;
    }
}
