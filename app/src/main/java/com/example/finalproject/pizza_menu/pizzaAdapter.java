package com.example.finalproject.pizza_menu;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.finalproject.models.Pizza;
import com.example.finalproject.preferencesShared.SharedPrefManager;


public class pizzaAdapter extends RecyclerView.Adapter<pizzaAdapter.MyViewPizza>{

    Context context;

    Pizza[] Pizzas;

    int imagesOfPizza[];

    FragmentManager fragmentManager_1;
    View root;
    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
    DataBaseHelper dataBaseHelper;
    Cursor cursor;

    boolean isFavorite[] ;
    public pizzaAdapter(Context c, Pizza[] Pizza,boolean[] isFavorite, int[] imagesOfPizza,FragmentManager f,View root)
    {
        this.context = c;
        this.Pizzas = Pizza;
        this.isFavorite = isFavorite;
        this.imagesOfPizza = imagesOfPizza;
        this.fragmentManager_1 = f;
        this.root = root;
    }



    @NonNull
    @Override
    public MyViewPizza onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.pizzatypes, parent, false);
            return new MyViewPizza(v);

    }



    @NonNull
    @Override
    public void onBindViewHolder(@NonNull MyViewPizza holder, int position) {


        holder.pizzaNames.setText(Pizzas[position].getName());

        dataBaseHelper = new DataBaseHelper(context);


        pizzaDetails pizzaDetails = new pizzaDetails();

        holder.pizzaNames.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("pizzaType", holder.pizzaNames.getText().toString());
            pizzaDetails.setArguments(bundle);

            SearchView searchView = root.findViewById(R.id.search_view);
            searchView.setVisibility(View.INVISIBLE);
            HorizontalScrollView horizontalScrollView = root.findViewById(R.id.horizontal_scroll_view);
            horizontalScrollView.setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = root.findViewById(R.id.pizzaMenuRecyclerView);
            recyclerView.setVisibility(View.INVISIBLE);
            LinearLayout filterSizePrise = root.findViewById(R.id.filterSizePrise);
            filterSizePrise.setVisibility(View.INVISIBLE);

            FragmentTransaction fragmentTransaction = fragmentManager_1.beginTransaction();
            fragmentTransaction.replace(R.id.pizza_menu, pizzaDetails);
            fragmentTransaction.commit();

        });
        holder.pizzaFeavourite.setOnClickListener(v -> {
            if(holder.pizzaFeavourite.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.heart).getConstantState()){
                holder.pizzaFeavourite.setImageResource(R.drawable.heartempty);
                isFavorite[position]= false;
                Cursor getFavoritesByEmail = dataBaseHelper.getFavoritesByEmailAndPizza(sharedPrefManager.readString("logIn email", ""),holder.pizzaNames.getText().toString());
                getFavoritesByEmail.moveToFirst();
                if (!dataBaseHelper.deleteFavorite(sharedPrefManager.readString("logIn email", ""),  holder.pizzaNames.getText().toString(), getFavoritesByEmail.getString(2)))
                    Toast.makeText(context, "Error in removing from favourites", Toast.LENGTH_SHORT).show();

            }
            else if(holder.pizzaFeavourite.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.heartempty).getConstantState()){
                holder.pizzaFeavourite.setImageResource(R.drawable.heart);
                isFavorite[position]= true;
                if (!dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), holder.pizzaNames.getText().toString())){
                    String sizes[] = {"Small", "Medium", "Large"};
                    for (String size : sizes) {
                        cursor = dataBaseHelper.getPizzaByNameAndSize(holder.pizzaNames.getText().toString().trim(), size);
                        if (cursor.moveToFirst()) {
                            if (dataBaseHelper.insertFavorite(sharedPrefManager.readString("logIn email", ""), holder.pizzaNames.getText().toString(), size, 1, Double.parseDouble(cursor.getString(4)),cursor.getString(3))) {
                                break;
                            } else {
                                Toast.makeText(context, "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });
        pizzaOrder pizzaOrder = new pizzaOrder();
        holder.orderButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("pizzaType", holder.pizzaNames.getText().toString());

            pizzaOrder.setArguments(bundle);

            SearchView searchView = root.findViewById(R.id.search_view);
            searchView.setVisibility(View.INVISIBLE);
            HorizontalScrollView horizontalScrollView = root.findViewById(R.id.horizontal_scroll_view);
            horizontalScrollView.setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = root.findViewById(R.id.pizzaMenuRecyclerView);
            recyclerView.setVisibility(View.INVISIBLE);
            LinearLayout filterSizePrise = root.findViewById(R.id.filterSizePrise);
            filterSizePrise.setVisibility(View.INVISIBLE);

            FragmentTransaction fragmentTransaction = fragmentManager_1.beginTransaction();
            fragmentTransaction.replace(R.id.pizza_menu, pizzaOrder);
            fragmentTransaction.commit();
        });

        if(Pizzas[position].getName().equals("Margarita")){
            holder.logos.setImageResource(imagesOfPizza[0]) ;
        }
        else if(Pizzas[position].getName().equals("Neapolitan")){
            holder.logos.setImageResource(imagesOfPizza[1]) ;
        }
        else if (Pizzas[position].getName().equals("Hawaiian")){
            holder.logos.setImageResource(imagesOfPizza[2]) ;
        }
        else if (Pizzas[position].getName().equals("Pepperoni")){
            holder.logos.setImageResource(imagesOfPizza[3]) ;
        }
        else if (Pizzas[position].getName().equals("New York Style")){
            holder.logos.setImageResource(imagesOfPizza[4]) ;
        }
        else if (Pizzas[position].getName().equals("Calzone")){
            holder.logos.setImageResource(imagesOfPizza[5]) ;
        }
        else if (Pizzas[position].getName().equals("Tandoori Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[6]) ;
        }
        else if (Pizzas[position].getName().equals("BBQ Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[7]) ;
        }
        else if (Pizzas[position].getName().equals("Seafood Pizza")){
            holder.logos.setImageResource(imagesOfPizza[8]) ;
        }
        else if (Pizzas[position].getName().equals("Vegetarian Pizza")){
            holder.logos.setImageResource(imagesOfPizza[9]) ;
        }
        else if (Pizzas[position].getName().equals("Buffalo Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[10]) ;
        }
        else if (Pizzas[position].getName().equals("Mushroom Truffle Pizza")){
            holder.logos.setImageResource(imagesOfPizza[11]) ;
        }
        else if (Pizzas[position].getName().equals("Pesto Chicken Pizza")){
            holder.logos.setImageResource(imagesOfPizza[12]) ;
        }



        holder.pizzaCategory.setText(Pizzas[position].getCategory());
        if (isFavorite[position]){
            holder.pizzaFeavourite.setImageResource(R.drawable.heart);
        }else
            holder.pizzaFeavourite.setImageResource(R.drawable.heartempty);




    }



    public class MyViewPizza extends RecyclerView.ViewHolder {

        TextView pizzaNames;
        ImageView logos;
        ImageButton pizzaFeavourite;
        ImageButton orderButton;

        TextView pizzaCategory;



        public MyViewPizza(@NonNull View pizzaView) {
            super(pizzaView);
            pizzaNames = pizzaView.findViewById(R.id.pizzaName);
            logos = pizzaView.findViewById(R.id.pizzaImage);
            pizzaFeavourite = pizzaView.findViewById(R.id.favoriteButton);
            orderButton = pizzaView.findViewById(R.id.orderButton);
            pizzaCategory = pizzaView.findViewById(R.id.pizzaCategory);

        }


    }

    @Override
    public int getItemCount() {
        return Pizzas.length;
    }
}
