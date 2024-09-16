package com.example.finalproject.your_orders;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.databinding.FragmentYourOrdersBinding;
import com.example.finalproject.models.orderdPizza;

import java.util.ArrayList;

public class Your_ordersFragment extends Fragment {

    private FragmentYourOrdersBinding binding;
    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
    int imagesOfPizza[] = {
            R.drawable.margarita,
            R.drawable.neapolitan,
            R.drawable.hawaiian,
            R.drawable.pepperoni,
            R.drawable.newyorkstyle,
            R.drawable.calzone,
            R.drawable.tandoorichickenpizza,
            R.drawable.bbqchickenpizza,
            R.drawable.seafoodpizza,
            R.drawable.vegetarianpizza,
            R.drawable.buffalochickenpizza,
            R.drawable.mushroomtrufflepizza,
            R.drawable.pestochickenpizza

    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentYourOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ArrayList<orderdPizza> orderdPizzas = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        Cursor cursor = dataBaseHelper.getAllOrdersByEmail(sharedPrefManager.readString("logIn email", ""));
        Cursor cursor1 ;
        String category;
        String pizzaDescription;
        while (cursor.moveToNext()) {
            cursor1 = dataBaseHelper.getPizzaByName(cursor.getString(2));
            cursor1.moveToFirst();
            category = cursor1.getString(3);
            pizzaDescription = cursor1.getString(2);
            orderdPizzas.add(new orderdPizza(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Double.parseDouble(cursor.getString(5)), cursor.getString(6),category,pizzaDescription,false));
        }
        ArrayList<orderdPizza> specialOrders = new ArrayList<>();
        cursor = dataBaseHelper.getAllSpecialOrdersByEmail(sharedPrefManager.readString("logIn email", ""));

        while (cursor.moveToNext()) {
            cursor1 = dataBaseHelper.getPizzaByName(cursor.getString(2));
            cursor1.moveToFirst();
            category = cursor1.getString(3);
            pizzaDescription = cursor1.getString(2);
            orderdPizzas.add(new orderdPizza(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Double.parseDouble(cursor.getString(5)), cursor.getString(6),category,pizzaDescription,true));
        }
        orderdPizzaAdapter orderdPizzaAdapter = new orderdPizzaAdapter(root, getParentFragmentManager(), getContext(), orderdPizzas.toArray(new orderdPizza[orderdPizzas.size()]), imagesOfPizza);
        binding.yourOrdersRecyclerView.setAdapter(orderdPizzaAdapter);
        binding.yourOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}