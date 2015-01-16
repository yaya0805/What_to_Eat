package com.example.user.wteproject;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;

import domain.Restaurant;
import http.HttpDelegate;


public class newResActivity extends ActionBarActivity {
    public final String BASE_URL = "http://trim-mix-807.appspot.com";

    private Button addNewResBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_res);

        addNewResBtn = (Button) findViewById(R.id.addNewResBtn);

        addNewResBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Restaurant res = new Restaurant();
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
        new AsyncTask<Void,Void,Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                HttpDelegate delegate = new HttpDelegate();
                Gson gson = new Gson();
                String jsonString = gson.toJson(res);
                try {
                    String result = delegate.doPost(BASE_URL+"/restaurants" , jsonString);
                    if(result!=null) return true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

        };
    }
}
