package com.example.user.wteproject;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;

import domain.Restaurant;
import http.HttpDelegate;


public class newResActivity extends ActionBarActivity {
    public final String BASE_URL = "http://trim-mix-807.appspot.com";

    private Button addNewResBtn;
    private EditText adrText;
    private EditText nameText;
    private EditText phoneText;
    private CheckBox breakfastcheck;
    private CheckBox lunchCheck;
    private CheckBox dinnerCheck;
    private CheckBox night_snackCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_res);

        addNewResBtn = (Button) findViewById(R.id.addNewResBtn);
        adrText = (EditText) findViewById(R.id.adrText);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        breakfastcheck = (CheckBox) findViewById(R.id.breakfastcheck);
        lunchCheck = (CheckBox) findViewById(R.id.lunchcheck);
        dinnerCheck = (CheckBox) findViewById(R.id.dinnercheck);
        night_snackCheck = (CheckBox) findViewById(R.id.night_snackcheck);

        addNewResBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = nameText.getText().toString();
                String address = adrText.getText().toString();
                String phone = phoneText.getText().toString();
                // name ,id ,address ,phone
                Restaurant res = new Restaurant(name,-1,address,phone);
                postRes(res);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_res, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postRes(final Restaurant res){
        new AsyncTask<Void,Void,Restaurant>() {
            @Override
            protected Restaurant doInBackground(Void... params) {
                HttpDelegate delegate = new HttpDelegate();
                Gson gson = new Gson();
                String jsonString = gson.toJson(res);
                try {
                    String result = delegate.doPost(BASE_URL+"/restaurants" , jsonString);
                    Restaurant newRes = gson.fromJson(result,Restaurant.class);
                    return newRes;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };
    }
}
