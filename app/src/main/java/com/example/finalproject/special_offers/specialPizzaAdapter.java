package com.example.finalproject.special_offers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.specialPizza;
import com.example.finalproject.preferencesShared.SharedPrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class specialPizzaAdapter extends RecyclerView.Adapter<specialPizzaAdapter.MyViewPizza>{

    Context context;

    com.example.finalproject.models.specialPizza[] specialPizza;

    int imagesOfPizza[];

    FragmentManager fragmentManager_1;
    View root;
    SharedPrefManager sharedPrefManager ;
    DataBaseHelper dataBaseHelper;
    Cursor cursor;

    boolean isFavorite[] ;
    public specialPizzaAdapter(Context c, specialPizza[] specialPizzas,boolean[] isFavorites, int[] imagesOfPizza,FragmentManager f,View root)
    {
        this.sharedPrefManager = SharedPrefManager.getInstance(context);
        this.context = c;

        this.imagesOfPizza = imagesOfPizza;
        this.fragmentManager_1 = f;
        this.root = root;
        String orderDate ;

        if (specialPizzas.length == 0){
            Toast.makeText(context, "No special pizza available", Toast.LENGTH_SHORT).show();
            this.specialPizza = specialPizzas;
            this.isFavorite = isFavorites;
        }else {
            ArrayList<specialPizza> specialPizzaArrayList = new ArrayList<>();
            ArrayList<Boolean> isSpecialPizza = new ArrayList<>();
            for (int i = 0; i < specialPizzas.length; i++) {
                specialPizzaArrayList.add(specialPizzas[i]);
            }
            for (int i = 0; i < isFavorites.length; i++) {
                isSpecialPizza.add(isFavorites[i]);
            }
            ArrayList<Integer> removedIndexs = new ArrayList<>();
            for (int i = 0; i < specialPizzaArrayList.size(); i++) {
                orderDate = "";
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String endDatestr = specialPizzaArrayList.get(i).getEndDate();
                LocalDate date = LocalDate.parse(orderDate, formatter);
                LocalDate endDate = LocalDate.parse(endDatestr, formatter);
                dataBaseHelper = new DataBaseHelper(context);

                if(date.isAfter(endDate)){
                    dataBaseHelper.deleteSpecialFavorite(sharedPrefManager.readString("logIn email", ""),  specialPizzaArrayList.get(i).getName(), specialPizzaArrayList.get(i).getSize() );
                    dataBaseHelper.deleteSpecialOffer(specialPizzaArrayList.get(i).getName(), specialPizzaArrayList.get(i).getSize());
                    removedIndexs.add(i);

                }
            }

            for (int i = 0; i < removedIndexs.size(); i++) {
                specialPizzaArrayList.remove(removedIndexs.get(i));
                isSpecialPizza.remove(removedIndexs.get(i));
            }
            this.specialPizza = new specialPizza[specialPizzaArrayList.size()];
            this.isFavorite = new boolean[isSpecialPizza.size()];
            for (int i = 0; i < specialPizzaArrayList.size(); i++) {
                this.specialPizza[i] = specialPizzaArrayList.get(i);
            }
            for (int i = 0; i < isSpecialPizza.size(); i++) {
                this.isFavorite[i] = isSpecialPizza.get(i);
            }

        }
    }



    @NonNull
    @Override
    public specialPizzaAdapter.MyViewPizza onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.special_pizza, parent, false);
        return new specialPizzaAdapter.MyViewPizza(v,fragmentManager_1,root);

    }



    @NonNull
    @Override
    public void onBindViewHolder(@NonNull specialPizzaAdapter.MyViewPizza holder, int position) {

        dataBaseHelper = new DataBaseHelper(context);
        holder.pizzaNames.setText(specialPizza[position].getName());
        holder.pizzaCategory.setText(specialPizza[position].getCategory());
        if (isFavorite[position]){
            holder.pizzaFeavourite.setImageResource(R.drawable.heart);
        }
        holder.specialImage.startAnimation(AnimationUtils.loadAnimation(context,R.anim.spacial_scale_animation));
        holder.pizzaSize.setText("Size: "+specialPizza[position].getSize());
        holder.oldPrice.setText(specialPizza[position].getOldPrice()+"$");
        holder.oldPrice.setTextColor(Color.RED);
        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.newPrice.setText(specialPizza[position].getNewPrice()+"$");
        holder.newPrice.setTextColor(Color.GREEN);

        holder.pizzaFeavourite.setOnClickListener(v -> {
            if(holder.pizzaFeavourite.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.heart).getConstantState()){
                holder.pizzaFeavourite.setImageResource(R.drawable.heartempty);
                isFavorite[position]= false;
                if (!dataBaseHelper.deleteSpecialFavorite(sharedPrefManager.readString("logIn email", ""),  specialPizza[position].getName(), specialPizza[position].getSize()))
                    Toast.makeText(context, "Error in removing from favourites", Toast.LENGTH_SHORT).show();

            }
            else if(holder.pizzaFeavourite.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.heartempty).getConstantState()){
                holder.pizzaFeavourite.setImageResource(R.drawable.heart);
                isFavorite[position]= true;
                if(!dataBaseHelper.isExistSpecialFavorite(sharedPrefManager.readString("logIn email", ""), specialPizza[position].getName(), specialPizza[position].getSize()))
                    if (!dataBaseHelper.insertSpecialFavorite(sharedPrefManager.readString("logIn email", ""), specialPizza[position].getName(), specialPizza[position].getSize(), 1, specialPizza[position].getNewPrice(),specialPizza[position].getCategory())) {
                        Toast.makeText(context, "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                    }

            }
        });
        specialDetails specialDetails = new specialDetails();


        holder.pizzaNames.setOnClickListener(v -> {

            goToSpecialDetails(specialPizza[position].getName(),specialPizza[position].getSize(),fragmentManager_1,specialDetails);

        });


        holder.orderButton.setOnClickListener(v -> {
            goToSpecialDetails(specialPizza[position].getName(),specialPizza[position].getSize(),fragmentManager_1,specialDetails);
        });


        if(specialPizza[position].getName().equals("Margarita")){
            holder.logos.setImageResource(imagesOfPizza[0]) ;
        }
        else if(specialPizza[position].getName().equals("Neapolitan")){
            holder.logos.setImageResource(imagesOfPizza[1]) ;
        }
        else if (specialPizza[position].getName().equals("Hawaiian")){
            holder.logos.setImageResource(imagesOfPizza[2]) ;
        }
        else if (specialPizza[position].getName().equals("Pepperoni")){
            holder.logos.setImageResource(imagesOfPizza[3]) ;
        }
        else if (specialPizza[position].getName().equals("New York Style")){
            holder.logos.setImageResource(imagesOfPizza[4]) ;
        }
        else if (specialPizza[position].getName().equals("Calzone")){
            holder.logos.setImageResource(imagesOfPizza[5]) ;
        }
        else if (specialPizza[position].getName().equals("Tandoori Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[6]) ;
        }
        else if (specialPizza[position].getName().equals("BBQ Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[7]) ;
        }
        else if (specialPizza[position].getName().equals("Seafood Pizza")){
            holder.logos.setImageResource(imagesOfPizza[8]) ;
        }
        else if (specialPizza[position].getName().equals("Vegetarian Pizza")){
            holder.logos.setImageResource(imagesOfPizza[9]) ;
        }
        else if (specialPizza[position].getName().equals("Buffalo Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[10]) ;
        }
        else if (specialPizza[position].getName().equals("Mushroom Truffle Pizza")){
            holder.logos.setImageResource(imagesOfPizza[11]) ;
        }
        else if (specialPizza[position].getName().equals("Pesto Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[12]) ;
        }








    }



    public class MyViewPizza extends RecyclerView.ViewHolder {

        TextView pizzaNames;
        ImageView logos;
        ImageView specialImage;
        ImageButton pizzaFeavourite;
        ImageButton orderButton;

        TextView pizzaCategory;
        TextView pizzaSize;
        TextView oldPrice;
        TextView newPrice;



        public MyViewPizza(@NonNull View pizzaView,FragmentManager fragmentManager,View root) {
            super(pizzaView);

            pizzaNames = pizzaView.findViewById(R.id.pizzaName);
            logos = pizzaView.findViewById(R.id.pizzaImage);
            specialImage = pizzaView.findViewById(R.id.specialImage);
            pizzaFeavourite = pizzaView.findViewById(R.id.favoriteButton);
            orderButton = pizzaView.findViewById(R.id.orderButton);
            pizzaCategory = pizzaView.findViewById(R.id.pizzaCategory);
            pizzaSize = pizzaView.findViewById(R.id.pizzaSize);
            oldPrice = pizzaView.findViewById(R.id.oldPrice);
            newPrice = pizzaView.findViewById(R.id.newPrice);

        }


    }
    public void goToSpecialDetails(String pizzaNames,String pizzaSize ,FragmentManager fragmentManager, specialDetails specialDetails){

        Bundle bundle = new Bundle();
        bundle.putString("pizzaType", pizzaNames);
        bundle.putString("pizzaSize", pizzaSize);
        specialDetails.setArguments(bundle);

        SearchView searchView = root.findViewById(R.id.search_view);
        searchView.setVisibility(View.INVISIBLE);
        HorizontalScrollView horizontalScrollView = root.findViewById(R.id.horizontal_scroll_view);
        horizontalScrollView.setVisibility(View.INVISIBLE);
        RecyclerView recyclerView = root.findViewById(R.id.specialPizzaMenuRecyclerView);
        recyclerView.setVisibility(View.INVISIBLE);
        LinearLayout filterSizePrise = root.findViewById(R.id.filterSizePrise);
        filterSizePrise.setVisibility(View.INVISIBLE);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.special_pizza_menu, specialDetails);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return specialPizza.length;
    }
}
