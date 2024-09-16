package com.example.finalproject.pizza_menu;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.Pizza;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.databinding.FragmentPizzaMenuBinding;

import java.util.ArrayList;


public class PizzaMenuFragment extends Fragment {

    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
    String pizzaNames[];
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

    Button all_button;

    Button chicken_button;
    Button beef_button;
    Button veggies_button;
    Button others_button;
    SearchView searchView;
    Spinner sizeSpinner;
    EditText searchPrice;
    String selectedSize;
    Double selectedPrice;

    final int[] chickedButton = {0};
    final String[] filterText = {""};
    DataBaseHelper dataBaseHelper;
    final Cursor[] allPizzasCursor = {null};
    FragmentManager fragmentManager;
    View root;
    private FragmentPizzaMenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataBaseHelper = new DataBaseHelper(getContext());
        binding = FragmentPizzaMenuBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        fragmentManager = getParentFragmentManager();

        pizzaNames = getResources().getStringArray(R.array.pizza_names);
        all_button = root.findViewById(R.id.all_button);
        chicken_button = root.findViewById(R.id.chicken_button);
        beef_button = root.findViewById(R.id.beef_button);
        veggies_button = root.findViewById(R.id.veggies_button);
        others_button = root.findViewById(R.id.others_button);
        searchView = root.findViewById(R.id.search_view);
        searchPrice = root.findViewById(R.id.MaxPrice);
        selectedPrice = 1000.0;
        searchView.clearFocus();
        chickedButton[0] = 0;
        filterText[0] = "";
        sizeSpinner = root.findViewById(R.id.sizeSpinner);
        String[] sizes = {"All Sizes", "Small", "Medium", "Large"};
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sizes);
        sizeSpinner.setAdapter(objGenderArr);
        selectedSize = "All Sizes";
        filter(filterText[0], selectedSize);


        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals("")) { // For backspace
                    return source;
                }
                if (source.toString().matches("[0-9.]+")) {
                    if (source.toString().contains(".")) {
                        if (dest.toString().contains(".")) {
                            return ""; // Only one decimal point allowed
                        }
                    }
                    return source;
                }
                return "";
            }
        };
        searchPrice.setFilters(new InputFilter[]{filter});

        searchPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().isEmpty()) {
                        selectedPrice = 1000.0;
                    } else {
                        selectedPrice = Double.parseDouble(s.toString());
                    }
                    filter(filterText[0], selectedSize);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid double", Toast.LENGTH_SHORT).show();
                    searchPrice.setText("");
                }
            }
        });

        sizeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = sizeSpinner.getSelectedItem().toString().trim();
                filter(filterText[0], selectedSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chickedButton[0] = 0;
                all_button.setBackgroundColor(Color.BLACK);
                chicken_button.setBackgroundColor(Color.RED);
                beef_button.setBackgroundColor(Color.RED);
                veggies_button.setBackgroundColor(Color.RED);
                others_button.setBackgroundColor(Color.RED);
                filter(filterText[0], selectedSize);
            }
        });
        chicken_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chickedButton[0] = 1;
                all_button.setBackgroundColor(Color.RED);
                chicken_button.setBackgroundColor(Color.BLACK);
                beef_button.setBackgroundColor(Color.RED);
                veggies_button.setBackgroundColor(Color.RED);
                others_button.setBackgroundColor(Color.RED);
                filter(filterText[0], selectedSize);
            }
        });
        beef_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chickedButton[0] = 2;
                all_button.setBackgroundColor(Color.RED);
                chicken_button.setBackgroundColor(Color.RED);
                beef_button.setBackgroundColor(Color.BLACK);
                veggies_button.setBackgroundColor(Color.RED);
                others_button.setBackgroundColor(Color.RED);
                filter(filterText[0], selectedSize);
            }
        });
        veggies_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chickedButton[0] = 3;
                all_button.setBackgroundColor(Color.RED);
                chicken_button.setBackgroundColor(Color.RED);
                beef_button.setBackgroundColor(Color.RED);
                veggies_button.setBackgroundColor(Color.BLACK);
                others_button.setBackgroundColor(Color.RED);
                filter(filterText[0], selectedSize);
            }
        });
        others_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chickedButton[0] = 4;
                all_button.setBackgroundColor(Color.RED);
                chicken_button.setBackgroundColor(Color.RED);
                beef_button.setBackgroundColor(Color.RED);
                veggies_button.setBackgroundColor(Color.RED);
                others_button.setBackgroundColor(Color.BLACK);
                filter(filterText[0], selectedSize);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText[0] = newText;
                filter("text", selectedSize);
                return true;
            }
        });
        return root;
    }

    private void filter(String type, String size) {
        boolean isAdded[] = new boolean[13];
        for (int i = 0; i < 13; i++)
            isAdded[i] = false;
        int x = 0;


        ArrayList<String> pizzasTypes = new ArrayList<>();
        ArrayList<Pizza> pizzas = new ArrayList<>();
        allPizzasCursor[0] = dataBaseHelper.getAllPizzas();

        while (allPizzasCursor[0].moveToNext()) {
            if (type.isEmpty() || allPizzasCursor[0].getString(0).toLowerCase().startsWith(filterText[0].toLowerCase())) {
                if (allPizzasCursor[0].getString(0).equals("Margarita")) {
                    x = 0;
                } else if (allPizzasCursor[0].getString(0).equals("Neapolitan")) {
                    x = 1;
                } else if (allPizzasCursor[0].getString(0).equals("Hawaiian")) {
                    x = 2;
                } else if (allPizzasCursor[0].getString(0).equals("Pepperoni")) {
                    x = 3;
                } else if (allPizzasCursor[0].getString(0).equals("New York Style")) {
                    x = 4;
                } else if (allPizzasCursor[0].getString(0).equals("Calzone")) {
                    x = 5;
                } else if (allPizzasCursor[0].getString(0).equals("Tandoori Chicken Pizza")) {
                    x = 6;
                } else if (allPizzasCursor[0].getString(0).equals("BBQ Chicken Pizza")) {
                    x = 7;
                } else if (allPizzasCursor[0].getString(0).equals("Seafood Pizza")) {
                    x = 8;
                } else if (allPizzasCursor[0].getString(0).equals("Vegetarian Pizza")) {
                    x = 9;
                } else if (allPizzasCursor[0].getString(0).equals("Buffalo Chicken Pizza")) {
                    x = 10;
                } else if (allPizzasCursor[0].getString(0).equals("Mushroom Truffle Pizza")) {
                    x = 11;
                } else if (allPizzasCursor[0].getString(0).equals("Pesto Chicken Pizza")) {
                    x = 12;
                }

                if (!isAdded[x] && (Double.parseDouble(allPizzasCursor[0].getString(4)) <= selectedPrice) && ((chickedButton[0] == 0 && (allPizzasCursor[0].getString(1).trim().equals(size) || size.equals("All Sizes")))
                        || (chickedButton[0] == 1 && (allPizzasCursor[0].getString(1).trim().equals(size) || size.equals("All Sizes")) && allPizzasCursor[0].getString(3).contains("Chicken"))
                        || (chickedButton[0] == 2 && (allPizzasCursor[0].getString(1).trim().equals(size) || size.equals("All Sizes")) && allPizzasCursor[0].getString(3).contains("Beef"))
                        || (chickedButton[0] == 3 && (allPizzasCursor[0].getString(1).trim().equals(size) || size.equals("All Sizes")) && allPizzasCursor[0].getString(3).contains("Vegetarian"))
                        || (chickedButton[0] == 4 && (allPizzasCursor[0].getString(1).trim().equals(size) || size.equals("All Sizes")) && !allPizzasCursor[0].getString(3).contains("Chicken") && !allPizzasCursor[0].getString(3).contains("Beef") && !allPizzasCursor[0].getString(3).contains("Vegetarian")))) {

                    pizzas.add(new Pizza(allPizzasCursor[0].getString(0), allPizzasCursor[0].getString(3)));
                    pizzasTypes.add(allPizzasCursor[0].getString(0));
                    isAdded[x] = true;
                }
            }
        }
        boolean isFavorite[] = new boolean[pizzas.size()];
        for (int position = 0; position < pizzas.size(); position++)
            isFavorite[position] = false;
        Cursor getFavoritesByEmail = dataBaseHelper.getFavoritesByEmail(sharedPrefManager.readString("logIn email", ""));
        while (getFavoritesByEmail.moveToNext()) {
            for (int position = 0; position < pizzas.size(); position++) {
                if (getFavoritesByEmail.getString(1).equals(pizzasTypes.get(position))) {
                    isFavorite[position] = true;
                }
            }
        }
        pizzaAdapter pizzaAdapterOpj = new pizzaAdapter(getContext(), pizzas.toArray(new Pizza[pizzas.size()]), isFavorite, imagesOfPizza, fragmentManager, root);
        binding.pizzaMenuRecyclerView.setAdapter(pizzaAdapterOpj);
        binding.pizzaMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}