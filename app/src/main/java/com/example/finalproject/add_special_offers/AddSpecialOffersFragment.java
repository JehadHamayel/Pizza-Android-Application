package com.example.finalproject.add_special_offers;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentAddSpecialOffersBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddSpecialOffersFragment extends Fragment {

    private FragmentAddSpecialOffersBinding binding;
    private String selectedCategory, selectedName, selectedSize; // to store the admin's choices
    private TextView previousPriceEditText; // for the previous item's price
    private EditText newPriceEditText, startDateField, endDateField; // date fields
    private double previousPriceValue, newPriceValue; // to store the previous price
    private String startDateString, endDateString; // to store the dates as strings
    private String itemDescription;
    private Calendar startDate, endDate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        binding = FragmentAddSpecialOffersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // For Category choices
        Cursor allCategoriesCursor = dataBaseHelper.getAllCategoriesUniquely();
        ArrayList<String> categoryOptions = new ArrayList<>();
        while (allCategoriesCursor.moveToNext()) {
            categoryOptions.add(allCategoriesCursor.getString(0));
        }
        allCategoriesCursor.close(); // close cursor

        final Spinner pizzaCategory = root.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> objCategoryArr = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryOptions);
        pizzaCategory.setAdapter(objCategoryArr);

        pizzaCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                // For pizza name choices
                Cursor allNamesCursor = dataBaseHelper.getAllPizzasByCategory(selectedCategory);
                ArrayList<String> nameOptions = new ArrayList<>(); // Array list to store the names
                while (allNamesCursor.moveToNext()) {
                    nameOptions.add(allNamesCursor.getString(0));
                }
                allNamesCursor.close(); // close the cursor

                final Spinner pizzaName = root.findViewById(R.id.nameSpinner);
                ArrayAdapter<String> objNameArr = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameOptions);
                pizzaName.setAdapter(objNameArr);

                // Listener for the pizza name spinner
                pizzaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedName = parent.getItemAtPosition(position).toString();
                        ArrayList<String> sizeOptions = new ArrayList<>(); // Array list to store the available sizes
                        Cursor allSizesCursor = dataBaseHelper.getSizeOfPizza(selectedCategory, selectedName);

                        while (allSizesCursor.moveToNext()) {
                            sizeOptions.add(allSizesCursor.getString(0));
                        }

                        final Spinner pizzaSize = root.findViewById(R.id.sizeSpinner);
                        ArrayAdapter<String> objSizeArr = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sizeOptions);
                        pizzaSize.setAdapter(objSizeArr);

                        pizzaSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedSize = parent.getItemAtPosition(position).toString();
                                Cursor priceCursor = dataBaseHelper.getPizzaPrice(selectedCategory, selectedName, selectedSize);

                                if (priceCursor.moveToFirst()) {
                                    previousPriceValue = priceCursor.getDouble(0);
                                    previousPriceEditText = root.findViewById(R.id.previousPrice);
                                    previousPriceEditText.setText("Total Previous Price: $" + String.format("%.2f", previousPriceValue));
                                }
                                priceCursor.close();
                                Cursor descriptionCursor = dataBaseHelper.getDistinctDescription(selectedCategory, selectedName, selectedSize);
                                if (descriptionCursor.moveToFirst()) {
                                    itemDescription = descriptionCursor.getString(0);
                                }
                                descriptionCursor.close();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Initialize newPriceEditText and handle price validation
        newPriceEditText = root.findViewById(R.id.newPrice);
        Button makeOfferButton = root.findViewById(R.id.makeOfferBtn);

        // Initialize date fields
        startDateField = root.findViewById(R.id.startDateField);
        endDateField = root.findViewById(R.id.endDateField);

        // Set up date picker dialog for start date
        startDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dayOfMonthstr = String.valueOf(dayOfMonth);
                        String monthOfYearstr = String.valueOf(monthOfYear);

                        if (dayOfMonth < 10) {
                            dayOfMonthstr = "0" + dayOfMonth;
                        }else {
                            dayOfMonthstr = ""+dayOfMonth;
                        }
                        if ((monthOfYear+ 1) < 10) {
                            monthOfYearstr = "0" + (monthOfYear+1);
                        }else {
                            monthOfYearstr = ""+(monthOfYear+1);
                        }
                        startDateString = dayOfMonthstr + "/" + monthOfYearstr + "/" + year;
                        startDateField.setText(startDateString);
                        startDate = Calendar.getInstance();
                        startDate.set(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Set up date picker dialog for end date
        endDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dayOfMonthstr = String.valueOf(dayOfMonth);
                        String monthOfYearstr = String.valueOf(monthOfYear);

                        if (dayOfMonth < 10) {
                            dayOfMonthstr = "0" + dayOfMonth;
                        }else {
                            dayOfMonthstr = ""+dayOfMonth;
                        }
                        if ((monthOfYear+ 1) < 10) {
                            monthOfYearstr = "0" + (monthOfYear+1);
                        }else {
                            monthOfYearstr = ""+(monthOfYear+1);
                        }

                        endDateString = dayOfMonthstr + "/" + monthOfYearstr + "/" + year;
                        endDateField.setText(endDateString);
                        endDate = Calendar.getInstance();  /// returns a Calendar object initialized with the current date and time
                        endDate.set(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
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
        newPriceEditText.setFilters(new InputFilter[]{filter});

        final double[] selectedPrice = {-1};
        newPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if ( ! s.toString().isEmpty()) {
                        selectedPrice[0] = Double.parseDouble(s.toString());
                    }
                } catch (NumberFormatException e) {
                    newPriceEditText.setError("Please enter a valid price (It should be less than the previous one)");
                    newPriceEditText.setText("");
                }
            }
        });
        makeOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPriceText = newPriceEditText.getText().toString();
                if (selectedCategory == null || selectedName == null || selectedSize == null || startDateString == null || endDateString == null || newPriceText.isEmpty()) {
                    if (startDateString == null) {
                        Toast.makeText(getContext(), "Please enter a start date", Toast.LENGTH_SHORT).show();
                    }
                    if (endDateString == null) {
                        Toast.makeText(getContext(), "Please enter an end date", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

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
                LocalDate currentDate = LocalDate.parse(orderDate, formatter);
                LocalDate start = LocalDate.parse(startDateString, formatter);
                LocalDate end = LocalDate.parse(endDateString, formatter);

                if(currentDate.isAfter(end)){
                    Toast.makeText(getContext(), "End date must be after today's date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentDate.isAfter(start)) {
                    Toast.makeText(getContext(), "Start date must be after today's date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (start.isAfter(end)) {
                    Toast.makeText(getContext(), "End date must be after the start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                newPriceValue = selectedPrice[0];
                if (newPriceValue >= previousPriceValue) {
                    newPriceEditText.setError("Please enter a valid price (It should be less than the previous one)");
                } else {

                    boolean isCreated = false;
                    boolean isUpdated = false;
                    if(dataBaseHelper.isExistSpecialOffer(selectedName,selectedSize)){
                        isUpdated = dataBaseHelper.updateSpecialOffer(selectedName, selectedSize,itemDescription, selectedCategory , newPriceValue, startDateString, endDateString);
                    }else {
                        isCreated = dataBaseHelper.insertSpecialOffer(selectedName, selectedSize,itemDescription, selectedCategory , newPriceValue, startDateString, endDateString);
                    }
                    if (!isCreated && isUpdated) {
                        Toast.makeText(getContext(), "Offer Updated Successfully", Toast.LENGTH_LONG).show();
                        Cursor cursor = dataBaseHelper.getAllSpecialFavoritesOfAllUsers();
                        while (cursor.moveToNext()){
                            if (cursor.getString(1).equals(selectedName) && cursor.getString(2).equals(selectedSize)){
                                Cursor cursor1 = dataBaseHelper.getSpecialFavoritesByEmailAndPizzaAndSize(cursor.getString(0),selectedName,selectedSize);
                                cursor1.moveToFirst();
                                dataBaseHelper.updateSpecialFavorite(cursor.getString(0),selectedName,selectedSize,Integer.parseInt(cursor1.getString(3)),newPriceValue,cursor1.getString(5));
                            }
                        }
                        resetFields();
                    } else if (isCreated && !isUpdated) {
                        Toast.makeText(getContext(), "Offer Created Successfully", Toast.LENGTH_LONG).show();
                        resetFields();
                    } else {
                        Toast.makeText(getContext(), "Failed to create offer", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return root;
    }
    private void resetFields() {
        // Reset EditText fields
        newPriceEditText.setText("");
        startDateField.setText("");
        endDateField.setText("");
        // Reset Spinners to the first item
        Spinner pizzaCategory = binding.getRoot().findViewById(R.id.categorySpinner);
        pizzaCategory.setSelection(0);

        Spinner pizzaName = binding.getRoot().findViewById(R.id.nameSpinner);
        pizzaName.setSelection(0);

        Spinner pizzaSize = binding.getRoot().findViewById(R.id.sizeSpinner);
        pizzaSize.setSelection(0);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
