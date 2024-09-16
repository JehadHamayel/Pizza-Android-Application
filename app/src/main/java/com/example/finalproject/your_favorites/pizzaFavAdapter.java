package com.example.finalproject.your_favorites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.favoritesPizza;
import com.example.finalproject.preferencesShared.SharedPrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class pizzaFavAdapter extends RecyclerView.Adapter<pizzaFavAdapter.MyViewPizza> {

    Context context;

    favoritesPizza favoritePizzas[];

    int imagesOfPizza[];

    String userEmail;
    String pizzaType[];
    int Quantityp[];
    double totalPrice[];


    DataBaseHelper dataBaseHelper;
    String [][] price;
    FragmentManager fragmentManager;
    RecyclerView favRecyclerView;
    TextView totalCostOfFavorites;
    Double totalCost = 0.0;
    Button orderButton;
    View root;
    SharedPrefManager sharedPrefManager;
    public pizzaFavAdapter(View root,FragmentManager fragmentManager, Context c, favoritesPizza[] favoritePizzas, int[] imagesOfPizza) {
        sharedPrefManager = SharedPrefManager.getInstance(c);
        this.context = c;
        this.root = root;
        this.favoritePizzas = favoritePizzas;
        this.imagesOfPizza = imagesOfPizza;
        if (favoritePizzas.length > 0){
        this.userEmail = favoritePizzas[0].getUserEmail();
        this.pizzaType = new String[favoritePizzas.length];
        this.Quantityp = new int[favoritePizzas.length];
        this.totalPrice = new double[favoritePizzas.length];
        for (int i = 0; i < favoritePizzas.length; i++) {
            pizzaType[i] = favoritePizzas[i].getPizzaName();
            Quantityp[i] = favoritePizzas[i].getQuantity();
            totalPrice[i] = favoritePizzas[i].getPrice();
            totalCost += totalPrice[i];
        }
        this.price = new String[favoritePizzas.length][3];
    }else {
            this.userEmail = "";
            this.pizzaType = new String[0];
            this.Quantityp = new int[0];
            this.totalPrice = new double[0];
            this.price = new String[0][3];
            Toast.makeText(context, "No Favorites Added", Toast.LENGTH_SHORT).show();
        }
        this.fragmentManager = fragmentManager;
        this.favRecyclerView = root.findViewById(R.id.favoritesRecyclerView);
        this.totalCostOfFavorites = root.findViewById(R.id.totalCostOfFavorites);
        this.orderButton = root.findViewById(R.id.orderButton);
        this.totalCostOfFavorites.setText("Total Cost: $"+totalCost);

    }



    @NonNull
    @Override
    public MyViewPizza onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.pizza_favorates, parent, false);
        return new MyViewPizza(v);

    }



    @NonNull
    @Override
    public void onBindViewHolder(@NonNull MyViewPizza holder, @SuppressLint("RecyclerView") int position) {
        holder.Quantity = favoritePizzas[position].getQuantity();
        holder.price = favoritePizzas[position].getPrice();

        holder.fav_PizzaName.setText(favoritePizzas[position].getPizzaName());
        holder.fav_PizzaCategory.setText(favoritePizzas[position].getCategory());

        holder.fav_PizzaQuantity.setText(favoritePizzas[position].getQuantity()+"");
        String [] sizesOfPizza = {"Small", "Medium", "Large"};
        String specialSize = "";

        dataBaseHelper = new DataBaseHelper(context);
        Cursor cursor ;
        if (!favoritePizzas[position].isSpecial()) {
            int i = 0;
            for (String size : sizesOfPizza) {

                cursor = dataBaseHelper.getPizzaByNameAndSize(pizzaType[position], size);
                if (cursor.moveToFirst()) {
                    this.price[position][i] = cursor.getString(4).trim();
                } else {
                    this.price[position][i] = "-1";
                }
                i++;
            }
        }else {
            cursor = dataBaseHelper.getSpecialOfferByNameAndSize(pizzaType[position],favoritePizzas[position].getSize());
            if (cursor.moveToFirst()){
                specialSize = cursor.getString(1);
                if (specialSize.equals("Small")){
                    this.price[position][0] = cursor.getString(4).trim();
                    this.price[position][1] = "-1";
                    this.price[position][2] = "-1";
                }else if (specialSize.equals("Medium")){
                    this.price[position][0] = "-1";
                    this.price[position][1] = cursor.getString(4).trim();
                    this.price[position][2] = "-1";
                }else if (specialSize.equals("Large")){
                    this.price[position][0] = "-1";
                    this.price[position][1] = "-1";
                    this.price[position][2] = cursor.getString(4).trim();
                }
            }else {
                Toast.makeText(context, "Error in getting special price", Toast.LENGTH_SHORT).show();
            }
        }
        if (favoritePizzas[position].getSize().equals("Small")){
            holder.favPizzaPrice.setText(price[position][0]+"$");
        } else if (favoritePizzas[position].getSize().equals("Medium")){
            holder.favPizzaPrice.setText(price[position][1]+"$");
        } else if (favoritePizzas[position].getSize().equals("Large")){
            holder.favPizzaPrice.setText(price[position][2]+"$");
        }

        final int[] lastSpinnerPosition = {holder.fav_PizzaSizeSpinner.getSelectedItemPosition()};

        holder.fav_PizzaSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                if (price[position][position1].equals("-1")){
                    Toast.makeText(context, "Not Avalible Size", Toast.LENGTH_SHORT).show();
                    holder.fav_PizzaSizeSpinner.setSelection(lastSpinnerPosition[0]);

                }else {
                    if(dataBaseHelper.updateFavorite(userEmail, favoritePizzas[position].getPizzaName(), sizesOfPizza[holder.fav_PizzaSizeSpinner.getSelectedItemPosition()], Integer.parseInt(holder.fav_PizzaQuantity.getText().toString().trim()), Double.parseDouble(price[position][position1]) * holder.Quantity, holder.fav_PizzaCategory.getText().toString())){
                        holder.favPizzaPrice.setText(price[position][holder.fav_PizzaSizeSpinner.getSelectedItemPosition()]+"$");
                        totalPrice[position] = Double.parseDouble(price[position][position1]) * holder.Quantity;
                        updateData();
                        lastSpinnerPosition[0] = holder.fav_PizzaSizeSpinner.getSelectedItemPosition();
                    }

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.inceaase_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                double price =  Double.parseDouble(holder.favPizzaPrice.getText().toString().replace("$",""));
                holder.Quantity++;
                holder.fav_PizzaQuantity.setText(holder.Quantity+"");
                if(!favoritePizzas[position].isSpecial()){
                    if(dataBaseHelper.updateFavorite(userEmail, favoritePizzas[position].getPizzaName(), sizesOfPizza[holder.fav_PizzaSizeSpinner.getSelectedItemPosition()], holder.Quantity, price*holder.Quantity, holder.fav_PizzaCategory.getText().toString())){
                        totalPrice[position] = price * holder.Quantity;
                        updateData();

                    }else {
                        Toast.makeText(context, "Not Updated Quantity", Toast.LENGTH_SHORT).show();
                    }
                }else {
                        if(dataBaseHelper.updateSpecialFavorite(userEmail, favoritePizzas[position].getPizzaName(), favoritePizzas[position].getSize(), holder.Quantity, price*holder.Quantity, holder.fav_PizzaCategory.getText().toString())){
                            totalPrice[position] = price * holder.Quantity;
                            updateData();
                        }else {
                            Toast.makeText(context, "Not Updated Quantity", Toast.LENGTH_SHORT).show();
                        }

                }
            }
        });
        holder.decaase_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Quantity > 1){
                    double price =  Double.parseDouble(holder.favPizzaPrice.getText().toString().replace("$",""));
                    holder.Quantity--;
                    holder.fav_PizzaQuantity.setText(holder.Quantity+"");
                    if(!favoritePizzas[position].isSpecial()){
                        if(dataBaseHelper.updateFavorite(userEmail, favoritePizzas[position].getPizzaName(), sizesOfPizza[holder.fav_PizzaSizeSpinner.getSelectedItemPosition()], holder.Quantity, price * holder.Quantity, holder.fav_PizzaCategory.getText().toString())){
                            totalPrice[position] = price * holder.Quantity;
                            updateData();
                        }else {
                            Toast.makeText(context, "Not Updated Quantity", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(dataBaseHelper.updateSpecialFavorite(userEmail, favoritePizzas[position].getPizzaName(), favoritePizzas[position].getSize(), holder.Quantity, price * holder.Quantity, holder.fav_PizzaCategory.getText().toString())){
                            totalPrice[position] = price * holder.Quantity;
                            updateData();
                        }else {
                            Toast.makeText(context, "Not Updated Quantity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(context, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cancle_Fav_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favoritePizzas[position].isSpecial()){
                    Cursor getFavoritesByEmail = dataBaseHelper.getFavoritesByEmailAndPizza(userEmail, favoritePizzas[position].getPizzaName());
                    getFavoritesByEmail.moveToFirst();
                    if(dataBaseHelper.deleteFavorite(userEmail, favoritePizzas[position].getPizzaName(), getFavoritesByEmail.getString(2))){
                        updateData();
                    }else {
                        Toast.makeText(context, "Error in deleting", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(dataBaseHelper.deleteSpecialFavorite(userEmail, favoritePizzas[position].getPizzaName(),  favoritePizzas[position].getSize())){
                        updateData();
                    }else {
                        Toast.makeText(context, "Error in deleting", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        orderButton.setOnClickListener(v -> {
            dataBaseHelper = new DataBaseHelper(context);
            boolean Done = false;
            boolean notDone = false;
            Cursor cursor1 = dataBaseHelper.getAllFavorites(userEmail);
            while (cursor1.moveToNext()){
                if(dataBaseHelper.insertOrder(userEmail, cursor1.getString(1), cursor1.getString(2), cursor1.getInt(3), cursor1.getDouble(4), DateFormat.getDateTimeInstance().format(new Date() ).toString())){
                    Done = true;
                }else {
                    notDone = true;
                }
            }
            if (!notDone && Done){
                if(!dataBaseHelper.deleteAllFavorites(userEmail)){
                    Toast.makeText(context, "Error in placing order", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Order Placed For all Pizza how are not special", Toast.LENGTH_SHORT).show();
                }
                updateData();
            }else if (Done && notDone) {
                Toast.makeText(context, "Error in placing order", Toast.LENGTH_SHORT).show();
            }else if (!Done && notDone) {
                Toast.makeText(context, "Error in placing order", Toast.LENGTH_SHORT).show();
            }
            boolean end = false;
            boolean notStart = false;
            boolean DoneSpecial = false;
            boolean notDoneSpecial = false;
            ArrayList<String> ordersHasEnded = new ArrayList<>();
            ArrayList<String> ordersNotStarted = new ArrayList<>();
            cursor1 = dataBaseHelper.getAllSpecialFavorites(userEmail);
            while (cursor1.moveToNext()){

                String orderDate = "";
                try {
                    // Storing the date and time in dataOfOrder
                    String dataOfOrder = DateFormat.getDateTimeInstance().format(new Date() ).toString() ;

                    // Create a SimpleDateFormat for parsing the stored date and time
                    DateFormat parser = DateFormat.getDateTimeInstance();

                    // Parse the dataOfOrder back to a Date object
                    Date date = parser.parse(dataOfOrder);

                    // Creating date format for date and time separately
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    // Extracting date and time
                    orderDate= dateFormat.format(date);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Cursor cursor2 = dataBaseHelper.getSpecialOfferByNameAndSize(cursor1.getString(1), cursor1.getString(2));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                cursor2.moveToNext();
                String startDatestr = cursor2.getString(5);
                String endDatestr = cursor2.getString(6);
                LocalDate date = LocalDate.parse(orderDate, formatter);
                LocalDate startDate = LocalDate.parse(startDatestr, formatter);
                LocalDate endDate = LocalDate.parse(endDatestr, formatter);
                if(!date.isBefore(startDate) && !date.isAfter(endDate)){
                    if(dataBaseHelper.insertSpecialOrder(userEmail, cursor1.getString(1), cursor1.getString(2), cursor1.getInt(3), cursor1.getDouble(4), DateFormat.getDateTimeInstance().format(new Date() ).toString())){
                        DoneSpecial = true;
                        dataBaseHelper.deleteSpecialFavorite(userEmail, cursor1.getString(1), cursor1.getString(2));
                        updateData();
                        cursor1 = dataBaseHelper.getAllSpecialFavorites(userEmail);
                    }else {
                        notDoneSpecial = true;
                    }
                }else if (date.isBefore(startDate)){
                    notStart = true;
                    ordersNotStarted.add(cursor1.getString(1) + " " + cursor1.getString(2));
                }else if (date.isAfter(endDate)){
                    dataBaseHelper.deleteSpecialFavorite(userEmail, cursor1.getString(1), cursor1.getString(2));
                    updateData();
                    cursor1 = dataBaseHelper.getAllSpecialFavorites(userEmail);
                    end = true;
                    ordersHasEnded.add(cursor1.getString(1) + " " + cursor1.getString(2));
                }

            }
            if (end){
                Set<String> setWithUniqueValues = new HashSet<>(ordersHasEnded);
                List<String> uniqueList = new ArrayList<>(setWithUniqueValues);
                String ordersHas_Ended = "";
                for (String s : uniqueList){
                    ordersHas_Ended += s + "\n";
                }
                Toast.makeText(context, "Not Ordered Special Offers has ended for: \n" + ordersHas_Ended, Toast.LENGTH_SHORT).show();
            }
            if (notStart){
                Set<String> setWithUniqueValues = new HashSet<>(ordersNotStarted);
                List<String> uniqueList = new ArrayList<>(setWithUniqueValues);
                String ordersNot_Started = "";
                for (String s : uniqueList){
                    ordersNot_Started += s + "\n";
                }
                Toast.makeText(context, "Special Offers has not started for: \n" + ordersNot_Started, Toast.LENGTH_SHORT).show();
            }
            if (!notDoneSpecial && DoneSpecial){
                Toast.makeText(context, "Special Offers Placed", Toast.LENGTH_SHORT).show();
            }else if (DoneSpecial && notDoneSpecial) {
                Toast.makeText(context, "Error in placing order", Toast.LENGTH_SHORT).show();
            }
            else if(!DoneSpecial && !notDoneSpecial && !Done && !notDone) {
                Toast.makeText(context, "No Orders to Place", Toast.LENGTH_SHORT).show();
            }
            else if (DoneSpecial && !notDoneSpecial) {
                Toast.makeText(context, "Special Offers Placed", Toast.LENGTH_SHORT).show();
            }else if (!DoneSpecial && notDoneSpecial) {
                Toast.makeText(context, "Error in placing order", Toast.LENGTH_SHORT).show();
            }



        });

        if(favoritePizzas[position].isSpecial()){
            holder.specialImage.setImageResource(R.drawable.offer);
            holder.specialImage.startAnimation(AnimationUtils.loadAnimation(context,R.anim.spacial_scale_animation));
        }

        if (favoritePizzas[position].getSize().equals("Small")){
            holder.fav_PizzaSizeSpinner.setSelection(0);
        }
        else if (favoritePizzas[position].getSize().equals("Medium")){
            holder.fav_PizzaSizeSpinner.setSelection(1);
        }
        else if (favoritePizzas[position].getSize().equals("Large")){
            holder.fav_PizzaSizeSpinner.setSelection(2);
        }

        if(favoritePizzas[position].getPizzaName().equals("Margarita")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[0]) ;
        }
        else if(favoritePizzas[position].getPizzaName().equals("Neapolitan")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[1]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Hawaiian")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[2]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Pepperoni")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[3]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("New York Style")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[4]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Calzone")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[5]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Tandoori Chicken Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[6]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("BBQ Chicken Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[7]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Seafood Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[8]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Vegetarian Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[9]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Buffalo Chicken Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[10]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Mushroom Truffle Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[11]) ;
        }
        else if (favoritePizzas[position].getPizzaName().equals("Pesto Chicken Pizza")){
            holder.fav_PizzaImage.setImageResource(imagesOfPizza[12]) ;
        }




    }


    // Function to update the data
    public void updateData() {
        ArrayList<favoritesPizza> favoritePizzasArrayList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        Cursor cursor = dataBaseHelper.getAllFavorites(userEmail);
        while (cursor.moveToNext()){
            favoritePizzasArrayList.add(new favoritesPizza(cursor.getString(0),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),cursor.getString(5),false));
        }
        cursor = dataBaseHelper.getAllSpecialFavorites(userEmail);
        while (cursor.moveToNext()){
            favoritePizzasArrayList.add(new favoritesPizza(cursor.getString(0),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),cursor.getString(5),true));
        }
        favoritesPizza[] favoritePizzasArray = new favoritesPizza[favoritePizzasArrayList.size()];
        favoritePizzasArray = favoritePizzasArrayList.toArray(favoritePizzasArray);
        totalCost = 0.0;
        if (favoritePizzasArrayList.size() > 0){
            for (int i = 0; i < favoritePizzasArrayList.size(); i++) {
                totalCost += favoritePizzasArrayList.get(i).getPrice();
            }
        }
        else{
            totalCost = 0.0;
        }

        totalCost = Math.round(totalCost * 100.0) / 100.0;
        totalCostOfFavorites.setText("Total Cost: $"+totalCost);

        this.favoritePizzas = favoritePizzasArray;
        if (this.favoritePizzas.length > 0){
            this.pizzaType = new String[this.favoritePizzas.length];
            this.Quantityp = new int[this.favoritePizzas.length];
            this.totalPrice = new double[this.favoritePizzas.length];
            for (int i = 0; i < this.favoritePizzas.length; i++) {
                this.pizzaType[i] = this.favoritePizzas[i].getPizzaName();
                this.Quantityp[i] = this.favoritePizzas[i].getQuantity();
                this.totalPrice[i] = this.favoritePizzas[i].getPrice();
                this.totalCost += this.totalPrice[i];
            }
        }else {
            this.pizzaType = new String[0];
            this.Quantityp = new int[0];
            this.totalPrice = new double[0];
        }
        notifyDataSetChanged();
    }
    public class MyViewPizza extends RecyclerView.ViewHolder {

        TextView fav_PizzaName;
        TextView fav_PizzaCategory;

        ImageView fav_PizzaImage;
        ImageView specialImage;
        ImageButton cancle_Fav_Button;
        ImageButton inceaase_Button;
        ImageButton decaase_Button;
        Spinner fav_PizzaSizeSpinner;
        TextView fav_PizzaQuantity;
        TextView favPizzaPrice;

        int Quantity;
        double price;


        public MyViewPizza(@NonNull View pizzaView) {
            super(pizzaView);

            fav_PizzaName = pizzaView.findViewById(R.id.favPizzaName);
            fav_PizzaImage = pizzaView.findViewById(R.id.favPizzaImage);
            cancle_Fav_Button = pizzaView.findViewById(R.id.cancleFavButton);
            fav_PizzaCategory = pizzaView.findViewById(R.id.favPizzaCategory);
            inceaase_Button = pizzaView.findViewById(R.id.increaseButton);
            decaase_Button = pizzaView.findViewById(R.id.decreaseButton);
            fav_PizzaSizeSpinner = pizzaView.findViewById(R.id.favPizzaSizeSpinner);
            String pizzaSizes[] = {"Small","Medium","Large"};
            ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, pizzaSizes);
            fav_PizzaSizeSpinner.setAdapter(objGenderArr);
            fav_PizzaQuantity = pizzaView.findViewById(R.id.favPizzaQuantity);
            favPizzaPrice = pizzaView.findViewById(R.id.favPizzaPrice);
            specialImage = pizzaView.findViewById(R.id.specialImage);

        }


    }

    @Override
    public int getItemCount() {
        return favoritePizzas.length;
    }
}

