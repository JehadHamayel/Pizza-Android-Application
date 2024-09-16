package com.example.finalproject.view_all_orders;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentReportOfPizzaTypesBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reportOfPizzaTypes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reportOfPizzaTypes extends Fragment {
    FragmentReportOfPizzaTypesBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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

    public reportOfPizzaTypes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reportOfPizzaTypes.
     */
    // TODO: Rename and change types and number of parameters
    public static reportOfPizzaTypes newInstance(String param1, String param2) {
        reportOfPizzaTypes fragment = new reportOfPizzaTypes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportOfPizzaTypesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        ArrayList<reportPizzaTypes> reportPizzaTypesArrayList = new ArrayList<>();
        Cursor cursor,cursor1;

        cursor1 = dataBaseHelper.getAllPizzas();
        while (cursor1.moveToNext()) {
            cursor = dataBaseHelper.getNumberOfOrdersForEachPizzaType(cursor1.getString(0),cursor1.getString(1));
            if (cursor.moveToFirst()) {
                if (!cursor.getString(1).equals("0")) {
                    reportPizzaTypesArrayList.add(new reportPizzaTypes(cursor1.getString(0), cursor.getString(0), cursor1.getString(1), cursor.getString(1), cursor.getString(2),false));;
                }
            }
        }
        ArrayList<String> specialPizza = new ArrayList<>();

        cursor1 = dataBaseHelper.getAllSpecialOrders();
        while (cursor1.moveToNext()) {
            if (!specialPizza.contains(cursor1.getString(2)+","+cursor1.getString(3)))
                specialPizza.add(cursor1.getString(2)+","+cursor1.getString(3));
        }

        for (int i=0; i<specialPizza.size();i++){
            String[] pizza = specialPizza.get(i).split(",");
            cursor = dataBaseHelper.getNumberOfSpecialOrdersForEachPizzaType(pizza[0],pizza[1]);
            if (cursor.moveToFirst()) {
                if (!cursor.getString(1).equals("0")) {
                    reportPizzaTypesArrayList.add(new reportPizzaTypes(pizza[0], cursor.getString(0), pizza[1], cursor.getString(1), cursor.getString(2),true));;
                }
            }
        }
        double totalIncome = 0;
        for (int i=0; i<reportPizzaTypesArrayList.size();i++){
            totalIncome += Double.parseDouble(reportPizzaTypesArrayList.get(i).getTotalIncome());
        }
        totalIncome = Math.round(totalIncome * 100.0) / 100.0;
        binding.totalIncomeTextView.setText("total income for all types: "+totalIncome+"$");
        reportAdapter reportAdapter = new reportAdapter(getContext(), reportPizzaTypesArrayList.toArray(new reportPizzaTypes[reportPizzaTypesArrayList.size()]), getFragmentManager(), root, imagesOfPizza);
        binding.reportOfPizzaTypesRecyclerView.setAdapter(reportAdapter);
        binding.reportOfPizzaTypesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllOrdersFragment specialOffersFragment = new ViewAllOrdersFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewAllOrdersFragment, specialOffersFragment);
                fragmentTransaction.commit();
            }
        });
        return root;
    }
}