package com.example.finalproject.tasksConnectionAsync;

import com.example.finalproject.models.Pizza;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PizzaJsonParser {

    public static List<Pizza> parseJson(String json) {

        List<Pizza> pizzaList = null;
        try {
            JSONObject root = new JSONObject(json);
            pizzaList = new ArrayList<>();

            JSONArray types = root.getJSONArray("types");
            for (int i = 0; i < types.length(); i++) {
                String name = types.getString(i);
                String description = getDiscrption(name);
                String category = getCategory(name);
                String sizes[];
                if (name.equals("Margarita")) {
                    sizes = new String[]{"Medium","Large"};
                }else if (name.equals("Mushroom Truffle Pizza")) {
                    sizes = new String[]{"Small","Medium"};
                }else if (name.equals("Pesto Chicken Pizza")) {
                    sizes = new String[]{"Small","Large"};
                }else {
                     sizes = new String[]{"Small", "Medium", "Large"};
                }
                for (String size : sizes) {
                    double price = getPrice(size, name);
                    Pizza pizza = new Pizza(name,size, description, category , price);
                    pizzaList.add(pizza);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pizzaList;
    }

    private static String getDiscrption(String type) {


        String description = "";
        switch (type) {
            case "Margarita":
                description = "A classic Italian pizza topped with tomato sauce, mozzarella cheese, fresh basil leaves, and olive oil. It's known for its simplicity and traditional flavors.";
                break;
            case "Neapolitan":
                description = "Originating from Naples, Italy, Neapolitan pizza features a thin crust, tomato sauce made from San Marzano tomatoes, fresh mozzarella cheese, basil leaves, and olive oil. It's traditionally cooked in a wood-fired oven.";
                break;
            case "Hawaiian":
                description = "This pizza typically includes tomato sauce, mozzarella cheese, ham or Canadian bacon, and pineapple chunks. It's known for its sweet and savory flavor combination.";
                break;
            case "Pepperoni":
                description = "A popular pizza topping, pepperoni is a type of salami made from cured pork and beef. Pepperoni pizza usually consists of tomato sauce, mozzarella cheese, and slices of pepperoni.";
                break;
            case "New York Style":
                description = "Characterized by its large, foldable slices, New York-style pizza has a thin crust that's crispy on the edges but soft and pliable in the center. It's typically topped with tomato sauce and mozzarella cheese, with various toppings available.";
                break;
            case "Calzone":
                description = "A folded pizza that resembles a turnover or a half-moon shape. It's made from pizza dough stuffed with ingredients like ricotta cheese, mozzarella cheese, tomato sauce, and various fillings such as meats and vegetables.";
                break;
            case "Tandoori Chicken Pizza":
                description = "Inspired by Indian cuisine, this pizza features a spicy tandoori chicken topping, along with traditional pizza ingredients like tomato sauce, mozzarella cheese, onions, and sometimes bell peppers.";
                break;
            case "BBQ Chicken Pizza":
                description = "This pizza swaps traditional tomato sauce for barbecue sauce and is topped with grilled or roasted chicken, red onions, mozzarella cheese, and sometimes additional toppings like bacon or cilantro.";
                break;
            case "Seafood Pizza":
                description = "A pizza topped with an assortment of seafood, such as shrimp, clams, mussels, or squid, along with tomato sauce, mozzarella cheese, and sometimes garlic or other seasonings.";
                break;
            case "Vegetarian Pizza":
                description = "A pizza that excludes meat and seafood toppings, featuring a variety of vegetables such as bell peppers, onions, mushrooms, olives, spinach, and artichokes, often accompanied by tomato sauce and mozzarella cheese.";
                break;
            case "Buffalo Chicken Pizza":
                description = "Similar to buffalo chicken wings, this pizza features spicy buffalo sauce-coated chicken pieces, along with mozzarella cheese, sometimes blue cheese or ranch dressing, and typically includes celery or celery salt as a garnish.";
                break;
            case "Mushroom Truffle Pizza":
                description = "A gourmet pizza topped with a combination of various mushrooms (such as cremini, shiitake, or porcini), truffle oil or truffle paste, mozzarella cheese, and sometimes additional ingredients like caramelized onions or garlic.";
                break;
            case "Pesto Chicken Pizza":
                description = "Instead of tomato sauce, this pizza is topped with basil pesto sauce, grilled or roasted chicken pieces, mozzarella cheese, and often additional ingredients such as cherry tomatoes or sun-dried tomatoes.";
                break;
        }
        return description;
    }

    private static String getCategory(String type) {

        String category = "";
        switch (type) {
            case "Margarita":
                category = "Vegetarian";
                break;
            case "Neapolitan":
                category = "Vegetarian";
                break;
            case "Hawaiian":
                category = "Others";
                break;
            case "Pepperoni":
                category = "Beef";
                break;
            case "New York Style":
                category = "Others";
                break;
            case "Calzone":
                category = "Others";
                break;
            case "Tandoori Chicken Pizza":
                category = "Chicken";
                break;
            case "BBQ Chicken Pizza":
                category = "Chicken";
                break;
            case "Seafood Pizza":
                category = "Others";
                break;
            case "Vegetarian Pizza":
                category = "Vegetarian";
                break;
            case "Buffalo Chicken Pizza":
                category = "Chicken";
                break;
            case "Mushroom Truffle Pizza":
                category = "Vegetarian";
                break;
            case "Pesto Chicken Pizza":
                category = "Chicken";
                break;
        }
        return category;
    }

    private static double getPrice(String size, String type) {

        double price = 0;
        switch (size) {
            case "Small":
                switch (type) {
                    case "Margarita":
                        price = 8.99;
                        break;
                    case "Neapolitan":
                        price = 9.99;
                        break;
                    case "Hawaiian":
                        price = 10.99;
                        break;
                    case "Pepperoni":
                        price = 9.99;
                        break;
                    case "New York Style":
                        price = 10.99;
                        break;
                    case "Calzone":
                        price = 11.99;
                        break;
                    case "Tandoori Chicken Pizza":
                        price = 12.99;
                        break;
                    case "BBQ Chicken Pizza":
                        price = 11.99;
                        break;
                    case "Seafood Pizza":
                        price = 13.99;
                        break;
                    case "Vegetarian Pizza":
                        price = 10.99;
                        break;
                    case "Buffalo Chicken Pizza":
                        price = 12.99;
                        break;
                    case "Mushroom Truffle Pizza":
                        price = 13.99;
                        break;
                    case "Pesto Chicken Pizza":
                        price = 12.99;
                        break;
                }
                break;
            case "Medium":
                switch (type) {
                    case "Margarita":
                        price = 11.99;
                        break;
                    case "Neapolitan":
                        price = 12.99;
                        break;
                    case "Hawaiian":
                        price = 13.99;
                        break;
                    case "Pepperoni":
                        price = 12.99;
                        break;
                    case "New York Style":
                        price = 13.99;
                        break;
                    case "Calzone":
                        price = 14.99;
                        break;
                    case "Tandoori Chicken Pizza":
                        price = 15.99;
                        break;
                    case "BBQ Chicken Pizza":
                        price = 14.99;
                        break;
                    case "Seafood Pizza":
                        price = 16.99;
                        break;
                    case "Vegetarian Pizza":
                        price = 13.99;
                        break;
                    case "Buffalo Chicken Pizza":
                        price = 15.99;
                        break;
                    case "Mushroom Truffle Pizza":
                        price = 16.99;
                        break;
                    case "Pesto Chicken Pizza":
                        price = 15.99;
                        break;
                }
                break;
            case "Large":
                switch (type) {
                    case "Margarita":
                        price = 14.99;
                        break;
                    case "Neapolitan":
                        price = 15.99;
                        break;
                    case "Hawaiian":
                        price = 16.99;
                        break;
                    case "Pepperoni":
                        price = 15.99;
                        break;
                    case "New York Style":
                        price = 16.99;
                        break;
                    case "Calzone":
                        price = 17.99;
                        break;
                    case "Tandoori Chicken Pizza":
                        price = 18.99;
                        break;
                    case "BBQ Chicken Pizza":
                        price = 17.99;
                        break;
                    case "Seafood Pizza":
                        price = 19.99;
                        break;
                    case "Vegetarian Pizza":
                        price = 16.99;
                        break;
                    case "Buffalo Chicken Pizza":
                        price = 18.99;
                        break;
                    case "Mushroom Truffle Pizza":
                        price = 19.99;
                        break;
                    case "Pesto Chicken Pizza":
                        price = 18.99;
                        break;
                }
                break;
        }
        return price;
    }
}