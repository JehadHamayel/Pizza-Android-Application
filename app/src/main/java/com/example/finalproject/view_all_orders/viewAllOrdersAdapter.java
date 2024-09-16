package com.example.finalproject.view_all_orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.orderdPizza;


public class viewAllOrdersAdapter extends RecyclerView.Adapter<viewAllOrdersAdapter.MyViewPizza> {
    Context context;
    orderdPizza[] orderdPizzas;
    int[] imagesOfPizza;



    public viewAllOrdersAdapter( Context c, orderdPizza[] allOrderdPizzas, int[] imagesOfPizza) {
        this.context = c;
        this.orderdPizzas = allOrderdPizzas;
        this.imagesOfPizza = imagesOfPizza;
        if(orderdPizzas.length == 0){
            Toast.makeText(context, "No Orders Added", Toast.LENGTH_SHORT).show();
        }

    }
    @NonNull
    @Override
    public MyViewPizza onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.user_orders, parent, false);
        return new MyViewPizza(v);

    }

    @Override
    public void onBindViewHolder(@NonNull viewAllOrdersAdapter.MyViewPizza holder, @SuppressLint("RecyclerView") int position) {

        holder.userName.setText(orderdPizzas[position].getUserName());
        holder.userEmail.setText("Email: " + orderdPizzas[position].getUserEmail());
        holder.orderdPizzaName.setText(orderdPizzas[position].getPizzaName());
        holder.orderdPizzaCategory.setText("Category: " + orderdPizzas[position].getCategory());
        holder.orderdId.setText("Order ID: "+orderdPizzas[position].getOrderId());
        holder.orderdPizzaSize.setText("Sizes: " + orderdPizzas[position].getPizzaSize());
        holder.orderdPizzaQuantity.setText("Quantity: " + orderdPizzas[position].getQuantity());
        holder.orderdPizzaPrice.setText("Total Price: " + orderdPizzas[position].getPrice());
        holder.orderingDate.setText(orderdPizzas[position].getOrderDate());
        holder.orderingTime.setText(orderdPizzas[position].getOrderTime());

        if (orderdPizzas[position].isSpecialOrder()){
            holder.specialImage.setImageResource(R.drawable.offer);
            holder.specialImage.startAnimation(AnimationUtils.loadAnimation(context,R.anim.spacial_scale_animation));

        }else {
            holder.specialImage.setImageResource(0);
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
        ImageView specialImage;
        TextView userName;
        TextView userEmail;
        TextView orderdPizzaName;
        TextView orderdPizzaCategory;
        TextView orderdId;
        TextView orderdPizzaSize;
        TextView orderdPizzaQuantity;
        TextView orderdPizzaPrice;
        TextView orderingDate;
        TextView orderingTime;
        public MyViewPizza(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);
            specialImage = itemView.findViewById(R.id.specialImage);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            orderdPizzaName = itemView.findViewById(R.id.pizzaName);
            orderdPizzaCategory = itemView.findViewById(R.id.orderdPizzaCategory);
            orderdId = itemView.findViewById(R.id.orderId);
            orderdPizzaSize = itemView.findViewById(R.id.orderdPizzaSize);
            orderdPizzaQuantity = itemView.findViewById(R.id.orderdPizzaQuantity);
            orderdPizzaPrice = itemView.findViewById(R.id.orderdPizzaTotalPrice);
            orderingDate = itemView.findViewById(R.id.orderdPizzaOrderedDate);
            orderingTime = itemView.findViewById(R.id.orderdPizzaOrderedTime);



        }
    }
    @Override
    public int getItemCount() {
        return orderdPizzas.length;
    }
}
