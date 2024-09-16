package com.example.finalproject.your_orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.orderdPizza;

public class orderdPizzaAdapter extends RecyclerView.Adapter<orderdPizzaAdapter.MyViewPizza> {
    Context context;
    orderdPizza[] orderdPizzas;
    int[] imagesOfPizza;
    View root;
    FragmentManager fragmentManager;


    public orderdPizzaAdapter(View root, FragmentManager fragmentManager, Context c, orderdPizza[] orderdPizzas, int[] imagesOfPizza) {
        this.context = c;
        this.orderdPizzas = orderdPizzas;
        this.imagesOfPizza = imagesOfPizza;
        this.root = root;
        this.fragmentManager = fragmentManager;
        if (orderdPizzas.length == 0)
            Toast.makeText(context, "No Orders Added", Toast.LENGTH_SHORT).show();
            
    }


    @NonNull
    @Override
    public MyViewPizza onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.order_details, parent, false);
        return new MyViewPizza(v);

    }

    @Override
    public void onBindViewHolder(@NonNull orderdPizzaAdapter.MyViewPizza holder, @SuppressLint("RecyclerView") int position) {
        holder.orderdPizzaName.setText(orderdPizzas[position].getPizzaName());
        holder.orderingDate.setText(orderdPizzas[position].getOrderDate());
        holder.orderingTime.setText(orderdPizzas[position].getOrderTime());
        holder.orderdPizzaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderedPizzaDetails orderedPizzaDetails = new orderedPizzaDetails();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderdPizzas[position].getOrderId());
                bundle.putString("userEmail", orderdPizzas[position].getUserEmail());
                bundle.putString("pizzaName", orderdPizzas[position].getPizzaName());
                bundle.putString("pizzaSize", orderdPizzas[position].getPizzaSize());
                bundle.putInt("quantity", orderdPizzas[position].getQuantity());
                bundle.putDouble("price", orderdPizzas[position].getPrice());
                bundle.putString("orderDate", orderdPizzas[position].getOrderDate());
                bundle.putString("orderTime", orderdPizzas[position].getOrderTime());
                bundle.putString("category", orderdPizzas[position].getCategory());
                bundle.putString("pizzaDescription", orderdPizzas[position].getPizzaDescription());
                bundle.putString("specialOrder", String.valueOf(orderdPizzas[position].isSpecialOrder()));
                orderedPizzaDetails.setArguments(bundle);
                RecyclerView recyclerView = root.findViewById(R.id.yourOrdersRecyclerView);
                recyclerView.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.your_orders, orderedPizzaDetails);
                fragmentTransaction.commit();
            }
        });
        if (orderdPizzas[position].isSpecialOrder()){
            holder.offerImage.setImageResource(R.drawable.offer);
            holder.offerImage.startAnimation(AnimationUtils.loadAnimation(context,R.anim.spacial_scale_animation));

        }
        else {
            holder.offerImage.setImageResource(0);
        }

        if(orderdPizzas[position].getPizzaName().equals("Margarita")){
            holder.pizzaImage.setImageResource(imagesOfPizza[0]) ;
        }
        else if(orderdPizzas[position].getPizzaName().equals("Neapolitan")){
            holder.pizzaImage.setImageResource(imagesOfPizza[1]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Hawaiian")){
            holder.pizzaImage.setImageResource(imagesOfPizza[2]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Pepperoni")){
            holder.pizzaImage.setImageResource(imagesOfPizza[3]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("New York Style")){
            holder.pizzaImage.setImageResource(imagesOfPizza[4]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Calzone")){
            holder.pizzaImage.setImageResource(imagesOfPizza[5]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Tandoori Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[6]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("BBQ Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[7]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Seafood Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[8]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Vegetarian Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[9]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Buffalo Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[10]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Mushroom Truffle Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[11]) ;
        }
        else if (orderdPizzas[position].getPizzaName().equals("Pesto Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[12]) ;
        }
    }

    public class MyViewPizza extends RecyclerView.ViewHolder {
        ImageView pizzaImage;
        ImageView offerImage;
        TextView orderdPizzaName;
        TextView orderingDate;
        TextView orderingTime;

        public MyViewPizza(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);
            orderdPizzaName = itemView.findViewById(R.id.orderdPizzaName);
            orderingDate = itemView.findViewById(R.id.orderingDate);
            orderingTime = itemView.findViewById(R.id.orderingTime);
            offerImage = itemView.findViewById(R.id.offerImage);
        }
    }
    @Override
    public int getItemCount() {
        return orderdPizzas.length;
    }
}
