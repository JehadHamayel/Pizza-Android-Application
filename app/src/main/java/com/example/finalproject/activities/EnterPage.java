///By: Jehad Hamayel 1200348, Abdalkarim Eiss 1200015
package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.tasksConnectionAsync.ConnectionAsyncTask;
import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.Pizza;
import com.example.finalproject.R;

import java.util.List;

public class EnterPage extends AppCompatActivity {
    Button getStarted;
    Button login;
    Button signUp;

    DataBaseHelper dataBaseHelper =new DataBaseHelper(EnterPage.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setProgress(false);
        getStarted = (Button) findViewById(R.id.buttonGETSTARTED);
        login = (Button) findViewById(R.id.buttonLOGIN);
        signUp = (Button) findViewById(R.id.buttonSIGNUP);
        login.setVisibility(View.INVISIBLE);
        signUp.setVisibility(View.INVISIBLE);
        setProgress(false);

        getStarted.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick (View v){
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(EnterPage.this);
                connectionAsyncTask.execute("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent logInIntent = new Intent(EnterPage.this, LogIn.class);
                    EnterPage.this.startActivity(logInIntent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(EnterPage.this, SignUp.class);
                EnterPage.this.startActivity(signUpIntent);
            }
        });


    }
    public void setButtonText(String text) {
        getStarted.setText(text);
    }
    public void fillPizzas(List<Pizza> pizzas) {
        for (Pizza pizza : pizzas) {
            dataBaseHelper.insertPizza(pizza);
        }
        getStarted.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.VISIBLE);
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}