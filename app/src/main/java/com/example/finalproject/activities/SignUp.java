package com.example.finalproject.activities;

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
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.Hash;
import com.example.finalproject.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SignUp extends AppCompatActivity {

    Uri imageUri;
    int code =100;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    ImageView setImageProfile ;
    final int[] currentProfileImageResId = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setImageProfile = (ImageView)findViewById(R.id.firenasImage);

        Button uploadImageToFirebase = (Button) findViewById(R.id.uploadImageToFirebase);

        uploadImageToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUp.this, "Uploading image", Toast.LENGTH_SHORT).show();
                selectImage();
            }
        });

        final EditText editTextFirstName = (EditText) findViewById(R.id.fname);
        final EditText editTextLastName = (EditText) findViewById(R.id.lname);
        final EditText Email = (EditText) findViewById(R.id.email);

        String[] options = {"Male", "Female"};
        final Spinner genderSpinner = (Spinner) findViewById(R.id.genderSp);
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        genderSpinner.setAdapter(objGenderArr);
        final EditText editTextPhoneNumber = (EditText) findViewById(R.id.phoneNum);
        final EditText editTextPassword = (EditText) findViewById(R.id.password);
        final EditText editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        currentProfileImageResId[0] = R.drawable.man;
        final Button buttonSignUp = (Button) findViewById(R.id.updateButton);
        DataBaseHelper dataBaseHelper =new DataBaseHelper(SignUp.this);
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
                        setImageProfile.setImageResource(R.drawable.woman);
                        currentProfileImageResId[0] = R.drawable.woman;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
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
                    User user = new User(firstName, lastName, emailText,phoneNumber,gender,password) ;
                    Cursor cursor1 = dataBaseHelper.getAdminByEmail(emailText);
                    Cursor cursor2 = dataBaseHelper.getUserByEmail(emailText);
                    if (cursor1.getCount() == 0 && cursor2.getCount() == 0) {
                    if(dataBaseHelper.insertUser(user)){
                        if(imageUri != null){
                            uploadImage(-1,emailText);
                        }else if(gender.trim().equals("Male")){
                            uploadImage(R.drawable.man,emailText);
                        }else if(gender.trim().equals("Female")){
                            uploadImage(R.drawable.woman,emailText);
                        }

                        Toast.makeText(SignUp.this, "User added successfully", Toast.LENGTH_SHORT).show();
                        editTextFirstName.setText("");
                        editTextLastName.setText("");
                        Email.setText("");
                        editTextPhoneNumber.setText("");
                        genderSpinner.setSelection(0);
                        editTextPassword.setText("");
                        editTextConfirmPassword.setText("");

                    }else{
                        Toast.makeText(SignUp.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(SignUp.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignUp.this, "Please enter the correct data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private InputStream convertDrawableToInputStream(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
    }

    private void uploadImage(int drawableId,String emailText) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("SIGHING UP");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("Images/" + emailText);
        if(drawableId == -1)
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                setImageProfile.setImageResource(R.drawable.man);
                Toast.makeText(SignUp.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        else{
            InputStream inputStream = convertDrawableToInputStream(drawableId);
            storageReference.putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SignUp.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, LogIn.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == code && data != null && data.getData() != null) {
            imageUri = data.getData();
            setImageProfile.setImageURI(imageUri);
            currentProfileImageResId[0] = -1;
        }
    }

    private boolean hasACharAndANumber(String password) {
        ///Initialization
        boolean hasAnumber = false;
        boolean hasAchar = false;
        for (int i = 0; i < password.length(); i++) {
            char a = password.toCharArray()[i];
            if (Character.isLetter(a)) {
                hasAchar = true;
            } else if (Character.isDigit(a)) {
                hasAnumber = true;
            }

            // If there is at least one character and one number--> the condition is satisfy
            if (hasAchar && hasAnumber) {
                return true;
            }
        }
        ////If the conditions isn't satisfied
        return false;
    }
}