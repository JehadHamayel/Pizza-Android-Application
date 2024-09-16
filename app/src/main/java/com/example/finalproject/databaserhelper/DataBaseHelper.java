package com.example.finalproject.databaserhelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalproject.models.Hash;
import com.example.finalproject.models.Pizza;
import com.example.finalproject.models.User;


public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PizzaRestDBV0.db";
    private static final int DATABASE_VERSION = 1;
    private static String ADMIN_EMAIL = "admin@admin.com";
    private static String ADMIN_PASSWORD = Hash.hashPassword("admin@1234");
    private static String ADMIN_FIRST_NAME = "admin";
    private static String ADMIN_LAST_NAME = "admin";
    private static String ADMIN_PHONE_NUMBER = "0598765432";
    private static String GENDER = "Male";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PIZZA(NAME TEXT ,SIZE TEXT,DESCRIPTION TEXT, CATEGORY TEXT,  PRICE DOUBLE,PRIMARY KEY (NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE USER(E_MAIL TEXT PRIMARY KEY, FIRST_NAME TEXT, LAST_NAME TEXT, PHONE_NUMBER TEXT, GENDER TEXT, PASSWORD TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE ADMINS(E_MAIL TEXT PRIMARY KEY, FIRST_NAME TEXT, LAST_NAME TEXT, PHONE_NUMBER TEXT,GENDER TEXT, PASSWORD TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE FAVORITES(USER_EMAIL TEXT, PIZZA_NAME TEXT, PIZZA_SIZE TEXT,QUANTITY INTEGER,PRICE DOUBLE, CATEGORY TEXT ,PRIMARY KEY (USER_EMAIL, PIZZA_NAME, PIZZA_SIZE), FOREIGN KEY (USER_EMAIL) REFERENCES USER(E_MAIL), FOREIGN KEY (PIZZA_NAME, PIZZA_SIZE) REFERENCES PIZZA(NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE ORDERS(ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_EMAIL TEXT, PIZZA_NAME TEXT, PIZZA_SIZE TEXT, QUANTITY INTEGER, PRICE DOUBLE ,ORDER_DATE TEXT, FOREIGN KEY (USER_EMAIL) REFERENCES USER(E_MAIL), FOREIGN KEY (PIZZA_NAME, PIZZA_SIZE) REFERENCES PIZZA(NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE SPECIAL_OFFERS(NAME TEXT ,SIZE TEXT, DESCRIPTION TEXT, CATEGORY TEXT,  PRICE DOUBLE, START_DATE TEXT, END_DATE TEXT, PRIMARY KEY (NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE SPECIAL_FAVORITES(USER_EMAIL TEXT, PIZZA_NAME TEXT, PIZZA_SIZE TEXT,QUANTITY INTEGER,PRICE DOUBLE, CATEGORY TEXT ,PRIMARY KEY (USER_EMAIL, PIZZA_NAME, PIZZA_SIZE), FOREIGN KEY (USER_EMAIL) REFERENCES USER(E_MAIL), FOREIGN KEY (PIZZA_NAME, PIZZA_SIZE) REFERENCES PIZZA(NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE SPECIAL_ORDERS(ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_EMAIL TEXT, PIZZA_NAME TEXT, PIZZA_SIZE TEXT, QUANTITY INTEGER, PRICE DOUBLE ,ORDER_DATE TEXT, FOREIGN KEY (USER_EMAIL) REFERENCES USER(E_MAIL), FOREIGN KEY (PIZZA_NAME, PIZZA_SIZE) REFERENCES PIZZA(NAME, SIZE))");

        insertStaticAdmin(sqLiteDatabase);
    }
    private void insertStaticAdmin(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("E_MAIL", ADMIN_EMAIL);
        contentValues.put("FIRST_NAME", ADMIN_FIRST_NAME);
        contentValues.put("LAST_NAME", ADMIN_LAST_NAME);
        contentValues.put("PHONE_NUMBER", ADMIN_PHONE_NUMBER);
        contentValues.put("PASSWORD", ADMIN_PASSWORD);
        contentValues.put("GENDER" , GENDER);
        db.insert("ADMINS", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertPizza(Pizza pizza) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", pizza.getName());
        contentValues.put("SIZE", pizza.getSize());
        contentValues.put("DESCRIPTION", pizza.getDescription());
        contentValues.put("CATEGORY", pizza.getCategory());
        contentValues.put("PRICE", pizza.getPrice());
        if(sqLiteDatabase.insert("PIZZA", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }
            public Cursor getPizzaByName(String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA WHERE NAME = ?", new String[]{name});
    }
    public Cursor getPizzaByNameAndSize(String name, String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA WHERE NAME = ? AND SIZE = ?", new String[]{name, size});
    }


    public boolean insertFavorite(String email, String name, String size, int quantity, double price, String category) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("PIZZA_NAME", name);
        contentValues.put("PIZZA_SIZE", size);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("PRICE", price);
        contentValues.put("CATEGORY", category);
        if(sqLiteDatabase.insert("FAVORITES", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean deleteFavorite(String email, String name, String size) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("FAVORITES", "USER_EMAIL = ? AND PIZZA_NAME = ? AND PIZZA_SIZE = ?", new String[]{email, name, size}) != -1;
    }
    public boolean isExistFavorite(String email, String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM FAVORITES WHERE USER_EMAIL = ? AND PIZZA_NAME = ?", new String[]{email, name});
        return cursor.getCount() != 0;
    }
    public Cursor getFavoritesByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM FAVORITES WHERE USER_EMAIL = ?", new String[]{email});
    }
    public Cursor getFavoritesByEmailAndPizza(String email, String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM FAVORITES WHERE USER_EMAIL = ? AND PIZZA_NAME = ?", new String[]{email, name});
    }
    public Cursor getAllFavorites(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM FAVORITES WHERE USER_EMAIL = ?", new String[]{email});
    }
    public boolean deleteAllFavorites(String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("FAVORITES", "USER_EMAIL = ?", new String[]{email}) != -1;
    }
    public boolean updateFavorite(String email, String name, String newSize, int quantity, double price, String category) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("PIZZA_NAME", name);
        contentValues.put("PIZZA_SIZE", newSize);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("PRICE", price);
        contentValues.put("CATEGORY", category);
        return sqLiteDatabase.update("FAVORITES", contentValues, "USER_EMAIL = ? AND PIZZA_NAME = ?", new String[]{email, name}) != -1;
    }
    public boolean insertOrder(String email, String name, String size, int quantity,  double price,String orderDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("PIZZA_NAME", name);
        contentValues.put("PIZZA_SIZE", size);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("PRICE", price);
        contentValues.put("ORDER_DATE", orderDate);
        if(sqLiteDatabase.insert("ORDERS", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getAllOrdersByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERS WHERE USER_EMAIL = ?", new String[]{email});
    }
    public Cursor getAllOrders() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERS", null);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("E_MAIL", user.getEmail());
        contentValues.put("FIRST_NAME", user.getFirstName());
        contentValues.put("LAST_NAME", user.getLastName());
        contentValues.put("PHONE_NUMBER", user.getPhoneNumber());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("PASSWORD", user.getPassword());
        if(sqLiteDatabase.insert("USER", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean updateUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("E_MAIL", user.getEmail());
        contentValues.put("FIRST_NAME", user.getFirstName());
        contentValues.put("LAST_NAME", user.getLastName());
        contentValues.put("PHONE_NUMBER", user.getPhoneNumber());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("PASSWORD", user.getPassword());
        return sqLiteDatabase.update("USER", contentValues, "E_MAIL = ?", new String[]{user.getEmail()}) != -1;
    }
    public boolean insertAdmin(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("E_MAIL", user.getEmail());
        contentValues.put("FIRST_NAME", user.getFirstName());
        contentValues.put("LAST_NAME", user.getLastName());
        contentValues.put("PHONE_NUMBER", user.getPhoneNumber());
        contentValues.put("Gender", user.getGender());
        contentValues.put("PASSWORD", user.getPassword());
        if(sqLiteDatabase.insert("ADMINS", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean updateAdmin(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("E_MAIL", user.getEmail());
        contentValues.put("FIRST_NAME", user.getFirstName());
        contentValues.put("LAST_NAME", user.getLastName());
        contentValues.put("PHONE_NUMBER", user.getPhoneNumber());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("PASSWORD", user.getPassword());
        return sqLiteDatabase.update("ADMINS", contentValues, "E_MAIL = ?", new String[]{user.getEmail()}) != -1;
    }

    public Cursor getAdminByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ADMINS WHERE E_MAIL = ?", new String[]{email});
    }
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE E_MAIL = ?", new String[]{email});
    }
    public Cursor getAllPizzas() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA", null);
    }


    public Cursor getAllCategoriesUniquely() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT DISTINCT CATEGORY FROM PIZZA", null);
    }
    public Cursor getAllPizzasByCategory(String Category) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT DISTINCT NAME FROM PIZZA WHERE CATEGORY = ?";
        return sqLiteDatabase.rawQuery(query, new String[]{Category});
    }
    public boolean insertSpecialOffer(String name, String size, String description, String category, double price, String startDate, String endDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("SIZE", size);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("CATEGORY", category);
        contentValues.put("PRICE", price);
        contentValues.put("START_DATE", startDate);
        contentValues.put("END_DATE", endDate);
        if(sqLiteDatabase.insert("SPECIAL_OFFERS", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean isExistSpecialOffer(String name, String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_OFFERS WHERE NAME = ? AND SIZE = ?", new String[]{name, size});
        return cursor.getCount() != 0;
    }
    public boolean updateSpecialOffer(String name, String size, String description, String category, double price, String startDate, String endDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("SIZE", size);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("CATEGORY", category);
        contentValues.put("PRICE", price);
        contentValues.put("START_DATE", startDate);
        contentValues.put("END_DATE", endDate);
        return sqLiteDatabase.update("SPECIAL_OFFERS", contentValues, "NAME = ? AND SIZE = ?", new String[]{name, size}) != -1;
    }
    public Cursor getAllSpecialOffers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_OFFERS", null);
    }
    public Cursor getSpecialOfferByNameAndSize(String name,String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_OFFERS WHERE NAME = ? AND SIZE = ?", new String[]{name, size});
    }
    public Cursor getSpecialFavoritesByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_FAVORITES WHERE USER_EMAIL = ?", new String[]{email});
    }
    public boolean insertSpecialFavorite(String email, String name, String size, int quantity, double price, String category) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("PIZZA_NAME", name);
        contentValues.put("PIZZA_SIZE", size);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("PRICE", price);
        contentValues.put("CATEGORY", category);
        if(sqLiteDatabase.insert("SPECIAL_FAVORITES", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean isExistSpecialFavorite(String email, String name,String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_FAVORITES WHERE USER_EMAIL = ? AND PIZZA_NAME = ? AND PIZZA_SIZE = ?", new String[]{email, name, size});
        return cursor.getCount() != 0;
    }

    public boolean deleteSpecialFavorite(String email, String name, String size) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("SPECIAL_FAVORITES", "USER_EMAIL = ? AND PIZZA_NAME = ? AND PIZZA_SIZE = ?", new String[]{email, name, size}) != -1;
    }


    public Cursor getSpecialFavoritesByEmailAndPizzaAndSize(String email, String name, String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_FAVORITES WHERE USER_EMAIL = ? AND PIZZA_NAME = ? AND PIZZA_SIZE = ?", new String[]{email, name, size});
    }
    public  Cursor getAllSpecialFavorites(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_FAVORITES WHERE USER_EMAIL = ?", new String[]{email});
    }
    public Cursor getAllSpecialFavoritesOfAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_FAVORITES", null);
    }



    public boolean updateSpecialFavorite(String email, String name, String size, int newQuantity, double newPrice, String category) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("PIZZA_NAME", name);
        contentValues.put("PIZZA_SIZE", size);
        contentValues.put("QUANTITY", newQuantity);
        contentValues.put("PRICE", newPrice);
        contentValues.put("CATEGORY", category);
        return sqLiteDatabase.update("SPECIAL_FAVORITES", contentValues, "USER_EMAIL = ? AND PIZZA_NAME = ? AND PIZZA_SIZE = ?", new String[]{email, name, size}) != -1;
    }


    public boolean insertSpecialOrder(String email, String name, String size, int quantity,  double price,String orderDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("PIZZA_NAME", name);
        contentValues.put("PIZZA_SIZE", size);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("PRICE", price);
        contentValues.put("ORDER_DATE", orderDate);
        if(sqLiteDatabase.insert("SPECIAL_ORDERS", null, contentValues) == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getAllSpecialOrdersByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_ORDERS WHERE USER_EMAIL = ?", new String[]{email});
    }
    public Cursor getAllSpecialOrders() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL_ORDERS", null);
    }
    public boolean deleteSpecialOffer(String name, String size) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("SPECIAL_OFFERS", "NAME = ? AND SIZE = ?", new String[]{name, size}) != -1;
    }


    public Cursor getNumberOfOrdersForEachPizzaType(String name, String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT PIZZA.CATEGORY, SUM(ORDERS.QUANTITY) AS NUMBER_OF_ORDERS, SUM(ORDERS.PRICE) AS TOTAL_INCOME " +
                "FROM ORDERS " +
                "INNER JOIN PIZZA ON ORDERS.PIZZA_NAME = PIZZA.NAME AND ORDERS.PIZZA_SIZE = PIZZA.SIZE " +
                "WHERE ORDERS.PIZZA_NAME = ? AND ORDERS.PIZZA_SIZE = ? " +
                "GROUP BY PIZZA.CATEGORY";
        return sqLiteDatabase.rawQuery(query, new String[]{name, size});
    }
    public Cursor getNumberOfSpecialOrdersForEachPizzaType(String name, String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT PIZZA.CATEGORY, SUM(SPECIAL_ORDERS.QUANTITY) AS NUMBER_OF_ORDERS, SUM(SPECIAL_ORDERS.PRICE) AS TOTAL_INCOME " +
                "FROM SPECIAL_ORDERS " +
                "INNER JOIN PIZZA ON SPECIAL_ORDERS.PIZZA_NAME = PIZZA.NAME AND SPECIAL_ORDERS.PIZZA_SIZE = PIZZA.SIZE " +
                "WHERE SPECIAL_ORDERS.PIZZA_NAME = ? AND SPECIAL_ORDERS.PIZZA_SIZE = ? " +
                "GROUP BY PIZZA.CATEGORY";
        return sqLiteDatabase.rawQuery(query, new String[]{name, size});
    }

    public Cursor getSizeOfPizza(String selectedCategory, String selectedName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT DISTINCT SIZE FROM PIZZA WHERE CATEGORY = ? AND NAME = ?";
        return sqLiteDatabase.rawQuery(query, new String[]{selectedCategory, selectedName});
    }

    public Cursor getPizzaPrice(String selectedCategory, String selectedName, String selectedSize){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT PRICE FROM PIZZA WHERE CATEGORY = ? AND NAME = ? AND SIZE = ?";
        return sqLiteDatabase.rawQuery(query, new String[]{selectedCategory, selectedName, selectedSize});
    }

    public Cursor getDistinctDescription(String selectedCategory, String selectedName, String selectedSize){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT DESCRIPTION FROM PIZZA WHERE CATEGORY = ? AND NAME = ? AND SIZE = ?";
        return sqLiteDatabase.rawQuery(query, new String[]{selectedCategory, selectedName, selectedSize});
    }

}

