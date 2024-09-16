package com.example.finalproject.pizza_menu;

import android.database.Cursor;
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
import com.example.finalproject.databinding.FragmentPizzaDetailsBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pizzaDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pizzaDetails extends Fragment {
    private FragmentPizzaDetailsBinding binding;
    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public pizzaDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pizzaDetils.
     */
    // TODO: Rename and change types and number of parameters
    public static pizzaDetails newInstance(String param1, String param2) {
        pizzaDetails fragment = new pizzaDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String pizzaType;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPizzaDetailsBinding.inflate(inflater, container, false);

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
        String price = "";
        String description = "";
        binding.sizeLarge.setText("Large: Not Available");
        binding.sizeMedium.setText("Medium: Not Available");
        binding.sizeSmall.setText("Small: Not Available");
        for (String size : pizzaSizes) {
            cursor[0] = dataBaseHelper.getPizzaByNameAndSize(pizzaType.trim(), size);
            if(cursor[0].moveToFirst()){
                price = cursor[0].getString(1) + " - $"+ cursor[0].getString(4);
                description = cursor[0].getString(2);

                switch (size) {
                    case "Small":
                        binding.sizeSmall.setText(price);
                        break;
                    case "Medium":
                        binding.sizeMedium.setText(price);
                        break;
                    case "Large":
                        binding.pizzaDescription.setText(description);
                        binding.sizeLarge.setText(price);
                        break;
                }
            }
        }


        binding.favoriteButton.setOnClickListener(v -> {
            if(binding.favoriteButton.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.heart).getConstantState()){
                binding.favoriteButton.setImageResource(R.drawable.heartempty);
                Cursor getFavoritesByEmail = dataBaseHelper.getFavoritesByEmailAndPizza(sharedPrefManager.readString("logIn email", ""),pizzaType);
                getFavoritesByEmail.moveToFirst();
                if (!dataBaseHelper.deleteFavorite(sharedPrefManager.readString("logIn email", ""),  pizzaType, getFavoritesByEmail.getString(2)))
                    Toast.makeText( getContext(), "Error in removing from favourites", Toast.LENGTH_SHORT).show();

            }
            else if(binding.favoriteButton.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.heartempty).getConstantState()){
                binding.favoriteButton.setImageResource(R.drawable.heart);
                if (!dataBaseHelper.isExistFavorite(sharedPrefManager.readString("logIn email", ""), binding.pizzaName.getText().toString())){
                    String sizes[] = {"Small", "Medium", "Large"};
                    for (String size : sizes) {
                        cursor[0] = dataBaseHelper.getPizzaByNameAndSize(binding.pizzaName.getText().toString().trim(), size);
                        if (cursor[0].moveToFirst()) {
                            if (dataBaseHelper.insertFavorite(sharedPrefManager.readString("logIn email", ""), binding.pizzaName.getText().toString(), size, 1, Double.parseDouble(cursor[0].getString(4)),cursor[0].getString(3))) {
                                break;
                            } else {
                                Toast.makeText(getContext(), "Error in adding in favourites", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });
        Cursor getFavoritesByEmail = dataBaseHelper.getFavoritesByEmailAndPizza(sharedPrefManager.readString("logIn email", ""),pizzaType);
        if (getFavoritesByEmail.getCount() > 0){
            binding.favoriteButton.setImageResource(R.drawable.heart);
        }
        pizzaOrder pizzaOrder = new pizzaOrder();
        binding.orderButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("pizzaType", pizzaType);

            pizzaOrder.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.pizza_menu, pizzaOrder);
            fragmentTransaction.commit();
        });


        PizzaMenuFragment pizzaMenu = new PizzaMenuFragment();
        binding.backToMenuButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.pizza_menu, pizzaMenu);
            fragmentTransaction.commit();
           });

        // Inflate the layout for this fragment
        return root;
    }
}