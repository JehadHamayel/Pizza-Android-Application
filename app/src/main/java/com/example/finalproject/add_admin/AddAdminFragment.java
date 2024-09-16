package com.example.finalproject.add_admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.Hash;
import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.example.finalproject.databinding.FragmentAddAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddAdminFragment extends Fragment {

    private FragmentAddAdminBinding binding;
    Uri imageUri;
    int code =100;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    ImageView setImageProfile ;
    final int[] currentProfileImageResId = {0};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        binding = FragmentAddAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setImageProfile = (ImageView)root.findViewById(R.id.firenasImage);
        currentProfileImageResId[0] = R.drawable.man;
        Button uploadImageToFirebase = (Button) root.findViewById(R.id.uploadImageToFirebase);

        uploadImageToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Uploading an image", Toast.LENGTH_SHORT).show();
                selectImage();
            }
        });
        final EditText editTextFirstName = (EditText) root.findViewById(R.id.fname);
        final EditText editTextLastName = (EditText) root.findViewById(R.id.lname);
        final EditText Email = (EditText) root.findViewById(R.id.email);
        final EditText editTextPhoneNumber = (EditText) root.findViewById(R.id.phoneNum);
        final EditText editTextPassword = (EditText) root.findViewById(R.id.password);
        final EditText editTextConfirmPassword = (EditText) root.findViewById(R.id.confirmPassword);
        final Button buttonAddAdmin = (Button) root.findViewById(R.id.updateButton);
        String[] options = {"Male", "Female"};

        final Spinner genderSpinner = (Spinner) root.findViewById(R.id.genderSp);
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        genderSpinner.setAdapter(objGenderArr);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(genderSpinner.getSelectedItem().toString().trim().equals("Male")){
                    if(currentProfileImageResId[0] == R.drawable.woman){
                        setImageProfile.setImageResource(R.drawable.man);
                        currentProfileImageResId[0] = R.drawable.man;
                    }
                }else{
                    if(currentProfileImageResId[0] == R.drawable.man){
                        currentProfileImageResId[0] = R.drawable.woman;
                        setImageProfile.setImageResource(R.drawable.woman);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        buttonAddAdmin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                ///First Name
                int correctConditions = 0;
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String password = Hash.hashPassword(editTextPassword.getText().toString().trim());
                String gender = genderSpinner.getSelectedItem().toString();

                if (editTextFirstName.getText().toString().length() < 3) {
                    editTextFirstName.setError("Please enter a valid first name (not less than 3 characters)");
                }
                else {
                    correctConditions++;
                }
                ///Last Name
                if (editTextLastName.getText().toString().length() < 3) {
                    editTextLastName.setError("Please enter a valid last name (not less than 3 characters)");
                }
                else {
                    correctConditions++;
                }
                ///Email
                String emailText = Email.getText().toString().trim();
                if (emailText.isEmpty()) {
                    Email.setError("Field can't be empty");
                }
                else {
                    correctConditions++;
                }
                ///To check the entered format
                if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    Email.setError("Please enter a valid email address");
                }
                else {
                    correctConditions++;
                }

                ///To check the phone number
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                if (editTextPhoneNumber.getText().toString().isEmpty() || !phoneNumber.matches("^05\\d{8}$")) {
                    editTextPhoneNumber.setError("Please enter a valid Phone Number (starts with 05, 10 digits)");
                }
                else {
                    correctConditions++;
                }
                ///To check the first password field
                if (!hasACharAndANumber(editTextPassword.getText().toString()) || editTextPassword.getText().length() < 8) {
                    editTextPassword.setError("Please enter a valid password (must not be less than 8 characters and must include at least 1 character and 1 number)");
                }
                else {
                    correctConditions++;
                }
                ///To check the confirm password field
                if (editTextConfirmPassword.getText().toString().compareTo(editTextPassword.getText().toString()) != 0) {
                    editTextConfirmPassword.setError("Please enter the password correctly (The passwords do not match)");
                }
                else {
                    correctConditions++;
                }
                if (correctConditions == 7) {
                    User admin = new User(firstName, lastName, emailText,phoneNumber,gender,password) ;
                    Cursor cursor1 = dataBaseHelper.getAdminByEmail(emailText);
                    Cursor cursor2 = dataBaseHelper.getUserByEmail(emailText);
                    if (cursor1.getCount() == 0 && cursor2.getCount() == 0) {
                        if (dataBaseHelper.insertAdmin(admin)) {
                            if (imageUri != null) {
                                uploadImage(-1, emailText);
                            } else if (gender.trim().equals("Male")) {
                                uploadImage(R.drawable.man, emailText);
                            } else if (gender.trim().equals("Female")) {
                                uploadImage(R.drawable.woman, emailText);
                            }

                            Toast.makeText(getContext(), "New Admin added successfully", Toast.LENGTH_SHORT).show();
                            editTextFirstName.setText("");
                            editTextLastName.setText("");
                            Email.setText("");
                            editTextPhoneNumber.setText("");
                            genderSpinner.setSelection(0);
                            editTextPassword.setText("");
                            editTextConfirmPassword.setText("");


                        } else {
                            Toast.makeText(getContext(), "Failed to add new Admin", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Please enter the correct data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
    }

    private void uploadImage(int drawableId, String emailText) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Adding Image");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("Images/" + emailText);
        if (drawableId == -1)
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    setImageProfile.setImageResource(R.drawable.man);
                    Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        else {
            InputStream inputStream = convertDrawableToInputStream(drawableId);
            storageReference.putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private InputStream convertDrawableToInputStream(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == code && data != null && data.getData() != null) {
            imageUri = data.getData();
            setImageProfile.setImageURI(imageUri);
            currentProfileImageResId[0] = -1;

        }
    }
    private boolean hasACharAndANumber(String password) {
        boolean hasAnumber = false;
        boolean hasAchar = false;
        for (int i = 0; i < password.length(); i++) {
            char a = password.toCharArray()[i];
            if (Character.isLetter(a)) {
                hasAchar = true;
            } else if (Character.isDigit(a)) {
                hasAnumber = true;
            }

            if (hasAchar && hasAnumber) {
                return true;
            }
        }
        return false;
    }

}