package com.example.user.wteproject;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import domain.Information;
import domain.User;
import http.HttpDelegate;
import service.SysApplication;


public class LoginActivity extends ActionBarActivity {
    TextView t;
    public final HttpDelegate delegate = new HttpDelegate();
    public final String BASE_URL = "http://trim-mix-807.appspot.com";
    public Information info ;

    private ActionBar actionBar;

    private ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SysApplication.getInstance().addActivity(this);

        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // hide action bar
        actionBar = getSupportActionBar();
        actionBar.hide();

        // login btn
        Button btn;
        btn = (Button) findViewById(R.id.Login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    userLogin("root");

                } catch (IOException | URISyntaxException | JSONException e) {
                    Toast.makeText(LoginActivity.this,"連線失敗,請確認網路",Toast.LENGTH_LONG);
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
    private void userLogin(final String id) throws IOException, URISyntaxException, JSONException {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Gson gson = new Gson();
                User user = new User();
                user.setId(id);
                String jsonString = gson.toJson(user);
                String result = null;
                try {
                    result = delegate.doPost(BASE_URL+"/login" , jsonString);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            }

            protected void onPostExecute(String result){
                if(result!=null){
                    Gson gson = new Gson();
                    Log.d("info_before",result);
                    info = gson.fromJson(result,Information.class);
                    Log.d("info_after",gson.toJson(info));
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info",info);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast.makeText(LoginActivity.this,"連線失敗,請確認網路",Toast.LENGTH_LONG);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }.execute(null,null,null);
    }

}
