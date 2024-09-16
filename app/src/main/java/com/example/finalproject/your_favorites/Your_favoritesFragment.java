package com.example.finalproject.your_favorites;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.databinding.FragmentYourFavoritesBinding;
import com.example.finalproject.models.favoritesPizza;

import java.util.ArrayList;

public class Your_favoritesFragment extends Fragment {

    private FragmentYourFavoritesBinding binding;
    SharedPrefManager sharedPrefManager ;

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

        binding = FragmentYourFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefManager =SharedPrefManager.getInstance(getContext());

        FragmentManager fragmentManager = getParentFragmentManager();
        ArrayList<favoritesPizza> favoritePizzas = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        Cursor cursor = dataBaseHelper.getAllFavorites(sharedPrefManager.readString("logIn email",""));
        while (cursor.moveToNext()){
            favoritePizzas.add(new favoritesPizza(cursor.getString(0),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),cursor.getString(5),false));
        }
        cursor = dataBaseHelper.getAllSpecialFavorites(sharedPrefManager.readString("logIn email",""));
        while (cursor.moveToNext()){
            favoritePizzas.add(new favoritesPizza(cursor.getString(0),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),cursor.getString(5),true));
        }
        favoritesPizza[] favoritePizzasArray = new favoritesPizza[favoritePizzas.size()];
        favoritePizzasArray = favoritePizzas.toArray(favoritePizzasArray);

        pizzaFavAdapter pizzaFavoAdapter = new pizzaFavAdapter(root,fragmentManager,getContext(),favoritePizzasArray,imagesOfPizza);
        binding.favoritesRecyclerView.setAdapter(pizzaFavoAdapter);
        binding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}