package com.example.finalproject.pizza_menu;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.databinding.FragmentPizzaOrderBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pizzaOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pizzaOrder extends Fragment {
    private FragmentPizzaOrderBinding binding;
    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public pizzaOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pizzaOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static pizzaOrder newInstance(String param1, String param2) {
        pizzaOrder fragment = new pizzaOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String pizzaType    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            pizzaType = bundle.getString("pizzaType");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        binding = FragmentPizzaOrderBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        int imagesOfPizzaType = 0;
        switch (pizzaType) {
            case ("Margarita"):
                imagesOfPizzaType = R.drawable.margarita;
                break;
            case ("Neapolitan"):
                imagesOfPizzaType = R.drawable.neapolitan;
                break;
            case ("Hawaiian"):
                imagesOfPizzaType = R.drawable.hawaiian;
                break;
            case ("Pepperoni"):
                imagesOfPizzaType = R.drawable.pepperoni;
                break;
            case ("New York Style"):
                imagesOfPizzaType = R.drawable.newyorkstyle;
                break;
            case ("Calzone"):
                imagesOfPizzaType = R.drawable.calzone;
                break;
            case ("Tandoori Chicken Pizza"):
                imagesOfPizzaType = R.drawable.tandoorichickenpizza;
                break;
            case ("BBQ Chicken Pizza"):
                imagesOfPizzaType = R.drawable.bbqchickenpizza;
                break;
            case ("Seafood Pizza"):
                imagesOfPizzaType = R.drawable.seafoodpizza;
                break;
            case ("Vegetarian Pizza"):
                imagesOfPizzaType = R.drawable.vegetarianpizza;
                break;
            case ("Buffalo Chicken Pizza"):
                imagesOfPizzaType = R.drawable.buffalochickenpizza;
                break;
            case ("Mushroom Truffle Pizza"):
                imagesOfPizzaType = R.drawable.mushroomtrufflepizza;
                break;
            case ("Pesto Chicken Pizza"):
                imagesOfPizzaType = R.drawable.pestochickenpizza;
                break;
        }
        binding.pizzaImage.setImageResource(imagesOfPizzaType);
        binding.pizzaName.setText(pizzaType);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        String [] pizzaSizes = {"Small", "Medium", "Large"};
        final Cursor[] cursor = new Cursor[1];
        String price[] = new String[3];
        String description ="";
        String category = "";
        int i = 0;
        for (String size : pizzaSizes) {
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), size);
            if (cursor[0].moveToFirst()){
                price[i] = cursor[0].getString(4).trim();
                description = cursor[0].getString(2);
                category = cursor[0].getString(3);
            }else {
                price[i] = "-1";
            }
            i++;
        }
        AtomicInteger buttonClicked = new AtomicInteger();

        binding.pizzaDescription.setText(description);
        cursor[0] = dataBaseHelper.getFavoritesByEmailAndPizza(sharedPrefManager.readString("logIn email",""),pizzaType);
        if(cursor[0].moveToNext()){
            if (cursor[0].getString(2).equals("Small")){
                buttonClicked.set(0);
                binding.buttonSizeSmall.setBackgroundColor(Color.GREEN);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[0]) ));
                binding.priceDisplay.setText("Price: $"+price[0]);
                binding.quantity.setText(cursor[0].getString(3));
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[0]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
            } else if (cursor[0].getString(2).equals("Medium")){
                buttonClicked.set(1);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.GREEN);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[1]) ));
                binding.priceDisplay.setText("Price: $"+price[1]);
                binding.quantity.setText(cursor[0].getString(3));
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[1]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
            } else if (cursor[0].getString(2).equals("Large")) {
                buttonClicked.set(2);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.GREEN);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[2]) ));
                binding.priceDisplay.setText("Price: $"+price[2]);
                binding.quantity.setText(cursor[0].getString(3));
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[2]) * Integer.parseInt(binding.quantity.getText().toString().trim())));

            }

        }else {
            boolean in = false;
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), "Small");
            if (cursor[0].moveToFirst()) {
                in = true;
                buttonClicked.set(0);
                binding.buttonSizeSmall.setBackgroundColor(Color.GREEN);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[0]) ));
                binding.priceDisplay.setText("Price: $"+price[0]);
            }
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), "Medium");
            if (cursor[0].moveToFirst() && !in) {
                in = true;
                buttonClicked.set(1);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.GREEN);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[1]) ));
                binding.priceDisplay.setText("Price: $"+price[1]);
            }
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), "Large");
            if (cursor[0].moveToFirst() && !in) {
                buttonClicked.set(2);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.GREEN);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[2]) ));
                binding.priceDisplay.setText("Price: $"+price[2]);
            }

        }

        String finalCategory = category;

        binding.buttonSizeSmall.setOnClickListener(v -> {
            if (price[0].equals("-1")){
                Toast.makeText(getContext(), "Small size not available", Toast.LENGTH_SHORT).show();
            }
            else {
                buttonClicked.set(0);
                binding.buttonSizeSmall.setBackgroundColor(Color.GREEN);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.priceDisplay.setText("Price: $" + price[0]);
                binding.totalCost.setText("Total Cost: $" + (Double.parseDouble(price[0]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
                if (dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType)) {
                    if (! dataBaseHelper.updateFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, "Small", Integer.parseInt(binding.quantity.getText().toString().trim()), Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()),finalCategory)) {
                        Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.buttonSizeMedium.setOnClickListener(v -> {
            if (price[1].equals("-1")){
                Toast.makeText(getContext(), "Medium size not available", Toast.LENGTH_SHORT).show();
            }else {
                buttonClicked.set(1);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.GREEN);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.priceDisplay.setText("Price: $"+price[1]);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[1]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
                if (dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType)){

                    if(! dataBaseHelper.updateFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, "Medium", Integer.parseInt(binding.quantity.getText().toString().trim()), Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()), finalCategory)){
                        Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        binding.buttonSizeLarge.setOnClickListener(v -> {
            if (price[2].equals("-1")){
                Toast.makeText(getContext(), "Large size not available", Toast.LENGTH_SHORT).show();
            }else {
                buttonClicked.set(2);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.GREEN);
                binding.priceDisplay.setText("Price: $" + price[2]);
                binding.totalCost.setText("Total Cost: $" + (Double.parseDouble(price[2]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
                if (dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType)) {

                    if (!dataBaseHelper.updateFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, "Large", Integer.parseInt(binding.quantity.getText().toString().trim()), Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()), finalCategory)) {
                        Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        String sizes[] = {"Small", "Medium", "Large"};
        binding.buttonDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.quantity.getText().toString().trim());
            if(quantity > 1) {
                quantity--;
                binding.quantity.setText(String.valueOf(quantity));
                if (dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType)){

                    if(!dataBaseHelper.updateFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, sizes[buttonClicked.get()], Integer.parseInt(binding.quantity.getText().toString().trim()), Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()),finalCategory)){

                        Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                    }
                }
                if (buttonClicked.get() == 0)
                    binding.totalCost.setText("Total Cost: $" + (Double.parseDouble(price[0]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
                else if (buttonClicked.get() == 1)
                    binding.totalCost.setText("Total Cost: $" + (Double.parseDouble(price[1]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
                else if (buttonClicked.get() == 2)
                    binding.totalCost.setText("Total Cost: $" + (Double.parseDouble(price[2]) * Integer.parseInt(binding.quantity.getText().toString().trim())));

            }
        });
        binding.buttonIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.quantity.getText().toString().trim());
            quantity++;
            binding.quantity.setText(String.valueOf(quantity));
            if (dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType)){

                if(!dataBaseHelper.updateFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, sizes[buttonClicked.get()], Integer.parseInt(binding.quantity.getText().toString().trim()), Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()),finalCategory)){
                    Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                }
            }
            if(buttonClicked.get() == 0)
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[0]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
            else if(buttonClicked.get() == 1)
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[1]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
            else if(buttonClicked.get() == 2)
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[2]) * Integer.parseInt(binding.quantity.getText().toString().trim())));
            });

        sharedPrefManager = SharedPrefManager.getInstance(getContext());

        cursor[0] = dataBaseHelper.getFavoritesByEmailAndPizza(sharedPrefManager.readString("logIn email", ""),pizzaType);
        if (cursor[0].getCount() > 0){
            binding.buttonFavorite.setImageResource(R.drawable.heart);
        }

        binding.buttonFavorite.setOnClickListener(v -> {
            if(binding.buttonFavorite.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.heart).getConstantState()){
                binding.buttonFavorite.setImageResource(R.drawable.heartempty);
                if (!dataBaseHelper.deleteFavorite(sharedPrefManager.readString("logIn email", ""),  pizzaType, pizzaSizes[buttonClicked.get()]))
                    Toast.makeText(getContext(), "Error in removing from favourites", Toast.LENGTH_SHORT).show();
            }
            else if(binding.buttonFavorite.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.heartempty).getConstantState()){
                binding.buttonFavorite.setImageResource(R.drawable.heart);

                if(!dataBaseHelper.insertFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSizes[buttonClicked.get()], Integer.parseInt(binding.quantity.getText().toString().trim()),Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()),finalCategory ))
                    Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
            }
        });

        binding.orderButton.setOnClickListener(v -> {
            boolean notDone = false;
            if(dataBaseHelper.insertOrder(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSizes[buttonClicked.get()], Integer.parseInt(binding.quantity.getText().toString().trim()),Double.parseDouble(price[buttonClicked.get()]) * Integer.parseInt(binding.quantity.getText().toString().trim()), DateFormat.getDateTimeInstance().format(new Date() ).toString() )){
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Error in placing order", Toast.LENGTH_SHORT).show();
                notDone = true;
            }
            if (dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType)) {
            if (!notDone) {
                if (dataBaseHelper.deleteFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSizes[buttonClicked.get()])){
                    binding.buttonFavorite.setImageResource(R.drawable.heartempty);
                }
                else{
                    Toast.makeText(getContext(), "Error in removing from favourites", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Error in placing order", Toast.LENGTH_SHORT).show();
            }

            }
            boolean in = false;
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), "Small");
            if (cursor[0].moveToFirst()) {
                in = true;
                buttonClicked.set(0);
                binding.buttonSizeSmall.setBackgroundColor(Color.GREEN);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[0]) ));
                binding.priceDisplay.setText("Price: $"+price[0]);
            }
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), "Medium");
            if (cursor[0].moveToFirst() && !in) {
                in = true;
                buttonClicked.set(1);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.GREEN);
                binding.buttonSizeLarge.setBackgroundColor(Color.RED);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[1]) ));
                binding.priceDisplay.setText("Price: $"+price[1]);
            }
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), "Large");
            if (cursor[0].moveToFirst() && !in) {
                buttonClicked.set(2);
                binding.buttonSizeSmall.setBackgroundColor(Color.RED);
                binding.buttonSizeMedium.setBackgroundColor(Color.RED);
                binding.buttonSizeLarge.setBackgroundColor(Color.GREEN);
                binding.totalCost.setText("Total Cost: $"+(Double.parseDouble(price[2]) ));
                binding.priceDisplay.setText("Price: $"+price[2]);
            }
            binding.quantity.setText("1");


        });
        PizzaMenuFragment pizzaMenu = new PizzaMenuFragment();

        binding.backToMenuFromOrderButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.pizza_menu, pizzaMenu);
            fragmentTransaction.commit();
        });
        // Inflate the layout for this fragment
        return root ;
    }
}