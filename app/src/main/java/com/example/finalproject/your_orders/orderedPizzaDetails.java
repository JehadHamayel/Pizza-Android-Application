package com.example.finalproject.your_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentOrderedPizzaDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link orderedPizzaDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orderedPizzaDetails extends Fragment {

    private FragmentOrderedPizzaDetailsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public orderedPizzaDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orderedPizzaDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static orderedPizzaDetails newInstance(String param1, String param2) {
        orderedPizzaDetails fragment = new orderedPizzaDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    String orderId;
    String pizzaType;
    String userEmail;
    String pizzaName;
    String pizzaSize;
    int quantity;
    double price;
    String orderDate;
    String orderTime;
    String category;
    String pizzaDescription;
    String specialOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            orderId = bundle.getString("orderId");
            pizzaType = bundle.getString("pizzaName");
            userEmail = bundle.getString("userEmail");
            pizzaName = bundle.getString("pizzaName");
            pizzaSize = bundle.getString("pizzaSize");
            quantity = bundle.getInt("quantity");
            price = bundle.getDouble("price");
            orderDate = bundle.getString("orderDate");
            orderTime = bundle.getString("orderTime");
            category = bundle.getString("category");
            pizzaDescription = bundle.getString("pizzaDescription");
            specialOrder = bundle.getString("specialOrder");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderedPizzaDetailsBinding.inflate(inflater, container, false);
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
        binding.pizzaDescription.setText(pizzaDescription);
        binding.orderId.setText("Order ID: " + orderId);
        binding.pizzaCategory.setText("Category: " + category);
        binding.pizzaPrice.setText("Sizes: " + pizzaSize);
        binding.pizzaQuantity.setText("Quantity: " + quantity);
        binding.pizzaTotalPrice.setText("Total Price: " + price+"$");
        binding.pizzaOrderedDate.setText("Ordered Date: " + orderDate);
        binding.pizzaOrderedTime.setText("Ordered Time: " + orderTime);
        if (specialOrder.equals("true")) {
            binding.offerImage.setImageResource(R.drawable.offer);
            binding.offerImage.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.spacial_scale_animation));
        }
        Your_ordersFragment Your_ordersFragment = new Your_ordersFragment();
        binding.backToMenuButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.your_orders, Your_ordersFragment);
            fragmentTransaction.commit();
        });

        return root;
    }
}