package com.example.finalproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;
import com.example.finalproject.databinding.ActivityMainNavigationPageForAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainNavigationPageForAdmin extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainNavigationPageForAdminBinding binding;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavigationPageForAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainNavigationPageForAdmin.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_profile, R.id.nav_add_admin, R.id.nav_view_all_orders,R.id.nav_add_special_offers,R.id.nav_logout_admin)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_navigation_page_for_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.getMenu().findItem(R.id.nav_logout_admin).setOnMenuItemClickListener(menuItem -> {
            //ask for confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#C04121'>Confirmation</font>"));
            builder.setMessage("Are you sure you want to logout?");
            builder.setIcon(R.drawable.logout_2_svgrepo_com);
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                Intent intent = new Intent(this, LogIn.class);
                startActivity(intent);
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {
                //do nothing

            });
            builder.create().show();
            return true;


        });
        navigationView.getMenu().findItem(R.id.nav_admin_profile).setOnMenuItemClickListener(menuItem -> {
            // show the fragment of pizza menu when the pizza menu is selected give me the navigation controller
            navController.navigate(R.id.nav_admin_profile);
            // Close the drawer
            drawer.closeDrawers();
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_add_admin).setOnMenuItemClickListener(menuItem -> {
            // show the fragment of pizza menu when the pizza menu is selected give me the navigation controller
            navController.navigate(R.id.nav_add_admin);
            // Close the drawer
            drawer.closeDrawers();
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_view_all_orders).setOnMenuItemClickListener(menuItem -> {
            // show the fragment of pizza menu when the pizza menu is selected give me the navigation controller
            navController.navigate(R.id.nav_view_all_orders);
            // Close the drawer
            drawer.closeDrawers();
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_add_special_offers).setOnMenuItemClickListener(menuItem -> {
            // show the fragment of pizza menu when the pizza menu is selected give me the navigation controller
            navController.navigate(R.id.nav_add_special_offers);
            // Close the drawer
            drawer.closeDrawers();
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation_page_for_admin, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Do nothing here. The back button will be disabled.
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_navigation_page_for_admin);
        DataBaseHelper dataBaseHelper =new DataBaseHelper(this);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String email = sharedPrefManager.readString("logIn email admin","");

        TextView userEmail = navigationView.getHeaderView(0).findViewById(R.id.AdminEmalAddress);
        userEmail.setText(email);
        ImageView adminImage = navigationView.getHeaderView(0).findViewById(R.id.adminImage);
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("Images/" + email);
        try {

            File localFile = File.createTempFile("Images", ".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    adminImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error in downloading image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error in IO", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        Cursor cursor = dataBaseHelper.getAdminByEmail(email);
        cursor.moveToFirst();
        TextView adminUserName = navigationView.getHeaderView(0).findViewById(R.id.adminUserNAme);
        adminUserName.setText(cursor.getString(1)+" "+cursor.getString(2));

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}