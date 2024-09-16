package com.example.finalproject.view_all_orders;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentViewAllOrdersBinding;
import com.example.finalproject.models.orderdPizza;

import java.util.ArrayList;

public class ViewAllOrdersFragment extends Fragment {


    private FragmentViewAllOrdersBinding binding;
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

        binding = FragmentViewAllOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ArrayList<orderdPizza> orderdPizzas = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        Cursor cursor = dataBaseHelper.getAllOrders();
        Cursor cursor1;
        Cursor cursor2;
        String category;
        String userName;
        while (cursor.moveToNext()) {
            cursor1 = dataBaseHelper.getPizzaByName(cursor.getString(2));
            cursor2 = dataBaseHelper.getUserByEmail(cursor.getString(1));
            cursor1.moveToFirst();
            cursor2.moveToFirst();
            category = cursor1.getString(3);
            userName = cursor2.getString(1) + " " + cursor2.getString(2);
            orderdPizzas.add(new orderdPizza(cursor.getString(0), cursor.getString(1),userName , cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Double.parseDouble(cursor.getString(5)), cursor.getString(6), category,false));
        }

        cursor = dataBaseHelper.getAllSpecialOrders();
        while (cursor.moveToNext()) {
            cursor1 = dataBaseHelper.getPizzaByName(cursor.getString(2));
            cursor2 = dataBaseHelper.getUserByEmail(cursor.getString(1));
            cursor1.moveToFirst();
            cursor2.moveToFirst();
            category = cursor1.getString(3);
            userName = cursor2.getString(1) + " " + cursor2.getString(2);
            orderdPizzas.add(new orderdPizza(cursor.getString(0), cursor.getString(1),userName , cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(5), cursor.getString(6), category,true));
        }

        viewAllOrdersAdapter viewAllOrdersAdapter = new viewAllOrdersAdapter(getContext(), orderdPizzas.toArray(new orderdPizza[orderdPizzas.size()]), imagesOfPizza);
        binding.viewAllOrdersRecyclerView.setAdapter(viewAllOrdersAdapter);
        binding.viewAllOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.reportButton.setOnClickListener(v -> {
            reportOfPizzaTypes reportOfPizzaTypes = new reportOfPizzaTypes();
            RecyclerView viewAllOrdersRecyclerView = root.findViewById(R.id.viewAllOrdersRecyclerView);
            viewAllOrdersRecyclerView.setVisibility(View.INVISIBLE);
            Button reportButton = root.findViewById(R.id.reportButton);
            reportButton.setVisibility(View.INVISIBLE);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewAllOrdersFragment, reportOfPizzaTypes);
            fragmentTransaction.commit();

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}