package com.example.finalproject.special_offers;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.databinding.FragmentSpecialDetailsBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link specialDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class specialDetails extends Fragment {
    private FragmentSpecialDetailsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public specialDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment specialDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static specialDetails newInstance(String param1, String param2) {
        specialDetails fragment = new specialDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String pizzaType;
    String pizzaSize;
    SharedPrefManager sharedPrefManager ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        if (getArguments() != null) {
            pizzaType = getArguments().getString("pizzaType");
            pizzaSize = getArguments().getString("pizzaSize");

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSpecialDetailsBinding.inflate(inflater, container, false);
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
        binding.specialPizzaSize.setText(pizzaSize);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        Cursor cursor = dataBaseHelper.getPizzaByNameAndSize(pizzaType,pizzaSize);
        cursor.moveToNext();
        binding.pizzaDescription.setText(cursor.getString(2));


        binding.oldPrice.setText(cursor.getString(4)+"$");
        binding.oldPrice.setTextColor(Color.RED);
        binding.oldPrice.setPaintFlags(binding.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String category = cursor.getString(3);
        binding.specialPizzaCategory.setText(category);
        cursor = dataBaseHelper.getSpecialOfferByNameAndSize(pizzaType,pizzaSize);
        cursor.moveToNext();
        double specialPriceOfPizza = Double.parseDouble(cursor.getString(4));
        binding.specialPrice.setText(specialPriceOfPizza+"$");
        binding.specialPrice.setTextColor(Color.GREEN);
        String startDatestr = cursor.getString(5);
        String endDatestr = cursor.getString(6);
        binding.startDate.setText("Start:" + cursor.getString(5));
        binding.endDate.setText("End: " + cursor.getString(6));
        binding.offerImage.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.spacial_scale_animation));

        Cursor cursor1 = dataBaseHelper.getSpecialFavoritesByEmailAndPizzaAndSize(sharedPrefManager.readString("logIn email",""),pizzaType,pizzaSize);
        double totalPrice;
        if(cursor1.moveToFirst()){
            totalPrice= Double.parseDouble(cursor1.getString(4)) * Integer.parseInt(cursor1.getString(3));
            binding.quantity.setText(cursor1.getString(3));

            binding.totalCost.setText("Total Cost: "+totalPrice+"$");
            binding.favoriteButton.setImageResource(R.drawable.heart);
        }
        else{
            totalPrice = Double.parseDouble(cursor.getString(4));
            binding.quantity.setText(1+"");
            binding.totalCost.setText("Total Cost: "+totalPrice+"$");
        }
        binding.favoriteButton.setOnClickListener(v -> {
            if(binding.favoriteButton.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.heart).getConstantState()){
                        binding.favoriteButton.setImageResource(R.drawable.heartempty);
                        if (!dataBaseHelper.deleteSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize))
                            Toast.makeText(getContext(), "Error in removing from favourites", Toast.LENGTH_SHORT).show();

                    }
                    else if(binding.favoriteButton.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.heartempty).getConstantState()){
                        binding.favoriteButton.setImageResource(R.drawable.heart);
                        if(!dataBaseHelper.isExistSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize))
                            if (!dataBaseHelper.insertSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize, Integer.parseInt(binding.quantity.getText().toString()), totalPrice,category)) {
                                Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                            }

                    }
                });
        binding.buttonDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.quantity.getText().toString().trim());
            if(quantity > 1) {
                quantity--;
                binding.quantity.setText(String.valueOf(quantity));
                if (dataBaseHelper.isExistSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize)){
                    if(!dataBaseHelper.updateSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType,pizzaSize, Integer.parseInt(binding.quantity.getText().toString().trim()), specialPriceOfPizza * Integer.parseInt(binding.quantity.getText().toString().trim()),category)){

                        Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                    }
                }

                binding.totalCost.setText("Total Cost: " + (specialPriceOfPizza* Integer.parseInt(binding.quantity.getText().toString().trim()))+"$");

            }
        });
        binding.buttonIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.quantity.getText().toString().trim());
            quantity++;
            binding.quantity.setText(String.valueOf(quantity));
            if (dataBaseHelper.isExistSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize)){
                if(!dataBaseHelper.updateSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType,pizzaSize, Integer.parseInt(binding.quantity.getText().toString().trim()), specialPriceOfPizza * Integer.parseInt(binding.quantity.getText().toString().trim()),category)){

                    Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                }
            }
            binding.totalCost.setText("Total Cost: "+(specialPriceOfPizza * Integer.parseInt(binding.quantity.getText().toString().trim()))+"$");
        });

        binding.orderSpecialPizzaButton.setOnClickListener(v -> {
            boolean notDone = false;
            String orderDate = "";
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

            // Parsing the date strings into LocalDate objects
            LocalDate date = LocalDate.parse(orderDate, formatter);
            LocalDate startDate = LocalDate.parse(startDatestr, formatter);
            LocalDate endDate = LocalDate.parse(endDatestr, formatter);


            if(!date.isBefore(startDate) && !date.isAfter(endDate)){
                if(dataBaseHelper.insertSpecialOrder(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize, Integer.parseInt(binding.quantity.getText().toString().trim()),specialPriceOfPizza * Integer.parseInt(binding.quantity.getText().toString().trim()), DateFormat.getDateTimeInstance().format(new Date() ).toString() )){
                    Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Error in placing order", Toast.LENGTH_SHORT).show();
                    notDone = true;
                }
                if (dataBaseHelper.isExistSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize)){
                    if (!notDone) {
                        if (dataBaseHelper.deleteSpecialFavorite(sharedPrefManager.readString("logIn email", ""), pizzaType, pizzaSize))
                            binding.favoriteButton.setImageResource(R.drawable.heartempty);
                        else
                            Toast.makeText(getContext(), "Error in removing from favourites", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Error in placing order", Toast.LENGTH_SHORT).show();
                    }

                }
                binding.quantity.setText("1");
                binding.totalCost.setText("Total Cost: "+specialPriceOfPizza+"$");
            }else if (date.isBefore(startDate)){
                Toast.makeText(getContext(), "Special Offer is not started yet", Toast.LENGTH_SHORT).show();
            }else if (date.isAfter(endDate)){
                Toast.makeText(getContext(), "Special Offer has ended", Toast.LENGTH_SHORT).show();
            }

        });

        SpecialOffersFragment specialOffersFragment = new SpecialOffersFragment();

        binding.backToMenuButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.special_pizza_menu, specialOffersFragment);
            fragmentTransaction.commit();
        });


        return root;
    }
}