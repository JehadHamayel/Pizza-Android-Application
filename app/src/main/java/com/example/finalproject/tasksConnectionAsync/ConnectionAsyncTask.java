package com.example.finalproject.tasksConnectionAsync;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.finalproject.activities.EnterPage;
import com.example.finalproject.models.Pizza;

import java.util.List;
public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    Activity activity;
    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        ((EnterPage) activity).setButtonText("connecting");
        super.onPreExecute();
        ((EnterPage) activity).setProgress(true);
    }
    @Override
    protected String doInBackground(String... params) {
        String data = HttpManager.getData(params[0]);
        return data;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((EnterPage) activity).setProgress(false);
        if (s != null && !s.isEmpty()){
            ((EnterPage) activity).setButtonText("connected");
            List<Pizza> pizzas = PizzaJsonParser.parseJson(s);
            ((EnterPage) activity).fillPizzas(pizzas);
        }else{
            ((EnterPage) activity).setButtonText("GET STARTED");
            Toast.makeText(activity, "Error: No connection"+s, Toast.LENGTH_SHORT).show();
        }
        

    }

}
