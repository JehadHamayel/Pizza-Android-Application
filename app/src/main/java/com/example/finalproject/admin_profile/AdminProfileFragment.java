package com.example.finalproject.admin_profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.Hash;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.models.User;
import com.example.finalproject.databinding.ActivityMainNavigationPageForAdminBinding;
import com.example.finalproject.databinding.FragmentAdminProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class AdminProfileFragment extends Fragment {

    private FragmentAdminProfileBinding binding;
    private ActivityMainNavigationPageForAdminBinding binding1;
    Uri imageUri;
    int code = 100;
    StorageReference storageReference = null;
    ProgressDialog progressDialog;
    ImageView setImageProfile;
    SharedPrefManager sharedPrefManager;
    String email = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        if (sharedPrefManager.readString("logIn email admin", "").isEmpty()) {
            Toast.makeText(getContext(), "Error in SharedPref", Toast.LENGTH_SHORT).show();
            return root;
        }
        email = sharedPrefManager.readString("logIn email admin", "");
        setImageProfile = (ImageView) root.findViewById(R.id.profileImage);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading Data");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        try {
            storageReference = FirebaseStorage.getInstance().getReference("Images/" + email);
            File localFile = File.createTempFile("Images", ".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    setImageProfile.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error in IO", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        Cursor cursor = dataBaseHelper.getAdminByEmail(email.trim());
        cursor.moveToFirst();

        binding.firstNameE.setText(cursor.getString(1));
        binding.lastNameE.setText(cursor.getString(2));
        binding.emailE.setText(cursor.getString(0));
        binding.PhoneNumberE.setText(cursor.getString(3));
        binding.GenderE.setText(cursor.getString(4));

        if (progressDialog.isShowing())
            progressDialog.dismiss();


        binding.uploadAdminImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int correctConditions = 0;
                if (binding.firstNameE.getText().toString().length() < 3) {
                    binding.firstNameE.setError("Please enter a valid first name (not less than 3 characters)");
                } else {
                    correctConditions++;
                }
                if (binding.lastNameE.getText().toString().length() < 3) {
                    binding.lastNameE.setError("Please enter a valid last name (not less than 3 characters)");
                } else {
                    correctConditions++;
                }
                String phoneNumber = binding.PhoneNumberE.getText().toString().trim();
                if (binding.PhoneNumberE.getText().toString().isEmpty() || !phoneNumber.matches("^05\\d{8}$")) {
                    binding.PhoneNumberE.setError("Please enter a valid Phone Number (starts with 05, 10 digits)");
                } else {
                    correctConditions++;
                }
                String oldPassword = Hash.hashPassword(binding.OldPassE.getText().toString().trim());
                Cursor cursor = dataBaseHelper.getAdminByEmail(email.trim());
                cursor.moveToFirst();
                boolean passCheck = false;
                boolean noNeedToCheck = true;
                if (oldPassword.compareTo(cursor.getString(5)) != 0 && !binding.OldPassE.getText().toString().isEmpty()) {
                    binding.OldPassE.setError("Please enter the correct password");
                    noNeedToCheck = false;
                } else if (!binding.OldPassE.getText().toString().isEmpty()) {
                    if (!hasACharAndANumber(binding.NewPassE.getText().toString()) && (binding.NewPassE.getText().length() < 8 && binding.NewPassE.getText().length() > 0)) {
                        binding.NewPassE.setError("Please enter a valid password (must not be less than 8 characters and must include at least 1 character and 1 number)");
                        noNeedToCheck = false;
                    } else {
                        if (binding.NewPassE.getText().toString().compareTo(binding.confirmPassword.getText().toString()) != 0 && !binding.confirmPassword.getText().toString().isEmpty()) {
                            binding.confirmPassword.setError("Please enter the password correctly (The passwords do not match)");
                            noNeedToCheck = false;
                        } else if (binding.confirmPassword.getText().toString().length() < 8 && !binding.confirmPassword.getText().toString().isEmpty()) {
                            binding.confirmPassword.setError("Please enter a valid password (must not be less than 8 characters and must include at least 1 character and 1 number)");
                            noNeedToCheck = false;
                        } else {
                            passCheck = true;
                            correctConditions++;
                        }
                    }
                }

                if (correctConditions == 4 && passCheck) {
                    if (imageUri != null) {
                        uploadImage(-1, email);
                    } else if (binding.GenderE.toString().trim().equals("Male")) {
                        uploadImage(R.drawable.man, email);
                    } else if (binding.GenderE.toString().trim().equals("Female")) {
                        uploadImage(R.drawable.woman, email);
                    }
                    String firstName = binding.firstNameE.getText().toString().trim();
                    String lastName = binding.lastNameE.getText().toString().trim();
                    User updatedAdmin = new User(firstName, lastName, email, phoneNumber, binding.GenderE.getText().toString().trim(), Hash.hashPassword(binding.NewPassE.getText().toString().trim()));
                    if (dataBaseHelper.updateAdmin(updatedAdmin)) {
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                } else if (correctConditions == 3 && noNeedToCheck) {
                    if (imageUri != null) {
                        uploadImage(-1, email);
                    } else if (binding.GenderE.toString().trim().equals("Male")) {
                        uploadImage(R.drawable.man, email);
                    } else if (binding.GenderE.toString().trim().equals("Female")) {
                        uploadImage(R.drawable.woman, email);
                    }
                    Cursor cursor1 = dataBaseHelper.getAdminByEmail(email);
                    cursor1.moveToFirst();

                    String firstName = binding.firstNameE.getText().toString().trim();
                    String lastName = binding.lastNameE.getText().toString().trim();
                    User updatedAdmin = new User(firstName, lastName, email, phoneNumber, binding.GenderE.getText().toString().trim(), cursor1.getString(5));
                    if (dataBaseHelper.updateAdmin(updatedAdmin)) {
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        binding.OldPassE.setText("");
                        binding.NewPassE.setText("");
                        binding.confirmPassword.setText("");
                    } else {
                        Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                    }
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
        progressDialog.setTitle("Updating Image");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("Images/" + emailText);
        if (drawableId == -1)
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
