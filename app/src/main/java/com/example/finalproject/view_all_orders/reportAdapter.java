package com.example.finalproject.view_all_orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

public class reportAdapter extends RecyclerView.Adapter<reportAdapter.MyViewPizza>{

    Context context;

    reportPizzaTypes[] reportPizzaTypes;

    FragmentManager fragmentManager;
    View root;
    int imagesOfPizza[];

    public reportAdapter(Context context,reportPizzaTypes[] reportPizzaTypes,FragmentManager fragmentManager, View root, int[] imagesOfPizza) {
        this.context = context;
        this.reportPizzaTypes = reportPizzaTypes;
        this.fragmentManager = fragmentManager;
        this.root = root;
        this.imagesOfPizza = imagesOfPizza;
        if(reportPizzaTypes.length == 0){
            Toast.makeText(context, "No Orders Added", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public reportAdapter.MyViewPizza onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.report_pizza, parent, false);
        return new reportAdapter.MyViewPizza(v,fragmentManager,root);
    }

    @Override
    public void onBindViewHolder(@NonNull reportAdapter.MyViewPizza holder, int position) {
        holder.pizzaName.setText(reportPizzaTypes[position].getPizzaType());
        holder.orderdPizzaCategory.setText(reportPizzaTypes[position].getCategory());
        holder.orderdPizzaSize.setText(reportPizzaTypes[position].getSize());
        holder.orderdPizzaNumberOfOrders.setText(String.valueOf(reportPizzaTypes[position].getNumberOfOrders()));
        holder.orderdPizzaTotalIncome.setText(String.valueOf(reportPizzaTypes[position].getTotalIncome()));
        if (reportPizzaTypes[position].isSpecial()){
            holder.specialImage.setImageResource(R.drawable.offer);
            holder.specialImage.startAnimation(AnimationUtils.loadAnimation(context,R.anim.spacial_scale_animation));

        }else {
            holder.specialImage.setImageResource(0);
        }

        if(reportPizzaTypes[position].getPizzaType().equals("Margarita")){
            holder.pizzaImage.setImageResource(imagesOfPizza[0]) ;
        }
        else if(reportPizzaTypes[position].getPizzaType().equals("Neapolitan")){
            holder.pizzaImage.setImageResource(imagesOfPizza[1]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Hawaiian")){
            holder.pizzaImage.setImageResource(imagesOfPizza[2]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Pepperoni")){
            holder.pizzaImage.setImageResource(imagesOfPizza[3]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("New York Style")){
            holder.pizzaImage.setImageResource(imagesOfPizza[4]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Calzone")){
            holder.pizzaImage.setImageResource(imagesOfPizza[5]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Tandoori Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[6]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("BBQ Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[7]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Seafood Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[8]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Vegetarian Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[9]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Buffalo Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[10]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Mushroom Truffle Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[11]) ;
        }
        else if (reportPizzaTypes[position].getPizzaType().equals("Pesto Chicken Pizza")){
            holder.pizzaImage.setImageResource(imagesOfPizza[12]) ;
        }
    }



    public class MyViewPizza extends RecyclerView.ViewHolder{
        ImageView pizzaImage;
        ImageView specialImage;
        TextView pizzaName;
        TextView orderdPizzaCategory;
        TextView orderdPizzaSize;
        TextView orderdPizzaNumberOfOrders;
        TextView orderdPizzaTotalIncome;

        public MyViewPizza(@NonNull View pizzaView, FragmentManager fragmentManager, View root) {
            super(pizzaView);
            pizzaImage = pizzaView.findViewById(R.id.pizzaImage);
            specialImage = pizzaView.findViewById(R.id.specialImage);
            pizzaName = pizzaView.findViewById(R.id.pizzaName);
            orderdPizzaCategory = pizzaView.findViewById(R.id.orderdPizzaCategory);
            orderdPizzaSize = pizzaView.findViewById(R.id.orderdPizzaSize);
            orderdPizzaNumberOfOrders = pizzaView.findViewById(R.id.numberOfOrders);
            orderdPizzaTotalIncome = pizzaView.findViewById(R.id.totalIncome);
        }

    }
    @Override
    public int getItemCount() {
        return reportPizzaTypes.length;
    }
}
